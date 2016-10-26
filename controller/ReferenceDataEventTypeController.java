package org.iqvis.nvolv3.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.ReferenceData;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.service.ReferenceDataService;
import org.iqvis.nvolv3.utils.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("restriction")
@Controller
@RequestMapping("referencedata/")
public class ReferenceDataEventTypeController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_REFERENCE_DATA)
	private ReferenceDataService reference_Data_Service;

	@RequestMapping(value = "eventtype/{type}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ReferenceData get(@PathVariable("type") String referenceDataType, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch an reference data");

		ReferenceData existingReferenceData = null;

		existingReferenceData = reference_Data_Service.get(referenceDataType);

		if (existingReferenceData == null) {
			throw new NotFoundException(referenceDataType, "reference data");
		}

		return existingReferenceData;
	}

}
