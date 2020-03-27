package org.climbing.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.apache.tika.Tika;
import org.climbing.domain.Person;
import org.climbing.repo.ConfigurationsDAO;
import org.climbing.repo.PersonDAO;
import org.climbing.util.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value="/mailing")
public class MailingController {

	private static final Logger log = LoggerFactory.getLogger(MailingController.class);
	
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	
	@Autowired
	MailUtil mailUtil;
	
	@Autowired
	ConfigurationsDAO configurationsDao;
	
	@Autowired
	PersonDAO personDao;
	
	@Autowired
	Tika tika;
	
	@RequestMapping(method=RequestMethod.GET)
    public String load(Model model)
    {
        return "mailing";
    }
	
	@RequestMapping(method=RequestMethod.POST, params = "method=send")
    public String send(MultipartHttpServletRequest request, HttpServletResponse response,
			ModelMap model, final RedirectAttributes redirectAttributes)
    {
		String subject = request.getParameter("subject");
		String message = request.getParameter("text");
		String type = request.getParameter("type");
		
		List<String> toListCCN = new ArrayList<String>();
		List<String> alreadySent = new ArrayList<String>();
		String fromEmail = configurationsDao.findByKey("smtp.default.from.email").getValue();
		String fromName = configurationsDao.findByKey("smtp.default.from.name").getValue();
		Integer mailingBatchSize = Integer.parseInt(configurationsDao.findByKey("mailing.batch.size").getValue());;
		
		Iterator<String> attIterator = request.getFileNames();
		HashMap<String, byte[]> attachments = new HashMap<String, byte[]>();
		HashMap<String, String> mimeTypes = new HashMap<String, String>();
		while(attIterator.hasNext()) {
			try {
				String v = attIterator.next();
				if(!request.getFile(v).isEmpty()) {
					InputStream att = request.getFile(v).getInputStream();
					String mimeType = tika.detect(request.getFile(v).getOriginalFilename());
//					String mimeType = tika.detect(att, request.getFile(v).getOriginalFilename());
//					ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//
//					int nRead;
//					byte[] data = new byte[16384];
//
//					while ((nRead = att.read(data, 0, data.length)) != -1) {
//					  buffer.write(data, 0, nRead);
//					}
//
//					buffer.flush();
//
//					byte[] attContent = buffer.toByteArray();
					
					byte[] attContent = IOUtils.toByteArray(att);
					
//					FileOutputStream fos = new FileOutputStream(new File("E://temp//out.pdf"));
//					fos.write(attContent);
//					fos.close();
					
					log.info("Attachment {} size: {}", request.getFile(v).getOriginalFilename(), attContent.length);
					attachments.put(request.getFile(v).getOriginalFilename(), attContent);
					mimeTypes.put(request.getFile(v).getOriginalFilename(), mimeType);
				}
			} catch (IOException e) {
				log.error("Error adding attchment");
				e.printStackTrace();
			}
		}
		
		String result = "";
		if(!"recipients".equals(type)) {
			String env = System.getProperty("PLATFORM");
			String testEnvAllDest = configurationsDao.findByKey("mailing.test.env.to").getValue();
			if("develop".equals(env) || "test".equals(env)) {
				toListCCN.add(testEnvAllDest);
				try {
        			mailUtil.sendMail(fromEmail, fromName, null, null, toListCCN, subject, message, attachments, mimeTypes, true);
        			result = "Email inviata a " + toListCCN.size() + " indirizzi";
        		} catch (Exception e) {
        			
        			log.info("Cannot send mailing: {}", e.getMessage());
        			e.printStackTrace();
        			
        		}
			} else {
				List<Person> persons = new ArrayList<Person>();
				List<Person> personsWithNoValidEmail = new ArrayList<Person>();
				if("all".equals(type)) {
					persons = personDao.findMailingAll();
					personsWithNoValidEmail = personDao.findMailingAllWithNotValidEmail();
				} else if("subscribed".equals(type)) {
					persons = personDao.findMailingRegistered();
					personsWithNoValidEmail = personDao.findMailingRegisteredWithNoValidEmail();
				} else if("nocertificate".equals(type)) {
					persons = personDao.findPersonsWithoutCertificate(true);
					personsWithNoValidEmail = persons.stream().filter(person -> person.getEmail() != null ? !isEmailValid(person.getEmail()) : true).collect(Collectors.toList());
				} else {
					result = "Specificare i destinatari";
				}

				List<String> sent = new ArrayList<String>(); 
				for(int i = 0; i < persons.size(); i++) {
					Person p = persons.get(i);
					Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(p.getEmail());
					/*
					 * Only valid emails
					 */
			        if(matcher.find()) {
			        	log.info("Sending mailing to {} : {} {} ", p.getEmail(), p.getName(), p.getSurname());
			        	if(!alreadySent.contains(p.getEmail())) {
			        		toListCCN.add(p.getEmail());
			        		alreadySent.add(p.getEmail());
			        		
			        	}
			        } else {
			        	log.warn("Email not valid: {}", p.getEmail());
			        }
			        
			        if(toListCCN.size() == mailingBatchSize || ((i == persons.size()-1) && toListCCN.size() > 0)) {
			        	try {
		        			mailUtil.sendMail(fromEmail, fromName, null, null, toListCCN, subject, message, attachments, mimeTypes, true);
		        			sent.addAll(toListCCN);
		        		} catch (Exception e) {
		        			
		        			log.info("Cannot send mailing: {}", e.getMessage());
		        			e.printStackTrace();
		        			
		        		}
			        	toListCCN = new ArrayList<>();
			        }
				}
				result = "Email inviata a " + sent.size() + " indirizzi (su " + alreadySent.size() + " totali)"
					+ (personsWithNoValidEmail.isEmpty() ? "" : "<br/><h3>ATTENZIONE trovate persone con email non valide</h3>: "
						+ personsWithNoValidEmail.stream().map(person -> person.getName() + " " + person.getSurname() + ", ").reduce("", String::concat));
			}
		} else {
			
			String recipients = request.getParameter("recipients");
			if(recipients != null) {
				toListCCN = Arrays.asList(recipients.split("\\,"));
				alreadySent.addAll(toListCCN);
				for(String ccn: toListCCN) {
					log.info("Sending test mailing to {}", ccn);
				}
			}
			try {

				String checkMessage = checkEmailAddresses(toListCCN);
				if ( ! "".equals(checkMessage)) {
					result = "Errore nell'invio. " + checkMessage;
				} else {

					mailUtil.sendMail(fromEmail, fromName, null, null, toListCCN, subject, message, attachments, mimeTypes, true);

					result = "Email inviata a " + alreadySent.size() + " indirizzi";
				}
    		} catch (Exception e) {
    			
    			log.info("Cannot send mailing: {}", e.getMessage());
    			e.printStackTrace();
    			result = "Errore nell'invio";
    		}
		}
		
		log.info("Mailing result: {}", result);
		
		redirectAttributes.addAttribute("result", result);
        return "redirect:/mailing";
    }

    static String checkEmailAddresses(List<String> emailAddresses) {
		String message = "";
		for (String emailAddress: emailAddresses ) {
			if ( ! isEmailValid(emailAddress)){
				message += " Email " + emailAddress + " not valid.";
			}
		}
		return message;
	}

	static boolean isEmailValid(String email) {
		//String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		//return email.matches(regex);
		return VALID_EMAIL_ADDRESS_REGEX.matcher(email).find();
	}
	
}
