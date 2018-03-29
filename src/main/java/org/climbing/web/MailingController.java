package org.climbing.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	
	@RequestMapping(method=RequestMethod.GET)
    public String load(Model model)
    {
        return "mailing";
    }
	
	@RequestMapping(method=RequestMethod.POST, params = "method=send")
    public String send(HttpServletRequest request, HttpServletResponse response,
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
		
		String result = "";
		if(!"recipients".equals(type)) {
			String env = System.getProperty("PLATFORM");
			String testEnvAllDest = configurationsDao.findByKey("mailing.test.env.to").getValue();
			if("develop".equals(env) || "test".equals(env)) {
				toListCCN.add(testEnvAllDest);
				try {
        			mailUtil.sendMail(fromEmail, fromName, null, null, toListCCN, subject, message, null, null, true);
        			result = "Email inviata a " + toListCCN.size() + " indirizzi";
        		} catch (Exception e) {
        			
        			log.info("Cannot send mailing: {}", e.getMessage());
        			e.printStackTrace();
        			
        		}
			} else {
				List<Person> persons = new ArrayList<Person>();
				if("all".equals(type)) {
					persons = personDao.findMailingAll();
				} else {
					persons = personDao.findMailingRegistered();
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
		        			mailUtil.sendMail(fromEmail, fromName, null, null, toListCCN, subject, message, null, null, true);
		        			sent.addAll(toListCCN);
		        		} catch (Exception e) {
		        			
		        			log.info("Cannot send mailing: {}", e.getMessage());
		        			e.printStackTrace();
		        			
		        		}
			        	toListCCN = new ArrayList<>();
			        }
				}
				result = "Email inviata a " + sent.size() + " indirizzi (su " + alreadySent.size() + " totali)";
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
    			mailUtil.sendMail(fromEmail, fromName, null, null, toListCCN, subject, message, null, null, true);
    			
    			result = "Email inviata a " + alreadySent.size() + " indirizzi";
    		} catch (Exception e) {
    			
    			log.info("Cannot send mailing: {}", e.getMessage());
    			e.printStackTrace();
    			result = "Errore nell'invio";
    		}
		}
		
		
		redirectAttributes.addAttribute("result", result);
        return "redirect:/mailing";
    }
	
}
