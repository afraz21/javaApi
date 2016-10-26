package org.iqvis.nvolv3.checkhealth.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.dao.KeyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@Controller
@RequestMapping("checkhealth")
public class CheckHealthController {
	
	@Autowired
	private KeyDao keyDao;

	protected static Logger logger = Logger.getLogger("controller");
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public long getCountApiConfig(HttpServletRequest request) {
	
		logger.debug("Received request to show count for appConfigurationKeys");
	
		long count = keyDao.getCount();
		
		return count;
	}

}
