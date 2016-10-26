package org.iqvis.nvolv3.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.service.EventConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("restriction")
@Controller
@RequestMapping("/userfeedbackconfiguration")
public class UserFeedBackConfigurationController {

	protected static Logger logger = Logger.getLogger("controller");

	@Autowired
	EventConfigurationService eventConfigurationService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Object getUserFeedBackConfiguration(HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch a UserFeedBackConfiguration");
		// System.out.println("-----------------------"+eventConfigurationService.getQuestionType("RADIO"));

		return eventConfigurationService.getGlobalFeedbackConfiguration();
	}

}
