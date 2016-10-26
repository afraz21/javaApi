package org.iqvis.nvolv3.service;

import org.iqvis.nvolv3.domain.PushLog;
import org.iqvis.nvolv3.push.Query;

public interface PushLoggingService {
	PushLog add(PushLog logs);

	Query add(Query query);
}
