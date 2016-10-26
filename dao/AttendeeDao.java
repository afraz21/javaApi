package org.iqvis.nvolv3.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.Attendee;
import org.iqvis.nvolv3.exceptionHandler.AttendeeProfileExistsAlready;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class AttendeeDao {

	protected static Logger logger = Logger.getLogger("dao");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	public Attendee add(Attendee attendee) throws AttendeeProfileExistsAlready {

		logger.debug("Insert new attendee");

		// Map<String, Object> map = new HashMap<String, Object>();
		//
		// map.put("email", attendee.getEmail());
		//
		// map.put("deviceToken", attendee.getDeviceToken());
		//
		// long count = this.count(map);
		//
		// if (count > 0) {
		// throw new AttendeeProfileExistsAlready(attendee.getEmail());
		// }

		mongoTemplate.insert(attendee);

		return this.get(attendee.getId());
	}

	public void delete(String id) {

		Query query = new Query();

		query = query.addCriteria(Criteria.where("id").in(id).and("isDeleted").is(false));

		Update update = new Update();

		update.set("isDeleted", true);

		mongoTemplate.updateFirst(query, update, Attendee.class);
	}

	public List<Attendee> get(List<String> ids) {

		logger.debug("Retrieving all attendees in ids list");

		Query query = new Query();

		query = query.addCriteria(Criteria.where("id").in(ids).and("isDeleted").is(false));

		return mongoTemplate.find(query, Attendee.class);
	}

	public List<Attendee> get(String selector, String value) {

		logger.debug("Retrieving all attendees in ids list");

		Query query = new Query();

		query = query.addCriteria(Criteria.where(selector).is(value).and("isDeleted").is(false));

		return mongoTemplate.find(query, Attendee.class);
	}

	public List<Attendee> get(Map<String, Object> map) {

		logger.debug("Retrieving all attendees in map keys");

		Query query = new Query();

		query = query.addCriteria(Criteria.where("isDeleted").is(false));

		Set<String> keys = map.keySet();

		for (String key : keys) {

			query = query.addCriteria(Criteria.where(key).is(map.get(key)));
		}

		return mongoTemplate.find(query, Attendee.class);
	}

	public long count(String selector, String value) {

		logger.debug("counting all attendees with " + selector + " equal to " + value);

		Query query = new Query();

		query = query.addCriteria(Criteria.where(selector).is(value).and("isDeleted").is(false));

		return mongoTemplate.count(query, Attendee.class);
	}

	public long count(Map<String, Object> map) {

		Query query = new Query();

		query = query.addCriteria(Criteria.where("isDeleted").is(false));

		Set<String> keys = map.keySet();

		for (String key : keys) {

			query = query.addCriteria(Criteria.where(key).is(map.get(key)));
		}

		return mongoTemplate.count(query, Attendee.class);
	}

	public Attendee get(String id) {

		logger.debug("Retrieving all attendees in ids list");

		Query query = new Query();

		query = query.addCriteria(Criteria.where("id").in(id).and("isDeleted").is(false));

		return mongoTemplate.findOne(query, Attendee.class);
	}

	public Page<Attendee> get(Pageable pageAble, List<String> eventIds) {

		logger.debug("Retrieving all attendees pageable in ids list");

		List<Attendee> attendees = new ArrayList<Attendee>();

		Query query = new Query();

		query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where("id").in(eventIds).and("isDeleted").is(false));

		if (null != pageAble) {

			query.with(pageAble);
		}

		attendees = mongoTemplate.find(query, Attendee.class);

		long total = mongoTemplate.count(query, Attendee.class);

		Page<Attendee> attendeePage = new PageImpl<Attendee>(attendees, pageAble, total);

		return attendeePage;
	}

	public Page<Attendee> get(Pageable pageAble, Query query) {

		logger.debug("Retrieving all attendees on query");

		List<Attendee> attendees = new ArrayList<Attendee>();

		if (null != pageAble) {

			query.with(pageAble);
		}
		
		attendees = mongoTemplate.find(query, Attendee.class);

		long total = mongoTemplate.count(query, Attendee.class);

		logger.debug(query);

		Page<Attendee> attendeePage = new PageImpl<Attendee>(attendees, pageAble, total);

		return attendeePage;
	}

	public Attendee edit(Attendee attendee) throws NotFoundException {

		Attendee existingAttendee = this.get(attendee.getId());

		if (existingAttendee == null) {
			throw new NotFoundException(attendee.getId(), "Attendee");
		}

		mongoTemplate.save(attendee);

		return this.get(attendee.getId());
	}

}
