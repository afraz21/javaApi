package org.iqvis.nvolv3.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hamcrest.Matchers;
import org.iqvis.nvolv3.domain.EventCampaign;
import org.iqvis.nvolv3.domain.EventCampaignParticipant;
import org.iqvis.nvolv3.domain.Media;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.OrderBy;
import org.iqvis.nvolv3.search.QueryOperator;
import org.iqvis.nvolv3.utils.MongoDBCollections;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
import org.joda.time.DateTime;
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
public class EventCampaignPariticipantDao {

	protected static Logger logger = Logger.getLogger("service");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	@Autowired
	MediaDao mediaDao;

	/**
	 * Retrieves a single EventCampaignParticipant
	 * 
	 * @param String
	 *            id
	 * @return EventCampaignParticipant
	 * @throws NotFoundException
	 */
	public EventCampaignParticipant get(String eventCampaignParticipantId, String campaignId, String organizerId) throws NotFoundException {

		logger.debug("Retrieving an existing EventCampaignParticipant");

		Query query = new Query(Criteria.where("id").is(campaignId).and("organizerId").is(organizerId).and("isDeleted").is(false));

		query.fields().include("participants");

		EventCampaign eventCampaign = mongoTemplate.findOne(query, EventCampaign.class, MongoDBCollections.EVENT_CAMPAIGN.toString());

		if (eventCampaign == null) {
			throw new NotFoundException(eventCampaignParticipantId, "EventCampaignParticipant");
		}

		List<EventCampaignParticipant> eventCampaignParticipants = Lambda.select(eventCampaign.getParticipants(), Lambda.having(Lambda.on(EventCampaignParticipant.class).getId(), Matchers.equalTo(eventCampaignParticipantId)));

		if (eventCampaignParticipants != null && eventCampaignParticipants.size() > 0) {

			return eventCampaignParticipants.get(0);

		}
		else {

			throw new NotFoundException(eventCampaignParticipantId, "EventCampaignParticipant");
		}
	}

	public EventCampaignParticipant get(String eventCampaignParticipantId, String eventId) throws NotFoundException {

		logger.debug("Retrieving an existing EventCampaignParticipant");

		Query query = new Query(Criteria.where("participants._id").is(eventCampaignParticipantId).and("eventId").is(eventId).and("isDeleted").is(false));

		query.fields().include("participants");

		EventCampaign eventCampaign = mongoTemplate.findOne(query, EventCampaign.class, MongoDBCollections.EVENT_CAMPAIGN.toString());

		if (eventCampaign == null) {
			throw new NotFoundException(eventCampaignParticipantId, "EventCampaignParticipant");
		}

		List<EventCampaignParticipant> eventCampaignParticipants = Lambda.select(eventCampaign.getParticipants(), Lambda.having(Lambda.on(EventCampaignParticipant.class).getId(), Matchers.equalTo(eventCampaignParticipantId)));

		if (eventCampaignParticipants != null && eventCampaignParticipants.size() > 0) {

			return eventCampaignParticipants.get(0);

		}
		else {

			throw new NotFoundException(eventCampaignParticipantId, "EventCampaignParticipant");
		}
	}

	/**
	 * Adds a new EventCampaignParticipant
	 * 
	 * @param EventCampaignParticipant
	 *            EventCampaignParticipant
	 * @return EventCampaignParticipant
	 */
	public EventCampaignParticipant add(EventCampaignParticipant eventCampaignParticipant) throws Exception {
		logger.debug("Adding a new EventCampaignParticipant");

		try {

			EventCampaign e = mongoTemplate.findOne(new Query(Criteria.where("id").is(eventCampaignParticipant.getCampaignId())), EventCampaign.class);

			if (e != null) {

				eventCampaignParticipant.setId(UUID.randomUUID().toString());

				if (e.getParticipants() != null) {

					e.getParticipants().add(eventCampaignParticipant);

				}
				else {

					List<EventCampaignParticipant> tList = new ArrayList<EventCampaignParticipant>();

					tList.add(eventCampaignParticipant);

					e.setParticipants(tList);
				}

				mongoTemplate.save(e);

			}
			else {

				throw new NotFoundException(eventCampaignParticipant.getCampaignId(), "EventCampaign");
			}

			return eventCampaignParticipant;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to add new eventCampaignParticipant", e);

			throw e;
		}
	}

	/**
	 * Edits an existing EventCampaignParticipant
	 * 
	 * @param EventCampaignParticipant
	 *            eventCampaignParticipant
	 * @return EventCampaignParticipant
	 */
	public EventCampaignParticipant edit(EventCampaignParticipant eventCampaignParticipant) throws Exception {
		logger.debug("Editing existing EventCampaignParticipant");

		try {

			MongoConverter converter = mongoTemplate.getConverter();

			DBObject newEventCampaignParticipantRec = (DBObject) converter.convertToMongoType(eventCampaignParticipant);

			Query query = Query.query(Criteria.where("participants._id").is(eventCampaignParticipant.getId()));

			Update update = new Update().set("participants.$", newEventCampaignParticipantRec);

			mongoTemplate.updateFirst(query, update, EventCampaign.class);

			return eventCampaignParticipant;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to edit existing EventCampaignParticipant", e);

			throw e;
		}

	}

	public Page<EventCampaignParticipant> getAll(Criteria userCriteria, org.iqvis.nvolv3.search.Criteria search, Pageable pageAble, String organizerId, String campaignId) {

		logger.debug("Retrieving all EventCampaignParticipant");

		String collection = "participants";

		List<AggregationOperation> operations = new ArrayList<AggregationOperation>();

		operations.add(Aggregation.unwind(collection));

		operations.add(Aggregation.match(userCriteria.and(collection + ".isDeleted").is(false).and("id").is(campaignId).and("organizerId").is(organizerId)));

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

		AggregationResults<EventCampaignParticipant> result = mongoTemplate.aggregate(aggregation, EventCampaign.class, EventCampaignParticipant.class);

		DBObject object = result.getRawResults();

		logger.debug(object);
		@SuppressWarnings("unchecked")
		List<DBObject> dbObjects = (List<DBObject>) object.get("result");

		List<EventCampaignParticipant> eventCampaignParticipants = new ArrayList<EventCampaignParticipant>();

		Gson gson = new Gson();

		for (DBObject dbObject : dbObjects) {

			DBObject venue = (DBObject) dbObject.get(collection);

			String id = venue.get("_id").toString();

			System.out.print(venue.get("startDate"));

			Date startDate = (Date) venue.get("startDate");

			Date endDate = (Date) venue.get("endDate");

			EventCampaignParticipant obj = gson.fromJson(dbObject.get(collection).toString(), EventCampaignParticipant.class);

			obj.setId(id);

			obj.setStartDate(startDate == null ? null : new DateTime(startDate));

			obj.setEndDate(endDate == null ? null : new DateTime(endDate));

			if (null != venue.get("picture")) {

				DBRef picture = (DBRef) venue.get("picture");

				String mediaID = picture.getId().toString();

				Media media = mediaDao.get(mediaID);

				obj.setPicture(media);
			}

			eventCampaignParticipants.add(obj);
		}

		// Total Records Counting
		Aggregation aggregationForTotalCount = Aggregation.newAggregation(Aggregation.unwind(collection), Aggregation.match(userCriteria));

		AggregationResults<EventCampaignParticipant> resultCount = this.mongoTemplate.aggregate(aggregationForTotalCount, EventCampaign.class, EventCampaignParticipant.class);

		DBObject objectCount = resultCount.getRawResults();

		@SuppressWarnings("unchecked")
		List<DBObject> total = (List<DBObject>) objectCount.get("result");

		Page<EventCampaignParticipant> eventCampaignParticipantsPage = new PageImpl<EventCampaignParticipant>(eventCampaignParticipants, pageAble, total.size());

		return eventCampaignParticipantsPage;
	}

	public String getEventCampaignParticipantDetailUrl(EventCampaignParticipant eventCampaignParticipant, HttpServletRequest request) {

		String detailUrl = "";

		String replaceUrlToken = Urls.TRACK_BASE_URL;

		replaceUrlToken += Urls.TOP_LEVEL.replace(Urls.TOP_LEVEL_ORGANIZER_ID, eventCampaignParticipant.getOrganizerId()) + Urls.GET_TRACK.replace(Urls.SECOND_LEVEL_DOMAIN_ID, eventCampaignParticipant.getId().toString());

		detailUrl = Utils.getURLWithContextPath(request) + replaceUrlToken;

		return detailUrl;
	}

	public EventCampaignParticipant getEventCampaignParticipant(String eventCampaignParticipantId) {

		return null;

		// Query query = new
		// Query(Criteria.where("id").is(campaignId).and("organizerId").is(organizerId).and("isDeleted").is(false));
		//
		// query.fields().include("participants");
		//
		// EventCampaign eventCampaign = mongoTemplate.findOne(query,
		// EventCampaign.class, MongoDBCollections.EVENT_CAMPAIGN.toString());
		//
		// if (eventCampaign == null) {
		// throw new NotFoundException(eventCampaignParticipantId,
		// "EventCampaignParticipant");
		// }
		//
		// List<EventCampaignParticipant> eventCampaignParticipants =
		// Lambda.select(eventCampaign.getParticipants(),
		// Lambda.having(Lambda.on(EventCampaignParticipant.class).getId(),
		// Matchers.equalTo(eventCampaignParticipantId)));
		//
		// if (eventCampaignParticipants != null &&
		// eventCampaignParticipants.size() > 0) {
		//
		// return eventCampaignParticipants.get(0);
		//
		// }
		// else {
		//
		// throw new NotFoundException(eventCampaignParticipantId,
		// "EventCampaignParticipant");
		// }
		//
		// Query query = new
		// Query(Criteria.where("id").is(pId).and("isDeleted").is(false));
		//
		// List<EventCampaign> eventCampaign = mongoTemplate.find(query,
		// EventCampaign.class);
		//
		// EventCampaignParticipant ecpart = new EventCampaignParticipant();
		//
		// for (EventCampaign ec : eventCampaign) {
		//
		// boolean flag = false;
		//
		// List<EventCampaignParticipant> ecp = ec.getParticipants();
		//
		// for (EventCampaignParticipant eP : ecp) {
		//
		// if (eP.getId().equals(pId)) {
		//
		// ecpart = eP;
		//
		// flag = true;
		//
		// break;
		// }
		// }
		//
		// if (flag) {
		//
		//
		// break;
		// }
		// }
		//
		// // eventCampaign.
		//
		// // List<EventCampaignParticipant> eventCampaignParticipants =
		// Lambda.select(eventCampaign.getParticipants(),
		// Lambda.having(Lambda.on(EventCampaignParticipant.class).getId(),
		// Matchers.equalTo(pId)));
		// //
		// // if (eventCampaignParticipants != null &&
		// eventCampaignParticipants.size() > 0) {
		// //
		// // return eventCampaignParticipants.get(0);
		// //
		// // }
		// // else {
		// //
		// // return null;
		// // }
		//
		// return ecpart;
	}

	// public List<EventCampaignParticipant>
	// getAllEventCampaignParticipants(String eventId) {
	//
	// List<EventCampaignParticipant> ecpList = new
	// ArrayList<EventCampaignParticipant>();
	//
	// Query query = new Query();
	//
	// query.addCriteria(Criteria.where("eventId").is(eventId).and("isDeleted").is(false));
	//
	// ecpList = mongoTemplate.find(query, EventCampaignParticipant.class);
	//
	// return ecpList;
	// }

	public List<EventCampaignParticipant> getAll(String organizerId, String campaignId) {

		logger.debug("Retrieving all EventCampaignParticipant");

		String collection = "participants";

		List<AggregationOperation> operations = new ArrayList<AggregationOperation>();

		operations.add(Aggregation.match(Criteria.where(collection + ".isDeleted").is(false).and(collection + ".campaignId").is(campaignId).and(collection + ".organizerId").is(organizerId)));

		operations.add(Aggregation.unwind(collection));

		Aggregation aggregation = Aggregation.newAggregation(operations);

		AggregationResults<EventCampaignParticipant> result = mongoTemplate.aggregate(aggregation, EventCampaign.class, EventCampaignParticipant.class);

		DBObject object = result.getRawResults();

		logger.debug(object);
		@SuppressWarnings("unchecked")
		List<DBObject> dbObjects = (List<DBObject>) object.get("result");

		List<EventCampaignParticipant> eventCampaignParticipants = new ArrayList<EventCampaignParticipant>();

		Gson gson = new Gson();

		for (DBObject dbObject : dbObjects) {

			System.out.println("DBOBJECT : " + dbObject);

			DBObject venue = (DBObject) dbObject.get(collection);

			String id = venue.get("_id").toString();

			EventCampaignParticipant obj = gson.fromJson(dbObject.get(collection).toString(), EventCampaignParticipant.class);

			obj.setId(id);

			// System.out.println("Participant Id : " + obj.getId() +
			// " | Participant Start Date : " + obj.getStartDate() +
			// " | Participant End Date : " + obj.getEndDate());

			if (null != venue.get("picture")) {

				DBRef picture = (DBRef) venue.get("picture");

				String mediaID = picture.getId().toString();

				Media media = mediaDao.get(mediaID);

				obj.setPicture(media);
			}

			if (null != venue.get("overlayLandscape")) {

				DBRef picture = (DBRef) venue.get("overlayLandscape");

				String mediaID = picture.getId().toString();

				Media media = mediaDao.get(mediaID);

				obj.setOverlayLandscape(media);
			}

			if (null != venue.get("overlayPotrait")) {

				DBRef picture = (DBRef) venue.get("overlayPotrait");

				String mediaID = picture.getId().toString();

				Media media = mediaDao.get(mediaID);

				obj.setOverlayPotrait(media);
			}

			eventCampaignParticipants.add(obj);
		}

		return eventCampaignParticipants;
	}

	public List<EventCampaignParticipant> getAll() {

		logger.debug("Retrieving all EventCampaignParticipant");

		String collection = "participants";

		List<AggregationOperation> operations = new ArrayList<AggregationOperation>();

		operations.add(Aggregation.match(Criteria.where(collection + ".isDeleted").is(false).and(collection + ".isActive").is(true)));

		operations.add(Aggregation.unwind(collection));

		Aggregation aggregation = Aggregation.newAggregation(operations);

		AggregationResults<EventCampaignParticipant> result = mongoTemplate.aggregate(aggregation, EventCampaign.class, EventCampaignParticipant.class);

		DBObject object = result.getRawResults();

		logger.debug(object);
		@SuppressWarnings("unchecked")
		List<DBObject> dbObjects = (List<DBObject>) object.get("result");

		List<EventCampaignParticipant> eventCampaignParticipants = new ArrayList<EventCampaignParticipant>();

		Gson gson = new Gson();

		for (DBObject dbObject : dbObjects) {

			System.out.println("DBObject : " + dbObjects);

			// DBObject venue = (DBObject) dbObject.get(collection);
			//
			// String id = venue.get("_id").toString();
			//
			// EventCampaignParticipant obj =
			// gson.fromJson(dbObject.get(collection).toString(),
			// EventCampaignParticipant.class);
			//
			// obj.setId(id);
			//
			// if (null != venue.get("picture")) {
			//
			// DBRef picture = (DBRef) venue.get("picture");
			//
			// String mediaID = picture.getId().toString();
			//
			// Media media = mediaDao.get(mediaID);
			//
			// obj.setPicture(media);
			// }

			// eventCampaignParticipants.add(obj);
		}

		return eventCampaignParticipants;
	}

	public List<EventCampaignParticipant> getAll(Query query) {

		logger.debug("Retrieving all event Participants");

		List<EventCampaignParticipant> eventCampaignParticipants = new ArrayList<EventCampaignParticipant>();

		logger.debug(query);

		eventCampaignParticipants = mongoTemplate.find(query, EventCampaignParticipant.class);

		return eventCampaignParticipants;
	}

	public List<EventCampaignParticipant> getAllParticipants(String organizerId, String campaignId) {

		logger.debug("Retrieving all EventCampaignParticipant");

		String collection = "participants";

		List<AggregationOperation> operations = new ArrayList<AggregationOperation>();

		operations.add(Aggregation.match(Criteria.where(collection + ".isDeleted").is(false).and(collection + ".campaignId").is(campaignId).and(collection + ".organizerId").is(organizerId)));

		operations.add(Aggregation.unwind(collection));

		Aggregation aggregation = Aggregation.newAggregation(operations);

		AggregationResults<EventCampaignParticipant> result = mongoTemplate.aggregate(aggregation, EventCampaign.class, EventCampaignParticipant.class);

		DBObject object = result.getRawResults();

		logger.debug(object);
		@SuppressWarnings("unchecked")
		List<DBObject> dbObjects = (List<DBObject>) object.get("result");

		List<EventCampaignParticipant> eventCampaignParticipants = new ArrayList<EventCampaignParticipant>();

		Gson gson = new Gson();

		for (DBObject dbObject : dbObjects) {

			System.out.println("DBOBJECT : " + dbObject);

			DBObject participant = (DBObject) dbObject.get(collection);

			String id = participant.get("_id").toString();

			System.out.println("Venue ID------------------------------------- : " + id);

			EventCampaignParticipant ecp = new EventCampaignParticipant();

			try {
				ecp = get(id, campaignId, organizerId);

				System.out.println("Participant : ---------------Start Date" + ecp.getStartDate() + " | End Date " + ecp.getEndDate());
			}
			catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			eventCampaignParticipants.add(ecp);
		}

		return eventCampaignParticipants;
	}

}
