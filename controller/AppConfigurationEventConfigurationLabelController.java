package org.iqvis.nvolv3.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;
import org.iqvis.nvolv3.mobile.app.config.service.AppConfigService;
import org.iqvis.nvolv3.mobile.bean.DataCMS;
import org.iqvis.nvolv3.service.EventConfigurationService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("application/{appId}/organizer/{organizerId}/events/{eventId}/configuration")
public class AppConfigurationEventConfigurationLabelController {

	@Resource(name = Constants.SERVICE_APP_CONFIG)
	private AppConfigService appConfiguration_Service;

	@Resource(name = Constants.SERVICE_EVENT_CONFIGURATION)
	private EventConfigurationService eventConfigurationService;

	@RequestMapping(value = Urls.ADD_ACTIVITY, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ConfigurationResponse getConfigurations(@PathVariable("organizerId") String organizerId, @PathVariable("appId") String appId, @PathVariable("eventId") String eventId, HttpServletRequest request) throws Exception {

		AppConfiguration appConfiguration = appConfiguration_Service.get(appId, organizerId);

		Object eventConfiguration = eventConfigurationService.get(eventId, organizerId);

		List<DataCMS> appTextLabellist = new ArrayList<DataCMS>();

		if (appConfiguration != null && appConfiguration.getGeneral() != null) {

			if (appConfiguration.getGeneral().getTexts() != null) {

				appTextLabellist.addAll(appConfiguration.getGeneral().getTexts());
			}

			if (appConfiguration.getGeneral().getLabels() != null) {

				appTextLabellist.addAll(appConfiguration.getGeneral().getLabels());
			}
		}

		ConfigurationResponse response = new ConfigurationResponse();

		response.setApplicationConfigurationTexts(appTextLabellist);
		
		response.setEventConfigurationTexts(Utils.getKeyFromObjectTypeConfiguration(eventConfiguration, "texts"));

		return response;
	}

}
