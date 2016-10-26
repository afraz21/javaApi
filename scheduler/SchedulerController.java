package org.iqvis.nvolv3.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.dao.EventCampaignDao;
import org.iqvis.nvolv3.dao.EventCampaignPariticipantDao;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.EventAlert;
import org.iqvis.nvolv3.domain.EventCampaign;
import org.iqvis.nvolv3.domain.EventCampaignParticipant;
import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;
import org.iqvis.nvolv3.mobile.app.config.service.AppConfigService;
import org.iqvis.nvolv3.mobile.bean.MobileEventAlert;
import org.iqvis.nvolv3.mobile.service.UserDeviceInfoService;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.objectchangelog.service.PushExtra;
import org.iqvis.nvolv3.push.PushData;
import org.iqvis.nvolv3.push.Query;
import org.iqvis.nvolv3.service.EventAlertService;
import org.iqvis.nvolv3.service.EventCampaignParticipantService;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.service.PushLoggingService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Utils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("restriction")
@RequestMapping("scheduler/")
@Controller
public class SchedulerController {

	@Resource(name = Constants.SERVICE_EVENT_ALERT)
	private EventAlertService eventAlertService;

	@Resource(name = Constants.PUSH_LOGGING_SERVICE)
	private PushLoggingService pushLoggingService;

	@Resource(name = Constants.SERVICE_USER_DEVICE_INFO_SERVICE)
	private UserDeviceInfoService userDeviceInfoService;

	@Resource(name = Constants.SERVICE_EVENT_CAMPAIGN_PARTICIPANT)
	private EventCampaignParticipantService eventCampaignParticipantService;

	// @Resource(name = Constants.SERVICE_EVENT_CAMPAIGN)
	// private EventCampaignService eventCampaignService;

	@Resource(name = Constants.SERVICE_EVENT)
	private EventService eventService;

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	@Resource(name = Constants.SERVICE_APP_CONFIG)
	private AppConfigService appConfiguration_Service;

	@Autowired
	EventCampaignPariticipantDao eventCampaignParticipantDao;

	@Autowired
	EventCampaignDao eventCampaignDao;

	protected static Logger logger = Logger.getLogger("controller");

	@RequestMapping(value = "alerts", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public String getEvent(HttpServletRequest request) throws Exception {

		List<EventAlert> list = eventAlertService.getPendingAlerts();

		HashMap<String, List<AppConfiguration>> map = new HashMap<String, List<AppConfiguration>>();

		for (EventAlert eventAlert : list) {
			try {
				MobileEventAlert t = new MobileEventAlert(eventAlert);
				
				Event event = eventService.get(eventAlert.getEventId(), eventAlert.getOrganizerId());

				t.setGmtTime(eventAlert.getAlertScheduledTime());

				if (t.getStartDate() != null) {

					if (DateTime.now(DateTimeZone.UTC).compareTo(t.getStartDate()) >= 0) {

						eventAlert.setIsSent(true);
						
						DateTimeFormatter formater = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
						
						String eventTime=Utils.toEventTimeZoon(DateTime.now(DateTimeZone.UTC), event.getTimeZone());
						
						eventAlert.setSentTime(formater.parseDateTime(eventTime + " Z"));

						eventAlertService.edit(eventAlert);

						if (eventAlert.getSendAsPush()) {

							final Query query = userDeviceInfoService.getQuery("eventId", eventAlert.getEventId(), null,false);

							PushData pushData = new PushData();

							PushExtra extra = new PushExtra();

							extra.setObjectId(eventAlert.getId());

							extra.setObjectType("ALERT");

							extra.setEventId(eventAlert.getEventId());

							pushData.setExtra(extra);

							pushData.setAlert(eventAlert.getTitle());

							pushData.setBadge("Increment");

							query.setData(pushData);

							List<AppConfiguration> appList = new ArrayList<AppConfiguration>();

							if (map.containsKey(eventAlert.getEventId())) {
								appList = new ArrayList<AppConfiguration>(map.get(eventAlert.getEventId()));
							}
							else {

								List<String> keys = new ArrayList<String>();

								List<Object> values = new ArrayList<Object>();

								appList = new ArrayList<AppConfiguration>(appConfiguration_Service.get(keys, values));
							}

							List<String> checkList = new ArrayList<String>();

							for (AppConfiguration appConfiguration : appList) {

								if ((appConfiguration.getX_PARSE_APPLICATION_ID() == null || appConfiguration.getX_PARSE_REST_API_KEY() == null) || (appConfiguration.getX_PARSE_APPLICATION_ID() == "" || appConfiguration.getX_PARSE_REST_API_KEY() == "")) {

									continue;
								}

								if (checkList.contains(appConfiguration.getX_PARSE_APPLICATION_ID())) {
									continue;
								}
								else {
									checkList.add(appConfiguration.getX_PARSE_APPLICATION_ID());
								}

								Utils.sendAlert(query, appConfiguration.getX_PARSE_APPLICATION_ID(), appConfiguration.getX_PARSE_REST_API_KEY());

							}

							pushLoggingService.add(query);
						}

					}
				}

			}
			catch (NullPointerException nullPointerHandler) {

			}
			catch (Exception e) {

				e.printStackTrace();
			}

		}
		
		map=null;
		
		list=null;
		
		System.gc();
		
		return "Call Success !";
	}


	@RequestMapping(value = "participants", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public String getParticipantsSch(HttpServletRequest request) throws Exception {

		logger.debug("got call for event campaign processing");

		List<EventCampaign> campaignList = eventCampaignDao.getALL();

		DateTime current = DateTime.now(DateTimeZone.UTC);

		logger.debug("Campaign List Is Not Null");

		DateTimeFormatter formatGMT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS").withZoneUTC();

		if (campaignList != null) {

			for (EventCampaign eventCampaign : campaignList) {

				DateTime campaignStartDateTime = null, campaignEndDateTime = null, eventStartDateTime = null, eventEndDateTime = null;

				Event event = eventService.get(eventCampaign.getEventId());

				try {
					// Event Dates

					logger.debug("eventId is : " + event.getId());

					eventStartDateTime = event.getEventDates().get(0);

					eventEndDateTime = event.getEventDates().get(event.getEventDates().size() - 1);

					// Schedule Campaign Dates
					if (eventCampaign.getCompleteEvent() == false && eventCampaign.getSchedule() == true) {

						campaignStartDateTime = formatGMT.parseDateTime(Utils.toGMT(eventCampaign.getStartDate(), event.getTimeZone()));

						campaignEndDateTime = formatGMT.parseDateTime(Utils.toGMT(eventCampaign.getEndDate(), event.getTimeZone()));

					}
					else {

						campaignStartDateTime = eventStartDateTime;

						campaignEndDateTime = eventEndDateTime;
					}

					if (eventCampaign.getParticipants() != null) {

						for (EventCampaignParticipant eventCampaignParticipant : eventCampaign.getParticipants()) {

							try {

								DateTime participantStartDateTime = null, participantEndDateTime = null;

								if (eventCampaignParticipant.getCompleteCampaign() == false && eventCampaignParticipant.getSchedule() == true) {

									participantStartDateTime = formatGMT.parseDateTime(Utils.toGMT(eventCampaignParticipant.getStartDate(), event.getTimeZone()));

									participantEndDateTime = formatGMT.parseDateTime(Utils.toGMT(eventCampaignParticipant.getEndDate(), event.getTimeZone()));

								}
								else if (eventCampaignParticipant.getCompleteCampaign() == true && eventCampaignParticipant.getSchedule() == false) {

									participantStartDateTime = campaignStartDateTime;

									participantEndDateTime = campaignEndDateTime;

								}

								if (current.isAfter(participantStartDateTime) && current.isBefore(participantEndDateTime) && eventCampaignParticipant.isSent() == false) {

									eventCampaignParticipant.setPrepared(true);

									eventCampaignParticipant.setIs_end(false);

									eventCampaignParticipant.setSent(true);

									eventCampaignParticipantDao.edit(eventCampaignParticipant);

									logger.debug("Campaign Participant Time Exists Between event time range and current DateTime");

									eventCampaignParticipant.setPrepared(true);

									List<String> l = new ArrayList<String>();

									l.add(event.getId());

									l.remove("");

									if (l.size() > 0 && eventCampaignParticipant.getId() != null) {

										System.out.println(eventCampaignParticipant.getId());

										dataChangeLogService.add(l, "EVENT", Constants.EVENT_CAMPAIGN__LOG_KEY, eventCampaignParticipant.getId(), Constants.LOG_ACTION_UPDATE, EventCampaign.class.toString());
									}

								}
								else if (current.isAfter(participantEndDateTime) == true && eventCampaignParticipant.getIs_end() == false) {

									eventCampaignParticipant.setIs_end(true);

									eventCampaignParticipantDao.edit(eventCampaignParticipant);

									logger.debug("Campaign Participant Time Expire");

									List<String> l = new ArrayList<String>();

									l.add(event.getId());

									l.remove("");

									if (l.size() > 0 && eventCampaignParticipant.getId() != null) {

										dataChangeLogService.add(l, "EVENT", Constants.EVENT_CAMPAIGN__LOG_KEY, eventCampaignParticipant.getId(), Constants.LOG_ACTION_DELETE, EventCampaign.class.toString());
									}

								}

							}
							catch (Exception e) {

								logger.debug("Campaign Participant Skip Due To Exception");
							}

						}
					}
				}

				catch (Exception e) {

					logger.debug("Campaign Skip Due To Exception");
				}
			}

		}

		campaignList=null;
		
		System.gc();
		
		return "Success Call";

	}

}
