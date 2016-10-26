package org.iqvis.nvolv3.dao;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class ThemeDao {
	
	protected static Logger logger = Logger.getLogger("KeyDao");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;
	
	public Object get(String themeId){
		
		Query query=new Query();
		
		query.addCriteria(Criteria.where("_id").is(themeId));
		
		return mongoTemplate.findOne(query,Object.class, "eventThemes");
	}

}
