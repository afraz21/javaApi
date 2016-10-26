package org.iqvis.nvolv3.mobile.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.DeviceInfo;
import org.iqvis.nvolv3.domain.EventSearch;
import org.iqvis.nvolv3.domain.Personnel;
import org.iqvis.nvolv3.domain.PushNotificationConfiguration;
import org.iqvis.nvolv3.domain.Track;
import org.iqvis.nvolv3.domain.User;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;
import org.iqvis.nvolv3.mobile.app.config.service.AppConfigService;
import org.iqvis.nvolv3.mobile.bean.AppConfResponse;
import org.iqvis.nvolv3.mobile.bean.ListEvent;
import org.iqvis.nvolv3.mobile.bean.ResponseClass;
import org.iqvis.nvolv3.mobile.bean.Texts;
import org.iqvis.nvolv3.mobile.service.MobileEventServiceImpl;
import org.iqvis.nvolv3.mobile.service.UserDeviceInfoService;
import org.iqvis.nvolv3.service.ConstantFactoryImpl;
import org.iqvis.nvolv3.service.TrackService;
import org.iqvis.nvolv3.service.UserService;
import org.iqvis.nvolv3.utils.AppType;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("restriction")
@Controller
@RequestMapping(Urls.MOBILE_APP_CONFIG_DATA_BASE_URL)
public class MobileAppConfigController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_APP_CONFIG)
	private AppConfigService appConfiguration_Service;

	@Resource(name = Constants.SERVICE_USER)
	private UserService userService;

	@Autowired
	private MobileEventServiceImpl mobileEventServiceImpl;

	@Resource(name = Constants.SERVICE_USER_DEVICE_INFO_SERVICE)
	private UserDeviceInfoService userDeviceInfoService;

	@Resource(name = Constants.SERVICE_TRACK)
	private TrackService trackService;

	@Autowired
	MobileEventSearchController mobileEventSearchController;

	@Autowired
	private String geoIpLocationDatabasePath;

	// @Autowired
	Integer APP_STARTUP_PREVIOUS_DAYS = ConstantFactoryImpl.APP_STARTUP_PREVIOUS_DAYS;

	/*
	 * Get Application Configuration Object By Id
	 */

	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public AppConfiguration get(@PathVariable("appid") String AppConfigurationId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch an App Configuration Object");

		AppConfiguration existingAppConfig = null;

		if (null != AppConfigurationId && !AppConfigurationId.equalsIgnoreCase("")) {

			existingAppConfig = appConfiguration_Service.getAppObject(AppConfigurationId);

			User user = userService.get(existingAppConfig.getOrganizerId());

			existingAppConfig.setLanguages(user.getLanguages());

		}

		if (existingAppConfig == null) {

			throw new NotFoundException(AppConfigurationId, "App Configuration");
		}

		return existingAppConfig;
	}

	/*
	 * Get AppConfiguration
	 */
	@RequestMapping(value = Urls.Mobile_APP_CONFIG_LIST + "/{code}/{version}", method = { RequestMethod.GET })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseClass getAppConfigurationListTest(@PathVariable(value = "organizerId") String organizerId, @PathVariable(value = "appid") String appId, @PathVariable(value = "code") String code, @PathVariable(value = "version") String version, @RequestParam(value = "deviceToken", required = false) String deviceToken, @RequestParam(value = "deviceType", required = false) String deviceType, @RequestParam(value = "pageSize", required = false) Integer pageSize, @RequestParam(value = "pageNumber", required = false) Integer pageNumber, Model model, HttpServletRequest request) throws Exception {

		// logger.debug("Received request to show all appConfiguration");

		if (code != null && code.toLowerCase().contains("zh-")) {
			code = "ZH";
		}

		if (deviceToken != null) {

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

			deviceInfo.setDeviceType(deviceType);

			deviceInfo.setEventId(null);

			userDeviceInfoService.add(deviceInfo);

		}

		ResponseClass responseObj = new ResponseClass();

		AppConfiguration appConf = appConfiguration_Service.get(appId, organizerId);

		User user = userService.get(appConf.getOrganizerId());

		appConf.setLanguages(user.getLanguages());

		// appConf.filterByLangCode(code);

		List<Personnel> organizerPersonnels = null;// personnelService.getOrganizerPersonnelsList(organizerId);

		if (organizerPersonnels == null) {

			organizerPersonnels = new ArrayList<Personnel>();
		}

		if (AppType.GENERAL.toString().equals(appConf.getAppType() + "")) {

			String ipAddress = request.getHeader("X-Forwarded-For");
			
			if(ipAddress==null || ipAddress.equals("")){
			
				ipAddress = request.getRemoteAddr();	
			}
			
			EventSearch search = new EventSearch();

			search.setAppId(appId);

			try {

				search.setLocation(Utils.getCountryByGeoIPLocation(geoIpLocationDatabasePath, ipAddress));
			}
			catch (Exception ex) {

				ex.printStackTrace();
			}
			// search.setFromDateTime(DateTime.now());

			Page<ListEvent> list = mobileEventSearchController.searchEvent(search, request, appConf.getPartnerId(), appId);

			responseObj.setEvents_list(Utils.getFilteredEventList(list.getContent(), APP_STARTUP_PREVIOUS_DAYS));

		}
		else {

			List<Track> tracks = trackService.getOrganizerTracks(organizerId);

			if (tracks == null) {

				tracks = new ArrayList<Track>();
			}

			List<ListEvent> eventList = mobileEventServiceImpl.getAllAppEvents(appConf.getEvents(), tracks, code);

			responseObj.setEvents_list(Utils.getFilteredEventListWightLabel(eventList));

		}

		responseObj.setApp_config(null);

		if (Integer.parseInt(version) < appConf.getVersion() || appConf.getVersion() == 0) {

			Texts appConfRes = mobileEventServiceImpl.getAppConfigResponse(appConf, code);

			AppConfResponse res = new AppConfResponse();

			res.setId(appConf.getId());

			res.setOrganizerId(appConf.getOrganizerId());

			res.setVersion(appConf.getVersion());

			res.setTexts(appConfRes);

			res.setShow_news(appConf.getShow_news());

			res.setShow_organizer_logo_on_events_listing(appConf.getShow_organizer_logo_on_events_listing());

			res.setShow_profile_screen(appConf.getShow_profile_screen());

			res.setProfile_screen_skippable(appConf.getProfile_screen_skippable());

			res.setSupported_languages(appConf.getSupported_languages());

			res.setCurrent_lang(appConf.getCurrent_lang());

			res.setETC(appConf.getEtc());

			responseObj.setApp_config(res);

		}

		List<ListEvent> sortedList = responseObj.getEvents_list();

		if (pageNumber != null) {

			List<List<ListEvent>> pages = Utils.getPages(sortedList, pageSize);

			if (pages.size() > 0) {

				sortedList = pages.get(0);
			}
			else {

				sortedList = new ArrayList<ListEvent>();
			}

		}

		return responseObj;

	}

	@RequestMapping(value = Urls.Mobile_APP_CONFIG_LIST + "/{code}/{version}" + "/object", method = { RequestMethod.GET })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseClass getAppConfigurationObject(@PathVariable(value = "organizerId") String organizerId, @PathVariable(value = "appid") String appId, @PathVariable(value = "code") String code, @PathVariable(value = "version") String version, @RequestParam(value = "deviceToken", required = false) String deviceToken, @RequestParam(value = "deviceType", required = false) String deviceType, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to show all appConfiguration");

		if (code != null && code.toLowerCase().contains("zh-")) {
			code = "ZH";
		}

		if (deviceToken != null) {

			DeviceInfo deviceInfo = userDeviceInfoService.get(deviceToken);

			if (deviceInfo == null) {

				deviceInfo = new DeviceInfo();
			}

			deviceInfo.setAppId(appId);

			deviceInfo.setDeviceToken(deviceToken);

			deviceInfo.setOrganizerId(organizerId);

			deviceInfo.setDeviceType(deviceType);

			deviceInfo.setEventId(null);

			userDeviceInfoService.add(deviceInfo);

		}

		ResponseClass responseObj = new ResponseClass();

		AppConfiguration appConf = appConfiguration_Service.get(appId, organizerId);

		User user = userService.get(appConf.getOrganizerId());

		appConf.setLanguages(user.getLanguages());

		Texts appConfRes = mobileEventServiceImpl.getAppConfigResponse(appConf, code);

		AppConfResponse res = new AppConfResponse();

		res.setId(appConf.getId());

		res.setOrganizerId(appConf.getOrganizerId());

		res.setVersion(appConf.getVersion());

		res.setTexts(appConfRes);

		res.setShow_news(appConf.getShow_news());

		res.setShow_organizer_logo_on_events_listing(appConf.getShow_organizer_logo_on_events_listing());

		res.setShow_profile_screen(appConf.getShow_profile_screen());

		res.setProfile_screen_skippable(appConf.getProfile_screen_skippable());

		res.setSupported_languages(appConf.getSupported_languages());

		res.setETC(appConf.getEtc());

		res.setCurrent_lang(appConf.getCurrent_lang());

		responseObj.setApp_config(res);

		return responseObj;

	}

}
