package org.iqvis.nvolv3.dao;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.AppConfigKeys;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@SuppressWarnings("restriction")
@Repository
public class KeyDao {

	protected static Logger logger = Logger.getLogger("KeyDao");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	public AppConfigKeys getKeyContainer() {
		Query query = new Query();
		return mongoTemplate.findOne(query, AppConfigKeys.class, "AppConfigKeys");

	}
	
	public long getCount() {
		Query query = new Query();
		return mongoTemplate.count(query, "AppConfigKeys");

	}

}
