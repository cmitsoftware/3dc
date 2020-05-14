package org.climbing.repo;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.climbing.domain.Person;
import org.climbing.domain.Subscription;
import org.climbing.domain.SubscriptionType;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class PersonDAO extends BaseHibernateDAO{

	private static final Logger log = LoggerFactory
			.getLogger(PersonDAO.class);

    public Person findById(Integer id) {
    	log.debug("getting Person instance with id: " + id);
		
		try {
			Person instance = (Person) getSession().get(Person.class, id);
			return instance;
			
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
    }

    public void delete(Person persistentInstance) {
    	
    	log.debug("deleting Person instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
    }
    
    public void delete(Integer id) {
    	delete(findById(id));
    }
    
    public Person save(Person transientInstance) {
		log.debug("saving Person instance");
		try {

			// delete persisted subscriptions not anymore needed (link to person removed)
			Set<Subscription> subscriptions = new HashSet<>();
			for (Subscription subscription: (transientInstance.getSubscriptions() != null ? transientInstance.getSubscriptions() : new ArrayList<Subscription>())) {
				if (subscription.getId() != null && subscription.getPerson() == null) {
					getSession().delete(subscription);
				} else {
					subscriptions.add(subscription);
				}
			}
			transientInstance.setSubscriptions(subscriptions);

			getSession().saveOrUpdate(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
		return transientInstance;
	}

	public Person save2(Person transientInstance, Set<Subscription> newSubscriptions) {
		log.debug("saving Person instance");
		try {

			for (Subscription subscription: transientInstance.getSubscriptions()) {
					getSession().delete(subscription);
			}
			transientInstance.setSubscriptions(newSubscriptions);

			getSession().saveOrUpdate(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
		return transientInstance;
	}

	public void deleteSubscriptions(Set<Subscription> subscriptions) {
		log.debug("deleting subscriptions");
		try {

			for (Subscription subscription: subscriptions) {
				getSession().delete(subscription);
			}

			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public Person preparePersonSubscriptionsForSave(Person person, Set<Subscription> formSubscriptions) {

		for (Subscription sessionSubscription: (person.getSubscriptions() != null ? person.getSubscriptions() : new ArrayList<Subscription>())) {
			Optional<Subscription> optionalSubscription = formSubscriptions.stream().filter(
					subscriptionEl -> subscriptionEl.getTypeName().equals(sessionSubscription.getTypeName())).findAny();
			if (optionalSubscription.isPresent()) {
				//update subscription
				Subscription formSubscription = optionalSubscription.get();
				sessionSubscription.setStartDate(formSubscription.getStartDate());
				sessionSubscription.setReferenceYear(formSubscription.getReferenceYear());
				sessionSubscription.setEndDate(formSubscription.getEndDate());
				formSubscriptions.remove(formSubscription);
			} else {
				//delete subscription, removing the person link will force Dao delete it
				sessionSubscription.setPerson(null);
			}
		}

		//add new subscriptions
		formSubscriptions.forEach(subscription -> subscription.setPerson(person));
		if (person.getSubscriptions()!=null) {
			person.getSubscriptions().addAll(formSubscriptions);
		} else{
			person.setSubscriptions(formSubscriptions);
		}

		return person;
	}
    
    public List<Person> findAll(String order, String direction){
    	
    	log.debug("finding all Person instances");
		try {
			
			DetachedCriteria dc = DetachedCriteria.forClass(Person.class);
			if(order != null) {
	    		if("asc".equals(direction)) {
	    			dc.addOrder(Order.asc(order));
	    		} else {
	    			dc.addOrder(Order.desc(order));
	    		}
	        }
			return findByCriteria(dc);
			
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
    }
    
    public List<Person> findPersonsWithoutCertificate(Boolean mailing) {
    	
    	Calendar yearStart = Calendar.getInstance();
    	yearStart.set(Calendar.MONTH, 0);
    	yearStart.set(Calendar.DAY_OF_MONTH, 1);
    	
    	Calendar oneYearAgo = Calendar.getInstance();
    	oneYearAgo.add(Calendar.YEAR, -1);
         
    	DetachedCriteria dc = DetachedCriteria.forClass(Person.class);
    	dc.add(Restrictions.and(
    			Restrictions.gt("registrationDate", yearStart.getTime()),
    			Restrictions.or(
    					Restrictions.lt("certificationDate", oneYearAgo.getTime()),
    					Restrictions.isNull("certificationDate")
    					)
    			));
    	if(mailing != null) {
    		dc.add(Restrictions.eq("mailing", mailing));
    	}
    	dc.addOrder(Order.asc("surname"));
    	return findByCriteria(dc);
    }
    
    public List<Person> findThisYearSubscribed(String order, String direction) {
    	
    	Calendar yearStart = Calendar.getInstance();
    	yearStart.set(Calendar.MONTH, 0);
    	yearStart.set(Calendar.DAY_OF_MONTH, 1);
    	Calendar oneYearAgo = Calendar.getInstance();
    	oneYearAgo.add(Calendar.YEAR, -1);
         
    	DetachedCriteria dc = DetachedCriteria.forClass(Person.class);
    	dc.add(Restrictions.gt("registrationDate", yearStart.getTime()));
    	if(order != null) {
    		if("asc".equals(direction)) {
    			dc.addOrder(Order.asc(order));
    		} else {
    			dc.addOrder(Order.desc(order));
    		}
        }
    	return findByCriteria(dc);
    }
    
    @SuppressWarnings("unchecked")
	public List<Person> search(String searchToken, String order, String direction,
    		int firstResult, int maxResults){
    	
    	DetachedCriteria dc = DetachedCriteria.forClass(Person.class);
    	if(order != null) {
    		if("asc".equals(direction)) {
    			dc.addOrder(Order.asc(order));
    		} else {
    			dc.addOrder(Order.desc(order));
    		}
        }
    	
    	if(!StringUtils.isEmpty(searchToken)) {
    		Disjunction filter = Restrictions.disjunction();
        	filter.add(Restrictions.like("name", "%" + searchToken + "%"));
        	filter.add(Restrictions.like("surname", "%" + searchToken + "%"));
        	try {
        		filter.add(Restrictions.eq("number", Integer.parseInt(searchToken)));
        	} catch (Exception e) {}
        	
        	if(!StringUtils.isEmpty(searchToken)) {
    			dc.add(filter);
    		}
		}
    	
    	return findByCriteria(dc, firstResult, maxResults);
    }
    
    public Long searchCount(String searchToken) {

		DetachedCriteria dc = DetachedCriteria.forClass(Person.class);

		Disjunction filter = Restrictions.disjunction();
		filter.add(Restrictions.like("name", "%" + searchToken + "%"));
		filter.add(Restrictions.like("surname", "%" + searchToken + "%"));
		try {
			filter.add(Restrictions.eq("number", Integer.parseInt(searchToken)));
		} catch (Exception e) {}

		if(!StringUtils.isEmpty(searchToken)) {
			dc.add(filter);
		}

		return (long)getCount(dc);
    }

	public Integer getNextNumber() {
		
		DetachedCriteria dc = DetachedCriteria.forClass(Person.class);
    	dc.setProjection(Projections.max("number"));
    	Object ret = uniqueResult(dc);
    	if(ret == null) {
    		return 0;
    	} else {
    		return (Integer)ret;
    	}
	}

	public List<Person> findMailingAll() {
		
		DetachedCriteria dc = DetachedCriteria.forClass(Person.class);
    	dc.add(Restrictions.eq("mailing", true));
    	dc.add(Restrictions.isNotNull("email"));
    	dc.add(Restrictions.like("email", "%@%"));
    	
    	return findByCriteria(dc);
	}

	public List<Person> findMailingAllWithNotValidEmail() {

		DetachedCriteria dc = DetachedCriteria.forClass(Person.class);
		dc.add(Restrictions.eq("mailing", true));
		dc.add( Restrictions.or()
				.add(Restrictions.isNull("email"))
				.add(Restrictions.eq("email", ""))
				.add(Restrictions.not(Restrictions.like("email", "%@%"))));

		return findByCriteria(dc);
	}
	
	public List<Person> findMailingRegistered() {
		
		Calendar yearStart = Calendar.getInstance();
		yearStart.set(Calendar.MONTH, 0);
		yearStart.set(Calendar.DAY_OF_MONTH, 0);
		
		DetachedCriteria dc = DetachedCriteria.forClass(Person.class);
    	dc.add(Restrictions.eq("mailing", true));
    	dc.add(Restrictions.isNotNull("email"));
    	dc.add(Restrictions.like("email", "%@%"));
    	dc.add(Restrictions.ge("registrationDate", yearStart.getTime()));
    	
    	return findByCriteria(dc);
	}

	public List<Person> findMailingRegisteredWithNoValidEmail() {

		Calendar yearStart = Calendar.getInstance();
		yearStart.set(Calendar.MONTH, 0);
		yearStart.set(Calendar.DAY_OF_MONTH, 0);

		DetachedCriteria dc = DetachedCriteria.forClass(Person.class);
		dc.add(Restrictions.eq("mailing", true));
		dc.add(Restrictions.ge("registrationDate", yearStart.getTime()));
		dc.add(Restrictions.or()
				.add(Restrictions.isNull("email"))
				.add(Restrictions.eq("email",""))
				.add(Restrictions.not(Restrictions.like("email", "%@%"))));

		return findByCriteria(dc);
	}
}
