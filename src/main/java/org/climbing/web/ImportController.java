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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.climbing.domain.Person;
import org.climbing.repo.PersonDAO;
import org.climbing.repo.UserDAO;
import org.climbing.security.ClimbingUserDetails;
import org.climbing.util.ReportUtil;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
	PersonDAO personDao;
	
	@Autowired
	ReportUtil reportUtil;
	
	@Value("${tmp.user.path}") private String tmpUserPath;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@RequestMapping(params = "method=importPersons", method = RequestMethod.POST)
	public String importPersons(@RequestParam(value="file") MultipartFile[] file,
			HttpServletRequest request, HttpServletResponse response,
			final RedirectAttributes redirectAttributes) {
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");

		int total = 0, inserted = 0, updated = 0;
		Map<Integer, String> errors = new HashMap<Integer, String>();
		
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		
		try {

			MultipartFile multipartFile = file[0];
			File tmpFile = new File(tmpUserPath + File.separator + UUID.randomUUID().toString());
			
			multipartFile.transferTo(tmpFile);
			
			OPCPackage opcPackage = OPCPackage.open(tmpFile);
			workbook = new XSSFWorkbook(opcPackage);
			sheet = workbook.getSheetAt(0);
			
			Iterator<Row> rowIterator = null;
			rowIterator = sheet.iterator();
			// skip headers
			if (rowIterator.hasNext()) {
				rowIterator.next();
			}
			

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
										errors.put(row.getRowNum(), error);
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
											errors.put(row.getRowNum(), error);
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
									try {
										registrationDate = cell.getDateCellValue();
									} catch (Exception e) {
										log.warn("Cannot read registration date as date cell");
									}
									if(registrationDate == null ) {
										String registrationDateS = cell.getStringCellValue().trim();
										if(!StringUtils.isEmpty(registrationDateS)) {
											try {
												registrationDate = sdf1.parse(registrationDateS);
											} catch (Exception e) {}
											if(registrationDate == null) {
												try {
													registrationDate = sdf2.parse(registrationDateS);
												} catch (Exception e) {}
											}
											if(registrationDate == null) {
												String error = "";
												if(errors.containsKey(row.getRowNum())) {
													error = errors.get(row.getRowNum());
												}
												error += "Errore nel leggere il campo: Data di iscrizione 3dc annuale\n";
												errors.put(row.getRowNum(), error);
											}
										} 
									}
								break;
							case 5:
								try {
									certificationDate = cell.getDateCellValue();
								} catch (Exception e) {
									log.warn("Cannot read certification date as date cell");
								}
								if(certificationDate == null ) {
									String certificationDateS = cell.getStringCellValue().trim();
									if(!StringUtils.isEmpty(certificationDateS)) {
										try {
											certificationDate = sdf1.parse(certificationDateS);
										} catch (Exception e) {}
										if(certificationDate == null) {
											try {
												certificationDate = sdf2.parse(certificationDateS);
											} catch (Exception e) {}
										}
										if(certificationDate == null) {
											String error = "";
											if(errors.containsKey(row.getRowNum())) {
												error = errors.get(row.getRowNum());
											}
											error += "Errore nel leggere il campo: Data certificato medico\n";
											errors.put(row.getRowNum(), error);
										}
									} 
								}
								break;
							case 6:
								try {
									subscriptionDate = cell.getDateCellValue();
								} catch (Exception e) {
									log.warn("Cannot read subscription date as date cell");
								}
								if(subscriptionDate == null ) {
									String subscriptionDateS = cell.getStringCellValue().trim();
									if(!StringUtils.isEmpty(subscriptionDateS)) {
										try {
											subscriptionDate = sdf1.parse(subscriptionDateS);
										} catch (Exception e) {}
										if(subscriptionDate == null) {
											try {
												subscriptionDate = sdf2.parse(subscriptionDateS);
											} catch (Exception e) {}
										}
										if(subscriptionDate == null) {
											String error = "";
											if(errors.containsKey(row.getRowNum())) {
												error = errors.get(row.getRowNum());
											}
											error += "Errore nel leggere il campo: Data abbonamento\n";
											errors.put(row.getRowNum(), error);
										}
									} 
								}
								break;
							case 7:
								try {
									freeEntryDate = cell.getDateCellValue();
								} catch (Exception e) {
									log.warn("Cannot read free entry date as date cell");
								}
								if(freeEntryDate == null ) {
									String freeEntryDateS = cell.getStringCellValue().trim();
									if(!StringUtils.isEmpty(freeEntryDateS)) {
										try {
											freeEntryDate = sdf1.parse(freeEntryDateS);
										} catch (Exception e) {}
										if(freeEntryDate == null) {
											try {
												freeEntryDate = sdf2.parse(freeEntryDateS);
											} catch (Exception e) {}
										}
										if(freeEntryDate == null) {
											String error = "";
											if(errors.containsKey(row.getRowNum())) {
												error = errors.get(row.getRowNum());
											}
											error += "Errore nel leggere il campo: Data entrata gratuita\n";
											errors.put(row.getRowNum(), error);
										}
									} 
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
									birthDate = cell.getDateCellValue();
								} catch (Exception e) {
									log.warn("Cannot read birth date as date cell");
								}
								if(birthDate == null ) {
									String birthDateS = cell.getStringCellValue().trim();
									if(!StringUtils.isEmpty(birthDateS)) {
										try {
											birthDate = sdf1.parse(birthDateS);
										} catch (Exception e) {}
										if(birthDate == null) {
											try {
												birthDate = sdf2.parse(birthDateS);
											} catch (Exception e) {}
										}
										if(birthDate == null) {
											String error = "";
											if(errors.containsKey(row.getRowNum())) {
												error = errors.get(row.getRowNum());
											}
											error += "Errore nel leggere il campo: Data di nascita\n";
											errors.put(row.getRowNum(), error);
										}
									} 
								}
								break;
							case 13:
								try {
									affiliationDate = cell.getDateCellValue();
								} catch (Exception e) {
									log.warn("Cannot read affiliation date as date cell");
								}
								if(affiliationDate == null ) {
									String affiliationDateS = cell.getStringCellValue().trim();
									if(!StringUtils.isEmpty(affiliationDateS)) {
										try {
											affiliationDate = sdf1.parse(affiliationDateS);
										} catch (Exception e) {}
										if(affiliationDate == null) {
											try {
												affiliationDate = sdf2.parse(affiliationDateS);
											} catch (Exception e) {}
										}
										if(affiliationDate == null) {
											String error = "";
											if(errors.containsKey(row.getRowNum())) {
												error = errors.get(row.getRowNum());
											}
											error += "Errore nel leggere il campo: Data di affiliazione\n";
											errors.put(row.getRowNum(), error);
										}
									} 
								}
								break;
							case 14:
								try {
									firstRegistrationDate = cell.getDateCellValue();
								} catch (Exception e) {
									log.warn("Cannot read first registration date as date cell");
								}
								if(firstRegistrationDate == null ) {
									String firstRegistrationDateS = cell.getStringCellValue().trim();
									if(!StringUtils.isEmpty(firstRegistrationDateS)) {
										try {
											firstRegistrationDate = sdf1.parse(firstRegistrationDateS);
										} catch (Exception e) {}
										if(firstRegistrationDate == null) {
											try {
												firstRegistrationDate = sdf2.parse(firstRegistrationDateS);
											} catch (Exception e) {}
										}
										if(firstRegistrationDate == null) {
											String error = "";
											if(errors.containsKey(row.getRowNum())) {
												error = errors.get(row.getRowNum());
											}
											error += "Errore nel leggere il campo: Data prima registrazione 3d\n";
											errors.put(row.getRowNum(), error);
										} 
									}
								}
								break;
							case 15:
								try {
									approvalDate = cell.getDateCellValue();
								} catch (Exception e) {
									log.warn("Cannot read approval date as date cell");
								}
								if(approvalDate == null ) {
									String approvalDateS = cell.getStringCellValue().trim();
									if(!StringUtils.isEmpty(approvalDateS)) {
										try {
											approvalDate = sdf1.parse(approvalDateS);
										} catch (Exception e) {}
										if(approvalDate == null) {
											try {
												approvalDate = sdf2.parse(approvalDateS);
											} catch (Exception e) {}
										}
										if(approvalDate == null) {
											String error = "";
											if(errors.containsKey(row.getRowNum())) {
												error = errors.get(row.getRowNum());
											}
											error += "Errore nel leggere il campo: Data di approvazione\n";
											errors.put(row.getRowNum(), error);
										}
									} 
								}
								break;
							case 16:
//								try {
//									creationDate = cell.getDateCellValue();
//								} catch (Exception e) {
//									log.warn("Cannot read creation date as date cell");
//								}
//								if(creationDate == null ) {
//									String creationDateS = cell.getStringCellValue().trim();
//									if(!StringUtils.isEmpty(creationDateS)) {
//										try {
//											creationDate = sdf1.parse(creationDateS);
//										} catch (Exception e) {}
//										if(approvalDate == null) {
//											try {
//												creationDate = sdf2.parse(creationDateS);
//											} catch (Exception e) {}
//										}
//										if(creationDate == null) {
//											String error = "";
//											if(errors.containsKey(row.getRowNum())) {
//												error = errors.get(row.getRowNum());
//											}
//											error += "Errore nel leggere il campo: Data creazione anagrafica\n";
//											errors.put(row.getRowNum(), error);
//										}
//									} 
//								}
								break;
							}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					Person person = new Person();
					if(number != null) {
						DetachedCriteria dc = DetachedCriteria.forClass(Person.class);
						dc.add(Restrictions.eq("number", number));
						List<Person> persons = personDao.findByCriteria(dc);
						if(CollectionUtils.isEmpty(persons)) {
							createNewPerson = true;
						} else {
							person = persons.get(0);
							//TODO
							// errore se piu di un utente con stesso number
						}
					} else {
						number = personDao.getNextNumber() + 1;
					}
					
					if(createNewPerson && 
							(StringUtils.isEmpty(name) || StringUtils.isEmpty(surname))) {
						throw new Exception("Riga " + row.getRowNum() + ": impossibile creare climber senza nome e cognome");
					}
					
					person.setNumber(number);
					person.setSurname(surname);
					person.setName(name);
					person.setPhone(phone);
					person.setRegistrationDate(registrationDate);
					person.setCertificationDate(certificationDate);
					//person.setSubscriptionDate(subscriptionDate);
					person.setFreeEntryDate(freeEntryDate);
					person.setCf(cf);
					person.setEmail(email);
					person.setCity(city);
					person.setAddress(address);
					person.setBirthDate(birthDate);
					person.setAffiliationDate(affiliationDate);
					person.setFirstRegistrationDate(firstRegistrationDate);
					person.setApprovalDate(approvalDate);
					
					if(createNewPerson) {
						log.info("Creating new person with number {}", number);
						person.setUser(((ClimbingUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser());
						if(creationDate == null) {
							creationDate = new Date();
						} 
						person.setCreationDate(creationDate);
						personDao.save(person);
						inserted++;
					} else {
						log.info("Updating person with number {}", number);
						personDao.save(person);
						updated++;
					}
					
				} catch (Exception e) {
					
					e.printStackTrace();
					String error = "";
					if(errors.containsKey(row.getRowNum())) {
						error = errors.get(row.getRowNum());
					}
					error += "Errore generico, verificare nei log\n";
					errors.put(row.getRowNum(), error);
					continue;
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		redirectAttributes.addFlashAttribute("importDone", true);
		redirectAttributes.addFlashAttribute("errors", errors);
		redirectAttributes.addFlashAttribute("total", total);
		redirectAttributes.addFlashAttribute("inserted", inserted);
		redirectAttributes.addFlashAttribute("updated", updated);
		
		return "redirect:report";
	}
}
