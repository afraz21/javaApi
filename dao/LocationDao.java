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
import org.iqvis.nvolv3.utils.MongoDBCollections;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
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
public class LocationDao {

	protected static Logger logger = Logger.getLogger("service");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	@Autowired
	VenueDao venueDao;

	@Autowired
	MediaDao mediaDao;

	/**
	 * Retrieves a single Location
	 * 
	 * @param String
	 *            id
	 * @return Location
	 * @throws NotFoundException
	 */
	public Location get(String id, String organizerId) throws NotFoundException {

		// logger.debug("Retrieving an existing location");

		Query query = new Query(Criteria.where("venues.locations._id").is(id).and("id").is(organizerId));

		query.fields().include("venues.locations.$");

		// logger.debug(query);

		User user = mongoTemplate.findOne(query, User.class, MongoDBCollections.USER.toString());

		if (user != null && user.getVenues() != null) {

			Venue venue = user.getVenues().get(0);

			if (venue != null) {

				if (venue.getLocations() != null && venue.getLocations().size() > 0) {

					List<Location> locations = Lambda.select(venue.getLocations(), Lambda.having(Lambda.on(Location.class).getId(), Matchers.equalTo(id)));

					if (locations.size() > 0) {

						if (!locations.get(0).getIsDeleted()) {

							return locations.get(0);
						}

					}
				}
			}
		}
		else {

			return null;
		}

		throw new NotFoundException(id, "Location");

	}

	/**
	 * Adds a new Location
	 * 
	 * @param Location
	 *            location
	 * @return location
	 */
	public Location add(Location location) throws Exception {
		logger.debug("Adding a new location");

		Boolean flag = false;

		try {

			User user = mongoTemplate.findOne(new Query(Criteria.where("venues._id").is(location.getVenueId())), User.class);

			if (user != null) {

				// Venue venue = null;

				location.setId(UUID.randomUUID().toString());

				location.setCreatedDate(new DateTime());

				if (user.getVenues() != null && user.getVenues().size() > 0) {

					for (Venue venue_element : user.getVenues()) {

						if (venue_element.getId().equals(location.getVenueId())) {

							flag = true;

							if (venue_element.getLocations() != null) {

								venue_element.getLocations().add(location);

							}

							else {

								List<Location> venueList = new ArrayList<Location>();

								venueList.add(location);

								venue_element.setLocations(venueList);

							}

						}

					}

					// venue = user.getVenues().get(0);
					//
					// location.setId(UUID.randomUUID().toString());
					//
					// location.setCreatedDate(new DateTime());
					//
					// if (venue.getLocations() != null) {
					//
					// venue.getLocations().add(location);
					//
					// }
					// else {
					//
					// List<Location> venueList = new ArrayList<Location>();
					//
					// venueList.add(location);
					//
					// venue.setLocations(venueList);
					// }

					mongoTemplate.save(user);

				}
				else {
					throw new NotFoundException(location.getVenueId(), "Venue");
				}

			}
			else {

				throw new NotFoundException(location.getVenueId(), "Venue");
			}

			if (!flag) {
				throw new NotFoundException(location.getVenueId(), "Venue");
			}

			return location;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to add new location", e);

			throw e;
		}
	}

	/**
	 * Edits an existing Location
	 * 
	 * @param Location
	 *            location
	 * @return Location
	 */
	public Location edit(Location location, String organizerId) throws Exception {
		logger.debug("Editing existing location");

		try {

			Venue venue = venueDao.get(location.getVenueId(), organizerId);

			List<Location> updatedLocations = new ArrayList<Location>();

			if (venue.getLocations() != null && venue.getLocations().size() > 0) {

				for (Location existinglocation : venue.getLocations()) {

					if (!location.getId().equals(existinglocation.getId())) {

						updatedLocations.add(existinglocation);
					}

				}

			}

			updatedLocations.add(location);

			venue.setLocations(updatedLocations);

			venueDao.edit(venue);

			/*
			 * MongoConverter converter = mongoTemplate.getConverter();
			 * 
			 * DBObject newLocationRec = (DBObject)
			 * converter.convertToMongoType(location);
			 * 
			 * Query query =
			 * Query.query(Criteria.where("venues._id").is(location
			 * .getVenueId()).and("id").is(organizerId));
			 * 
			 * Update update = new Update().set("venues.locations.$",
			 * newLocationRec);
			 * 
			 * mongoTemplate.updateFirst(query, update, User.class);
			 * 
			 * logger.debug("query is ::"+query.getQueryObject());
			 */
			return location;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to edit existing location", e);

			throw e;
		}

	}

	/**
	 * Deletes an existing Location
	 * 
	 * @param String
	 *            id
	 * @return Boolean
	 */
	public Boolean delete(String id) {
		logger.debug("Deleting existing Venue");

		try {

			Query query = Query.query(Criteria.where("locations._id").is(id));

			Update update = new Update().set("locations.$.isDeleted", true);

			mongoTemplate.updateFirst(query, update, Venue.class);

			return true;

		}
		catch (Exception e) {
			logger.error("An error has occurred while trying to delete location", e);
			return false;
		}
	}

	public Page<Location> getAll(Criteria userCriteria, Pageable pageAble, String organizerId) {

		logger.debug("Retrieving all locations");

		String collection = "venues.locations";

		Aggregation aggregation = Aggregation.newAggregation(Aggregation.unwind("venues"), Aggregation.unwind(collection), Aggregation.project("venues"), Aggregation.match(userCriteria.and(collection + ".isDeleted").is(false).and("venues.organizerId").is(organizerId)), Aggregation.skip(pageAble.getOffset()), Aggregation.limit(pageAble.getPageSize()));

		AggregationResults<Location> result = mongoTemplate.aggregate(aggregation, User.class, Location.class);

		DBObject object = result.getRawResults();

		@SuppressWarnings("unchecked")
		List<DBObject> dbObjects = (List<DBObject>) object.get("result");

		List<Location> locations = new ArrayList<Location>();

		Gson gson = new Gson();

		for (DBObject dbObject : dbObjects) {

			DBObject venue = (DBObject) dbObject.get("venues");

			Location obj = gson.fromJson(venue.get("locations").toString(), Location.class);

			DBObject location = (DBObject) venue.get("locations");

			obj.setId(location.get("_id").toString());

			if (null != location.get("picture")) {

				DBRef picture = (DBRef) location.get("picture");

				String mediaID = picture.getId().toString();

				Media media = mediaDao.get(mediaID);

				obj.setPicture(media);
			}

			locations.add(obj);
		}

		// Total Records Counting
		Aggregation aggregationForTotalCount = Aggregation.newAggregation(Aggregation.unwind("venues"), Aggregation.unwind(collection), Aggregation.project("venues"), Aggregation.match(userCriteria));

		AggregationResults<Location> resultCount = this.mongoTemplate.aggregate(aggregationForTotalCount, User.class, Location.class);

		DBObject objectCount = resultCount.getRawResults();

		@SuppressWarnings("unchecked")
		List<DBObject> totalCount = (List<DBObject>) objectCount.get("result");

		Page<Location> locationPage = new PageImpl<Location>(locations, pageAble, totalCount.size());

		return locationPage;
	}

	public String getLocationDetailUrl(Location location, String organizerId, HttpServletRequest request) {

		String detailUrl = "";

		String replaceUrlToken = Urls.LOCATION_BASE_URL;

		replaceUrlToken += Urls.TOP_LEVEL.replace(Urls.TOP_LEVEL_ORGANIZER_ID, organizerId) + Urls.GET_LOCATION.replace(Urls.GET_LOCATION, location.getId().toString());

		detailUrl = Utils.getURLWithContextPath(request) + replaceUrlToken;

		return detailUrl;
	}
}
