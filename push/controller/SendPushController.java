package org.iqvis.nvolv3.push.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.mobile.service.UserDeviceInfoService;
import org.iqvis.nvolv3.push.Query;
import org.iqvis.nvolv3.service.PushLoggingService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.gson.Gson;

@RequestMapping(Urls.MOBILE_PUSH_NOTIFICATION_BASE)
@Controller
public class SendPushController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_USER_DEVICE_INFO_SERVICE)
	private UserDeviceInfoService userDeviceInfoService;

	@Resource(name = Constants.PUSH_LOGGING_SERVICE)
	private PushLoggingService pushLoggingService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/{deviceType}", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Object sendPush(@RequestBody(required = true) Object object, @PathVariable("deviceType") String deviceType, @PathVariable("organizerId") String userId, HttpServletRequest request) throws NotFoundException {

		logger.debug("Start Push Notification Servcie ");

		Gson gson = new Gson();

		Map<String, Object> map = new HashMap<String, Object>();

		map = (Map<String, Object>) gson.fromJson(gson.toJson(object), map.getClass());

		Set<String> keys = map.keySet();

		Query query = userDeviceInfoService.getQuery(keys.toArray()[0].toString(), map.get(keys.toArray()[0]).toString(), deviceType,false);

		query.setData(map.get("data"));

		pushLoggingService.add(query);

		return Utils.sendAlert(query);
	}

}
