package org.iqvis.nvolv3.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hamcrest.Matchers;
import org.iqvis.nvolv3.domain.Media;
import org.iqvis.nvolv3.domain.Personnel;
import org.iqvis.nvolv3.domain.User;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
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
public class PersonnelDao {

	protected static Logger logger = Logger.getLogger("service");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	@Autowired
	MediaDao mediaDao;

	/**
	 * Retrieves a single Personnel
	 * 
	 * @param String
	 *            id
	 * @return Personnel
	 * @throws NotFoundException
	 */
	public Personnel get(String id, String organizerId) throws NotFoundException {

		logger.debug("Retrieving an existing personnel");

		Query query = new Query(Criteria.where("id").is(organizerId).and("personnels.isDeleted").is(false));

		query.fields().include("personnels");

		User user = mongoTemplate.findOne(query, User.class, MongoDBCollections.USER.toString());

		if (user == null) {
			throw new NotFoundException(organizerId, "Organizer");
		}

		List<Personnel> personnels = Lambda.select(user.getPersonnels(), Lambda.having(Lambda.on(Personnel.class).getId(), Matchers.equalTo(id)));

		if (personnels != null && personnels.size() > 0) {

			return personnels.get(0);

		}
		else {

			throw new NotFoundException(id, "Personnel");
		}

	}

	/**
	 * Adds a new Personnel
	 * 
	 * @param PersonnelBean
	 *            personnel
	 * @return Personnel
	 */
	public Personnel add(Personnel personnel) throws Exception {
		logger.debug("Adding a new Personnel");

		try {

			User organizer = mongoTemplate.findOne(new Query(Criteria.where("id").is(personnel.getOrganizerId())), User.class);

			if (organizer != null) {

				personnel.setId(UUID.randomUUID().toString());

				if (organizer.getPersonnels() != null) {

					organizer.getPersonnels().add(personnel);

				}
				else {

					List<Personnel> speakerList = new ArrayList<Personnel>();

					speakerList.add(personnel);

					organizer.setPersonnels(speakerList);
				}

				mongoTemplate.save(organizer);

			}
			else {

				throw new NotFoundException(personnel.getOrganizerId(), "Organizer");
			}

			return personnel;

		}
		catch (Exception e) {
			logger.error("An error has occurred while trying to add new personnel", e);
			throw e;
		}
	}

	/**
	 * Edits an existing Personnel
	 * 
	 * @param PersonnelBean
	 *            personnel
	 * @return Personnel
	 */
	public Personnel edit(Personnel personnel) throws Exception {
		logger.debug("Editing existing personnel");

		try {

			MongoConverter converter = mongoTemplate.getConverter();

			DBObject newPersonnel = (DBObject) converter.convertToMongoType(personnel);

			Query query = Query.query(Criteria.where("personnels._id").is(personnel.getId()));

			Update update = new Update().set("personnels.$", newPersonnel);

			mongoTemplate.updateFirst(query, update, User.class);

			return personnel;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to edit existing personnel", e);

			throw e;
		}

	}

	/**
	 * Deletes an existing Personnel
	 * 
	 * @param String
	 *            id
	 * @return Boolean
	 */
	public Boolean delete(String id) {
		logger.debug("Deleting existing Personnel");

		try {

			Query query = Query.query(Criteria.where("personnels._id").is(id));

			Update update = new Update().set("personnels.$.isDeleted", true);

			mongoTemplate.updateFirst(query, update, User.class);

			return true;

		}
		catch (Exception e) {
			logger.error("An error has occurred while trying to delete personnel", e);
			return false;
		}
	}

	/*
	 * 
	 * Get all the Personnels on the basis of organizer
	 */

	// @SuppressWarnings("unchecked")
	// public Page<Personnel> getAll(Criteria userCriteria,
	// org.iqvis.nvolv3.search.Criteria search, Pageable pageAble, final String
	// organizerId, final String eventId) {
	//
	// logger.debug("Retrieving all personnels");
	//
	// String collection = "personnels";
	//
	// final String storedFunction =
	// "function(orgId,eventId){return dump_user_for_personnels(orgId,eventId);}";
	//
	// Object commandResult = mongoTemplate.execute(new DbCallback<Object>() {
	//
	// public Object doInDB(DB db) throws MongoException, DataAccessException {
	//
	// db.command("db.loadServerScripts()");
	//
	// return db.getMongo().getDB("nvolv3").eval(storedFunction, organizerId,
	// eventId);
	// }
	// });
	//
	// List<AggregationOperation> operations = new
	// ArrayList<AggregationOperation>();
	//
	// operations.add(Aggregation.match(Criteria.where("id").is(organizerId)));
	//
	// operations.add(Aggregation.unwind(collection));
	//
	// operations.add(Aggregation.match(userCriteria.and(collection +
	// ".isDeleted").is(false)));
	//
	// if (pageAble.getOffset() > 0) {
	//
	// operations.add(Aggregation.skip(pageAble.getOffset()));
	// }
	// operations.add(Aggregation.limit(pageAble.getPageSize()));
	//
	// List<Order> orderByList = new ArrayList<Order>();
	//
	// if (null != search && null != search.getQuery().getOrderBy() &&
	// search.getQuery().getOrderBy().size() > 0) {
	//
	// for (OrderBy orderBy : search.getQuery().getOrderBy()) {
	//
	// if (orderBy.getDirection().equalsIgnoreCase(QueryOperator.ASC)) {
	//
	// orderByList.add(new Order(Direction.ASC, collection + "." +
	// orderBy.getField()));
	//
	// }
	// else {
	//
	// orderByList.add(new Order(Direction.DESC, collection + "." +
	// orderBy.getField()));
	// }
	// }
	//
	// operations.add(Aggregation.sort(new Sort(orderByList)));
	//
	// }
	//
	// Aggregation aggregation = Aggregation.newAggregation(operations);
	//
	// AggregationResults<Personnel> result =
	// mongoTemplate.aggregate(aggregation, CacheDump.class, Personnel.class);
	//
	// DBObject object = result.getRawResults();
	//
	// List<DBObject> dbObjects = (List<DBObject>) object.get("result");
	//
	// List<Personnel> personnels = new ArrayList<Personnel>();
	//
	// Gson gson = new Gson();
	//
	// for (DBObject dbObject : dbObjects) {
	//
	// DBObject personnel = (DBObject) dbObject.get(collection);
	//
	// String id = personnel.get("_id").toString();
	//
	// Personnel obj = gson.fromJson(dbObject.get(collection).toString(),
	// Personnel.class);
	//
	// obj.setId(id);
	//
	// if (null != personnel.get("picture")) {
	//
	// DBRef picture = (DBRef) personnel.get("picture");
	//
	// String mediaID = picture.getId().toString();
	//
	// Media media = mediaDao.get(mediaID);
	//
	// obj.setPicture(media);
	// }
	//
	// personnels.add(obj);
	// }
	//
	// logger.debug(userCriteria.getCriteriaObject());
	//
	// // Total Records Counting
	// // Aggregation aggregationForTotalCount =
	// // Aggregation.newAggregation(Aggregation.unwind(collection),
	// // Aggregation.match(userCriteria));
	//
	// // Aggregation aggregationForTotalCount =
	// // Aggregation.newAggregation(operations);//
	// // Aggregation.newAggregation(Aggregation.unwind(collection),
	// // Aggregation.match(Criteria.where("id").is(organizerId)));
	//
	// Aggregation aggregationForTotalCount =
	// Aggregation.newAggregation(Aggregation.match(Criteria.where("_id").is(organizerId)),
	// Aggregation.unwind(collection), Aggregation.match(userCriteria));
	//
	// AggregationResults<Personnel> resultCount =
	// this.mongoTemplate.aggregate(aggregationForTotalCount, CacheDump.class,
	// Personnel.class);
	//
	// DBObject objectCount = resultCount.getRawResults();
	//
	// List<DBObject> total = (List<DBObject>) objectCount.get("result");
	//
	// Page<Personnel> personnelPage = new PageImpl<Personnel>(personnels,
	// pageAble, total.size());
	//
	// return personnelPage;
	// }

	public String getPersonnelDetailUrl(Personnel personnel, HttpServletRequest request) {
		String detailUrl = "";

		String replaceUrlToken = Urls.PERSONNEL_BASE_URL;

		replaceUrlToken += Urls.TOP_LEVEL.replace(Urls.TOP_LEVEL_ORGANIZER_ID, personnel.getOrganizerId()) + Urls.GET_PERSONNEL.replace(Urls.GET_TRACK, personnel.getId().toString());

		detailUrl = Utils.getURLWithContextPath(request) + replaceUrlToken;

		return detailUrl;
	}

	public List<Personnel> getPersonnelsByOrganizer(String organizerId, List<String> ids) throws NotFoundException {

		List<AggregationOperation> operations = new ArrayList<AggregationOperation>();

		operations.add(Aggregation.match(Criteria.where("id").is(organizerId)));

		operations.add(Aggregation.unwind("personnels"));

		operations.add(Aggregation.match(Criteria.where("personnels.isDeleted").is(false).and("personnels._id").in(ids)));

		Aggregation aggregation = Aggregation.newAggregation(operations);

		AggregationResults<Personnel> result = mongoTemplate.aggregate(aggregation, User.class, Personnel.class);

		DBObject object = result.getRawResults();

		List<DBObject> dbObjects = (List<DBObject>) object.get("result");

		List<Personnel> personnels = new ArrayList<Personnel>();

		Gson gson = new Gson();

		for (DBObject dbObject : dbObjects) {

			DBObject personnel = (DBObject) dbObject.get("personnels");

			String id = personnel.get("_id").toString();

			Personnel obj = gson.fromJson(dbObject.get("personnels").toString(), Personnel.class);

			obj.setId(id);

			if (null != personnel.get("picture")) {

				DBRef picture = (DBRef) personnel.get("picture");

				String mediaID = picture.getId().toString();

				Media media = mediaDao.get(mediaID);

				obj.setPicture(media);
			}

			personnels.add(obj);
		}

		return personnels;
	}

	public List<Personnel> getPersonnelsByIds(List<String> ids, String organizerId) throws NotFoundException {

		// logger.debug("Retrieving an existing personnel by ids" +
		// ids.toString());

		List<AggregationOperation> operations = new ArrayList<AggregationOperation>();

		operations.add(Aggregation.match(Criteria.where("id").is(organizerId)));

		operations.add(Aggregation.unwind("personnels"));

		operations.add(Aggregation.match(Criteria.where("personnels.id").in(ids).and("personnels.isDeleted").is(false)));

		Aggregation aggregation = Aggregation.newAggregation(operations);

		AggregationResults<Personnel> result = mongoTemplate.aggregate(aggregation, User.class, Personnel.class);

		DBObject object = result.getRawResults();

		List<DBObject> dbObjects = (List<DBObject>) object.get("result");

		List<Personnel> personnels = new ArrayList<Personnel>();

		Gson gson = new Gson();

		for (DBObject dbObject : dbObjects) {

			DBObject personnel = (DBObject) dbObject.get("personnels");

			String id = personnel.get("_id").toString();

			Personnel obj = gson.fromJson(dbObject.get("personnels").toString(), Personnel.class);

			obj.setId(id);

			if (null != personnel.get("picture")) {

				DBRef picture = (DBRef) personnel.get("picture");

				String mediaID = picture.getId().toString();

				Media media = mediaDao.get(mediaID);

				obj.setPicture(media);
			}

			personnels.add(obj);
		}

		return personnels;
	}

	public List<Personnel> getOrganizerPersonnelsList(String organizerId) throws NotFoundException {

		// logger.debug("Retrieving an existing personnel by ids" +
		// ids.toString());

		List<AggregationOperation> operations = new ArrayList<AggregationOperation>();

		operations.add(Aggregation.match(Criteria.where("id").is(organizerId)));

		operations.add(Aggregation.unwind("personnels"));

		operations.add(Aggregation.match(Criteria.where("personnels.isDeleted").is(false)));

		Aggregation aggregation = Aggregation.newAggregation(operations);

		AggregationResults<Personnel> result = mongoTemplate.aggregate(aggregation, User.class, Personnel.class);

		DBObject object = result.getRawResults();

		List<DBObject> dbObjects = (List<DBObject>) object.get("result");

		List<Personnel> personnels = new ArrayList<Personnel>();

		Gson gson = new Gson();

		System.out.println("size of list--------------" + dbObjects.size());

		for (DBObject dbObject : dbObjects) {

			DBObject personnel = (DBObject) dbObject.get("personnels");

			String id = personnel.get("_id").toString();

			Personnel obj = gson.fromJson(dbObject.get("personnels").toString(), Personnel.class);

			obj.setId(id);

			if (null != personnel.get("picture")) {

				DBRef picture = (DBRef) personnel.get("picture");

				String mediaID = picture.getId().toString();

				Media media = mediaDao.get(mediaID);

				obj.setPicture(media);
			}

			personnels.add(obj);
		}

		return personnels;
	}

	@SuppressWarnings("unchecked")
	public Page<Personnel> getAll(Criteria userCriteria, org.iqvis.nvolv3.search.Criteria search, Pageable pageAble, final String organizerId) {

		logger.debug("Retrieving all personnels");

		String collection = "personnels";

		List<AggregationOperation> operations = new ArrayList<AggregationOperation>();

		operations.add(Aggregation.match(Criteria.where("id").is(organizerId)));

		operations.add(Aggregation.unwind(collection));

		operations.add(Aggregation.match(userCriteria.and(collection + ".isDeleted").is(false)));

		if (pageAble != null && pageAble.getOffset() > 0) {

			operations.add(Aggregation.skip(pageAble.getOffset()));
		}
		if (pageAble != null) {
			operations.add(Aggregation.limit(pageAble.getPageSize()));
		}
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

		Aggregation aggregation = Aggregation.newAggregation(operations);

		AggregationResults<Personnel> result = mongoTemplate.aggregate(aggregation, User.class, Personnel.class);

		DBObject object = result.getRawResults();

		List<DBObject> dbObjects = (List<DBObject>) object.get("result");

		List<Personnel> personnels = new ArrayList<Personnel>();

		Gson gson = new Gson();

		for (DBObject dbObject : dbObjects) {

			DBObject personnel = (DBObject) dbObject.get(collection);

			String id = personnel.get("_id").toString();

			Personnel obj = gson.fromJson(dbObject.get(collection).toString(), Personnel.class);

			obj.setId(id);

			if (null != personnel.get("picture")) {

				DBRef picture = (DBRef) personnel.get("picture");

				String mediaID = picture.getId().toString();

				Media media = mediaDao.get(mediaID);

				obj.setPicture(media);
			}

			personnels.add(obj);
		}

		logger.debug(userCriteria.getCriteriaObject());

		// Total Records Counting
		// Aggregation aggregationForTotalCount =
		// Aggregation.newAggregation(Aggregation.unwind(collection),
		// Aggregation.match(userCriteria));

		// Aggregation aggregationForTotalCount =
		// Aggregation.newAggregation(operations);//
		// Aggregation.newAggregation(Aggregation.unwind(collection),
		// Aggregation.match(Criteria.where("id").is(organizerId)));

		Aggregation aggregationForTotalCount = Aggregation.newAggregation(Aggregation.unwind(collection), Aggregation.match(Criteria.where("id").is(organizerId)));

		AggregationResults<Personnel> resultCount = this.mongoTemplate.aggregate(aggregationForTotalCount, User.class, Personnel.class);

		DBObject objectCount = resultCount.getRawResults();

		List<DBObject> total = (List<DBObject>) objectCount.get("result");

		Page<Personnel> personnelPage = new PageImpl<Personnel>(personnels, pageAble, total.size());

		return personnelPage;
	}

}
