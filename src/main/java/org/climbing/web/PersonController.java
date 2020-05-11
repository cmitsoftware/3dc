package org.climbing.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.climbing.domain.Person;
import org.climbing.domain.Subscription;
import org.climbing.repo.PersonDAO;
import org.climbing.security.ClimbingUserDetails;
import org.climbing.web.obj.PersonObj;
import org.climbing.web.obj.PersonSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value="/persons")
public class PersonController {

	private static final Logger log = LoggerFactory.getLogger(PersonController.class);
	
	private String[] personsTableColumn = {"number", "name", "surname", "registrationDate", "subscriptionDate", "certificationDate", "freeEntryDate"};

	@Autowired
	PersonDAO personDao;

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@RequestMapping(method=RequestMethod.GET)
	public String load(ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
		return "persons";
	}

	@RequestMapping(method=RequestMethod.GET, params = "method=detail")
	public String detail(ModelMap model,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) Integer id){
		Person p = new Person();
		if(id != null) {
			p = personDao.findById(id);


			log.info(p.getSubscriptions() != null ? "" + p.getSubscriptions().size(): " null found");
			for (Subscription sub : (p.getSubscriptions()!=null ? p.getSubscriptions() : new ArrayList<Subscription>())  ) {
				log.info("Subscription " + sub.getTypeName() + sub.getEndDate() + sub.getStartDate());
			}
		}
		model.put("person", p);
		return "person-detail";
	}

	@RequestMapping(method=RequestMethod.GET, params = "method=delete")
	public String delete(ModelMap model,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = true) Integer id, final RedirectAttributes redirectAttributes){
		try {
			
			personDao.delete(id);
			redirectAttributes.addFlashAttribute("message", "Climber eliminato");
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message", "Errore nell'eliminare il climber. chiama gianni");
		}
		return "redirect:/persons?reload=true";
	}
	
	@Transactional
	@RequestMapping(method=RequestMethod.POST, params = "method=save")
	public String save(HttpServletRequest request, HttpServletResponse response,
			ModelMap model, final RedirectAttributes redirectAttributes){

		Person person = new Person();

		try {
			redirectAttributes.addFlashAttribute("message", "Nuovo climber inserito");
			if(request.getParameter("id") != null && !"".equals(request.getParameter("id"))){
				person = personDao.findById(Integer.parseInt(request.getParameter("id")));
				redirectAttributes.addFlashAttribute("message", "Climber aggiornato");
			} else {
				person.setUser(((ClimbingUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser());
			}
			/*
			 * Get the next available number only if it is a new climber and number is not specified
			 */
			log.debug(request.getParameter("generateNumber"));
			Integer number;
			if(!StringUtils.isEmpty(request.getParameter("number"))) {
				try {
					number = Integer.parseInt(request.getParameter("number"));
					person.setNumber(number);
				} catch (Exception e) {
					log.error("Cannot convert number: {}", request.getParameter("number"));
				}
			} else {
				if(!StringUtils.isEmpty(request.getParameter("generateNumber"))){
					number = personDao.getNextNumber() + 1;
					person.setNumber(number);
				}
			}
			
			person.setAddress(request.getParameter("address"));
			if(!StringUtils.isEmpty(request.getParameter("affiliationDate"))) {
				person.setAffiliationDate(sdf.parse(request.getParameter("affiliationDate")));
			}
			if(!StringUtils.isEmpty(request.getParameter("birthDate"))) {
				person.setBirthDate(sdf.parse(request.getParameter("birthDate")));
			}
			if(!StringUtils.isEmpty(request.getParameter("certificationDate"))) {
				person.setCertificationDate(sdf.parse(request.getParameter("certificationDate")));
			}
			if(!StringUtils.isEmpty(request.getParameter("approvalDate"))) {
				person.setApprovalDate(sdf.parse(request.getParameter("approvalDate")));
			}
			if(!StringUtils.isEmpty(request.getParameter("firstRegistrationDate"))) {
				person.setFirstRegistrationDate(sdf.parse(request.getParameter("firstRegistrationDate")));
			}
			if(!StringUtils.isEmpty(request.getParameter("freeEntryDate"))) {
				person.setFreeEntryDate(sdf.parse(request.getParameter("freeEntryDate")));
			}
			if(!StringUtils.isEmpty(request.getParameter("cf"))) {
				person.setCf(request.getParameter("cf").trim());
			}
			if(!StringUtils.isEmpty(request.getParameter("city"))) {
				person.setCity(request.getParameter("city").trim());
			}
			if(!StringUtils.isEmpty(request.getParameter("email"))) {
				person.setEmail(request.getParameter("email").trim());
			}
			
			if("on".equals(request.getParameter("mailing"))){
				person.setMailing(true);
			} else {
				person.setMailing(false);
			}
			person.setName(request.getParameter("name"));
			person.setPhone(request.getParameter("phone"));
			if(!"".equals(request.getParameter("registrationDate"))) {
				person.setRegistrationDate(sdf.parse(request.getParameter("registrationDate")));
			}
			if(!"".equals(request.getParameter("subscriptionDate"))) {
				person.setSubscriptionDate(sdf.parse(request.getParameter("subscriptionDate")));
			}
			person.setSurname(request.getParameter("surname"));

			person = personDao.save(person);
			model.addAttribute("person", person);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "person-detail";
		}
		return "redirect:/persons?method=detail&id=" + person.getId();
	}

	@RequestMapping(method=RequestMethod.GET, params = "method=search")
	public ResponseEntity<PersonSearchResult> search(
			HttpServletRequest request, HttpServletResponse response) {

		try {
			
			/*
			 * Datatable parameters
			 */
			Integer start = Integer.parseInt(request.getParameter("start"));
			Integer length = Integer.parseInt(request.getParameter("length"));
			Integer draw = Integer.parseInt(request.getParameter("draw"));
			String searchToken = request.getParameter("search[value]");
			Integer orderColumn = Integer.parseInt(request.getParameter("order[0][column]"));
			String orderDir = request.getParameter("order[0][dir]");

			List<Person> persons = personDao.search(searchToken, 
					personsTableColumn[orderColumn], orderDir, start, length);

			long total = personDao.searchCount(searchToken);

			List<PersonObj> data = new ArrayList<PersonObj>();
			for(Person p: persons) {
				data.add(new PersonObj(p));
			}

			PersonSearchResult res = new PersonSearchResult();
			res.setData(data);
			res.setDraw(draw);
			res.setRecordsFiltered((int)total);
			res.setRecordsTotal((int)total);

			return new ResponseEntity<PersonSearchResult>(res, HttpStatus.OK);

		} catch (Exception e) {

			e.printStackTrace();
			PersonSearchResult res = new PersonSearchResult();
			res.setError("Errore. Colpa di gianni");
			return new ResponseEntity<PersonSearchResult>(res, HttpStatus.OK);
		}
	}
}
