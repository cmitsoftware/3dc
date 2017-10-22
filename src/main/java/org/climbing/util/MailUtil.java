package org.climbing.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.util.ByteArrayDataSource;
import javax.servlet.ServletContext;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.climbing.repo.ConfigurationsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MailUtil {

	@Autowired
	ReportUtil reportUtil;
	
	@Autowired
	ServletContext context;
	
	@Autowired
	ConfigurationsDAO configurationsDao;

	public void sendMail(String fromEmail, String fromName, 
			List<String> to, List<String> cc, List<String> bcc,
			String subject, String message, 
			Map<String, byte[]> attachments, Map<String, String> mimeTypes,
			boolean useHtmlTemplate) {
		
		try {

			String host = configurationsDao.findByKey("smtp.host").getValue();
			Integer port = Integer.parseInt(configurationsDao.findByKey("smtp.port").getValue().trim());
			String username = configurationsDao.findByKey("smtp.username").getValue();
			String password = configurationsDao.findByKey("smtp.password").getValue();
			
//			MultiPartEmail email = new MultiPartEmail();
			HtmlEmail email = new HtmlEmail();
	        email.setHostName(host);
	        email.setSmtpPort(port);
	        email.setAuthenticator(new DefaultAuthenticator(username, password));
	        email.setSSLOnConnect(true);
			email.setFrom(fromEmail, fromName);
	        
			if(to != null) {
				for(String r: to) {
					email.addTo(r);
				}
			}
	        if(cc != null) {
	        	for(String r: cc) {
	        		email.addCc(r);
	        	}
	        }
	        if(bcc != null) {
	        	for(String r: bcc) {
	        		email.addBcc(r);
	        	}
	        }
	        if(attachments != null) {
	        	for(String a: attachments.keySet()) {
	        		
	        		ByteArrayDataSource ds = new ByteArrayDataSource(
	        				attachments.get(a), mimeTypes.get(a));
	        		email.attach(ds, a, a);
	        	}
	        }
	        
	        String mailBody = message;
	        
	        if(useHtmlTemplate) {
	        	
	        	String m = Utils.readStringFromFile(context.getRealPath("/resources/template.html"), "UTF-8");
				mailBody = m.replace("<!--MAIL-TEXT-->", message);

				String headUrlString = configurationsDao.findByKey("mail.template.header").getValue();
				mailBody = mailBody.replace("<!--MAIL-HEADER-->", headUrlString);
//				URL headUrl = new URL(headUrlString);
//				String cidHead = email.embed(headUrl, "head.png");
//				mailBody = mailBody.replaceAll("cid:head", "cid:" + cidHead);
				
				String footerUrlString = configurationsDao.findByKey("mail.template.footer").getValue();
				mailBody = mailBody.replace("<!--MAIL-FOOTER-->", footerUrlString);
//				URL footerUrl = new URL(footerUrlString);
//				String cidFooter = email.embed(footerUrl, "footer.png");
//				mailBody = mailBody.replaceAll("cid:footer", "cid:" + cidFooter);
				
	        }
			
			email.setSubject(subject);
			email.setMsg(message);
	        email.setHtmlMsg(mailBody);
	        
//	        email.send();
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendTestMail() {
		
		try {
//			HtmlEmail email = new HtmlEmail();
			MultiPartEmail email = new MultiPartEmail();
	        email.setHostName("smtp.gmail.com");
	        email.setSmtpPort(465);
	        email.setAuthenticator(new DefaultAuthenticator("foofoobogus@gmail.com", "thisisabogusmail"));
	        email.setSSLOnConnect(true);
				email.setFrom("foofoobogus@gmail.com");
	        email.setSubject("Test mail 3dc");
	        email.setMsg("Se arriva la mail allora funziona lo scheduler di spring con jboss");
//	        email.setHtmlMsg("<html>\n" +
//	                "<body>\n" +
//	                "\n" +
//	                "<a href=\"http://jbosseap-cmidev1.rhcloud.com/\">\n" +
//	                "3D climbing</a>\n" +
//	                "\n" +
//	                "</body>\n" +
//	                "</html>");
	        email.addTo("michele.cervini@gmail.com");
	        email.addTo("giannirace@gmail.com");
	 
	        ByteArrayDataSource ds = new ByteArrayDataSource(reportUtil.buildPersonsReport(),
	        		"application/vnd.ms-excel");
//	        		"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	        

	     // add the attachment
	        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Date now = new Date();
			String name = "Report-iscritti-" + sdf.format(now) + ".xlsx";
	        email.attach(ds, name, "Report iscritti 3D climbing");
	        
	        email.send();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
