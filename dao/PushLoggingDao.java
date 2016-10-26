package org.iqvis.nvolv3.dao;

import javax.annotation.Resource;

import org.iqvis.nvolv3.domain.PushLog;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PushLoggingDao {

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	public PushLog add(PushLog query) {

		query.setCreatedDate(DateTime.now(DateTimeZone.UTC));

		mongoTemplate.save(query);

		return query;
	}

}
