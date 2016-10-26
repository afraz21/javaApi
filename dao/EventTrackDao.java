package org.iqvis.nvolv3.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.EventTrack;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.OrderBy;
import org.iqvis.nvolv3.search.QueryOperator;
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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import com.mongodb.DBObject;

//------------------------------------//
@Repository
public class EventTrackDao {
	protected static Logger logger = Logger.getLogger("service");

	@Autowired
	private EventDao eventDao;

	@Autowired
	private MongoTemplate mongoTemplate;

	public EventTrack add(EventTrack eventTrack, String organizerId, String eventId) throws NotFoundException, Exception {

		Event event = eventDao.get(eventId, organizerId);

		List<EventTrack> list = event.getTracks();

		list = (list != null ? list : new ArrayList<EventTrack>());

		list.add(eventTrack);

		event.setTracks(list);

		eventDao.edit(event);

		return eventTrack;

	}

	public EventTrack edit(EventTrack eventTrack, String organizerId, String eventId) throws NotFoundException, Exception {

		Event event = eventDao.get(eventId, organizerId);

		List<EventTrack> list = event.getTracks();

		if (list == null) {

			throw new NotFoundException(eventTrack.getTrackId(), "EventTrack");

		}

		List<EventTrack> listTemp = new ArrayList<EventTrack>();

		boolean found = false;

		for (EventTrack eventTrack2 : list) {

			if (new String(eventTrack2.getTrackId() + "").equals(eventTrack.getTrackId())) {

				eventTrack.setVersion(eventTrack2.getVersion() + 1);

				if (eventTrack.getColorCode() != null) {
					eventTrack2.setColorCode(eventTrack.getColorCode());
				}

				if (eventTrack.getSortOrder() != null) {
					eventTrack2.setSortOrder(eventTrack.getSortOrder());
				}

				if (eventTrack.getQuestionnaireType() != null) {
					eventTrack2.setQuestionnaireType(eventTrack.getQuestionnaireType());
				}

				if (eventTrack.getQuestionnaireId() != null) {
					eventTrack2.setQuestionnaireId(eventTrack.getQuestionnaireId());
				}

				listTemp.add(eventTrack2);

				found = true;

			}

			else {

				listTemp.add(eventTrack2);

			}
		}

		if (!found) {

			throw new NotFoundException(eventTrack.getTrackId(), "EventTrack");

		}

		event.setTracks(listTemp);

//		event.setVenue(null);

		eventDao.edit(event);

		return eventTrack;
	}

	public EventTrack delete(EventTrack eventTrack, String organizerId, String eventId) throws NotFoundException, Exception {

		Event event = eventDao.get(eventId, organizerId);

		List<EventTrack> list = event.getTracks();

		if (list == null) {
			throw new NotFoundException(eventTrack.getTrackId(), "EventTrack");
		}

		List<EventTrack> listTemp = new ArrayList<EventTrack>();

		boolean found = false;

		for (EventTrack eventTrack2 : list) {
			if (new String(eventTrack2.getTrackId() + "").equalsIgnoreCase(eventTrack.getTrackId())) {

				found = true;

			}

			else {

				listTemp.add(eventTrack2);

			}
		}

		if (!found) {
			throw new NotFoundException(eventTrack.getTrackId(), "EventTrack");
		}

		event.setTracks(listTemp);

		eventDao.edit(event);

		return eventTrack;
	}

	public EventTrack get(String trackId, String organizerId, String eventId) throws NotFoundException, Exception {

		Event event = eventDao.get(eventId, organizerId);

		logger.debug("Event Id : " + event.getId());

		List<EventTrack> list = event.getTracks();

		if (list == null) {

			throw new NotFoundException(eventId, "EventTrack");

		}

		for (EventTrack eventTrack : list) {

			if (trackId.equals(eventTrack.getTrackId() + "")) {

				logger.debug("Track Id : " + eventTrack.getTrackId());

				return eventTrack;

			}
		}

		throw new NotFoundException(trackId, "EventTrack");
	}

	public Page<EventTrack> getAll(Criteria userCriteria, org.iqvis.nvolv3.search.Criteria search, Pageable pageAble, String organizerId, String eventId) {

		logger.debug("Retrieving all Activities");

		String collection = "tracks";

		List<AggregationOperation> operations = new ArrayList<AggregationOperation>();

		operations.add(Aggregation.match(Criteria.where("id").is(eventId).and("organizerId").is(organizerId)));

		operations.add(Aggregation.unwind(collection));

		operations.add(Aggregation.match(userCriteria.and(collection + ".isDeleted").is(false)));

		List<Order> orderByList = new ArrayList<Order>();

		if (null != search && null != search.getQuery().getOrderBy() && search.getQuery().getOrderBy().size() > 0) {

			for (OrderBy orderBy : search.getQuery().getOrderBy()) {

				if (orderBy.getDirection().equalsIgnoreCase(QueryOperator.ASC)) {

					orderByList.add(new Order(Direction.ASC, collection + "." + orderBy.getField()));

				}
				else {

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

		AggregationResults<EventTrack> result = mongoTemplate.aggregate(aggregation, Event.class, EventTrack.class);

		DBObject object = result.getRawResults();

		logger.debug(result.getRawResults());

		logger.debug(userCriteria.getCriteriaObject());

		logger.debug(aggregation.toString());

		@SuppressWarnings("unchecked")
		List<DBObject> dbObjects = (List<DBObject>) object.get("result");

		List<EventTrack> activities = new ArrayList<EventTrack>();

		for (DBObject dbObject : dbObjects) {

			DBObject activity = (DBObject) dbObject.get(collection);

			String id = activity.get("_id").toString();

			EventTrack obj = mongoTemplate.getConverter().read(EventTrack.class, (DBObject) dbObject.get(collection));

			obj.setTrackId(id);

			activities.add(obj);
		}

		// Total Records Counting
		Aggregation aggregationForTotalCount = Aggregation.newAggregation(Aggregation.unwind(collection), Aggregation.match(userCriteria));

		AggregationResults<EventTrack> resultCount = this.mongoTemplate.aggregate(aggregationForTotalCount, Event.class, EventTrack.class);

		DBObject objectCount = resultCount.getRawResults();

		@SuppressWarnings("unchecked")
		List<DBObject> total = (List<DBObject>) objectCount.get("result");

		Page<EventTrack> activityPage = new PageImpl<EventTrack>(activities, pageAble, total.size());

		return activityPage;

	}

}
