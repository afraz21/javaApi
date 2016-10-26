package org.iqvis.nvolv3.mobile.app.config.controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.AppConfigKeys;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.dao.KeyDao;
import org.iqvis.nvolv3.domain.AppListItem;
import org.iqvis.nvolv3.domain.Track;
import org.iqvis.nvolv3.domain.User;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;
import org.iqvis.nvolv3.mobile.app.config.service.AppConfigService;
import org.iqvis.nvolv3.mobile.bean.CMSResponseAppConfiguration;
import org.iqvis.nvolv3.mobile.service.MobileEventServiceImpl;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.service.PersonnelService;
import org.iqvis.nvolv3.service.TrackService;
import org.iqvis.nvolv3.service.UserService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Messages;
import org.iqvis.nvolv3.utils.Urls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

@SuppressWarnings("restriction")
@Controller
@RequestMapping("")
public class AppConfigController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_APP_CONFIG)
	private AppConfigService appConfiguration_Service;

	@Resource(name = Constants.SERVICE_USER)
	private UserService userService;

	@Resource(name = Constants.SERVICE_PERSONNEL)
	private PersonnelService personnelService;

	@Resource(name = Constants.SERVICE_TRACK)
	private TrackService trackService;

	@Resource(name = Constants.SERVICE_EVENT)
	private EventService eventService;

	@Autowired
	private MobileEventServiceImpl mobileEventServiceImpl;

	/*
	 * Get All Organizer Apps
	 */

	@RequestMapping(value = Urls.APP_CONFIG_DATA_BASE_URL + Urls.APP_CONFIG_LIST, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<AppConfiguration> getAppConfigurationList(@RequestBody(required = false) @Valid Criteria search, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) {

		logger.debug("Received request to show all appConfiguration");

		Pageable pageAble = null;

		if (search != null) {
			if (search.getQuery() != null) {
				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());
					;
				}
			}
		}

		Page<AppConfiguration> referenceDataList = appConfiguration_Service.getAll(search, organizerId, request, pageAble);

		return referenceDataList;
	}

	@RequestMapping(value = Urls.APP_CONFIG_DATA_BASE_URL + "/listing", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<AppListItem> getAppOrganizerList(@RequestBody(required = false) @Valid Criteria search, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) {

		List<String> keys = new ArrayList<String>();

		keys.add("organizerId");

		List<Object> values = new ArrayList<Object>();

		values.add(organizerId);

		List<AppConfiguration> list = appConfiguration_Service.get(keys, values);

		User user = userService.get(organizerId);

		keys = new ArrayList<String>();

		keys.add("organizerId");

		values = new ArrayList<Object>();

		values.add(user.getPartnerId());

		List<AppConfiguration> listPartiner = appConfiguration_Service.get(keys, values);

		List<AppListItem> finalList = AppListItem.toList(list, true);

		finalList.addAll(AppListItem.toList(listPartiner, false));

		List<AppListItem> aps = new ArrayList<AppListItem>();

		for (AppListItem appListItem : finalList) {

			if (!aps.contains(appListItem)) {

				aps.add(appListItem);
			}

		}

		return new PageImpl<AppListItem>(aps);
	}

	@RequestMapping(value = Urls.APP_CONFIG_DATA_BASE_URL + "/partner", method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<AppListItem> gePartnerApp(@RequestBody(required = false) @Valid Criteria search, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) {

		List<String> keys = new ArrayList<String>();

		keys.add("partnerId");

		List<Object> values = new ArrayList<Object>();

		values.add(organizerId);

		Pageable pageAble = null;

		if (search != null) {
			if (search.getQuery() != null) {
				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());
					;
				}
			}
		}

		Page<AppListItem> page = appConfiguration_Service.getWithSearch(keys, values, search, pageAble, organizerId);

		return page;
	}

	@RequestMapping(value = Urls.APP_CONFIG_DATA_BASE_URL + "/all", method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<AppListItem> geAllApp(@RequestBody(required = false) @Valid Criteria search, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) {

		List<String> keys = new ArrayList<String>();

		List<Object> values = new ArrayList<Object>();

		Pageable pageAble = null;

		if (search != null) {

			if (search.getQuery() != null) {

				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());

				}
			}
		}

		Page<AppListItem> page = appConfiguration_Service.getWithSearch(keys, values, search, pageAble, null);

		return page;
	}

	/*
	 * Create App Config Object
	 */

	@Autowired
	private KeyDao keyDao;

	@RequestMapping(value = Urls.APP_CONFIG_DATA_BASE_URL + Urls.APP_CONFIG_LIST, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage add(@RequestBody @Valid AppConfiguration appConfiguration, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request, BindingResult result) throws Exception {

		logger.debug("Received request to add app configuration");

		ResponseMessage response = new ResponseMessage();

		if (result.hasErrors()) {

			response.setMessage(result.getFieldError().getDefaultMessage());

			response.setMessageCode(Constants.ERROR_CODE);

			// do something

		}
		else {

			try {

				AppConfiguration addedReferenceData = null;

				appConfiguration.setOrganizerId(organizerId);
				/**
				 * Setting Hard Coded Menu List To App Configuration Object
				 * */
				if (null != appConfiguration.getGeneral()) {

					appConfiguration.getGeneral().setMenu(mobileEventServiceImpl.getMenu());
				}
				AppConfigKeys container = keyDao.getKeyContainer();

				appConfiguration.setEtc(container.getEtc());

				addedReferenceData = appConfiguration_Service.add(appConfiguration);

				addedReferenceData = appConfiguration_Service.get(addedReferenceData.getId(), addedReferenceData.getOrganizerId());

				response.setMessage(String.format(Messages.ADD_SUCCESS_MESSAGE, "AppConfiguration Data"));

				response.setRecordId(addedReferenceData.getId().toString());

				response.setDetails_url(appConfiguration_Service.getAppConfigurationDetailUrl(addedReferenceData, request));

				response.setRecord(addedReferenceData);

				logger.debug("Reference Data has been added successfully");

			}
			catch (Exception e) {

				logger.debug("Exception while adding AppConfiguration Data", e);

				throw new Exception(e);
			}

			return response;
		}

		return response;
	}

	/*
	 * Update AppConfiguration Data
	 */

	@RequestMapping(value = Urls.APP_CONFIG_DATA_BASE_URL + Urls.APP_CONFIG_UPDATE, method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage edit(@RequestBody AppConfiguration appConfiguration, @PathVariable("id") String appConfigId, Model model, @PathVariable(value = "organizerId") String organizerId, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit an app configuration");

		ResponseMessage response = new ResponseMessage();

		try {

			appConfiguration.setOrganizerId(organizerId);

			AppConfiguration editedReferenceData = appConfiguration_Service.edit(appConfiguration, appConfigId);

			editedReferenceData = appConfiguration_Service.get(editedReferenceData.getId(), editedReferenceData.getOrganizerId());

			response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "AppConfiguration"));

			response.setRecordId(editedReferenceData.getId().toString());

			response.setRecord(editedReferenceData);

			response.setDetails_url(appConfiguration_Service.getAppConfigurationDetailUrl(editedReferenceData, request));

		}
		catch (Exception e) {

			logger.debug("Exception while updating AppConfiguration", e);

			if (e.getClass().equals(NotFoundException.class)) {

				throw new NotFoundException(appConfigId, "AppConfiguration");

			}
			else {

				throw new Exception(e);
			}
		}

		return response;
	}

	/*
	 * Delete app
	 */

	@RequestMapping(value = Urls.APP_CONFIG_DATA_BASE_URL + Urls.APP_CONFIG_UPDATE, method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage delete(@PathVariable("id") String appConfigurationId, Model model, @PathVariable(value = "organizerId") String organizerId, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit an reference Data");

		ResponseMessage response = new ResponseMessage();

		try {

			AppConfiguration appConfig = appConfiguration_Service.get(appConfigurationId, organizerId);

			if (appConfig != null) {

				appConfig.setIsDeleted(true);

				appConfiguration_Service.edit(appConfig, appConfigurationId);

				response.setMessage(String.format(Messages.DELETED_SUCCESS_MESSAGE, "AppConfiguration"));

				response.setRecordId(appConfigurationId);

				response.setDetails_url("");

			}
			else {

				throw new NotFoundException(appConfigurationId, "reference Data");
			}

		}
		catch (Exception e) {

			logger.debug("Exception while deleting reference Data", e);

			if (e.getClass().equals(NotFoundException.class)) {

				throw new NotFoundException(appConfigurationId, "reference Data");

			}
			else {

				throw new Exception(e);
			}
		}

		return response;
	}

	/*
	 * Get app By Id
	 */

	@RequestMapping(value = Urls.APP_CONFIG_DATA_BASE_URL + Urls.APP_CONFIG_GET, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public AppConfiguration get(@PathVariable("id") String AppConfigurationId, Model model, @PathVariable(value = "organizerId") String organizerId, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch an reference data");

		AppConfiguration existingAppConfig = null;

		if (null != AppConfigurationId && !AppConfigurationId.equalsIgnoreCase("")) {

			existingAppConfig = appConfiguration_Service.get(AppConfigurationId, organizerId);
		}

		if (existingAppConfig == null) {
			throw new NotFoundException(AppConfigurationId, "reference data");
		}

		User user = userService.get(organizerId);

		existingAppConfig.setLanguages(user.getLanguages());

		return existingAppConfig;
	}

	/*
	 * Get App Configuration For CMS
	 */

	@RequestMapping(value = Urls.APP_CONFIG_DATA_BASE_URL + Urls.APP_CONFIG_LIST + Urls.Event_List_Object, method = { RequestMethod.GET })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CMSResponseAppConfiguration getAppConfigurationListTestCMS(@PathVariable(value = "organizerId") String organizerId, @PathVariable(value = "appId") String appId, @PathVariable(value = "code") String code, @PathVariable(value = "version") String version, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to show all appConfiguration");

		CMSResponseAppConfiguration responseObj = new CMSResponseAppConfiguration();

		AppConfiguration appConf = appConfiguration_Service.get(appId, organizerId);

		User user = userService.get(organizerId);

		appConf.setLanguages(user.getLanguages());

		// appConf.filterByLangCode(code);

		List<Track> tracks = trackService.getOrganizerTracks(organizerId);

		if (tracks == null) {
			tracks = new ArrayList<Track>();
		}

		responseObj.setEvents_list(mobileEventServiceImpl.getAllAppEvents(appConf.getEvents(), tracks, code));

		responseObj.setApp_config(null);

		if (Integer.parseInt(version) < appConf.getVersion()) {
			responseObj.setApp_config(appConf);
		}

		return responseObj;

	}

	@RequestMapping(value = Urls.APP_CONFIG_DATA_BASE_URL + Urls.APP_CONFIG_UPDATE + "/etc", method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage addEditETC(@RequestBody Object appConfigurationETC, @PathVariable("id") String appConfigId, Model model, @PathVariable(value = "organizerId") String organizerId, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit an app configuration");

		ResponseMessage response = new ResponseMessage();

		try {

			Object editedReferenceData = appConfiguration_Service.addETC(appConfigId, organizerId, appConfigurationETC);

			response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "AppConfigurationETC"));

			response.setRecordId(appConfigId);

			response.setRecord(editedReferenceData);

		}
		catch (Exception e) {

			logger.debug("Exception while updating AppConfiguration", e);

			if (e.getClass().equals(NotFoundException.class)) {

				throw new NotFoundException(appConfigId, "AppConfiguration");

			}
			else {

				throw new Exception(e);
			}
		}

		return response;
	}

	@RequestMapping(value = "download/{pathid}/{fileid}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> redirectToExternalUrl(HttpServletResponse rp, @PathVariable(value = "pathid") String pathId, @PathVariable(value = "fileid") String fileId) throws URISyntaxException {
		// URI uri = new
		// URI("https://cdlgxr1vj6.cloudfs.io/v2/files/vqw1ot_VQIa0_Sgu5FatMA");
		// HttpHeaders httpHeaders = new HttpHeaders();
		// httpHeaders.set
		// ("Authorization","Bearer US2A.73526b148fd4484e9999d3bfa620ce4d.Jx4-VrIk7RWOyqvE1vYxOaqkydu5MbBSSHFNkNaDKeo");
		// httpHeaders.setLocation(uri);

		Client client = Client.create();

		// WebResource resource =
		// client.resource("https://cdlgxr1vj6.cloudfs.io/v2/files/vqw1ot_VQIa0_Sgu5FatMA");
		WebResource resource = client.resource("https://cdlgxr1vj6.cloudfs.io/v2/files/" + pathId + "/" + fileId);

		ClientResponse response = resource.header("Authorization", "Bearer US2A.73526b148fd4484e9999d3bfa620ce4d.Jx4-VrIk7RWOyqvE1vYxOaqkydu5MbBSSHFNkNaDKeo").get(ClientResponse.class);

		if (response.getStatus() == 200) {

			System.out.println("Ok");

		}
		else {

			System.out.println("Failure");
		}

		try {

			MultivaluedMap<String, String> repH = response.getHeaders();

			final HttpHeaders headers = new HttpHeaders();

			// headers.setContentType(MediaType.IMAGE_PNG);

			for (String key : repH.keySet()) {

				headers.add(key, repH.getFirst(key));

			}

			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(response.getEntity(File.class)), headers, HttpStatus.CREATED);

		}
		catch (ClientHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (UniformInterfaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

		// return new ResponseEntity<Object>(httpHeaders, HttpStatus.SEE_OTHER);
	}

	@RequestMapping(value = "/redirect", method = RequestMethod.GET)
	public void method(HttpServletResponse httpServletResponse) {

		httpServletResponse.setHeader("Location", "https://cdlgxr1vj6.cloudfs.io/v2/files/vqw1ot_VQIa0_Sgu5FatMA");

		httpServletResponse.setHeader("Authorization", "Bearer US2A.73526b148fd4484e9999d3bfa620ce4d.Jx4-VrIk7RWOyqvE1vYxOaqkydu5MbBSSHFNkNaDKeo");

	}

}
