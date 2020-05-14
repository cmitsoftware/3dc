package org.climbing.repo;

import org.apache.commons.lang.StringUtils;
import org.climbing.domain.Subscription;
import org.hibernate.criterion.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Repository
@Transactional
public class SubscriptionDAO extends BaseHibernateDAO{

	private static final Logger log = LoggerFactory
			.getLogger(SubscriptionDAO.class);

    public Subscription findById(Integer id) {
    	log.debug("getting Subscription instance with id: " + id);

		try {
			Subscription instance = (Subscription) getSession().get(Subscription.class, id);
			return instance;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
    }

    public void delete(Subscription persistentInstance) {

    	log.debug("deleting Subscription instance");
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

    public List<Subscription> findAllSubscriptionOfPerson(Integer personId) {

		DetachedCriteria dc = DetachedCriteria.forClass(Subscription.class);
		dc.add(Restrictions.eq("person", personId));
		return findByCriteria(dc);
	}

	public void deleteAllSubscriptionOfPerson(Integer personId) {

    	List<Subscription> subscriptions = findAllSubscriptionOfPerson(personId);
		for (Subscription subscription : (subscriptions!= null ? subscriptions : new ArrayList<Subscription>())) {
			delete(subscription);
		}
	}
}
