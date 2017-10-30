
package org.climbing.scheduled;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.climbing.domain.Person;
import org.climbing.repo.ConfigurationsDAO;
import org.climbing.repo.PersonDAO;
import org.climbing.util.MailUtil;
import org.climbing.util.ReportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TaskScheduler {

	@Autowired
	MailUtil mailUtil;
	
	@Autowired
	ReportUtil reportUtil;
	
	@Autowired
	ConfigurationsDAO configurationsDao;
	
	@Autowired
	PersonDAO personDao;
	
//	@Scheduled(cron="0 0 0/1 1/1 * ?")
//	@Scheduled(cron="0 0 10 1 * ?") // In prod ogni 1 del mese alle 10
	@Scheduled(cron="${report.mail.cron}")
	public void reportPersonsWithoutCertificate()
	{
		Date now = new Date();
		System.out.println("Generating report at " + now);
		
		byte[] report = reportUtil.buildPersonsReport();
		
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
		
		List<Person> personsWithoutCertificate = personDao.findPersonsWithoutCertificate();
		for(Person p: personsWithoutCertificate) {
			message += "<tr>";
			message += "<td style='border: 1px solid black;padding: 10px;'>";
			message += p.getId();
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
		
	    System.out.println("Sending report email " + now);
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
