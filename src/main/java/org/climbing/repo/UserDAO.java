package org.climbing.repo;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.climbing.domain.User;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserDAO extends BaseHibernateDAO {

	private static final Logger log = LoggerFactory
			.getLogger(PersonDAO.class);

    public User findById(Integer id) {
    	log.debug("getting User instance with id: " + id);
		
		try {
			User instance = (User) getSession().get(User.class, id);
			return instance;
			
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
    }

    public User findByUsername(String username) {
    	
    	DetachedCriteria dc = DetachedCriteria.forClass(User.class);
    	dc.add(Restrictions.eq("username", username));
    	
    	List<User> users = findByCriteria(dc);
    	
    	if(!CollectionUtils.isEmpty(users)) {
        	return users.get(0);
        }
        return null;
    	
    }

    public List<User> findAll(String order, String direction) {
    	
    	log.debug("finding all User instances");
		try {
			
			DetachedCriteria dc = DetachedCriteria.forClass(User.class);
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
}
