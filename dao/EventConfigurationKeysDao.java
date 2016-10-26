package org.iqvis.nvolv3.dao;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class EventConfigurationKeysDao {
	
	protected static Logger logger = Logger.getLogger("dao");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	public Object getKeyContainer() {
		
		Query query = new Query();
		
		query.fields().include("general");
		
		logger.debug(query);
		
		return mongoTemplate.findOne(query, Object.class, "eventConfigKeys");

	}

}
