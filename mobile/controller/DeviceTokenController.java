package org.iqvis.nvolv3.mobile.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.DeviceInfo;
import org.iqvis.nvolv3.domain.DownloadedEventList;
import org.iqvis.nvolv3.domain.EventLanguage;
import org.iqvis.nvolv3.domain.PushNotificationConfiguration;
import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;
import org.iqvis.nvolv3.mobile.app.config.service.AppConfigService;
import org.iqvis.nvolv3.mobile.service.UserDeviceInfoService;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Urls;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(Urls.MOBILE + "/organizer/{organizerId}")
public class DeviceTokenController {

	@Resource(name = Constants.SERVICE_EVENT)
	private EventService eventService;

	@Resource(name = Constants.SERVICE_USER_DEVICE_INFO_SERVICE)
	private UserDeviceInfoService userDeviceInfoService;

	@Resource(name = Constants.SERVICE_APP_CONFIG)
	private AppConfigService appConfiguration_Service;

	@RequestMapping(value = "/deviceType/{deviceType}/deviceToken/{deviceToken}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseMessage updateDeviceTokens(@RequestBody(required = true) DownloadedEventList deviceInfoReq, @PathVariable("appid") String appId, @PathVariable("organizerId") String organizerId, @PathVariable("deviceToken") String deviceToken, @PathVariable("deviceType") String deviceType, Model model, HttpServletRequest request) throws Exception {

		ResponseMessage message = new ResponseMessage();

		DeviceInfo deviceInfo = userDeviceInfoService.get(deviceToken);

		if (deviceInfo == null) {

			deviceInfo = new DeviceInfo();
		}

		AppConfiguration appConfiguration = appConfiguration_Service.get(appId, organizerId);

		if (appConfiguration != null) {

			deviceInfo.setPushNotificationConfiguration(appConfiguration.getPushNotificationConfiguration());
		}
		else {

			deviceInfo.setPushNotificationConfiguration(new PushNotificationConfiguration());
		}

		deviceInfo.setAppId(appId);

		deviceInfo.setDeviceToken(deviceToken);

		deviceInfo.setOrganizerId(organizerId);

		List<String> eventIds = deviceInfoReq.getEventId();

		deviceInfo.setEventId(null);

		List<EventLanguage> eventLangs = deviceInfo.getEvent_languages() == null ? new ArrayList<EventLanguage>() : deviceInfo.getEvent_languages();

		List<EventLanguage> tempLangs = new ArrayList<EventLanguage>();

		if (eventIds != null) {
			for (String id : eventIds) {

				for (EventLanguage eventLanguage : eventLangs) {

					if (eventLanguage != null && eventLanguage.getEventId().equals(id)) {

						tempLangs.add(eventLanguage);
					}

				}

				deviceInfo.setEventId(id);
			}
		}

		deviceInfo.setEvent_languages(null);

		for (EventLanguage eventLanguage : tempLangs) {

			deviceInfo.setEvent_languages(eventLanguage);
		}

		deviceInfo.setDeviceType(deviceType);

		deviceInfo = userDeviceInfoService.add(deviceInfo);

		message.setRecordId(deviceInfo.getDeviceToken());

		message.setRecord(deviceInfo);

		message.setMessage("Device Token has been updated");

		message.setMessageCode(Constants.SUCCESS_CODE);

		message.setHttpCode(Constants.SUCCESS_CODE);

		return message;
	}
}
