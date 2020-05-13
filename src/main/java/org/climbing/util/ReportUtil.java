package org.climbing.util;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.climbing.domain.Person;
import org.climbing.domain.Subscription;
import org.climbing.domain.SubscriptionType;
import org.climbing.repo.PersonDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportUtil {

	@Autowired
	PersonDAO personDao;

	@Autowired
	SubscriptionUtil subscriptionUtil;
	
	public byte[] buildGeneralReport() {
		try {
			List<Person> people = personDao.findAll("surname", "asc");
			return buildReport("Report generale", people);
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public byte[] buildCurrentYearReport() {

		try {
			List<Person> people = personDao.findThisYearSubscribed("surname", "asc");
			return buildReport("Report iscritti " + Calendar.getInstance().get(Calendar.YEAR), people);
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public byte[] buildPersonsWithoutCertificateReport() {

		try {
			List<Person> people = personDao.findPersonsWithoutCertificate(null);
			return buildReport("Contatori", people);
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	private byte[] buildReport(String reportName, List<Person> people) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		@SuppressWarnings("resource")
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheetsData = wb.createSheet(reportName);

		XSSFFont titlesFont = wb.createFont();
		titlesFont.setBold(true);
		CellStyle titlesStyle = wb.createCellStyle();
		titlesStyle.setAlignment(CellStyle.ALIGN_LEFT);
		titlesStyle.setFont(titlesFont);

		int rowOffset = 0;
		int colOffset = 0;
		Row titlesRow = sheetsData.createRow((short)rowOffset);

		titlesRow = addCell(titlesRow,"N.", colOffset, titlesStyle);
		titlesRow = addCell(titlesRow,"Cognome",++colOffset, titlesStyle);
		titlesRow = addCell(titlesRow,"Nome",++colOffset, titlesStyle);
		titlesRow = addCell(titlesRow,"Telefono",++colOffset, titlesStyle);
		titlesRow = addCell(titlesRow,"Data iscrizione 3dc annuale",++colOffset, titlesStyle);
		titlesRow = addCell(titlesRow,"Data certificato medico",++colOffset, titlesStyle);
		titlesRow = addCell(titlesRow,"Data prova gratuita",++colOffset, titlesStyle);
		titlesRow = addCell(titlesRow,"Codice fiscale",++colOffset, titlesStyle);
		titlesRow = addCell(titlesRow,"Email",++colOffset, titlesStyle);
		titlesRow = addCell(titlesRow,"Paese",++colOffset, titlesStyle);
		titlesRow = addCell(titlesRow,"Indirizzo",++colOffset, titlesStyle);
		titlesRow = addCell(titlesRow,"Data nascita",++colOffset, titlesStyle);
		titlesRow = addCell(titlesRow,"Data affiliazione FASI",++colOffset, titlesStyle);
		titlesRow = addCell(titlesRow,"Data richiesta prima iscrizione",++colOffset, titlesStyle);
		titlesRow = addCell(titlesRow,"Data approvazione 3dc",++colOffset, titlesStyle);
		titlesRow = addCell(titlesRow,"Data prima registrazione",++colOffset, titlesStyle);
		titlesRow = addCell(titlesRow,"Data inizio abbonamento Custom",++colOffset, titlesStyle);
		titlesRow = addCell(titlesRow,"Data fine abbonamento Custom",++colOffset, titlesStyle);

		List<SubscriptionType> subscriptionTypes = subscriptionUtil.getSubscriptionTypes();
		for (SubscriptionType subscriptionType: subscriptionTypes) {
			titlesRow = addCell(titlesRow,"Anno di inizio Abbonamento " + subscriptionType.getName(),++colOffset, titlesStyle);
		}

		for(Person p: people) {

			rowOffset++;
			colOffset = 0;
			Row personRow = sheetsData.createRow((short)rowOffset);

			personRow = addCellValueInt(personRow,p.getNumber(),colOffset, null);
			personRow = addCell(personRow,p.getSurname(),++colOffset, null);
			personRow = addCell(personRow,p.getName(),++colOffset, null);
			personRow = addCell(personRow,p.getPhone(),++colOffset, null);
			personRow = addCellValueDate(personRow,p.getRegistrationDate(),++colOffset, null, sdf);
			personRow = addCellValueDate(personRow,p.getCertificationDate(),++colOffset, null, sdf);
			personRow = addCellValueDate(personRow,p.getFreeEntryDate(),++colOffset, null, sdf);
			personRow = addCell(personRow,p.getCf(),++colOffset, null);
			personRow = addCell(personRow,p.getEmail(),++colOffset, null);
			personRow = addCell(personRow,p.getCity(),++colOffset, null);
			personRow = addCell(personRow,p.getAddress(),++colOffset, null);
			personRow = addCellValueDate(personRow,p.getBirthDate(),++colOffset, null, sdf);
			personRow = addCellValueDate(personRow,p.getAffiliationDate(),++colOffset, null, sdf);
			personRow = addCellValueDate(personRow,p.getFirstRegistrationDate(),++colOffset, null, sdf);
			personRow = addCellValueDate(personRow,p.getApprovalDate(),++colOffset, null, sdf);
			personRow = addCellValueDate(personRow,p.getCreationDate(),++colOffset, null, sdf);
			personRow = addCellValueDate(personRow,p.getCustomSubscriptionStartDate(),++colOffset, null, sdf);
			personRow = addCellValueDate(personRow,p.getCustomSubscriptionEndDate(),++colOffset, null, sdf);

			for (SubscriptionType subscriptionType: subscriptionTypes) {
				Subscription subscription = subscriptionUtil.getSubscriptionMatchingType(p.getSubscriptions(), subscriptionType);
				personRow = addCell(personRow,""+ (subscription != null ? subscription.getReferenceYear() : ""),++colOffset, null);
			}
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			wb.write(bos);
		} finally {
			bos.close();
		}
		byte[] bytes = bos.toByteArray();
		return bytes;
	}

	private Row addCell(Row row, String value, Integer colOffset, CellStyle cellStyle){
		RowCellPair rowCellPair = addCell(row, colOffset, cellStyle);
		rowCellPair.getCell().setCellValue(value != null ? value : "");
		return rowCellPair.getRow();
	}

	private Row addCellValueInt(Row row, Integer value, Integer colOffset, CellStyle cellStyle){
		RowCellPair rowCellPair = addCell(row, colOffset, cellStyle);
		if (value!=null){
			rowCellPair.getCell().setCellValue(value);
		}
		return rowCellPair.getRow();
	}

	private Row addCellValueDate(Row row, Date value, Integer colOffset, CellStyle cellStyle, SimpleDateFormat sdf){
		RowCellPair rowCellPair = addCell(row, colOffset, cellStyle);
		rowCellPair.getCell().setCellValue(value!=null ? sdf.format(value) : "");
		return rowCellPair.getRow();
	}

	private RowCellPair addCell(Row row, Integer colOffset, CellStyle cellStyle) {
		Cell cell = row.createCell(colOffset);
		if (cellStyle != null) {
			cell.setCellStyle(cellStyle);
		}
		return new RowCellPair(row, cell);
	}
}

class RowCellPair {

	Row row;
	Cell cell;

	public RowCellPair(Row row, Cell cell) {
		this.row = row;
		this.cell = cell;
	}

	public Row getRow() {return row;}
	public void setRow(Row row) {this.row = row;}
	public Cell getCell() {return cell;}
	public void setCell(Cell cell) {this.cell = cell;}
}
