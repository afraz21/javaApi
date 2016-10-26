package org.iqvis.nvolv3.objectchangelog.dao;

import java.util.List;

import org.iqvis.nvolv3.objectchangelog.domain.DataChangeLog;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.MongoDBCollections;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class DataChangeLogDao {

	@Autowired
	MongoTemplate mongoTemplate;
	
	public void deleteSubObject(String subObejct,String subObjectId){
		
		Query query=new Query();
		
		query.addCriteria(Criteria.where("subObjectId").is(subObjectId));
		
		mongoTemplate.remove(query, DataChangeLog.class);
	}

	public void add(DataChangeLog log) {

		mongoTemplate.insert(log, MongoDBCollections.Data_Change_Log.toString());

	}

	public boolean isChanged(String eventId, String organizerId, DateTime dateTime) {

		Query query = new Query();

		query.addCriteria(Criteria.where("timestamp").gt(dateTime).and("subObject").is("").and("eventIds").is(eventId));

		DataChangeLog log = mongoTemplate.findOne(query, DataChangeLog.class);

		if (log != null) {

			return true;

		}

		return false;

	}

	public boolean isChangedEventConfig(String eventId, String organizerId, DateTime dateTime) {

		Query query = new Query();

		query.addCriteria(Criteria.where("timestamp").gt(dateTime).and("subObject").is(Constants.EVENT_CONFIGURATION_LOG_KEY).and("eventIds").is(eventId));

		DataChangeLog log = mongoTemplate.findOne(query, DataChangeLog.class);

		if (log != null) {

			return true;

		}

		return false;
	}

	public List<DataChangeLog> getLogList(String eventId, String organizerId, DateTime dateTime, String objectType) {

		Query query = new Query();

		query.addCriteria(Criteria.where("timestamp").gt(dateTime).and("subObject").is(objectType).and("eventIds").is(eventId));

		List<DataChangeLog> temp = mongoTemplate.find(query, DataChangeLog.class);

		return temp;
	}

}
