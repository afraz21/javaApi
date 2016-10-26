package org.iqvis.nvolv3.service;

import org.iqvis.nvolv3.domain.EventConfigurationWrapper;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;

public interface EventConfigurationService {
	Object add(String eventId, String organizerId, Object eventConfiguration);

	Object get(String eventId, String organizerId) throws NotFoundException, Exception;

	public EventConfigurationWrapper getGlobalEventConfig() throws NotFoundException, Exception;

	public Object getEventConfiguration() throws NotFoundException, Exception;

	public void updateEventConfiguration(String organizerId, String eventId, String field, Object value);

	public String getQuestionType(String value);

	public Object getGlobalFeedbackConfiguration();

}
