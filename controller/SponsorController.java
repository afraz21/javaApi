package org.iqvis.nvolv3.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.Sponsor;
import org.iqvis.nvolv3.domain.User;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.SponsorService;
import org.iqvis.nvolv3.service.UserService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Messages;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
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
@RequestMapping(Urls.SPONSOR_BASE_URL)
public class SponsorController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_SPONSOR)
	private SponsorService sponserService;

	@Resource(name = Constants.SERVICE_USER)
	private UserService userService;

	@RequestMapping(value = Urls.ADD_SPONSOR, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage add(@RequestBody @Valid Sponsor sponser, Model model, @PathVariable(value = "organizerId") String organizerId, HttpServletRequest request) throws Exception {

		logger.debug("Received request to add sponser");

		ResponseMessage response = new ResponseMessage();

		sponser.setOrganizerId(organizerId);

		try {

			Sponsor addedSponser = sponserService.add(sponser);

			// Creating sponsor User
			
			if(sponser.getInvite()==true){
				
				User sponsorUser = new User();

				sponsorUser.setFirstName(addedSponser.getFirstName() + " " + addedSponser.getLastName());

				sponsorUser.setEmail(addedSponser.getEmail());

				sponsorUser.setPassword(Utils.getSHA512Hash(Constants.DEFAULT_PASSWORD));

				sponsorUser.setCreatedBy(addedSponser.getCreatedBy());

				sponsorUser.setUserType(Constants.USER_TYPE_SPONSOR);

				sponsorUser = userService.add(sponsorUser);

				addedSponser.setUser(sponsorUser);
				
			}
			
			
			sponserService.edit(addedSponser);

			addedSponser = sponserService.get(addedSponser.getId());

			// Response Message
			response.setMessage(String.format(Messages.ADD_SUCCESS_MESSAGE, "Sponsor"));

			response.setRecordId(addedSponser.getId().toString());

			response.setRecord(addedSponser);

			response.setDetails_url(sponserService.getSponserDetailUrl(addedSponser, request));

			logger.debug(String.format(Messages.ADD_SUCCESS_MESSAGE, "Sponsor"));

		}
		catch (Exception e) {

			logger.debug("Exception while adding sponser", e);

			throw new Exception(e);
		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_SPONSOR, method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage edit(@RequestBody Sponsor sponser, @PathVariable("id") String sponserid, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a sponsor");

		ResponseMessage response = new ResponseMessage();

		sponser.setId(sponserid);

		sponser.setOrganizerId(organizerId);

		try {

			Sponsor editedSponser = sponserService.edit(sponser);

			editedSponser = sponserService.get(editedSponser.getId());

			response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "Sponsor"));

			response.setRecordId(editedSponser.getId().toString());

			response.setRecord(editedSponser);

			response.setDetails_url(sponserService.getSponserDetailUrl(editedSponser, request));

		}
		catch (Exception e) {

			if (e.getClass().equals(NotFoundException.class)) {
				throw new NotFoundException(sponserid, "Sponsor");
			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_SPONSOR, method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage delete(@PathVariable("id") String sponserid, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a Sponsor");

		ResponseMessage response = new ResponseMessage();

		try {

			Sponsor sponser = sponserService.get(sponserid);

			if (sponser != null) {

				if (!sponser.getOrganizerId().equals(organizerId)) {

					throw new NotFoundException(sponserid, "Sponsor");

				}

				sponser.setIsDeleted(true);

				sponserService.edit(sponser);

				response.setMessage(String.format(Messages.DELETED_SUCCESS_MESSAGE, "Sponsor"));

				response.setRecordId(sponserid);

				response.setDetails_url("");

				// List<String> l = new ArrayList<String>();

			}
			else {

				throw new NotFoundException(sponserid, "Sponsor");
			}

		}
		catch (Exception e) {

			if (e.getClass().equals(NotFoundException.class)) {
				throw new NotFoundException(sponserid, "Sponsor");
			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.GET_SPONSOR, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Sponsor get(@PathVariable("id") String Id, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch a Sponsor");

		Sponsor existingSponser = null;

		if (null != Id && !Id.equalsIgnoreCase("")) {

			existingSponser = sponserService.get(Id);

			if (!existingSponser.getOrganizerId().equals(organizerId)) {

				throw new NotFoundException(Id, "Sponsor");

			}

		}

		if (existingSponser == null) {

			throw new NotFoundException(Id, "Sponsor");
		}

		return existingSponser;
	}

	/*
	 * @RequestMapping(value = Urls.GET_SPONSORS, method = RequestMethod.GET)
	 * 
	 * @ResponseBody
	 * 
	 * @ResponseStatus(value=HttpStatus.OK) public List<Sponsor> getAll(Model
	 * model,@PathVariable(value="organizerId") String
	 * organizerId,HttpServletRequest request) throws Exception {
	 * 
	 * logger.debug("Received request to fetch a Sponsor");
	 * 
	 * List<Sponsor> existingSponsers = null;
	 * 
	 * existingSponsers = sponserService.getAll(organizerId);
	 * 
	 * return existingSponsers; }
	 */

	@RequestMapping(value = Urls.GET_SPONSORS, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<Sponsor> getAll(@RequestBody(required = false) @Valid Criteria search, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) {

		logger.debug("Received request to show all sponsor");

		Pageable pageAble = new PageRequest(0, 20);

		if (search != null) {
			if (search.getQuery() != null) {
				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());
					;
				}
			}
		}

		Page<Sponsor> sponsors = sponserService.getAll(search, request, pageAble, organizerId);

		return sponsors;
	}

}
