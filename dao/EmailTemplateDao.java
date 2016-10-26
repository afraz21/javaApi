package org.iqvis.nvolv3.dao;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.EmailTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class EmailTemplateDao {

	protected static Logger logger = Logger.getLogger("dao");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	public EmailTemplate get(String id) {

		return mongoTemplate.findOne(new Query().addCriteria(Criteria.where("deleted").is(false).and("id").is(id)), EmailTemplate.class);
	}
	
	public EmailTemplate getByUniqueName(String name) {

		return mongoTemplate.findOne(new Query().addCriteria(Criteria.where("deleted").is(false).and("uniqueTitle").is(name)), EmailTemplate.class);
	}

	public EmailTemplate getEmailByTo(String to) {

		return mongoTemplate.findOne(new Query().addCriteria(Criteria.where("deleted").is(false).and("to").is(to)), EmailTemplate.class);
	}

	public EmailTemplate get(String mailFor,String to) {

		return mongoTemplate.findOne(new Query().addCriteria(Criteria.where("deleted").is(false).and("to").is(to).and("mailFor").is(mailFor)), EmailTemplate.class);
	}

}
