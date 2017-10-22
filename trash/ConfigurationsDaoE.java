package org.climbing.repo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import org.climbing.domain.Configurations;
import org.climbing.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ConfigurationsDaoE {

	@Autowired
    private EntityManager em;

    public Configurations findByKey(String key)
    {
        return em.find(Configurations.class, key);
    }
}
