package org.iqvis.nvolv3.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hamcrest.Matchers;
import org.iqvis.nvolv3.domain.Location;
import org.iqvis.nvolv3.domain.Media;
import org.iqvis.nvolv3.domain.User;
import org.iqvis.nvolv3.domain.Venue;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.mobile.bean.Event;
import org.iqvis.nvolv3.search.OrderBy;
import org.iqvis.nvolv3.search.QueryOperator;
import org.iqvis.nvolv3.utils.MongoDBCollections;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import ch.lambdaj.Lambda;

import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.mongodb.DBRef;

@SuppressWarnings("restriction")
@Repository
public class VenueDao {

	protected static Logger logger = Logger.getLogger("service");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	@Autowired
	MediaDao mediaDao;
	
	@Autowired EventDao eventDao;

	/**
	 * Retrieves a single Venue
	 * 
	 * @param String
	 *            id
	 * @return Venue
	 * @throws NotFoundException
	 */
	public Venue get(String id, String organizerId) throws NotFoundException {

		// logger.debug("Retrieving an existing venue");

		Query query = new Query(Criteria.where("id").is(organizerId).and("venues.isDeleted").is(false));

		query.fields().include("venues");

		User user = mongoTemplate.findOne(query, User.class, MongoDBCollections.USER.toString());

		if (user == null) {
			throw new NotFoundException(organizerId, "Organizer");
		}

		List<Venue> venues = Lambda.select(user.getVenues(), Lambda.having(Lambda.on(Venue.class).getId(), Matchers.equalTo(id)));

		if (venues != null && venues.size() > 0) {

			return venues.get(0);

		}
		else {

			throw new NotFoundException(id, "Venues");
		}
	}

	/**
	 * Adds a new Venue
	 * 
	 * @param Venue
	 *            venue
	 * @return Venue
	 */
	public Venue add(Venue venu) throws Exception {
		logger.debug("Adding a new venue");

		try {

			User e = mongoTemplate.findOne(new Query(Criteria.where("id").is(venu.getOrganizerId())), User.class);

			if (e != null) {

				String venueId = UUID.randomUUID().toString();

				venu.setId(venueId);

				if (e.getVenues() != null) {

					addLocationInVenue(venu, venueId);

					e.getVenues().add(venu);

				}
				else {

					addLocationInVenue(venu, venueId);

					// adding venu
					List<Venue> vList = new ArrayList<Venue>();

					vList.add(venu);

					e.setVenues(vList);

				}

				mongoTemplate.save(e);

			}
			else {

				throw new NotFoundException(venu.getOrganizerId(), "Organizer");
			}

			return venu;

		}
		catch (Exception e) {
			logger.error("An error has occurred while trying to add new venue", e);
			throw e;
		}
	}

	/**
	 * Edits an existing Venue
	 * 
	 * @param Venue
	 *            venue
	 * @return Venue
	 */
	public Venue edit(Venue venue) throws Exception {
		logger.debug("Editing existing Venue");

		try {

			MongoConverter converter = mongoTemplate.getConverter();

			DBObject newVenueRec = (DBObject) converter.convertToMongoType(venue);

			Query query = Query.query(Criteria.where("venues._id").is(venue.getId()).and("id").is(venue.getOrganizerId()));

			Update update = new Update().set("venues.$", newVenueRec);

			mongoTemplate.updateFirst(query, update, User.class);
			
			eventDao.updateVenue(venue, venue.getId());

			return venue;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to edit existing venue", e);

			throw e;
		}

	}

	/**
	 * Deletes an existing Venue
	 * 
	 * @param String
	 *            id
	 * @return Boolean
	 */
	public Boolean delete(String id) {
		logger.debug("Deleting existing Venue");

		try {

			Query query = Query.query(Criteria.where("venues._id").is(id));

			Update update = new Update().set("venues.$.isDeleted", true);

			mongoTemplate.updateFirst(query, update, User.class);

			return true;

		}
		catch (Exception e) {
			logger.error("An error has occurred while trying to delete Venue", e);
			return false;
		}
	}

	/*
	 * 
	 * Get all the venues on the basis of organizer
	 */
	public Page<Venue> getAll(Criteria userCriteria, org.iqvis.nvolv3.search.Criteria search, Pageable pageAble, String organizerId) {

		logger.debug("Retrieving all venues");

		String collection = "venues";

		List<AggregationOperation> operations = new ArrayList<AggregationOperation>();

		operations.add(Aggregation.unwind(collection));

		operations.add(Aggregation.match(userCriteria.and(collection + ".isDeleted").is(false).and("id").is(organizerId)));

		operations.add(Aggregation.skip(pageAble.getOffset()));

		operations.add(Aggregation.limit(pageAble.getPageSize()));

		if (null != search && null != search.getQuery().getOrderBy() && search.getQuery().getOrderBy().size() > 0) {

			for (OrderBy orderBy : search.getQuery().getOrderBy()) {

				if (orderBy.getDirection().equalsIgnoreCase(QueryOperator.ASC)) {

					operations.add(Aggregation.sort(new Sort(Sort.Direction.ASC, collection + "." + orderBy.getField())));
				}
				else {

					operations.add(Aggregation.sort(new Sort(Sort.Direction.DESC, collection + "." + orderBy.getField())));
				}
			}

		}

		Aggregation aggregation = Aggregation.newAggregation(operations);

		AggregationResults<Venue> result = mongoTemplate.aggregate(aggregation, User.class, Venue.class);

		DBObject object = result.getRawResults();

		@SuppressWarnings("unchecked")
		List<DBObject> dbObjects = (List<DBObject>) object.get("result");

		List<Venue> venues = new ArrayList<Venue>();

		Gson gson = new Gson();

		for (DBObject dbObject : dbObjects) {

			DBObject venue = (DBObject) dbObject.get(collection);

			String id = venue.get("_id").toString();

			Venue obj = gson.fromJson(dbObject.get(collection).toString(), Venue.class);

			obj.setId(id);

			if (null != venue.get("picture")) {

				DBRef picture = (DBRef) venue.get("picture");

				String mediaID = picture.getId().toString();

				Media media = mediaDao.get(mediaID);

				obj.setPicture(media);
			}

			venues.add(obj);
		}

		// Total Records Counting
		Aggregation aggregationForTotalCount = Aggregation.newAggregation(Aggregation.unwind("venues"), Aggregation.match(userCriteria));

		AggregationResults<Venue> resultCount = this.mongoTemplate.aggregate(aggregationForTotalCount, User.class, Venue.class);

		DBObject objectCount = resultCount.getRawResults();

		@SuppressWarnings("unchecked")
		List<DBObject> total = (List<DBObject>) objectCount.get("result");

		Page<Venue> venuePage = new PageImpl<Venue>(venues, pageAble, total.size());

		return venuePage;

	}

	public String getVenueDetailUrl(Venue venue, HttpServletRequest request) {
		String detailUrl = "";

		String replaceUrlToken = Urls.VENUE_BASE_URL;

		replaceUrlToken += Urls.TOP_LEVEL.replace(Urls.TOP_LEVEL_ORGANIZER_ID, venue.getOrganizerId()) + Urls.GET_VENUE.replace(Urls.GET_TRACK, venue.getId().toString());

		detailUrl = Utils.getURLWithContextPath(request) + replaceUrlToken;

		return detailUrl;
	}

	private Venue addLocationInVenue(Venue venu, String venueId) {

		// adding location
		List<Location> locList = new ArrayList<Location>();

		if (venu.getLocations() != null && venu.getLocations().size() > 0) {

			for (Location loc : venu.getLocations()) {

				loc.setId(UUID.randomUUID().toString());

				loc.setVenueId(venueId);

				locList.add(loc);
			}

			if (locList.size() > 0) {

				venu.setLocations(locList);

			}
		}

		return venu;
	}

	public void onVenueDelete(String venueId) {
		Query query = new Query();

		query.addCriteria(Criteria.where("venueId").is(venueId).and("activities").exists(true));

		Update update = new Update();

		update.set("activities.$.location", "--");

		logger.debug(query);

		logger.debug(update);

		mongoTemplate.updateFirst(query, update, Event.class);

	}

}
