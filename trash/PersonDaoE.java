package org.climbing.repo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.climbing.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class PersonDaoE {

	@Autowired
    private EntityManager em;

    public Person findById(Integer id) {
        return em.find(Person.class, id);
    }

    public void delete(Integer id) {
    	Person p = em.find(Person.class, id);
    	em.remove(p);
    }
    
    public List<Person> findAll(String order, String direction){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Person> criteria = cb.createQuery(Person.class);
        Root<Person> persons = criteria.from(Person.class);
        criteria.select(persons);
        if(order != null) {
    		if("asc".equals(direction)) {
    			criteria.orderBy(cb.asc(persons.get(order)));
    		} else {
    			criteria.orderBy(cb.desc(persons.get(order)));
    		}
        }
        return em.createQuery(criteria).getResultList();
    }
    
    public List<Person> findPersonsWithoutCertificate() {
    	
    	CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Person> criteria = cb.createQuery(Person.class);
        Root<Person> persons = criteria.from(Person.class);
        
        criteria.select(persons);
        
        Expression<Date> registrationField = persons.get("registrationDate");
        Expression<Date> certificationField = persons.get("certificationDate");
        Calendar yearStart = Calendar.getInstance();
        yearStart.set(Calendar.MONTH, 0);
        yearStart.set(Calendar.DAY_OF_MONTH, 1);
        
        Calendar oneYearAgo = Calendar.getInstance();
        oneYearAgo.add(Calendar.YEAR, -1);
        criteria.where(cb.and(
    		cb.greaterThan(registrationField, yearStart.getTime()),
    		cb.or(
    				cb.lessThan(certificationField, oneYearAgo.getTime()),
        			cb.isNull(certificationField)
        		)
//    		cb.or(
//    			cb.lessThan(certificationField, yearStart.getTime()),
//    			cb.isNull(certificationField)
//    		)
		));
        criteria.orderBy(cb.asc(persons.get("surname")));
        Query query = em.createQuery(criteria);
        return (List<Person>)query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
	public List<Person> search(String searchToken, String order, String direction,
    		int firstResult, int maxResults){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Person> criteria = cb.createQuery(Person.class);
        Root<Person> persons = criteria.from(Person.class);
        
        criteria.select(persons);
        if(order != null) {
    		if("asc".equals(direction)) {
    			criteria.orderBy(cb.asc(persons.get(order)));
    		} else {
    			criteria.orderBy(cb.desc(persons.get(order)));
    		}
        }
        Expression<String> nameField = persons.get("name");
        Expression<String> surnameField = persons.get("surname");
        Expression<Integer> numberField = persons.get("number");
//        Expression<String> emailField = persons.get("email");
        Predicate orStrings = cb.or(
    		cb.like(nameField, "%" + searchToken + "%"), 
    		cb.like(surnameField, "%" + searchToken + "%")
		);
//        criteria.where(cb.or(
//        		cb.like(nameField, "%" + searchToken + "%"), 
//        		cb.like(surnameField, "%" + searchToken + "%"),
//        		cb.like(emailField, "%" + searchToken + "%")
//        		));
        Predicate orInts = null;
        try {
        	orInts = cb.equal(numberField, Integer.parseInt(searchToken));
        } catch (Exception e) {}

        if(orInts != null) {
        	criteria.where(cb.or(orStrings, orInts));
        } else {
        	criteria.where(orStrings);
        }
        
        Query query = em.createQuery(criteria);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        
        return (List<Person>)query.getResultList();
    }
    
    public Long searchCount(String searchToken) {
    	
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
    	Root<Person> persons = criteria.from(Person.class);
    	
    	criteria.select(cb.count(persons));
    	Expression<String> nameField = persons.get("name");
        Expression<String> surnameField = persons.get("surname");
        Expression<String> emailField = persons.get("email");
        
    	criteria.where(cb.or(
    			cb.like(nameField, "%" + searchToken + "%"), 
    			cb.like(surnameField, "%" + searchToken + "%"),
    			cb.like(emailField, "%" + searchToken + "%")));
    	
    	return em.createQuery(criteria).getSingleResult();
    }
    
    public Person save(Person person) {
    	return em.merge(person);
    }

	public Integer getNextNumber() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaQuery<Number> criteria = cb.createQuery(Number.class);
    	Root<Person> root = criteria.from(Person.class);
    	
    	criteria.select(cb.max(root.<Number>get("number")));
        Number ret = em.createQuery(criteria).getSingleResult();
        if(ret == null) {
        	return 0;
        } else {
        	return ret.intValue();
        }
	}

	public List<Person> findMailingPersons() {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Person> criteria = cb.createQuery(Person.class);
        Root<Person> persons = criteria.from(Person.class);
        
        criteria.select(persons);
        Expression<String> mailingField = persons.get("mailing");
        Expression<String> emailField = persons.get("email");
        criteria.where(cb.equal(mailingField, new Boolean(true)));
        criteria.where(cb.isNotNull(emailField));
        
        Query query = em.createQuery(criteria);
        
        return (List<Person>)query.getResultList();
	}
}
