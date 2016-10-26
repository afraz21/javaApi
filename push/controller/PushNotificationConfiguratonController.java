package org.iqvis.nvolv3.push.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.PushNotificationConfiguration;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.mobile.service.UserDeviceInfoService;
import org.iqvis.nvolv3.utils.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("/device")
@Controller
public class PushNotificationConfiguratonController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_USER_DEVICE_INFO_SERVICE)
	private UserDeviceInfoService userDeviceInfoService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/{deviceToken}/configuration", method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Object sendPush(@RequestBody(required = true) PushNotificationConfiguration configuration, @PathVariable("deviceToken") String deviceToken, HttpServletRequest request) throws NotFoundException {

		return userDeviceInfoService.editNotificationConfiguration(configuration, deviceToken);

	}
}
