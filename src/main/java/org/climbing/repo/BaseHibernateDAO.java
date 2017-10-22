package org.climbing.repo;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
@SuppressWarnings("rawtypes")
public class BaseHibernateDAO {

	private static final Logger log = LoggerFactory.getLogger(BaseHibernateDAO.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public List findByCriteria(DetachedCriteria detached) {
		log.debug("find by criteria");
		try {
			Criteria criteria = detached.getExecutableCriteria(sessionFactory.getCurrentSession());
			return criteria.list();
		} catch (RuntimeException re) {
			log.error("find by criteria failed {}", re);
			throw re;
		}
	}
	
	public List findByCriteria(DetachedCriteria detached, int start, int length) {
		try {
			Criteria criteria = detached.getExecutableCriteria(sessionFactory.getCurrentSession());	
			criteria.setFirstResult(start);
			criteria.setMaxResults(length);
			return criteria.list();
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("find by criteria failed: " + re);
			throw re;
		}
	}
	
	public Object uniqueResult(DetachedCriteria detached) {
		try {
			Criteria criteria = detached.getExecutableCriteria(sessionFactory.getCurrentSession());	
			return criteria.uniqueResult();
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("unique result by criteria failed: " + re);
			throw re;
		}
	}
	
	public Integer getCount(DetachedCriteria detached) {
		
		detached.setProjection(Projections.rowCount());
		Criteria criteria = detached.getExecutableCriteria(sessionFactory.getCurrentSession());
		Object result = criteria.uniqueResult();
		
		if(result != null) {
			return ((Long)criteria.uniqueResult()).intValue(); 
		}
		return null;
	}
}
