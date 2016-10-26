package org.iqvis.nvolv3.service;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.dao.EventConfigurationDao;
import org.iqvis.nvolv3.domain.EventConfigurationWrapper;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(Constants.SERVICE_EVENT_CONFIGURATION)
@Transactional
public class EventConfigurationServiceImpl implements EventConfigurationService {

	@Autowired
	EventConfigurationDao eventConfigurationDao;

	@Resource(name = Constants.SERVICE_EVENT)
	EventService eventService;

	protected static Logger logger = Logger.getLogger("service");

	public Object add(String eventId, String organizerId, Object eventConfiguration) {

		logger.debug("Adding EventCOnfiguration Object To Event");

		return eventConfigurationDao.add(eventId, organizerId, eventConfiguration);
	}

	public Object get(String eventId, String organizerId) throws NotFoundException, Exception {
		logger.debug("Retriveing EventCOnfiguration Object To Event");
		return eventConfigurationDao.get(eventId, organizerId);
	}

	public EventConfigurationWrapper getGlobalEventConfig() {

		return eventConfigurationDao.getGlobalEventConfiguration();

	}

	public Object getEventConfiguration() {

		return this.getGlobalEventConfig().getEventConfiguration();

	}

	public void updateEventConfiguration(String organizerId, String eventId, String field, Object value) {

		// Event event = eventService.get(eventId, organizerId);

		// event.setAppLevelChange(true);
		//
		// event.setEventLevelChange(true);

		// try {
		// eventService.edit(event, eventId, organizerId);
		// }
		// catch (NotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		eventConfigurationDao.updateEventConfiguration(organizerId, eventId, field, value);

	}

	public String getQuestionType(String value) {

		return eventConfigurationDao.getQuestionType(value);
	}

	public Object getGlobalFeedbackConfiguration() {

		return eventConfigurationDao.getGlobalFeedbackConfiguration();
	}

}
