package org.climbing.web;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.climbing.domain.Person;
import org.climbing.domain.Subscription;
import org.climbing.domain.SubscriptionType;
import org.climbing.repo.PersonDAO;
import org.climbing.repo.UserDAO;
import org.climbing.security.ClimbingUserDetails;
import org.climbing.util.ReportUtil;
import org.climbing.util.SubscriptionUtil;
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
import org.springframework.transaction.annotation.Transactional;
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

	@Autowired
	SubscriptionUtil subscriptionUtil;
	
	@Value("${tmp.user.path}") private String tmpUserPath;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");

	public static final LocalDate MAX_ALLOWED_DATE = LocalDate.of(9999,12,31);

	@Transactional
	@RequestMapping(params = "method=importPersons", method = RequestMethod.POST)
	public String importPersons(@RequestParam(value="file") MultipartFile[] file,
			HttpServletRequest request, HttpServletResponse response,
			final RedirectAttributes redirectAttributes) {

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

			List<SubscriptionType> subscriptionTypes = subscriptionUtil.getSubscriptionTypes();

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
					Date customSubscriptionStartDate = null;
					Date customSubscriptionEndDate = null;
					Set<Subscription> subscriptions = new HashSet<Subscription>();
					
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
									registrationDate = parseDateValueCell(cell);
								} catch (Exception e) {
									errors = manageDateCellErrorMessage(e.getMessage(), "Data di iscrizione 3dc annuale", errors, row);
								}
								break;
							case 5:
								try {
									certificationDate = parseDateValueCell(cell);
								} catch (Exception e) {
									errors = manageDateCellErrorMessage(e.getMessage(), "Data certificato medico", errors, row);
								}
								break;
							case 6:
								try {
									freeEntryDate = parseDateValueCell(cell);
								} catch (Exception e) {
									errors = manageDateCellErrorMessage(e.getMessage(), "Data entrata gratuita", errors, row);
								}
								break;
							case 7:
								cf = cell.getStringCellValue().trim();
								break;
							case 8:
								email = cell.getStringCellValue().trim();
								break;
							case 9:
								city = cell.getStringCellValue().trim();
								break;
							case 10:
								address = cell.getStringCellValue().trim();
								break;
							case 11:
								try {
									birthDate = parseDateValueCell(cell);
								} catch (Exception e) {
									errors = manageDateCellErrorMessage(e.getMessage(), "Data di nascita", errors, row);
								}
								break;
							case 12:
								try {
									affiliationDate = parseDateValueCell(cell);
								} catch (Exception e) {
									errors = manageDateCellErrorMessage(e.getMessage(), "Data di affiliazione", errors, row);
								}
								break;
							case 13:
								try {
									firstRegistrationDate = parseDateValueCell(cell);
								} catch (Exception e) {
									errors = manageDateCellErrorMessage(e.getMessage(), "Data prima registrazione 3d", errors, row);
								}
								break;
							case 14:
								try {
									approvalDate = parseDateValueCell(cell);
								} catch (Exception e) {
									errors = manageDateCellErrorMessage(e.getMessage(), "Data di approvazione", errors, row);
								}
								break;
							case 15:
//								try {
//									creationDate = cell.getDateCellValue();
//								} catch (Exception e) {
//									log.warn("Cannot read creation date as date cell");
//								}
//								if(creationDate == null ) {
//									String creationDateS = cell.getStringCellValue().trim();
//									if(!StringUtils.isEmpty(creationDateS)) {
//										try {
//											creationDate = sdf.parse(creationDateS);
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
							case 16:
								try {
									customSubscriptionStartDate = parseDateValueCell(cell);
								} catch (Exception e) {
									errors = manageDateCellErrorMessage(e.getMessage(), "Data inizio abbonamento Custom", errors, row);
								}
								break;
							case 17:
								try {
									customSubscriptionEndDate = parseDateValueCell(cell);
								} catch (Exception e) {
									errors = manageDateCellErrorMessage(e.getMessage(), "Data fine abbonamento Custom", errors, row);
								}
								break;
							}

							// subscriptions

							if (cell.getColumnIndex() >= 18 && !subscriptionTypes.isEmpty()) {

								int typesIndex = cell.getColumnIndex()-18;
								if (typesIndex >= 0  && typesIndex < subscriptionTypes.size()) {
									SubscriptionType subscriptionType = subscriptionTypes.get(typesIndex);
									Integer subscriptionReferenceYear = null;
									if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
										try {
											subscriptionReferenceYear = (int)cell.getNumericCellValue();
										} catch(Exception e) {
											e.printStackTrace();
											String error = "";
											if(errors.containsKey(row.getRowNum())) {
												error = errors.get(row.getRowNum());
											}
											error += "Errore nel leggere il campo: Anno di inizio abbonamento " + subscriptionType.getName() + "\n";
											errors.put(row.getRowNum(), error);
										}
									} else {
										String subscriptionReferenceYearS = cell.getStringCellValue().trim();
										if (!subscriptionReferenceYearS.isEmpty()) {
											try {
												subscriptionReferenceYear = Integer.parseInt(subscriptionReferenceYearS);
											} catch (Exception e) {
												e.printStackTrace();
												String error = "";
												if(errors.containsKey(row.getRowNum())) {
													error = errors.get(row.getRowNum());
												}
												error += "Errore nel leggere il campo: Anno di inizio abbonamento " + subscriptionType.getName() + "\n";
												errors.put(row.getRowNum(), error);
											}
										}
									}
									if(subscriptionReferenceYear!=null) {
										Subscription subscription = subscriptionUtil.buildSubscriptionFromType(subscriptionType, subscriptionReferenceYear);
										subscriptions.add(subscription);
									}
								} else {
									if ( cell.getCellType() != Cell.CELL_TYPE_BLANK ) {
										String error = errors.containsKey(row.getRowNum()) ? errors.get(row.getRowNum()) : "";
										String message = "Il numero di abbonamenti letti non coincide con gli abbonamenti configurati a sistema.";
										if ( ! error.contains(message)) {
											error += message + "\n";
											errors.put(row.getRowNum(), error);
										}
									}
								}
							}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					Person person = new Person();
					person.setNumber(number);
					person.setSurname(surname);
					person.setName(name);
					person.setPhone(phone);
					person.setRegistrationDate(registrationDate);
					person.setCertificationDate(certificationDate);
					person.setFreeEntryDate(freeEntryDate);
					person.setCf(cf);
					person.setEmail(email);
					person.setCity(city);
					person.setAddress(address);
					person.setBirthDate(birthDate);
					person.setAffiliationDate(affiliationDate);
					person.setFirstRegistrationDate(firstRegistrationDate);
					person.setApprovalDate(approvalDate);
					person.setCustomSubscriptionStartDate(customSubscriptionStartDate);
					person.setCustomSubscriptionEndDate(customSubscriptionEndDate);
					person.setSubscriptions(subscriptions);

					//Need to save person in Transactional method or the child entities remain detached.
					PersonSaveResult personSaveResult = savePerson(person, row.getRowNum());

					if (PersonSaveResult.UPDATE_CLIMBER.equals(personSaveResult)) {
						updated++;
					}
					if (PersonSaveResult.NEW_CLIMBER.equals(personSaveResult)){
						inserted++;
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

	private enum PersonSaveResult {
		NEW_CLIMBER, UPDATE_CLIMBER;
	}

	@Transactional
	PersonSaveResult savePerson(Person person, Integer rowNum) throws Exception {

		Person transientPerson = new Person();
		PersonSaveResult personSaveResult = PersonSaveResult.UPDATE_CLIMBER;

		Integer number = person.getNumber();
		if(number != null) {
			DetachedCriteria dc = DetachedCriteria.forClass(Person.class);
			dc.add(Restrictions.eq("number", number));
			List<Person> persons = personDao.findByCriteria(dc);
			if(CollectionUtils.isEmpty(persons)) {
				personSaveResult = PersonSaveResult.NEW_CLIMBER;
			} else {
				if (persons.size() > 1) {
					throw new Exception("Riga " + rowNum + ": trovate piu persone con lo stesso numero " + number );
				} else {
					transientPerson = persons.get(0);
				}
			}
		} else {
			number = personDao.getNextNumber() + 1;
			personSaveResult = PersonSaveResult.NEW_CLIMBER;
		}

		if(PersonSaveResult.NEW_CLIMBER.equals(personSaveResult) &&
				(StringUtils.isEmpty(person.getName()) || StringUtils.isEmpty(person.getSurname()))) {
			throw new Exception("Riga " + rowNum + ": impossibile creare climber senza nome e cognome");
		}

		transientPerson.setNumber(number);
		transientPerson.setSurname(person.getSurname());
		transientPerson.setName(person.getName());
		transientPerson.setPhone(person.getPhone());
		transientPerson.setRegistrationDate(person.getRegistrationDate());
		transientPerson.setCertificationDate(person.getCertificationDate());
		transientPerson.setFreeEntryDate(person.getFreeEntryDate());
		transientPerson.setCf(person.getCf());
		transientPerson.setEmail(person.getEmail());
		transientPerson.setCity(person.getCity());
		transientPerson.setAddress(person.getAddress());
		transientPerson.setBirthDate(person.getBirthDate());
		transientPerson.setAffiliationDate(person.getAffiliationDate());
		transientPerson.setFirstRegistrationDate(person.getFirstRegistrationDate());
		transientPerson.setApprovalDate(person.getApprovalDate());
		transientPerson.setCustomSubscriptionStartDate(person.getCustomSubscriptionStartDate());
		transientPerson.setCustomSubscriptionEndDate(person.getCustomSubscriptionEndDate());

		if(PersonSaveResult.NEW_CLIMBER.equals(personSaveResult)) {
			log.info("Creating new person with number {}", number);
			transientPerson.setUser(((ClimbingUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser());
			transientPerson.setCreationDate(person.getCreationDate() != null ? person.getCreationDate() : new Date());
		} else {
			log.info("Updating person with number {}", number);
		}
		transientPerson = subscriptionUtil.preparePersonWithFormSubscriptions(transientPerson, person.getSubscriptions());
		personDao.save(transientPerson);

		return personSaveResult;
	}

	private Date parseDateValueCell(Cell cell) throws IOException {

		Date date = null;
		try {
			date = cell.getDateCellValue();
		} catch (Exception e) {
		}
		if(date == null) {
			String dateAsString = cell.getStringCellValue().trim();
			if(!StringUtils.isEmpty(dateAsString)) {
				try {
					date = sdf.parse(dateAsString);
				} catch (Exception e) {}
				if(date == null) {
					try {
						date = sdf2.parse(dateAsString);
					} catch (Exception e) {}
				}
				if(date == null) {
					throw new IOException("Error readind date ");
				} else {
					if ( checkMaxAllowedDate(date) ) {  // 9999-12-31 23:59:59, after this date mysql rise error
						throw new IOException("Date exceeded max allowed limited 9999-12-31 23:59:59");
					}
				}
			}
		}

		return date;
	}
	
	
	private Map<Integer, String> manageDateCellErrorMessage(String message, String cellName, Map<Integer, String> errors, Row row) {

		log.error("Error reading " + cellName + " " + message);
		String error = "";
		if(errors.containsKey(row.getRowNum())) {
			error = errors.get(row.getRowNum());
		}
		error += "Errore nel leggere il campo: " + cellName + "\n";
		errors.put(row.getRowNum(), error);
		return errors;
	}

	private boolean checkMaxAllowedDate(Date date) {

		return  date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().compareTo(MAX_ALLOWED_DATE) > 0;
	}

}
