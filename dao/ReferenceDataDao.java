package org.iqvis.nvolv3.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hamcrest.Matchers;
import org.iqvis.nvolv3.bean.Data;
import org.iqvis.nvolv3.domain.ReferenceData;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.utils.MongoDBCollections;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
import org.joda.time.DateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import ch.lambdaj.Lambda;

@SuppressWarnings("restriction")
@Repository
public class ReferenceDataDao {

	protected static Logger logger = Logger.getLogger("service");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	/**
	 * Retrieves all ReferenceData
	 * 
	 * @param Search
	 *            ReferenceDataSearch
	 * @param HttpServletRequest
	 *            request
	 * @return List<Event>
	 */
	public org.springframework.data.domain.Page<ReferenceData> getAll(Query query, Pageable pageAble) {

		logger.debug("Retrieving all ReferenceData");

		List<ReferenceData> refrenceDataList = null;

		if (null != pageAble) {

			query.with(pageAble);
		}

		refrenceDataList = mongoTemplate.find(query, ReferenceData.class);

		long total = mongoTemplate.count(query, ReferenceData.class);

		org.springframework.data.domain.Page<ReferenceData> referenceDataPage = new org.springframework.data.domain.PageImpl<ReferenceData>(refrenceDataList, pageAble, total);

		return referenceDataPage;
	}

	/**
	 * Retrieves a single ReferenceData
	 * 
	 * @param String
	 *            id
	 * @return ReferenceData
	 */

	public ReferenceData getEventType(String type) {

		logger.debug("Retrieving an existing ReferenceData");

		Query query = new Query(Criteria.where("type").is(type).and("isDeleted").is(false));

		// logger.debug(query.getQueryObject());

		ReferenceData referenceData = mongoTemplate.findOne(query, ReferenceData.class, MongoDBCollections.REFERENCE_DATA.toString());

		if (referenceData != null && referenceData.getData() != null && referenceData.getData().size() > 0) {

			List<Data> dataOfRef = Lambda.select(referenceData.getData(), Lambda.having(Lambda.on(Data.class).getIsDeleted(), Matchers.equalTo(false)));

			referenceData.setData(dataOfRef);

		}

		return referenceData;
	}

	public ReferenceData get(String type, String organizerId) {

		logger.debug("Retrieving an existing ReferenceData");

		Query query = new Query(Criteria.where("type").is(type).and("organizerId").is(organizerId).and("isDeleted").is(false));

		// logger.debug(query.getQueryObject());

		ReferenceData referenceData = mongoTemplate.findOne(query, ReferenceData.class, MongoDBCollections.REFERENCE_DATA.toString());

		if (referenceData != null && referenceData.getData() != null && referenceData.getData().size() > 0) {

			List<Data> dataOfRef = Lambda.select(referenceData.getData(), Lambda.having(Lambda.on(Data.class).getIsDeleted(), Matchers.equalTo(false)));

			// referenceData.setData(dataOfRef);

			List<Data> data = new ArrayList<Data>();

			if (dataOfRef != null) {

				ArrayList<Integer> al = new ArrayList<Integer>();

				for (Data d : dataOfRef) {

					if (d.getSortOrder() != null) {

						int ii = Integer.parseInt(d.getSortOrder());

						al.add(ii);
					}
				}

				if (al.size() > 0) {

					Collections.sort(al, new Comparator<Integer>() {

						public int compare(Integer d1, Integer d2) {
							// TODO Auto-generated method stub

							System.out.println("Comparision : " + d1.compareTo(d2));

							return d1.compareTo(d2);
						}
					});
				}

				if (al.size() > 0) {

					for (Integer i : al) {

						for (Data d : dataOfRef) {

							if (i == Integer.parseInt(d.getSortOrder())) {

								data.add(d);

								// System.out.println("REF DATA NAME : " +
								// d.getName());

								break;
							}
						}
					}
				}
			}

			if (data.size() > 0) {
				referenceData.setData(data);
			}
			else {
				referenceData.setData(dataOfRef);
			}
		}

		return referenceData;
	}

	public ReferenceData get(String id, String selector, boolean f) {

		Query query = new Query(Criteria.where("type").is(selector).and("data._id").is(id).and("isDeleted").is(false));
		System.out.println(query.toString());
		ReferenceData referenceData = mongoTemplate.findOne(query, ReferenceData.class, MongoDBCollections.REFERENCE_DATA.toString());

		return referenceData;
	}

	/*
	 * 
	 * 
	 */

	public Data getById(String dataId, String organizerId) throws NotFoundException {

		// logger.debug("Retrieving an existing ReferenceData");

		/*
		 * Aggregation aggregation =
		 * Aggregation.newAggregation(Aggregation.unwind("data"),
		 * Aggregation.match(Criteria.where("organizerId").is(organizerId)),
		 * Aggregation.match(Criteria.where("data._id").is(dataId)));
		 * 
		 * AggregationResults<ReferenceData> result =
		 * mongoTemplate.aggregate(aggregation, "data", ReferenceData.class);
		 * 
		 * 
		 * 
		 * List<ReferenceData> referenceData = result.getMappedResults();
		 * 
		 * if (referenceData.size() > 0) {
		 * 
		 * return referenceData.get(0).getData().get(0); } else {
		 * 
		 * return null; // throw new NotFoundException(dataId, "ReferenceData");
		 * }
		 */

		Query query = new Query(Criteria.where("data._id").is(dataId).and("organizerId").is(organizerId));

		List<ReferenceData> referenceData = mongoTemplate.find(query, ReferenceData.class, MongoDBCollections.REFERENCE_DATA.toString());

		if (referenceData.size() > 0) {

			List<Data> dataList = Lambda.select(referenceData.get(0).getData(), Lambda.having(Lambda.on(Data.class).getId(), Matchers.equalTo(dataId)));

			if (dataList.size() > 0) {

				return dataList.get(0);
			}
			else {

				return null;

			}
		}
		else {

			return null;
		}

	}

	/**
	 * Adds a new ReferenceData
	 * 
	 * @param ReferenceData
	 *            referenceData
	 * @return ReferenceData
	 */
	public ReferenceData add(ReferenceData referenceData) throws Exception {
		logger.debug("Adding a new Reference Data");

		try {

			if (referenceData.getData() != null && referenceData.getData().size() > 0) {

				for (Data refData : referenceData.getData()) {

					refData.setId(UUID.randomUUID().toString());
					refData.setCreatedDate(new DateTime());
					refData.setCreatedBy(referenceData.getCreatedBy());
				}
			}

			mongoTemplate.insert(referenceData, MongoDBCollections.REFERENCE_DATA.toString());

			return referenceData;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to add new Reference Data", e);

			throw e;
		}
	}

	/**
	 * Deletes an existing ReferenceData
	 * 
	 * @param String
	 *            id
	 * @return Boolean
	 */
	public Boolean delete(String id) {
		logger.debug("Deleting existing ReferenceData");

		try {

			Query query = new Query(Criteria.where("id").is(id));

			mongoTemplate.remove(query);

			return true;

		}
		catch (Exception e) {
			logger.error("An error has occurred while trying to delete ReferenceData", e);
			return false;
		}
	}

	/**
	 * Edits an existing ReferenceData
	 * 
	 * @param ReferenceData
	 *            referenceData
	 * @param String
	 *            referenceDataId
	 * @return ReferenceData
	 */
	public ReferenceData edit(ReferenceData referenceData) throws Exception, NotFoundException {

		logger.debug("Editing existing ReferenceData");

		try {

			mongoTemplate.save(referenceData);

			return referenceData;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to edit existing referenceData", e);

			throw e;
		}

	}

	public String getReferenceDataDetailUrl(ReferenceData referenceData, HttpServletRequest request) {
		String detailUrl = "";

		String replaceUrlToken = Urls.REFERENCE_DATA_BASE_URL;

		replaceUrlToken += Urls.REFERENCE_DATA_GET.replace("{id}", referenceData.getId().toString());

		detailUrl = Utils.getURLWithContextPath(request) + replaceUrlToken;

		return detailUrl;
	}

}
