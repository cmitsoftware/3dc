package org.climbing.repo;

import org.climbing.domain.Configurations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ConfigurationsDAO extends BaseHibernateDAO{

	private static final Logger log = LoggerFactory
			.getLogger(ConfigurationsDAO.class);
	
	public Configurations findByKey(String key) {
		
		log.debug("getting Configuration instance with key: " + key);
		
		try {
			Configurations instance = (Configurations) getSession().get(Configurations.class, key);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}
