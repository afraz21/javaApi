package org.iqvis.nvolv3.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.AppConfigKeys;
import org.iqvis.nvolv3.dao.EventConfigurationKeysDao;
import org.iqvis.nvolv3.dao.KeyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class KeysController {

	@Autowired
	private KeyDao keyDao;
	
	@Autowired
	private EventConfigurationKeysDao eventConfigurationKeysDao;

	protected static Logger logger = Logger.getLogger("controller");

	@RequestMapping(value = "/keys/", method = { RequestMethod.GET })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public AppConfigKeys getKeys(HttpServletRequest request) throws Exception {

		logger.debug("Received request to show all appConfiguration Keys");
		AppConfigKeys container = keyDao.getKeyContainer();

		return container;
	}
	
	@RequestMapping(value = "/eventkeys/", method = { RequestMethod.GET })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Object getEventKeys(HttpServletRequest request) throws Exception {

		logger.debug("Received request to show all eventConfiguration Keys");

		Object container = eventConfigurationKeysDao.getKeyContainer();

		return container;
	}

}
