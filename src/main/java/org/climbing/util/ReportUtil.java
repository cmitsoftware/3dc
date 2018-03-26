package org.climbing.util;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.climbing.domain.Person;
import org.climbing.repo.PersonDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportUtil {

	@Autowired
	PersonDAO personDao;
	
	public byte[] buildPersonsReport() {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		try {
			
			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook();
	        XSSFSheet sheetsData = wb.createSheet("Contatori");
	        
	        XSSFFont titlesFont = wb.createFont();
	        titlesFont.setBold(true);
	        CellStyle titlesStyle = wb.createCellStyle();
	        titlesStyle.setAlignment(CellStyle.ALIGN_LEFT);
	        titlesStyle.setFont(titlesFont);
	        
	        int rowOffset = 0;
	        int colOffset = 0;
	        Row titlesRow = sheetsData.createRow((short)rowOffset);
	        Cell title1Cell = titlesRow.createCell(colOffset);
	        title1Cell.setCellValue("N.");
	        title1Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title2Cell = titlesRow.createCell(colOffset);
	        title2Cell.setCellValue("Cognome");
	        title2Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title3Cell = titlesRow.createCell(colOffset);
	        title3Cell.setCellValue("Nome");
	        title3Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title4Cell = titlesRow.createCell(colOffset);
	        title4Cell.setCellValue("Telefono");
	        title4Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title5Cell = titlesRow.createCell(colOffset);
	        title5Cell.setCellValue("Data iscrizione 3D");
	        title5Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title6Cell = titlesRow.createCell(colOffset);
	        title6Cell.setCellValue("Data certificato medico");
	        title6Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title7Cell = titlesRow.createCell(colOffset);
	        title7Cell.setCellValue("Data abbonamento");
	        title7Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title8Cell = titlesRow.createCell(colOffset);
	        title8Cell.setCellValue("Data prova gratuita");
	        title8Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title9Cell = titlesRow.createCell(colOffset);
	        title9Cell.setCellValue("Codice fiscale");
	        title9Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title10Cell = titlesRow.createCell(colOffset);
	        title10Cell.setCellValue("Email");
	        title10Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title11Cell = titlesRow.createCell(colOffset);
	        title11Cell.setCellValue("Paese");
	        title11Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title12Cell = titlesRow.createCell(colOffset);
	        title12Cell.setCellValue("Indirizzo");
	        title12Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title13Cell = titlesRow.createCell(colOffset);
	        title13Cell.setCellValue("Data nascita");
	        title13Cell.setCellStyle(titlesStyle);

	        colOffset++;
	        Cell title14Cell = titlesRow.createCell(colOffset);
	        title14Cell.setCellValue("Data affiliazione");
	        title14Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title15Cell = titlesRow.createCell(colOffset);
	        title15Cell.setCellValue("Data prima registrazione 3D");
	        title15Cell.setCellStyle(titlesStyle);
	        
	        for(Person p: personDao.findAll("surname", "asc")) {
	        	
	        	rowOffset++;
	        	colOffset = 0;
		        Row personRow = sheetsData.createRow((short)rowOffset);
		        Cell cell1 = personRow.createCell(colOffset);
//		        cell1.setCellValue(p.getId());
		        if(p.getNumber() != null) {
		        	cell1.setCellValue(p.getNumber());
		        }
		        
		        colOffset++;
		        Cell cell2 = personRow.createCell(colOffset);
		        cell2.setCellValue(p.getSurname() != null ? p.getSurname() : "");
		        
		        colOffset++;
		        Cell cell3 = personRow.createCell(colOffset);
		        cell3.setCellValue(p.getName() != null ? p.getName() : "");
		        
		        colOffset++;
		        Cell cell4 = personRow.createCell(colOffset);
		        cell4.setCellValue(p.getPhone() != null ? p.getPhone() : "");
		        
		        colOffset++;
		        Cell cell5 = personRow.createCell(colOffset);
		        cell5.setCellValue(p.getRegistrationDate() != null ? 
		        		sdf.format(p.getRegistrationDate()) : "");
		        
		        colOffset++;
		        Cell cell6 = personRow.createCell(colOffset);
		        cell6.setCellValue(p.getCertificationDate() != null ? 
		        		sdf.format(p.getCertificationDate()) : "");
		        
		        colOffset++;
		        Cell cell7 = personRow.createCell(colOffset);
		        cell7.setCellValue(p.getSubscriptionDate() != null ? 
		        		sdf.format(p.getSubscriptionDate()) : "");
		        
		        colOffset++;
		        Cell cell8 = personRow.createCell(colOffset);
		        cell8.setCellValue(p.getFreeEntryDate() != null ? 
		        		sdf.format(p.getFreeEntryDate()) : "");
		        
		        colOffset++;
		        Cell cell9 = personRow.createCell(colOffset);
		        cell9.setCellValue(p.getCf()  != null ? p.getCf() : "");
		        
		        colOffset++;
		        Cell cell10 = personRow.createCell(colOffset);
		        cell10.setCellValue(p.getEmail() != null ? p.getEmail() : "");
		        
		        colOffset++;
		        Cell cell11 = personRow.createCell(colOffset);
		        cell11.setCellValue(p.getCity() != null ? p.getCity() : "");
		        
		        colOffset++;
		        Cell cell12 = personRow.createCell(colOffset);
		        cell12.setCellValue(p.getAddress() != null ? p.getAddress() : "");
		        
		        colOffset++;
		        Cell cell13 = personRow.createCell(colOffset);
		        cell13.setCellValue(p.getBirthDate() != null ? 
		        		sdf.format(p.getBirthDate()) : "");

		        colOffset++;
		        Cell cell14 = personRow.createCell(colOffset);
		        cell14.setCellValue(p.getAffiliationDate() != null ? 
		        		sdf.format(p.getAffiliationDate()) : "");
		        
		        colOffset++;
		        Cell cell15 = personRow.createCell(colOffset);
		        cell15.setCellValue(p.getCreationDate() != null ? 
		        		sdf.format(p.getCreationDate()) : "");
	        }
	        
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        try {
		        wb.write(bos);
	        } finally {
	            bos.close();
	        }
	        byte[] bytes = bos.toByteArray();
	        return bytes;
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public byte[] buildPersonsWithoutCertificateReport() {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		try {
			
			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook();
	        XSSFSheet sheetsData = wb.createSheet("Contatori");
	        
	        XSSFFont titlesFont = wb.createFont();
	        titlesFont.setBold(true);
	        CellStyle titlesStyle = wb.createCellStyle();
	        titlesStyle.setAlignment(CellStyle.ALIGN_LEFT);
	        titlesStyle.setFont(titlesFont);
	        
	        int rowOffset = 0;
	        int colOffset = 0;
	        Row titlesRow = sheetsData.createRow((short)rowOffset);
	        Cell title1Cell = titlesRow.createCell(colOffset);
	        title1Cell.setCellValue("N.");
	        title1Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title2Cell = titlesRow.createCell(colOffset);
	        title2Cell.setCellValue("Cognome");
	        title2Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title3Cell = titlesRow.createCell(colOffset);
	        title3Cell.setCellValue("Nome");
	        title3Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title4Cell = titlesRow.createCell(colOffset);
	        title4Cell.setCellValue("Telefono");
	        title4Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title5Cell = titlesRow.createCell(colOffset);
	        title5Cell.setCellValue("Data iscrizione 3D");
	        title5Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title6Cell = titlesRow.createCell(colOffset);
	        title6Cell.setCellValue("Data certificato medico");
	        title6Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title7Cell = titlesRow.createCell(colOffset);
	        title7Cell.setCellValue("Data abbonamento");
	        title7Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title8Cell = titlesRow.createCell(colOffset);
	        title8Cell.setCellValue("Data prova gratuita");
	        title8Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title9Cell = titlesRow.createCell(colOffset);
	        title9Cell.setCellValue("Codice fiscale");
	        title9Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title10Cell = titlesRow.createCell(colOffset);
	        title10Cell.setCellValue("Email");
	        title10Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title11Cell = titlesRow.createCell(colOffset);
	        title11Cell.setCellValue("Paese");
	        title11Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title12Cell = titlesRow.createCell(colOffset);
	        title12Cell.setCellValue("Indirizzo");
	        title12Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title13Cell = titlesRow.createCell(colOffset);
	        title13Cell.setCellValue("Data nascita");
	        title13Cell.setCellStyle(titlesStyle);

	        colOffset++;
	        Cell title14Cell = titlesRow.createCell(colOffset);
	        title14Cell.setCellValue("Data affiliazione");
	        title14Cell.setCellStyle(titlesStyle);
	        
	        colOffset++;
	        Cell title15Cell = titlesRow.createCell(colOffset);
	        title15Cell.setCellValue("Data prima registrazione 3D");
	        title15Cell.setCellStyle(titlesStyle);
	        
	        for(Person p: personDao.findPersonsWithoutCertificate()) {
	        	
	        	rowOffset++;
	        	colOffset = 0;
		        Row personRow = sheetsData.createRow((short)rowOffset);
		        Cell cell1 = personRow.createCell(colOffset);
//		        cell1.setCellValue(p.getId());
		        if(p.getNumber() != null) {
		        	cell1.setCellValue(p.getNumber());
		        }
		        
		        colOffset++;
		        Cell cell2 = personRow.createCell(colOffset);
		        cell2.setCellValue(p.getSurname() != null ? p.getSurname() : "");
		        
		        colOffset++;
		        Cell cell3 = personRow.createCell(colOffset);
		        cell3.setCellValue(p.getName() != null ? p.getName() : "");
		        
		        colOffset++;
		        Cell cell4 = personRow.createCell(colOffset);
		        cell4.setCellValue(p.getPhone() != null ? p.getPhone() : "");
		        
		        colOffset++;
		        Cell cell5 = personRow.createCell(colOffset);
		        cell5.setCellValue(p.getRegistrationDate() != null ? 
		        		sdf.format(p.getRegistrationDate()) : "");
		        
		        colOffset++;
		        Cell cell6 = personRow.createCell(colOffset);
		        cell6.setCellValue(p.getCertificationDate() != null ? 
		        		sdf.format(p.getCertificationDate()) : "");
		        
		        colOffset++;
		        Cell cell7 = personRow.createCell(colOffset);
		        cell7.setCellValue(p.getSubscriptionDate() != null ? 
		        		sdf.format(p.getSubscriptionDate()) : "");
		        
		        colOffset++;
		        Cell cell8 = personRow.createCell(colOffset);
		        cell8.setCellValue(p.getFreeEntryDate() != null ? 
		        		sdf.format(p.getFreeEntryDate()) : "");
		        
		        colOffset++;
		        Cell cell9 = personRow.createCell(colOffset);
		        cell9.setCellValue(p.getCf()  != null ? p.getCf() : "");
		        
		        colOffset++;
		        Cell cell10 = personRow.createCell(colOffset);
		        cell10.setCellValue(p.getEmail() != null ? p.getEmail() : "");
		        
		        colOffset++;
		        Cell cell11 = personRow.createCell(colOffset);
		        cell11.setCellValue(p.getCity() != null ? p.getCity() : "");
		        
		        colOffset++;
		        Cell cell12 = personRow.createCell(colOffset);
		        cell12.setCellValue(p.getAddress() != null ? p.getAddress() : "");
		        
		        colOffset++;
		        Cell cell13 = personRow.createCell(colOffset);
		        cell13.setCellValue(p.getBirthDate() != null ? 
		        		sdf.format(p.getBirthDate()) : "");

		        colOffset++;
		        Cell cell14 = personRow.createCell(colOffset);
		        cell14.setCellValue(p.getAffiliationDate() != null ? 
		        		sdf.format(p.getAffiliationDate()) : "");
		        
		        colOffset++;
		        Cell cell15 = personRow.createCell(colOffset);
		        cell15.setCellValue(p.getCreationDate() != null ? 
		        		sdf.format(p.getCreationDate()) : "");
	        }
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        try {
		        wb.write(bos);
	        } finally {
	            bos.close();
	        }
	        byte[] bytes = bos.toByteArray();
	        return bytes;
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
