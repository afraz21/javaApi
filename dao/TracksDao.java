package org.iqvis.nvolv3.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hamcrest.Matchers;
import org.iqvis.nvolv3.bean.EventTrack;
import org.iqvis.nvolv3.domain.Activity;
import org.iqvis.nvolv3.domain.Media;
import org.iqvis.nvolv3.domain.Track;
import org.iqvis.nvolv3.domain.User;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.mobile.bean.Event;
import org.iqvis.nvolv3.search.OrderBy;
import org.iqvis.nvolv3.search.QueryOperator;
import org.iqvis.nvolv3.service.TrackService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.MongoDBCollections;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
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
public class TracksDao {

	protected static Logger logger = Logger.getLogger("service");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	@Resource(name = Constants.SERVICE_TRACK)
	private TrackService trackService;

	@Autowired
	MediaDao mediaDao;

	/**
	 * Retrieves a single Track
	 * 
	 * @param String
	 *            id
	 * @return Track
	 * @throws NotFoundException
	 */
	public Track get(String id, String organizerId) throws NotFoundException {

		// logger.debug("Retrieving an existing Track");

		Query query = new Query(Criteria.where("id").is(organizerId).and("tracks.isDeleted").is(false));

		User user = mongoTemplate.findOne(query, User.class, MongoDBCollections.USER.toString());

		if (user == null) {

			throw new NotFoundException(organizerId, "Organizer");
		}

		List<Track> tracks = Lambda.select(user.getTracks(), Lambda.having(Lambda.on(Track.class).getId(), Matchers.equalTo(id)));

		if (tracks != null && tracks.size() > 0) {

			return tracks.get(0);

		}
		else {

			throw new NotFoundException(id, "Track");
		}
	}
	
	public long countTrackActivities(String eventId,String trackId){
		
		List<AggregationOperation> operations=new ArrayList<AggregationOperation>();
		
		operations.add(Aggregation.match(Criteria.where("id").is(eventId)));
		
		operations.add(Aggregation.unwind("activities"));
		
		operations.add(Aggregation.project("activities"));
		
		operations.add(Aggregation.match(Criteria.where("activities.isDeleted").is(false)));
		
		operations.add(Aggregation.match(Criteria.where("activities.tracks").is(trackId)));
				
		Aggregation aggregation = Aggregation.newAggregation(operations);

		AggregationResults<Activity> result = mongoTemplate.aggregate(aggregation, Event.class, Activity.class);
		
		DBObject object = result.getRawResults();

		@SuppressWarnings("unchecked")
		List<DBObject> dbObjects = (List<DBObject>) object.get("result");
		
		return dbObjects.size();
	}

	/**
	 * Adds a new Track
	 * 
	 * @param Track
	 *            track
	 * @return Track
	 */
	public Track add(Track track) throws Exception {
		logger.debug("Adding a new Track");

		try {

			User e = mongoTemplate.findOne(new Query(Criteria.where("id").is(track.getOrganizerId())), User.class);

			if (e != null) {

				track.setId(UUID.randomUUID().toString());

				if (e.getTracks() != null) {

					e.getTracks().add(track);

				}
				else {

					List<Track> tList = new ArrayList<Track>();

					tList.add(track);

					e.setTracks(tList);
				}

				mongoTemplate.save(e);

			}
			else {

				throw new NotFoundException(track.getOrganizerId(), "Organizer");
			}

			return track;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to add new track", e);

			throw e;
		}
	}

	/**
	 * Edits an existing Track
	 * 
	 * @param Track
	 *            track
	 * @return Track
	 */
	public Track edit(Track track) throws Exception {
		logger.debug("Editing existing Track");

		try {

			MongoConverter converter = mongoTemplate.getConverter();

			DBObject newTrackRec = (DBObject) converter.convertToMongoType(track);

			Query query = Query.query(Criteria.where("tracks._id").is(track.getId()));

			Update update = new Update().set("tracks.$", newTrackRec);

			mongoTemplate.updateFirst(query, update, User.class);

			return track;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to edit existing track", e);

			throw e;
		}

	}

	/**
	 * Deletes an existing Track
	 * 
	 * @param String
	 *            id
	 * @return Boolean
	 */
	public Boolean delete(String id) {
		logger.debug("Deleting existing Track");

		try {

			Query query = Query.query(Criteria.where("tracks._id").is(id));

			Update update = new Update().set("tracks.$.isDeleted", true);

			mongoTemplate.updateFirst(query, update, User.class);

			return true;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to delete Track", e);

			return false;
		}
	}

	public Page<Track> getAll(Criteria userCriteria, org.iqvis.nvolv3.search.Criteria search, Pageable pageAble, String organizerId) {

		logger.debug("Retrieving all tracks");

		String collection = "tracks";

		/*
		 * Aggregation aggregation =
		 * Aggregation.newAggregation(Aggregation.unwind(collection),
		 * Aggregation
		 * .match(userCriteria.and(collection+".isDeleted").is(false).
		 * and("id").is(organizerId)), Aggregation.skip(pageAble.getOffset()),
		 * Aggregation.limit(pageAble.getPageSize()));
		 */

		List<AggregationOperation> operations = new ArrayList<AggregationOperation>();

		operations.add(Aggregation.match(Criteria.where("id").is(organizerId)));

		operations.add(Aggregation.unwind(collection));

		operations.add(Aggregation.match(userCriteria.and(collection + ".isDeleted").is(false)));

		System.out.println("Query : " + operations.toString());

		List<Order> orderByList = new ArrayList<Order>();

		if (null != search && null != search.getQuery().getOrderBy() && search.getQuery().getOrderBy().size() > 0) {

			for (OrderBy orderBy : search.getQuery().getOrderBy()) {

				if (orderBy.getDirection().equalsIgnoreCase(QueryOperator.ASC)) {

					// operations.add(Aggregation.sort(Sort.Direction.ASC,
					// collection + "." + orderBy.getField()));

					orderByList.add(new Order(Direction.ASC, collection + "." + orderBy.getField()));

					// operations.add(Aggregation.sort(new Sort(orders)));
					// new Sort(new Order(Direction.ASC, FIELD_NAME));

				}
				else {

					// operations.add(Aggregation.sort(Sort.Direction.DESC,
					// collection + "." + orderBy.getField()));

					orderByList.add(new Order(Direction.DESC, collection + "." + orderBy.getField()));
				}
			}
			operations.add(Aggregation.sort(new Sort(orderByList)));
		}

		if (pageAble.getOffset() > 0) {
			operations.add(Aggregation.skip(pageAble.getOffset()));
		}

		operations.add(Aggregation.limit(pageAble.getPageSize()));

		Aggregation aggregation = Aggregation.newAggregation(operations);

		AggregationResults<Track> result = mongoTemplate.aggregate(aggregation, User.class, Track.class);

		/*
		 * AggregationResults<Track> result =
		 * this.mongoTemplate.aggregate(aggregation,User.class,Track.class);
		 */

		DBObject object = result.getRawResults();

		@SuppressWarnings("unchecked")
		List<DBObject> dbObjects = (List<DBObject>) object.get("result");

		List<Track> tracks = new ArrayList<Track>();

		Gson gson = new Gson();

		for (DBObject dbObject : dbObjects) {

			DBObject track = (DBObject) dbObject.get(collection);

			String id = track.get("_id").toString();

			Track obj = gson.fromJson(dbObject.get(collection).toString(), Track.class);

			obj.setId(id);

			if (null != track.get("picture")) {

				DBRef picture = (DBRef) track.get("picture");

				String mediaID = picture.getId().toString();

				Media media = mediaDao.get(mediaID);

				obj.setPicture(media);
			}

			tracks.add(obj);
		}

		// Total Records Counting
		// Aggregation aggregationForTotalCount =
		// Aggregation.newAggregation(Aggregation.unwind(collection),
		// Aggregation.match(userCriteria));
		Aggregation aggregationForTotalCount = Aggregation.newAggregation(Aggregation.unwind(collection), Aggregation.match(Criteria.where("id").is(organizerId)));

		AggregationResults<Track> resultCount = this.mongoTemplate.aggregate(aggregationForTotalCount, User.class, Track.class);

		DBObject objectCount = resultCount.getRawResults();

		@SuppressWarnings("unchecked")
		List<DBObject> totalTracks = (List<DBObject>) objectCount.get("result");

		Page<Track> trackPage = new PageImpl<Track>(tracks, pageAble, totalTracks.size());

		return trackPage;
	}

	public Page<EventTrack> getAllEventTracks(Criteria userCriteria, org.iqvis.nvolv3.search.Criteria search, Pageable pageAble, String organizerId, String eventId) {

		logger.debug("Retrieving all tracks");

		String collection = "tracks";

		/*
		 * Aggregation aggregation =
		 * Aggregation.newAggregation(Aggregation.unwind(collection),
		 * Aggregation
		 * .match(userCriteria.and(collection+".isDeleted").is(false).
		 * and("id").is(organizerId)), Aggregation.skip(pageAble.getOffset()),
		 * Aggregation.limit(pageAble.getPageSize()));
		 */

		List<AggregationOperation> operations = new ArrayList<AggregationOperation>();

		// operations.add(Aggregation.match(Criteria.where("id").is(organizerId).and("eventId").is(eventId)));

		operations.add(Aggregation.match(Criteria.where("organizerId").is(organizerId)));

		operations.add(Aggregation.match(Criteria.where("_id").is(eventId)));

		operations.add(Aggregation.unwind(collection));

		// operations.add(Aggregation.match(Criteria.where(collection+".isDeleted").is(false)));

		// operations.add(Aggregation.match(userCriteria.and(collection +
		// ".isDeleted").is(false)));

		System.out.println("Query : " + operations.toString());

		List<Order> orderByList = new ArrayList<Order>();

		if (null != search && null != search.getQuery().getOrderBy() && search.getQuery().getOrderBy().size() > 0) {

			for (OrderBy orderBy : search.getQuery().getOrderBy()) {

				if (orderBy.getDirection().equalsIgnoreCase(QueryOperator.ASC)) {

					// operations.add(Aggregation.sort(Sort.Direction.ASC,
					// collection + "." + orderBy.getField()));

					orderByList.add(new Order(Direction.ASC, collection + "." + orderBy.getField()));

					// operations.add(Aggregation.sort(new Sort(orders)));
					// new Sort(new Order(Direction.ASC, FIELD_NAME));

				}
				else {

					// operations.add(Aggregation.sort(Sort.Direction.DESC,
					// collection + "." + orderBy.getField()));

					orderByList.add(new Order(Direction.DESC, collection + "." + orderBy.getField()));
				}
			}
			operations.add(Aggregation.sort(new Sort(orderByList)));
		}

		if (pageAble.getOffset() > 0) {
			operations.add(Aggregation.skip(pageAble.getOffset()));
		}

		operations.add(Aggregation.limit(pageAble.getPageSize()));

		Aggregation aggregation = Aggregation.newAggregation(operations);

		AggregationResults<org.iqvis.nvolv3.bean.EventTrack> result = mongoTemplate.aggregate(aggregation, Event.class, org.iqvis.nvolv3.bean.EventTrack.class);

		logger.debug(aggregation.toString());

		System.out.println("Mongo DB Query" + aggregation.toString());

		/*
		 * AggregationResults<Track> result =
		 * this.mongoTemplate.aggregate(aggregation,User.class,Track.class);
		 */

		System.out.println("");

		DBObject object = result.getRawResults();

		@SuppressWarnings("unchecked")
		List<DBObject> dbObjects = (List<DBObject>) object.get("result");

		List<EventTrack> tracks = new ArrayList<EventTrack>();

		Gson gson = new Gson();

		for (DBObject dbObject : dbObjects) {

			List<Track> trackList = getEventTracks(eventId, organizerId);

			DBObject track = (DBObject) dbObject.get(collection);

			String id = track.get("trackId").toString();

			for (Track t : trackList) {

				if (t.getId().equals(id) && (t.getIsDeleted() == false)) {

					EventTrack obj = gson.fromJson(dbObject.get(collection).toString(), EventTrack.class);

					obj.setTrackId(id);

					tracks.add(obj);
				}
			}
		}

		// Total Records Counting
		Aggregation aggregationForTotalCount = Aggregation.newAggregation(Aggregation.unwind(collection), Aggregation.match(Criteria.where("_id").is(eventId)));

		AggregationResults<EventTrack> resultCount = this.mongoTemplate.aggregate(aggregationForTotalCount, Event.class, EventTrack.class);

		DBObject objectCount = resultCount.getRawResults();

		@SuppressWarnings("unchecked")
		ArrayList<DBObject> totalTracks = (ArrayList<DBObject>) objectCount.get("result");

		totalTracks.trimToSize();

		Page<EventTrack> trackPage = new PageImpl<EventTrack>(tracks, pageAble, tracks.size());

		// Page<EventTrack> trackPage = new PageImpl<EventTrack>(tracks,
		// pageAble, totalTracks.size());

		return trackPage;
	}

	public List<Track> getTracks(List<String> tracks, String organizerId) {

		logger.debug("Retrieving all tracks");

		String collection = "tracks";

		List<AggregationOperation> operations = new ArrayList<AggregationOperation>();

		operations.add(Aggregation.match(Criteria.where("_id").is(organizerId)));

		operations.add(Aggregation.unwind(collection));

		operations.add(Aggregation.match(Criteria.where(collection + ".isDeleted").is(false).and(collection + "._id").in(tracks)));

		Aggregation aggregation = Aggregation.newAggregation(operations);

		AggregationResults<Track> result = mongoTemplate.aggregate(aggregation, User.class, Track.class);

		DBObject object = result.getRawResults();

		@SuppressWarnings("unchecked")
		List<DBObject> dbObjects = (List<DBObject>) object.get("result");

		List<Track> dbtracks = new ArrayList<Track>();

		Gson gson = new Gson();

		for (DBObject dbObject : dbObjects) {

			DBObject track = (DBObject) dbObject.get(collection);

			String id = track.get("_id").toString();

			Track obj = gson.fromJson(dbObject.get(collection).toString(), Track.class);

			obj.setId(id);

			if (null != track.get("picture")) {

				DBRef picture = (DBRef) track.get("picture");

				String mediaID = picture.getId().toString();

				Media media = mediaDao.get(mediaID);

				obj.setPicture(media);
			}

			dbtracks.add(obj);
		}

		return dbtracks;
	}

	public List<Track> getOrganizerTracks(String organizerId) {

		logger.debug("Retrieving all tracks");

		String collection = "tracks";

		List<AggregationOperation> operations = new ArrayList<AggregationOperation>();

		operations.add(Aggregation.match(Criteria.where("_id").is(organizerId)));

		operations.add(Aggregation.unwind(collection));

		operations.add(Aggregation.match(Criteria.where(collection + ".isDeleted").is(false)));

		Aggregation aggregation = Aggregation.newAggregation(operations);

		AggregationResults<Track> result = mongoTemplate.aggregate(aggregation, User.class, Track.class);

		DBObject object = result.getRawResults();

		@SuppressWarnings("unchecked")
		List<DBObject> dbObjects = (List<DBObject>) object.get("result");

		List<Track> dbtracks = new ArrayList<Track>();

		Gson gson = new Gson();

		for (DBObject dbObject : dbObjects) {

			DBObject track = (DBObject) dbObject.get(collection);

			String id = track.get("_id").toString();

			Track obj = gson.fromJson(dbObject.get(collection).toString(), Track.class);

			obj.setId(id);

			if (null != track.get("picture")) {

				DBRef picture = (DBRef) track.get("picture");

				String mediaID = picture.getId().toString();

				Media media = mediaDao.get(mediaID);

				obj.setPicture(media);
			}

			dbtracks.add(obj);
		}

		return dbtracks;
	}

	public String getTrackDetailUrl(Track track, HttpServletRequest request) {

		String detailUrl = "";

		String replaceUrlToken = Urls.TRACK_BASE_URL;

		replaceUrlToken += Urls.TOP_LEVEL.replace(Urls.TOP_LEVEL_ORGANIZER_ID, track.getOrganizerId()) + Urls.GET_TRACK.replace(Urls.SECOND_LEVEL_DOMAIN_ID, track.getId().toString());

		detailUrl = Utils.getURLWithContextPath(request) + replaceUrlToken;

		return detailUrl;
	}

	/*
	 * public Integer getCount(String nameOfSubDoc,String mainDocId){
	 * 
	 * Query query = new Query(Criteria.where("id").is(mainDocId));
	 * 
	 * query.fields().include(nameOfSubDoc+".$");
	 * 
	 * 
	 * Event event = mongoTemplate.findOne(query, Event.class,
	 * MongoDBCollections.EVENT.toString());
	 * 
	 * event.get
	 * 
	 * }
	 */

	@SuppressWarnings("unchecked")
	public List<Track> getEventTracks(String eventId, String organizerID) {

		List<AggregationOperation> operations = new ArrayList<AggregationOperation>();

		operations.add(Aggregation.match(Criteria.where("_id").is(eventId)));

		operations.add(Aggregation.project("tracks"));

		operations.add(Aggregation.unwind("tracks"));

		operations.add(Aggregation.project("tracks.trackId"));

		Aggregation aggregation = Aggregation.newAggregation(operations);

		System.out.println("Aggregation Query : " + operations);

		AggregationResults<Track> result = mongoTemplate.aggregate(aggregation, Event.class, Track.class);

		DBObject object = result.getRawResults();

		List<DBObject> dbObjects = (List<DBObject>) object.get("result");

		List<Track> tList = new ArrayList<Track>();

		Track t = new Track();

		for (DBObject obj : dbObjects) {

			System.out.println("DB Object : " + obj.toString());

			String tObj = (String) obj.get("trackId");

			System.out.println("T Object : " + tObj);

			try {
				t = get(tObj, organizerID);
			}
			catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tList.add(t);
		}

		return tList;
	}
}
