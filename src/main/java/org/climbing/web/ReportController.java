package org.climbing.web;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.climbing.repo.UserDAO;
import org.climbing.util.ReportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value="/report")
public class ReportController {

	private static final Logger log = LoggerFactory.getLogger(ReportController.class);
	
	@Autowired
	UserDAO userDao;
	
	@Autowired
	ReportUtil reportUtil;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@RequestMapping(method=RequestMethod.GET)
    public String load(Model model, final RedirectAttributes redirectAttributes){
        return "reports";
    }
	
	@RequestMapping(method=RequestMethod.GET, params = "method=general")
    public ResponseEntity<?> general(HttpServletRequest request, HttpServletResponse response,
			ModelMap model, final RedirectAttributes redirectAttributes){
		
		try {

			Date now = new Date();
			log.info("Requested general report");
			
			byte[] report = reportUtil.buildGeneralReport();

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			String name = "Report-iscritti-" + sdf.format(now) + ".xlsx";
			final HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
			headers.add("Content-disposition", "attachment; filename=" + name);
			
			return new ResponseEntity<byte[]>(report, headers, HttpStatus.OK);
			
		} catch (Exception e) {

			e.printStackTrace();
			return new ResponseEntity<String>(
					"Errore nella generazione del report. Prenditela con Gianni", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@RequestMapping(method=RequestMethod.GET, params = "method=currentYear")
    public ResponseEntity<?> currentYear(HttpServletRequest request, HttpServletResponse response,
			ModelMap model, final RedirectAttributes redirectAttributes){
		
		try {

			log.info("Requested current year report");
			
			byte[] report = reportUtil.buildCurrentYearReport();

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			String name = "Report-iscritti-anno-corrente.xlsx";
			final HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
			headers.add("Content-disposition", "attachment; filename=" + name);
			
			return new ResponseEntity<byte[]>(report, headers, HttpStatus.OK);
			
		} catch (Exception e) {

			e.printStackTrace();
			return new ResponseEntity<String>(
					"Errore nella generazione del report.", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@RequestMapping(method=RequestMethod.GET, params = "method=certificate")
    public ResponseEntity<?> certificate(HttpServletRequest request, HttpServletResponse response,
			ModelMap model, final RedirectAttributes redirectAttributes){
		
		try {

			Date now = new Date();
			log.info("Requested no certificate report");
			
			byte[] report = reportUtil.buildPersonsWithoutCertificateReport();

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			String name = "Report-iscritti-senza-certificato" + sdf.format(now) + ".xlsx";
			final HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
			headers.add("Content-disposition", "attachment; filename=" + name);
			
			return new ResponseEntity<byte[]>(report, headers, HttpStatus.OK);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<String>(
					"Errore nella generazione del report.", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
}
