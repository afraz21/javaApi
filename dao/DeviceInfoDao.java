package org.iqvis.nvolv3.dao;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.DeviceInfo;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@SuppressWarnings("restriction")
@Repository
public class DeviceInfoDao {
	protected static Logger logger = Logger.getLogger("service");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	public DeviceInfo add(DeviceInfo deviceInfo) {

		deviceInfo.setLastModifiedAt(DateTime.now());

		deviceInfo.setCreatedAt(DateTime.now());

		mongoTemplate.save(deviceInfo);

		return deviceInfo;
	}

	public DeviceInfo get(String deviceToken) {

		Query query = new Query();

		query.addCriteria(Criteria.where("_id").is(deviceToken));

		return mongoTemplate.findOne(query, DeviceInfo.class);

	}

	public List<DeviceInfo> get(String selector, Object value) {

		Query query = new Query();

		query.addCriteria(Criteria.where(selector).is(value));

		return mongoTemplate.find(query, DeviceInfo.class);

	}

	public List<DeviceInfo> get(List<String> ids) {

		Query query = new Query();

		query.addCriteria(Criteria.where("_id").in(ids));

		return mongoTemplate.find(query, DeviceInfo.class);

	}

}
