package org.climbing.repo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import org.climbing.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserDaoE {

	@Autowired
    private EntityManager em;

    public User findById(Long id)
    {
        return em.find(User.class, id);
    }
    
    public User findByUsername(String username)
    {
    	CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);
        criteria.select(user);
        Expression<String> usernameField = user.get("username");
        criteria.where(
        		cb.equal(usernameField, username) 
        		);
        
        List<User> users = em.createQuery(criteria).getResultList();
        if(users != null && !users.isEmpty()) {
        	return users.get(0);
        }
        return null;
    }

    public List<User> findAll(String order, String direction)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);
        criteria.select(user);
        if(order != null) {
    		if("asc".equals(direction)) {
    			criteria.orderBy(cb.asc(user.get(order)));
    		} else {
    			criteria.orderBy(cb.desc(user.get(order)));
    		}
        }
        return em.createQuery(criteria).getResultList();
    }
}
