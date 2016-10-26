package org.iqvis.nvolv3.dao;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.SystemConstant;
import org.iqvis.nvolv3.exceptionHandler.ConstantNotExistsException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@SuppressWarnings("restriction")
@Repository
public class SystemConstantDao {

	protected static Logger logger = Logger.getLogger("dao");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	/**
	 * @param env environment type flag has two possible values ["DEVELOPMENT","PRODUCTION"] 
	 * @param constantName name of constant in database document 
	 * @throws ConstantNotExistsException
	 * @return SystemConstant object will be the result you can get value for its respective function contains prefix "to"
	 * @see SystemConstant  
	 * */
	
	public SystemConstant get(String env, String constantName) throws ConstantNotExistsException {

		logger.debug("Retrieving an Configuration Constant : " + constantName);

		Query query = new Query(Criteria.where("type").is(env).and("name").is(constantName));
		
		SystemConstant systemConstant=mongoTemplate.findOne(query, SystemConstant.class);
		
		if(systemConstant==null){
			
			throw new ConstantNotExistsException(constantName);
		}

		return systemConstant;
	}

}
