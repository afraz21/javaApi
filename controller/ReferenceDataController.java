package org.iqvis.nvolv3.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.Data;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.ReferenceData;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.ReferenceDataService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Messages;
import org.iqvis.nvolv3.utils.Urls;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("restriction")
@Controller
@RequestMapping(Urls.REFERENCE_DATA_BASE_URL)
public class ReferenceDataController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_REFERENCE_DATA)
	private ReferenceDataService reference_Data_Service;

	/*
	 * Get All Reference Data with the optional criteria
	 */

	@RequestMapping(value = Urls.REFERENCE_DATA_LIST, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<ReferenceData> getReferenceDataList(@RequestBody(required = false) @Valid Criteria search, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) {

		logger.debug("Received request to show all reference data");

		Pageable pageAble = null;

		if (search != null) {
			if (search.getQuery() != null) {
				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());
					;
				}
			}
		}

		Page<ReferenceData> referenceDataList = reference_Data_Service.getAll(search, request, pageAble);

		return referenceDataList;
	}

	/*
	 * Create Reference Data
	 */

	@RequestMapping(value = Urls.REFERENCE_DATA_LIST, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage add(@RequestBody @Valid ReferenceData referenceData, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request, BindingResult result) throws Exception {

		logger.debug("Received request to add reference data");

		ResponseMessage response = new ResponseMessage();

		if (result.hasErrors()) {

			response.setMessage(result.getFieldError().getDefaultMessage());

			response.setMessageCode(Constants.ERROR_CODE);

			// do something

		}
		else {

			try {

				ReferenceData addedReferenceData = null;
				if (reference_Data_Service.get(referenceData.getType(), referenceData.getOrganizerId()) == null) {

					referenceData.setOrganizerId(organizerId);

					addedReferenceData = reference_Data_Service.add(referenceData);

				}
				else {

					referenceData.setOrganizerId(organizerId);

					addedReferenceData = reference_Data_Service.edit(referenceData, referenceData.getType());

				}

				response.setMessage(String.format(Messages.ADD_SUCCESS_MESSAGE, "Reference Data"));

				response.setRecordId(addedReferenceData.getId().toString());

				response.setRecord(addedReferenceData);

				response.setDetails_url(reference_Data_Service.getReferenceDataDetailUrl(addedReferenceData, request));

				logger.debug("Reference Data has been added successfully");

			}
			catch (Exception e) {

				logger.debug("Exception while adding Reference Data", e);

				throw new Exception(e);
			}

			return response;
		}

		return response;
	}

	/*
	 * Update Reference Data
	 */

	@RequestMapping(value = Urls.REFERENCE_DATA_UPDATE, method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage edit(@RequestBody ReferenceData referenceData, @PathVariable("type") String referenceDataType, Model model, @PathVariable(value = "organizerId") String organizerId, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit an Reference Data");

		ResponseMessage response = new ResponseMessage();

		try {

			referenceData.setOrganizerId(organizerId);

			ReferenceData editedReferenceData = reference_Data_Service.edit(referenceData, referenceDataType);

			response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "Reference Data"));

			response.setRecordId(editedReferenceData.getId().toString());

			response.setRecord(editedReferenceData);

			response.setDetails_url(reference_Data_Service.getReferenceDataDetailUrl(editedReferenceData, request));

		}
		catch (Exception e) {

			logger.debug("Exception while updating Reference Data", e);

			if (e.getClass().equals(NotFoundException.class)) {

				throw new NotFoundException(referenceDataType, "Reference Data");

			}
			else {

				throw new Exception(e);
			}
		}

		return response;
	}

	/*
	 * Delete Reference Data
	 */

	@RequestMapping(value = Urls.REFERENCE_DATA_UPDATE, method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage delete(@PathVariable("type") String referenceDataType, Model model, @PathVariable(value = "organizerId") String organizerId, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit an reference Data");

		ResponseMessage response = new ResponseMessage();

		try {

			ReferenceData refrenceData = reference_Data_Service.get(referenceDataType, organizerId);

			if (refrenceData != null) {

				refrenceData.setIsDeleted(true);

				reference_Data_Service.edit(refrenceData, referenceDataType);

				response.setMessage(String.format(Messages.DELETED_SUCCESS_MESSAGE, "reference Data"));

				response.setRecordId(referenceDataType);

				response.setDetails_url("");

			}
			else {

				throw new NotFoundException(referenceDataType, "reference Data");
			}

		}
		catch (Exception e) {

			logger.debug("Exception while deleting reference Data", e);

			if (e.getClass().equals(NotFoundException.class)) {

				throw new NotFoundException(referenceDataType, "reference Data");

			}
			else {

				throw new Exception(e);
			}
		}

		return response;
	}

	/*
	 * Get Reference Data By Id
	 */

	@RequestMapping(value = Urls.REFERENCE_DATA_GET, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ReferenceData get(@PathVariable("type") String referenceDataType, Model model, @PathVariable(value = "organizerId") String organizerId, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch an reference data");

		ReferenceData existingReferenceData = null;

		if (null != referenceDataType && !referenceDataType.equalsIgnoreCase("")) {

			existingReferenceData = reference_Data_Service.get(referenceDataType, organizerId);
		}

		if (existingReferenceData == null) {
			throw new NotFoundException(referenceDataType, "reference data");
		}

		return existingReferenceData;
	}

	@RequestMapping(value = Urls.REFERENCE_DATA_GET + Urls.SECOND_LEVEL_DOMAIN_ID, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Data getById(@PathVariable("id") String Id, @PathVariable("type") String referenceDataType, Model model, @PathVariable(value = "organizerId") String organizerId, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch an single reference data");

		ReferenceData existingReferenceData = null;
		Data existingData = null;

		if (null != referenceDataType && !referenceDataType.equalsIgnoreCase("")) {

			existingReferenceData = reference_Data_Service.get(referenceDataType, organizerId);
		}

		if (existingReferenceData != null) {
			if (existingReferenceData.getData().size() > 0) {

				for (Data data : existingReferenceData.getData()) {

					if (data.getId().equalsIgnoreCase(Id)) {

						existingData = data;

						break;
					}

				}

				if (existingData == null) {

					throw new NotFoundException(referenceDataType, "reference data");
				}

			}
			else {

				throw new NotFoundException(referenceDataType, "reference data");
			}
		}
		else {
			throw new NotFoundException(referenceDataType, "reference data");
		}

		return existingData;
	}
}
