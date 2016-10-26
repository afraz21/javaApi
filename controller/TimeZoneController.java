package org.iqvis.nvolv3.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.TimeZone;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.TimeZoneService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Messages;
import org.iqvis.nvolv3.utils.Urls;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("restriction")
@Controller
@RequestMapping(Urls.TIME_ZONE_BASE_URL)
public class TimeZoneController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_TIME_ZONE)
	private TimeZoneService timeZoneService;

	@RequestMapping(value = Urls.ADD_TIME_ZONE, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage add(@RequestBody @Valid TimeZone timeZone, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to add TimeZone");

		ResponseMessage response = new ResponseMessage();

		try {

			TimeZone addedVendor = timeZoneService.add(timeZone);

			// Response Message
			response.setMessage(String.format(Messages.ADD_SUCCESS_MESSAGE, "TimeZone"));

			response.setRecordId(addedVendor.getId().toString());

			response.setDetails_url(timeZoneService.getTimeZoneDetailUrl(addedVendor, request));

			response.setRecord(addedVendor);

			logger.debug(String.format(Messages.ADD_SUCCESS_MESSAGE, "TimeZone"));

		}
		catch (Exception e) {

			logger.debug("Exception while adding TimeZone", e);

			throw new Exception(e);
		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_TIME_ZONE, method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage edit(@RequestBody TimeZone vendor, @PathVariable("id") String timeZoneId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a TimeZone");

		ResponseMessage response = new ResponseMessage();

		vendor.setId(timeZoneId);

		try {

			TimeZone editedTimeZone = timeZoneService.edit(vendor);

			response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "TimeZone"));

			response.setRecordId(editedTimeZone.getId().toString());

			response.setRecord(editedTimeZone);

			response.setDetails_url(timeZoneService.getTimeZoneDetailUrl(editedTimeZone, request));

		}
		catch (Exception e) {

			if (e.getClass().equals(NotFoundException.class)) {
				throw new NotFoundException(timeZoneId, "TimeZone");
			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_TIME_ZONE, method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage delete(@PathVariable("id") String timeZoneid, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a TimeZone");

		ResponseMessage response = new ResponseMessage();

		try {

			TimeZone timeZone = timeZoneService.get(timeZoneid);

			if (timeZone != null) {

				timeZone.setIsDeleted(true);

				timeZoneService.edit(timeZone);

				response.setMessage(String.format(Messages.DELETED_SUCCESS_MESSAGE, "TimeZone"));

				response.setRecordId(timeZoneid);

				response.setDetails_url("");

			}
			else {

				throw new NotFoundException(timeZoneid, "TimeZone");
			}

		}
		catch (Exception e) {

			if (e.getClass().equals(NotFoundException.class)) {
				throw new NotFoundException(timeZoneid, "TimeZone");
			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.GET_TIME_ZONE, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public TimeZone get(@PathVariable("id") String Id, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch a TimeZone");

		TimeZone existingTimeZone = null;

		if (null != Id && !Id.equalsIgnoreCase("")) {

			existingTimeZone = timeZoneService.get(Id);

		}

		if (existingTimeZone == null) {

			throw new NotFoundException(Id, "TimeZone");
		}

		return existingTimeZone;
	}

	@RequestMapping(value = Urls.GET_TIME_ZONES, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<TimeZone> getAll(@RequestBody(required = false) @Valid Criteria search, Model model, HttpServletRequest request) {

		logger.debug("Received request to show all TimeZones");

		Pageable pageAble = new PageRequest(0, 20);

		if (search != null) {
			if (search.getQuery() != null) {
				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());

				}
			}
		}

		Page<TimeZone> timeZones = timeZoneService.getAll(search, request, pageAble);

		return timeZones;
	}

}
