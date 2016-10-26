package org.iqvis.nvolv3.service;

import org.iqvis.nvolv3.dao.PushLoggingDao;
import org.iqvis.nvolv3.domain.PushLog;
import org.iqvis.nvolv3.push.Query;
import org.iqvis.nvolv3.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(Constants.PUSH_LOGGING_SERVICE)
@Transactional
public class PushLoggingServiceImpl implements PushLoggingService {

	@Autowired
	private PushLoggingDao pushLoggingDao;

	public PushLog add(PushLog logs) {

		return pushLoggingDao.add(logs);
	}

	public Query add(Query query) {

		PushLog log = new PushLog();

		log.setDeviceTokens(query.getWhere().getDeviceToken().get$in());

		log.setData(query.getData());

		pushLoggingDao.add(log);

		return query;
	}

}
