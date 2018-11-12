package org.climbing.web;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.climbing.domain.Person;
import org.climbing.repo.PersonDAO;
import org.climbing.repo.UserDAO;
import org.climbing.util.ReportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value="/report")
public class ImportController {

	private static final Logger log = LoggerFactory.getLogger(ImportController.class);
	
	@Autowired
	UserDAO userDao;
	
	@Autowired
	ReportUtil reportUtil;
	
	@Value("${tmp.user.path}") private String tmpUserPath;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@RequestMapping(params = "method=importPersons", method = RequestMethod.POST)
	public String importPersons(@RequestParam(value="file") MultipartFile[] file,
			HttpServletRequest request, HttpServletResponse response) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		
		try {

			MultipartFile fFile = file[0];
//			File tmpFile = new File(tmpUserPath + File.separator + fFile.getOriginalFilename());
//			File tmpFile = new File(tmpUserPath + File.separator + UUID.randomUUID().toString());
			
//			File tmpFile = new File("E:\\michele\\temp\\3dc\\tmp" + File.separator + fFile.getOriginalFilename());
			File tmpFile = new File("E:\\michele\\temp\\3dc\\tmp" + File.separator + UUID.randomUUID().toString());
			
			fFile.transferTo(tmpFile);
			
			OPCPackage opcPackage = OPCPackage.open(tmpFile);
			workbook = new XSSFWorkbook(opcPackage);

			sheet = workbook.getSheetAt(0);
			
			Iterator<Row> rowIterator = null;
			rowIterator = sheet.iterator();
			// skip headers
			if (rowIterator.hasNext()) {
				rowIterator.next();
			}

			int total = 0;
			Map<Integer, String> errors = new HashMap<Integer, String>();
			while (rowIterator.hasNext()) {
				
				Row row = rowIterator.next();
				log.info("Processing row {}", row.getRowNum());
				total++;

				//For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();
				
				try {
					
					boolean createNewPerson = false;

					Integer number = null;
					String surname = null;
					String name = null;
					String phone = null;
					Date registrationDate = null;
					Date certificationDate = null;
					Date subscriptionDate = null;
					Date freeEntryDate = null;
					String cf = null;
					String email = null;
					String city = null;
					String address = null;
					Date birthDate = null;
					Date affiliationDate = null;
					Date firstRegistrationDate = null;
					Date approvalDate = null;
					Date creationDate = null;
					
					while (cellIterator.hasNext()) {

						Cell cell = cellIterator.next();
						try {

							switch (cell.getColumnIndex()) {
							
							case 0:
								if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
									try {
										number = (int)cell.getNumericCellValue();
									} catch(Exception e) {
										e.printStackTrace();
										String error = "";
										if(errors.containsKey(row.getRowNum())) {
											error = errors.get(row.getRowNum());
										}
										error += "Errore nel leggere il campo: Numero\n";
									}
								} else {
									String numberS = cell.getStringCellValue().trim();
									if(!StringUtils.isEmpty(numberS)) {
										log.info("Number is empty. A new climber will be created");
										createNewPerson = true;
									} else {
										try {
											number = Integer.parseInt(numberS);
										} catch (Exception e) {
											e.printStackTrace();
											String error = "";
											if(errors.containsKey(row.getRowNum())) {
												error = errors.get(row.getRowNum());
											}
											error += "Errore nel leggere il campo: Numero\n";
										}
									}
								}
								break;
							case 1:
								surname = cell.getStringCellValue().trim();
								break;
							case 2:
								name = cell.getStringCellValue().trim();
								break;
							case 3:
								phone = cell.getStringCellValue().trim();
								break;
							case 4:
									String registrationDateS = cell.getStringCellValue().trim();
									if(!StringUtils.isEmpty(registrationDateS)) {
										try {
											registrationDate = sdf.parse(registrationDateS);
										} catch (Exception e) {
											e.printStackTrace();
											String error = "";
											if(errors.containsKey(row.getRowNum())) {
												error = errors.get(row.getRowNum());
											}
											error += "Errore nel leggere il campo: Data di iscrizione 3dc annuale\n";
										}
									} 
								break;
							case 5:
									String certificationDateS = cell.getStringCellValue().trim();
									if(!StringUtils.isEmpty(certificationDateS)) {
										try {
											certificationDate = sdf.parse(certificationDateS);
										} catch (Exception e) {
											e.printStackTrace();
											String error = "";
											if(errors.containsKey(row.getRowNum())) {
												error = errors.get(row.getRowNum());
											}
											error += "Errore nel leggere il campo: Data certificato medico\n";
										}
									} 
							case 6:
								try {
									String subscriptionDateS = cell.getStringCellValue().trim();
									if(!StringUtils.isEmpty(subscriptionDateS)) {
										subscriptionDate = sdf.parse(subscriptionDateS);
									} 
								} catch (Exception e) {
									e.printStackTrace();
								}
								break;
							case 7:
								try {
									String freeEntryDateS = cell.getStringCellValue().trim();
									if(!StringUtils.isEmpty(freeEntryDateS)) {
										freeEntryDate = sdf.parse(freeEntryDateS);
									} 
								} catch (Exception e) {
									e.printStackTrace();
								}
								break;
							case 8:
								cf = cell.getStringCellValue().trim();
								break;
							case 9:
								email = cell.getStringCellValue().trim();
								break;
							case 10:
								city = cell.getStringCellValue().trim();
								break;
							case 11:
								address = cell.getStringCellValue().trim();
								break;
							case 12:
								try {
									String birthDateS = cell.getStringCellValue().trim();
									if(!StringUtils.isEmpty(birthDateS)) {
										birthDate = sdf.parse(birthDateS);
									} 
								} catch (Exception e) {
									e.printStackTrace();
								}
								break;
							case 13:
								try {
									String affiliationDateS = cell.getStringCellValue().trim();
									if(!StringUtils.isEmpty(affiliationDateS)) {
										affiliationDate = sdf.parse(affiliationDateS);
									} 
								} catch (Exception e) {
									e.printStackTrace();
								}
								break;
							case 14:
								try {
									String firstRegistrationDateS = cell.getStringCellValue().trim();
									if(!StringUtils.isEmpty(firstRegistrationDateS)) {
										firstRegistrationDate = sdf.parse(firstRegistrationDateS);
									} 
								} catch (Exception e) {
									e.printStackTrace();
								}
								break;
							case 15:
								try {
									String approvalDateS = cell.getStringCellValue().trim();
									if(!StringUtils.isEmpty(approvalDateS)) {
										approvalDate = sdf.parse(approvalDateS);
									} 
								} catch (Exception e) {
									e.printStackTrace();
								}
								break;
							case 16:
								try {
									String creationDateS = cell.getStringCellValue().trim();
									if(!StringUtils.isEmpty(creationDateS)) {
										creationDate = sdf.parse(creationDateS);
									} 
								} catch (Exception e) {
									e.printStackTrace();
								}
								break;
							}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					Person person = new Person();
					if(createNewPerson) {
						 person.setCreationDate(new Date());
					}
					person.setNumber(number);
					person.setSurname(surname);
					person.setName(name);
					person.setPhone(phone);
					person.setRegistrationDate(registrationDate);
					person.setCertificationDate(certificationDate);
					person.setSubscriptionDate(subscriptionDate);
					person.setFreeEntryDate(freeEntryDate);
					person.setCf(cf);
					person.setEmail(email);
					person.setCity(city);
					person.setAddress(address);
					person.setBirthDate(birthDate);
					person.setAffiliationDate(affiliationDate);
					person.setFirstRegistrationDate(firstRegistrationDate);
					person.setApprovalDate(approvalDate);
					
					log.debug(person.toString());
					
					if(createNewPerson) {
						
					}
					
				} catch (Exception e) {
					
					e.printStackTrace();
					String error = "";
					if(errors.containsKey(row.getRowNum())) {
						error = errors.get(row.getRowNum());
					}
					error += "Errore generico, verificare nei log\n";
					continue;
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
