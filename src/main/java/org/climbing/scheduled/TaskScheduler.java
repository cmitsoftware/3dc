
package org.climbing.scheduled;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.climbing.domain.Person;
import org.climbing.repo.ConfigurationsDAO;
import org.climbing.repo.PersonDAO;
import org.climbing.util.MailUtil;
import org.climbing.util.ReportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TaskScheduler {
	
	private static final Logger log = LoggerFactory.getLogger(TaskScheduler.class);

	@Autowired
	MailUtil mailUtil;
	
	@Autowired
	ReportUtil reportUtil;
	
	@Autowired
	ConfigurationsDAO configurationsDao;
	
	@Autowired
	PersonDAO personDao;
	
	@Scheduled(cron="${report.mail.cron}")
	public void reportPersonsWithoutCertificate()
	{
		Date now = new Date();
		log.info("Generating report");
		
		byte[] report = reportUtil.buildGeneralReport();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String name = "Report-iscritti-" + sdf.format(now) + ".xlsx";
		
		HashMap<String, byte[]> attachments = new HashMap<String, byte[]>();
		attachments.put(name, report);
		
		HashMap<String, String> mimeTypes = new HashMap<String, String>();
		mimeTypes.put(name, "application/vnd.ms-excel");
		
		List<String> to = new ArrayList<String>();
		String tos = configurationsDao.findByKey("report.mail.to.list").getValue();
		if(tos != null) {
			to = Arrays.asList(tos.split("\\,"));
		}
		
		List<String> cc = new ArrayList<String>();
		String ccs = configurationsDao.findByKey("report.mail.cc.list").getValue();
		if(ccs != null) {
			cc = Arrays.asList(ccs.split("\\,"));
		}
		
		List<String> bcc = new ArrayList<String>();
		String bccs = configurationsDao.findByKey("report.mail.ccn.list").getValue();
		if(bccs != null) {
			bcc = Arrays.asList(bccs.split("\\,"));
		}

		String fromEmail = configurationsDao.findByKey("smtp.default.from.email").getValue();
		String fromName = configurationsDao.findByKey("smtp.default.from.name").getValue();
		
		String message = "<html><head><style>";
		message += "table {border-collapse: collapse;width: 100%;}";		
		message += "table, th, td {border: 1px solid black;}";
		message += "th {height:30px;background-color: #3c8dbc;color: white;}";
		message += "th, td {padding: 10px;}";
		message += "tr:nth-child(even) {background-color: #f2f2f2}";
		message += "</style></head>";
		
		message += "<body><p>In allegato la lista completa degli iscritti.";
		message += "<br/>Di seguito gli iscritti di quest'anno senza certificato: </p><br/>";
		message += "<table style='border-collapse: collapse;width: 100%;border: 1px solid black;'>";
		message += "<tr>";
		message += "<th style='border: 1px solid black;height:30px;background-color: #3c8dbc;color: white;padding: 10px;'>Id</th>";
		message += "<th style='border: 1px solid black;height:30px;background-color: #3c8dbc;color: white;padding: 10px;'>Cognome</th>";
		message += "<th style='border: 1px solid black;height:30px;background-color: #3c8dbc;color: white;padding: 10px;'>Nome</th>";
		message += "<th style='border: 1px solid black;height:30px;background-color: #3c8dbc;color: white;padding: 10px;'>Email</th>";
		message += "<th style='border: 1px solid black;height:30px;background-color: #3c8dbc;color: white;padding: 10px;'>Telefono</th>";
		message += "</tr>";
		
		List<Person> personsWithoutCertificate = personDao.findPersonsWithoutCertificate(null);
		for(Person p: personsWithoutCertificate) {
			message += "<tr>";
			message += "<td style='border: 1px solid black;padding: 10px;'>";
//			message += p.getId();
			if(p.getNumber() != null) {
				message += p.getNumber();
			}
			message += "</td>";
			message += "<td style='border: 1px solid black;padding: 10px;'>";
			message += p.getSurname() != null ? p.getSurname() : "";
			message += "</td>";
			message += "<td style='border: 1px solid black;padding: 10px;'>";
			message += p.getName() != null ? p.getName() : "";;
			message += "</td>";
			message += "<td style='border: 1px solid black;padding: 10px;'>";
			message += p.getEmail() != null ? p.getEmail() : "";;
			message += "</td>";
			message += "<td style='border: 1px solid black;padding: 10px;'>";
			message += p.getPhone() != null ? p.getPhone() : "";;
			message += "</td>";
			message += "</tr>";
		}
		message += "</table></body></html>";
		
		mailUtil.sendMail(fromEmail, fromName, to, cc, bcc, "Report mensile iscritti " + sdf.format(now),
				message, attachments, mimeTypes, false);		
		
	    log.info("Sending report email");
	}
	
//	@Scheduled(cron="0 0 * * * *")
//	public void keepAlive() {
//		
//		HttpClient client = HttpClientBuilder.create().build();
//		
//		String keepAliveUrl = configurationsDao.findByKey("keep.alive.url").getValue();
//		System.out.println("Keep alive url: " + keepAliveUrl);
//		
////		HttpGet request = new HttpGet("http://gym-3dclimbing.rhcloud.com");
//		HttpGet request = new HttpGet(keepAliveUrl);
//
//		// add request header
//		HttpResponse response;
//		try {
//			response = client.execute(request);
//			System.out.println("Keep alive response Code : " 
//					+ response.getStatusLine().getStatusCode());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
