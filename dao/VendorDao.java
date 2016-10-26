package org.iqvis.nvolv3.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.Vendor;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
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
public class VendorDao {

	protected static Logger logger = Logger.getLogger("service");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	@Autowired
	UserDao userDao;

	@Autowired
	MediaDao mediaDao;

	/**
	 * Retrieves a single Vendor
	 * 
	 * @param String
	 *            id
	 * @return Vendor
	 */
	public Vendor get(String id, String organizerId) throws NotFoundException {

		Query query = new Query(Criteria.where("id").is(id).and("organizerId").is(organizerId).and("isDeleted").is(false));

		Vendor vendor = mongoTemplate.findOne(query, Vendor.class, MongoDBCollections.VENDOR.toString());

		return vendor;
	}

	public long count(String eventId, String organizerId) {

		Query query = new Query(Criteria.where("eventId").is(eventId).and("organizerId").is(organizerId).and("isDeleted").is(false));

		long count = mongoTemplate.count(query, Vendor.class, MongoDBCollections.VENDOR.toString());

		return count;
	}

	/**
	 * Adds a new Vendor
	 * 
	 * @param EventVendor
	 *            vendor
	 * @return Vendor
	 */
	public Vendor add(Vendor vendor) throws Exception {
		logger.debug("Adding a new vendor");

		try {

			mongoTemplate.insert(vendor, MongoDBCollections.VENDOR.toString());

			return vendor;

		}
		catch (Exception e) {
			logger.error("An error has occurred while trying to add new vendor", e);
			throw e;
		}
	}

	/**
	 * Edits an existing Vendor
	 * 
	 * @param EventVendor
	 *            vendor
	 * @return Vendor
	 */
	public Vendor edit(Vendor vendor) throws Exception {
		logger.debug("Editing existing Vendor");

		try {

			mongoTemplate.save(vendor);

			return vendor;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to edit existing vendor", e);

			throw e;
		}

	}

	/**
	 * Deletes an existing Vendor
	 * 
	 * @param String
	 *            id
	 * @return Boolean
	 */
	public Boolean delete(String id) {
		logger.debug("Deleting existing Vendor");

		try {

			Query query = Query.query(Criteria.where("id").is(id));

			mongoTemplate.remove(query, Vendor.class);

			return true;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to delete Vendor", e);

			return false;
		}
	}

	public Vendor get(String id) {
		Query query = Query.query(Criteria.where("id").is(id));

		return mongoTemplate.findOne(query, Vendor.class);

	}

	/*
	 * 
	 * Get all the sponsers on the basis
	 */

	/*
	 * public List<Vendor> getAll(String organizerId) throws NotFoundException {
	 * 
	 * logger.debug("Retrieving an existing vendor");
	 * 
	 * Query query = new Query();
	 * 
	 * query.addCriteria(Criteria.where("isDeleted").is(false).and("organizerId")
	 * .is(organizerId));
	 * 
	 * return mongoTemplate.find(query, Vendor.class); }
	 */

	/*
	 * public Page<Vendor> getAll(Criteria userCriteria,
	 * org.iqvis.nvolv3.search.Criteria search, Pageable pageAble, String
	 * organizerId) {
	 * 
	 * logger.debug("Retrieving all Vendors");
	 * 
	 * String collection = "vendors";
	 * 
	 * List<AggregationOperation> operations = new
	 * ArrayList<AggregationOperation>();
	 * 
	 * operations.add(Aggregation.unwind(collection));
	 * 
	 * operations.add(Aggregation.match(userCriteria.and(collection +
	 * ".isDeleted").is(false).and("id").is(organizerId)));
	 * 
	 * operations.add(Aggregation.skip(pageAble.getOffset()));
	 * 
	 * operations.add(Aggregation.limit(pageAble.getPageSize()));
	 * 
	 * if (null != search && null != search.getQuery().getOrderBy() &&
	 * search.getQuery().getOrderBy().size() > 0) {
	 * 
	 * for (OrderBy orderBy : search.getQuery().getOrderBy()) {
	 * 
	 * if (orderBy.getDirection().equalsIgnoreCase(QueryOperator.ASC)) {
	 * 
	 * operations.add(Aggregation.sort(new Sort(Sort.Direction.ASC, collection +
	 * "." + orderBy.getField()))); } else {
	 * 
	 * operations.add(Aggregation.sort(new Sort(Sort.Direction.DESC, collection
	 * + "." + orderBy.getField()))); } }
	 * 
	 * }
	 * 
	 * Aggregation aggregation = Aggregation.newAggregation(operations);
	 * 
	 * AggregationResults<Vendor> result = mongoTemplate.aggregate(aggregation,
	 * User.class, Vendor.class);
	 * 
	 * DBObject object = result.getRawResults();
	 * 
	 * @SuppressWarnings("unchecked") List<DBObject> dbObjects =
	 * (List<DBObject>) object.get("result");
	 * 
	 * List<Vendor> vendors = new ArrayList<Vendor>();
	 * 
	 * Gson gson = new Gson();
	 * 
	 * for (DBObject dbObject : dbObjects) {
	 * 
	 * DBObject vendor = (DBObject) dbObject.get(collection);
	 * 
	 * String id = vendor.get("_id").toString();
	 * 
	 * Vendor obj = gson.fromJson(dbObject.get(collection).toString(),
	 * Vendor.class);
	 * 
	 * obj.setId(id);
	 * 
	 * if (null != vendor.get("picture")) {
	 * 
	 * DBRef picture = (DBRef) vendor.get("picture");
	 * 
	 * String mediaID = picture.getId().toString();
	 * 
	 * Media media = mediaDao.get(mediaID);
	 * 
	 * obj.setPicture(media); }
	 * 
	 * vendors.add(obj); }
	 * 
	 * // Total Records Counting Aggregation aggregationForTotalCount =
	 * Aggregation.newAggregation(Aggregation.unwind("vendors"),
	 * Aggregation.match(userCriteria));
	 * 
	 * AggregationResults<Vendor> resultCount =
	 * this.mongoTemplate.aggregate(aggregationForTotalCount, User.class,
	 * Vendor.class);
	 * 
	 * DBObject objectCount = resultCount.getRawResults();
	 * 
	 * @SuppressWarnings("unchecked") List<DBObject> total = (List<DBObject>)
	 * objectCount.get("result");
	 * 
	 * Page<Vendor> vendorPage = new PageImpl<Vendor>(vendors, pageAble,
	 * total.size());
	 * 
	 * return vendorPage; }
	 */

	public Page<Vendor> getAll(Query query, Pageable pageAble, String organizerId) {

		logger.debug("Retrieving all vendors");

		List<Vendor> vendors = null;

		if (null != pageAble) {

			query.with(pageAble);
		}

		query.addCriteria(Criteria.where("organizerId").is(organizerId));

		vendors = mongoTemplate.find(query, Vendor.class);

		long total = mongoTemplate.count(query, Vendor.class);

		Page<Vendor> vendorsPage = new PageImpl<Vendor>(vendors, pageAble, total);

		return vendorsPage;
	}

	public List<Vendor> getEventVendors(String organizerId, String eventId) {

		Query query = new Query(Criteria.where("eventId").is(eventId).and("organizerId").is(organizerId).and("isDeleted").is(false));

		List<Vendor> vendors = mongoTemplate.find(query, Vendor.class, MongoDBCollections.VENDOR.toString());

		return vendors;
	}

	public String getVendorDetailUrl(Vendor vendor, HttpServletRequest request) {

		String detailUrl = "";

		String replaceUrlToken = Urls.VENDOR_BASE_URL;

		replaceUrlToken += Urls.TOP_LEVEL.replace(Urls.TOP_LEVEL_ORGANIZER_ID, vendor.getOrganizerId()) + Urls.GET_VENDOR.replace(Urls.SECOND_LEVEL_DOMAIN_ID, vendor.getId().toString());

		detailUrl = Utils.getURLWithContextPath(request) + replaceUrlToken;

		return detailUrl;
	}
}
