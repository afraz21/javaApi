package org.iqvis.nvolv3.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.Sponsor;
import org.iqvis.nvolv3.utils.MongoDBCollections;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@SuppressWarnings("restriction")
@Repository
public class SponsorDao {

	protected static Logger logger = Logger.getLogger("service");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	@Autowired
	UserDao userDao;

	/**
	 * Retrieves a single Sponsor
	 * 
	 * @param String
	 *            id
	 * @return Sponsor
	 */
	public Sponsor get(String id) {

		logger.debug("Retrieving an existing sponsor");

		Query query = new Query(Criteria.where("id").is(id).and("isDeleted").is(false));

		Sponsor sponsor = mongoTemplate.findOne(query, Sponsor.class, MongoDBCollections.SPONSOR.toString());

		return sponsor;
	}

	/**
	 * Adds a new Sponsor
	 * 
	 * @param EventSponsor
	 *            sponsor
	 * @return Sponsor
	 */
	public Sponsor add(Sponsor sponsor) throws Exception {
		logger.debug("Adding a new sponsor");

		try {

			mongoTemplate.insert(sponsor, MongoDBCollections.SPONSOR.toString());

			return sponsor;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to add new sponser", e);

			throw e;
		}
	}

	/**
	 * Edits an existing Sponsor
	 * 
	 * @param EventSponsor
	 *            sponsor
	 * @return Sponser
	 */
	public Sponsor edit(Sponsor sponser) throws Exception {
		logger.debug("Editing existing Sponsor");

		try {

			mongoTemplate.save(sponser);

			return sponser;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to edit existing sponser", e);

			throw e;
		}

	}

	/**
	 * Deletes an existing Sponsor
	 * 
	 * @param String
	 *            id
	 * @return Boolean
	 */
	public Boolean delete(String id) {
		logger.debug("Deleting existing Venue");

		try {

			Query query = Query.query(Criteria.where("id").is(id));

			mongoTemplate.remove(query, Sponsor.class);

			return true;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to delete Sponsor", e);

			return false;
		}
	}

	/*
	 * 
	 * Get all the sponsers on the basis
	 */

	/*
	 * public List<Sponsor> getAll(String organizerId) throws NotFoundException
	 * {
	 * 
	 * logger.debug("Retrieving an existing sponsor");
	 * 
	 * Query query = new Query();
	 * 
	 * query.addCriteria(Criteria.where("isDeleted").is(false).and("organizerId")
	 * .is(organizerId));
	 * 
	 * return mongoTemplate.find(query, Sponsor.class); }
	 */

	public Page<Sponsor> getAll(Query query, Pageable pageAble, String organizerId) {

		logger.debug("Retrieving all Sponsors");

		List<Sponsor> events = null;

		if (null != pageAble) {

			query.with(pageAble);
		}

		query.addCriteria(Criteria.where("organizerId").is(organizerId));

		events = mongoTemplate.find(query, Sponsor.class);

		long total = mongoTemplate.count(query, Sponsor.class);

		logger.debug(query + "------------" + total);
		
		if(events==null){
			
			events=new ArrayList<Sponsor>();
		}

		Page<Sponsor> sponsorPage = new PageImpl<Sponsor>(events, pageAble, total);

		return sponsorPage;
	}

	public List<Sponsor> getAll(Query query, String organizerId) {

		logger.debug("Retrieving all Sponsors");

		List<Sponsor> events = null;

		query.addCriteria(Criteria.where("organizerId").is(organizerId));

		events = mongoTemplate.find(query, Sponsor.class);

		long total = mongoTemplate.count(query, Sponsor.class);

		logger.debug(query + "------------" + total);

		return events;
	}

	public String getSponserDetailUrl(Sponsor sponser, HttpServletRequest request) {

		String detailUrl = "";

		String replaceUrlToken = Urls.SPONSOR_BASE_URL;

		replaceUrlToken += Urls.TOP_LEVEL.replace(Urls.TOP_LEVEL_ORGANIZER_ID, sponser.getOrganizerId()) + Urls.GET_SPONSOR.replace(Urls.SECOND_LEVEL_DOMAIN_ID, sponser.getId().toString());

		detailUrl = Utils.getURLWithContextPath(request) + replaceUrlToken;

		return detailUrl;
	}
}
