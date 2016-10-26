package org.iqvis.nvolv3.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.Data;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.ReferenceData;
import org.iqvis.nvolv3.domain.UpdateVendorList;
import org.iqvis.nvolv3.domain.Vendor;
import org.iqvis.nvolv3.domain.VendorSortOrder;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.ReferenceDataService;
import org.iqvis.nvolv3.service.SponsorService;
import org.iqvis.nvolv3.service.VendorService;
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
@RequestMapping(Urls.VENDOR_BASE_URL)
public class VendorController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_VENDOR)
	private VendorService vendorService;

	@Resource(name = Constants.SERVICE_SPONSOR)
	private SponsorService sponsorService;

	@Resource(name = Constants.SERVICE_REFERENCE_DATA)
	private ReferenceDataService reference_Data_Service;

	@RequestMapping(value = Urls.ADD_VENDOR, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage add(@RequestBody @Valid Vendor vendor, Model model, @PathVariable(value = "organizerId") String organizerId, HttpServletRequest request) throws Exception {

		logger.debug("Received request to add vendor");

		ResponseMessage response = new ResponseMessage();

		vendor.setOrganizerId(organizerId);

		try {

			Vendor addedVendor = vendorService.add(vendor);

			if (addedVendor != null && addedVendor.getSponsorId() != null && addedVendor.getSponsorId() != "") {

				addedVendor.setSponsor(sponsorService.get(addedVendor.getSponsorId()));

			}

			if (addedVendor != null && addedVendor.getVendorCategoryId() != null && addedVendor.getVendorCategoryId() != "") {

				addedVendor.setVendorCategory(this.getSponsorCategory(Constants.LOG_UTLITY_KEY_VENDOR_BUSINESS_CATEGORY, organizerId, addedVendor.getVendorCategoryId()));

			}

			// Response Message
			response.setMessage(String.format(Messages.ADD_SUCCESS_MESSAGE, "Vendor"));

			response.setRecordId(addedVendor.getId().toString());

			response.setDetails_url(vendorService.getVendorDetailUrl(addedVendor, request));

			response.setRecord(addedVendor);

			logger.debug(String.format(Messages.ADD_SUCCESS_MESSAGE, "Vendor"));

		}
		catch (Exception e) {

			logger.debug("Exception while adding vendor", e);

			throw new Exception(e);
		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_VENDOR, method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage edit(@RequestBody Vendor vendor, @PathVariable("id") String vendorId, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a vendor");

		ResponseMessage response = new ResponseMessage();

		vendor.setId(vendorId);

		vendor.setOrganizerId(organizerId);

		try {

			Vendor editedVendor = vendorService.edit(vendor);

			if (editedVendor != null && editedVendor.getSponsorId() != null && editedVendor.getSponsorId() != "") {

				editedVendor.setSponsor(sponsorService.get(editedVendor.getSponsorId()));

			}

			if (editedVendor != null && editedVendor.getVendorCategoryId() != null && editedVendor.getVendorCategoryId() != "") {

				editedVendor.setVendorCategory(this.getSponsorCategory(Constants.LOG_UTLITY_KEY_VENDOR_BUSINESS_CATEGORY, organizerId, editedVendor.getVendorCategoryId()));

			}

			response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "Vendor"));

			response.setRecordId(editedVendor.getId().toString());

			response.setRecord(editedVendor);

			response.setDetails_url(vendorService.getVendorDetailUrl(editedVendor, request));

		}
		catch (Exception e) {

			if (e.getClass().equals(NotFoundException.class)) {
				throw new NotFoundException(vendorId, "Vendor");
			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_VENDOR, method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage delete(@PathVariable("id") String vendorid, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a vendor");

		ResponseMessage response = new ResponseMessage();

		try {

			Vendor vendor = vendorService.get(vendorid, organizerId);

			if (vendor != null) {

				if (!vendor.getOrganizerId().equals(organizerId)) {

					throw new NotFoundException(vendorid, "vendor");

				}

				vendor.setIsDeleted(true);

				vendorService.edit(vendor);

				response.setMessage(String.format(Messages.DELETED_SUCCESS_MESSAGE, "Vendor"));

				response.setRecordId(vendorid);

				response.setDetails_url("");

			}
			else {

				throw new NotFoundException(vendorid, "Vendor");
			}

		}
		catch (Exception e) {

			if (e.getClass().equals(NotFoundException.class)) {
				throw new NotFoundException(vendorid, "Vendor");
			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.GET_VENDOR, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Vendor get(@PathVariable("id") String Id, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch a Vendor");

		Vendor existingVendor = null;

		if (null != Id && !Id.equalsIgnoreCase("")) {

			existingVendor = vendorService.get(Id, organizerId);

			if (existingVendor != null && existingVendor.getSponsorId() != null && existingVendor.getSponsorId() != "") {

				existingVendor.setSponsor(sponsorService.get(existingVendor.getSponsorId()));

			}

			if (existingVendor != null && existingVendor.getVendorCategoryId() != null && existingVendor.getVendorCategoryId() != "") {

				existingVendor.setVendorCategory(this.getSponsorCategory(Constants.LOG_UTLITY_KEY_VENDOR_BUSINESS_CATEGORY, organizerId, existingVendor.getVendorCategoryId()));

			}

			if (!existingVendor.getOrganizerId().equals(organizerId)) {

				throw new NotFoundException(Id, "Vendor");

			}

		}

		if (existingVendor == null) {

			throw new NotFoundException(Id, "Vendor");
		}

		return existingVendor;
	}

	@RequestMapping(value = Urls.GET_VENDORS, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<Vendor> getAll(@RequestBody(required = false) @Valid Criteria search, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) {

		logger.debug("Received request to show all vendor");

		Pageable pageAble = new PageRequest(0, 20);

		if (search != null) {
			if (search.getQuery() != null) {
				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());

				}
			}
		}

		Page<Vendor> vendors = vendorService.getAll(search, request, pageAble, organizerId);

		if (vendors != null && vendors.getContent() != null) {

			for (Vendor vendor : vendors.getContent()) {

				if (vendor != null && vendor.getSponsorId() != null && vendor.getSponsorId() != "") {

					vendor.setSponsor(sponsorService.get(vendor.getSponsorId()));

				}

				if (vendor != null && vendor.getVendorCategoryId() != null && vendor.getVendorCategoryId() != "") {

					vendor.setVendorCategory(this.getSponsorCategory(Constants.LOG_UTLITY_KEY_VENDOR_BUSINESS_CATEGORY, organizerId, vendor.getVendorCategoryId()));

				}
			}
		}
		return vendors;
	}

	public Vendor get(@PathVariable("id") String Id, @PathVariable(value = "organizerId") String organizerId) throws NotFoundException {

		logger.debug("Received request to fetch a Vendor");

		Vendor existingVendor = null;

		if (null != Id && !Id.equalsIgnoreCase("")) {

			existingVendor = vendorService.get(Id, organizerId);
			if (existingVendor != null) {
				if (!existingVendor.getOrganizerId().equals(organizerId)) {

					throw new NotFoundException(Id, "Vendor");

				}
			}

		}

		if (existingVendor == null) {

			throw new NotFoundException(Id, "Vendor");
		}

		return existingVendor;
	}

	private Data getSponsorCategory(String type, String organizerId, String sponsorCategoryId) {

		ReferenceData refData = reference_Data_Service.get(type, organizerId);

		if (refData != null && refData.getData() != null) {

			for (Data data : refData.getData()) {

				if (data.getId().equals(sponsorCategoryId)) {

					return data;

				}

			}

		}

		return null;
	}

	@RequestMapping(value = "/massupdate", method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage editMass(@RequestBody @Valid UpdateVendorList vendor, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a vendor");

		ResponseMessage response = new ResponseMessage();

		response.setDetails_url(request.getRequestURL().toString());

		List<Vendor> data = new ArrayList<Vendor>();

		if (vendor == null) {

			response.setHttpCode(Constants.ERROR_CODE);

			response.setMessageCode(Constants.ERROR_CODE);
		}
		else {

			for (VendorSortOrder sortOrder : vendor.getData()) {

				Vendor vend = vendorService.get(sortOrder.getId(), organizerId);

				vend.setSortOrder(sortOrder.getSortOrder());

				vend = vendorService.edit(vend);

				data.add(vend);
			}

			response.setRecord(data);
		}

		return response;
	}

}
