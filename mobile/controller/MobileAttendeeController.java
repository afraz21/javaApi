package org.iqvis.nvolv3.mobile.controller;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.dao.CommentDao;
import org.iqvis.nvolv3.dao.FeedDao;
import org.iqvis.nvolv3.domain.Attendee;
import org.iqvis.nvolv3.domain.Connector;
import org.iqvis.nvolv3.domain.Media;
import org.iqvis.nvolv3.exceptionHandler.ConstantNotExistsException;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;
import org.iqvis.nvolv3.mobile.app.config.service.AppConfigService;
import org.iqvis.nvolv3.mobile.bean.AttendeeDetail;
import org.iqvis.nvolv3.mobile.bean.MobileAttendee;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.AttendeeService;
import org.iqvis.nvolv3.service.MediaService;
import org.iqvis.nvolv3.thread.UpdateCommentsThread;
import org.iqvis.nvolv3.thread.UpdateFeedsThread;
import org.iqvis.nvolv3.upload.factory.MediaFactory;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

@SuppressWarnings("restriction")
@Controller
@RequestMapping(Urls.MOBILE_APP_ATTENDEE_BASE)
public class MobileAttendeeController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_ATTENDEE)
	private AttendeeService attendeeService;

	@Resource(name = Constants.SERVICE_MEDIA)
	private MediaService mediaService;

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	@Resource(name = Constants.SERVICE_APP_CONFIG)
	private AppConfigService appConfiguration_Service;

	@Autowired
	private FeedDao feedDao;

	@Autowired
	private CommentDao commentDao;

	@Autowired
	MediaFactory mediaFactory;

	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/attendee", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage addMobileAttendee(@RequestParam(required = true, value = "mediaFile") final MultipartFile file, @PathVariable("appid") String appId, @PathVariable("organizerId") String organizerId, @RequestParam(required = false, value = "title") String title, @RequestParam(required = true, value = "name") String name, @RequestParam(required = false, value = "image_url") String image_url, @RequestParam(required = false, value = "social_connected") String social_connected, @RequestParam(required = false, value = "profile_url") String profile_url, @RequestParam(required = true, value = "email") String email, @RequestParam(required = false, value = "deviceToken") String deviceToken, @RequestParam(required = false, value = "bio") String bio,
			@RequestParam(required = false, value = "organization") String company, @RequestParam(required = false, value = "accessToken") String accessToken, @RequestParam(required = false, value = "secretToken") String secretToken, final HttpServletRequest request) throws Exception {
		ResponseMessage response = new ResponseMessage();

		final File toFile = file != null ? Utils.multipartToFile(file, "") : null;

		String url = null;

		Attendee attendee = new Attendee();

		if (toFile != null && file != null && file.getSize() > 0) {

			url = mediaFactory.upload(toFile, "attendee-profile-image");

			if (toFile.exists()) {
				toFile.delete();
			}

			Media media = new Media();

			// media.setId(UUID.randomUUID().toString());

			media.setType("PROFILE_PICTURE");

			media.setUrl(url);

			media.setUrlSmall(url);

			media.setUrlMedium(url);

			media.setUrlLarge(url);

			media.setUrlThumb(url);

			media.setMediaContainer(mediaFactory.getMyClass());

			media = mediaService.add(media);

			attendee.setPicture(media);
		}

		attendee.setName(name == null ? null : URLDecoder.decode(name + "", "UTF-8"));

		attendee.setDeviceToken(deviceToken == null ? null : URLDecoder.decode(deviceToken + "", "UTF-8"));

		attendee.setEmail(URLDecoder.decode(email + "", "UTF-8"));

		attendee.setImageUrl(image_url == null ? null : URLDecoder.decode(image_url + "", "UTF-8"));

		attendee.setSocialNetwork(social_connected == null ? null : URLDecoder.decode(social_connected + "", "UTF-8"));

		attendee.setProfileUrl(profile_url == null ? null : URLDecoder.decode(profile_url + "", "UTF-8"));

		attendee.setBio(bio == null ? null : URLDecoder.decode(bio + "", "UTF-8"));

		attendee.setTitle(title == null ? null : URLDecoder.decode(title + "", "UTF-8"));

		attendee.setCompany(company == null ? null : URLDecoder.decode(company + "", "UTF-8"));

		attendee.setAppId(appId);

		attendee.setOrganizerId(organizerId);

		if (social_connected != null && !social_connected.equals("")) {

			attendee.setConnector(new Connector(social_connected, attendee.getProfileUrl(), accessToken, secretToken));

		}
		else {

			attendee.setConnector(null);
		}
		response.setMessageCode(Constants.SUCCESS_CODE);

		response.setMessage("Attendee Has Been Added Successfuly !");

		Attendee addedAttendee = attendeeService.addAttendee(attendee);

		response.setRecordId(addedAttendee.getId());

		response.setRecord(addedAttendee);

		return response;
	}

	// @SuppressWarnings("deprecation")
	// @RequestMapping(value = "/attendee", method = RequestMethod.POST)
	// @ResponseBody
	// @ResponseStatus(value = HttpStatus.CREATED)
	// public ResponseMessage addMobileAttendee(@RequestParam(required = true,
	// value = "mediaFile") final MultipartFile file, @PathVariable("appid")
	// String appId, @PathVariable("organizerId") String organizerId,
	// @RequestParam(required = false, value = "title") String title,
	// @RequestParam(required = true, value = "name") String name,
	// @RequestParam(required = false, value = "image_url") String image_url,
	// @RequestParam(required = false, value = "social_connected") String
	// social_connected, @RequestParam(required = false, value = "profile_url")
	// String profile_url, @RequestParam(required = true, value = "email")
	// String email, @RequestParam(required = false, value = "deviceToken")
	// String deviceToken, @RequestParam(required = false, value = "bio") String
	// bio,@RequestParam(required = false, value = "organization") String
	// company ,@RequestParam(required = false, value = "accessToken") String
	// accessToken,@RequestParam(required = false, value = "secretToken") String
	// secretToken,final HttpServletRequest request)
	// throws Exception {
	// ResponseMessage response = new ResponseMessage();
	//
	// boolean exists = false;
	//
	// final File toFile = file != null ? Utils.multipartToFile(file, "") :
	// null;
	//
	// String url = null;
	//
	// Attendee attendee = new Attendee();
	//
	// if (toFile != null && file != null && file.getSize() > 0) {
	//
	// url = mediaFactory.upload(toFile, "attendee-profile-image");
	//
	// if (toFile.exists()) {
	// toFile.delete();
	// }
	//
	// Media media = new Media();
	//
	// // media.setId(UUID.randomUUID().toString());
	//
	// media.setType("PROFILE_PICTURE");
	//
	// media.setUrl(url);
	//
	// media.setUrlSmall(url);
	//
	// media.setUrlMedium(url);
	//
	// media.setUrlLarge(url);
	//
	// media.setUrlThumb(url);
	//
	// media = mediaService.add(media);
	//
	// attendee.setPicture(media);
	// }
	//
	// attendee.setName(URLDecoder.decode(name + "", "UTF-8"));
	//
	// attendee.setDeviceToken(URLDecoder.decode(deviceToken + "", "UTF-8"));
	//
	// attendee.setEmail(URLDecoder.decode(email + "", "UTF-8"));
	//
	// attendee.setImageUrl(URLDecoder.decode(image_url + "", "UTF-8"));
	//
	// attendee.setSocialNetwork(social_connected==null?null:URLDecoder.decode(social_connected
	// + "", "UTF-8"));
	//
	// attendee.setProfileUrl(URLDecoder.decode(profile_url + "", "UTF-8"));
	//
	// attendee.setBio(URLDecoder.decode(bio + "", "UTF-8"));
	//
	// attendee.setTitle(URLDecoder.decode(title + "", "UTF-8"));
	//
	// attendee.setCompany(company == null ? null : URLDecoder.decode(company +
	// "", "UTF-8"));
	//
	// attendee.setAppId(appId);
	//
	// attendee.setOrganizerId(organizerId);
	//
	// if (social_connected != null && !social_connected.equals("")) {
	//
	// attendee.setConnector(new Connector(social_connected,
	// attendee.getProfileUrl(),accessToken,secretToken));
	//
	// }
	// else {
	//
	// attendee.setConnector(null);
	// }
	// response.setMessageCode(Constants.SUCCESS_CODE);
	//
	// response.setMessage("Attendee Has Been Added Successfuly !");
	//
	// Attendee addedAttendee = null;
	// try {
	// addedAttendee = attendeeService.add(attendee);
	// response.setRecordId(addedAttendee.getId());
	// }
	// catch (AttendeeProfileExistsAlready pe) {
	//
	// response.setMessageCode(Constants.ERROR_CODE);
	//
	// response.setMessage("Attendee Profile Exists Already !");
	//
	// addedAttendee = null;
	//
	// exists = true;
	//
	// // pe.printStackTrace();
	// }
	// catch (Exception e) {
	//
	// response.setMessageCode(Constants.ERROR_CODE);
	//
	// response.setMessage(e.getMessage());
	//
	// e.printStackTrace();
	// }
	//
	// if (exists) {
	//
	// List<Attendee> existingAttendeeList =
	// attendeeService.get(attendee.getMap());
	//
	// if (existingAttendeeList != null && existingAttendeeList.size() > 0) {
	//
	// Attendee existingAttendee = existingAttendeeList.get(0);
	//
	// addedAttendee = attendeeService.edit(attendee, existingAttendee.getId());
	//
	// response.setMessageCode(Constants.SUCCESS_CODE);
	//
	// response.setMessage("Attendee Profile Updated Successfuly !");
	//
	// }
	//
	// }
	//
	// response.setRecord(addedAttendee);
	//
	// return response;
	// }

	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/attendee/{id}", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage editMobileAttendee(@RequestParam(required = true, value = "mediaFile") final MultipartFile file, @PathVariable("appid") String appId, @PathVariable("organizerId") String organizerId, @RequestParam(required = false, value = "title") String title, @RequestParam(required = true, value = "name") String name, @RequestParam(required = false, value = "image_url") String image_url, @RequestParam(required = false, value = "social_connected") String social_connected, @RequestParam(required = false, value = "profile_url") String profile_url, @RequestParam(required = false, value = "deviceToken") String deviceToken, @PathVariable("id") String attendeeId, @RequestParam(required = false, value = "bio") String bio,
			@RequestParam(required = false, value = "accessToken") String accessToken, @RequestParam(required = false, value = "secretToken") String secretToken, @RequestParam(required = false, value = "organization") String company, final HttpServletRequest request) throws Exception {

		final String mediaUrl = request.getSession().getServletContext().getInitParameter("S3-bucket-prefix"), accessKey = request.getSession().getServletContext().getInitParameter("S3-accessKey"), secretKey = request.getSession().getServletContext().getInitParameter("S3-secretKey"), existingBucketName = request.getSession().getServletContext().getInitParameter("S3-bucket");

		ResponseMessage response = new ResponseMessage();

		final File toFile = file != null ? Utils.multipartToFile(file, "") : null;

		String url = null;

		Attendee attendee = new Attendee();

		boolean updateDp = false;

		if (toFile != null && file != null && file.getSize() > 0) {

			url = mediaFactory.upload(toFile, "attendee-profile-image");

			if (toFile.exists()) {
				toFile.delete();
			}

			Media media = new Media();

			// media.setId(UUID.randomUUID().toString());

			media.setType("PROFILE_PICTURE");

			media.setUrl(url);

			media.setUrlSmall(url);

			media.setUrlMedium(url);

			media.setUrlLarge(url);

			media.setUrlThumb(url);

			media = mediaService.add(media);

			attendee.setPicture(media);

			updateDp = true;

		}

		attendee.setName(name == null ? null : URLDecoder.decode(name + "", "UTF-8"));

		attendee.setImageUrl(image_url == null ? null : URLDecoder.decode(image_url + "", "UTF-8"));

		attendee.setSocialNetwork(social_connected == null ? null : URLDecoder.decode(social_connected + "", "UTF-8"));

		attendee.setProfileUrl(profile_url == null ? null : URLDecoder.decode(profile_url + "", "UTF-8"));

		attendee.setBio(bio == null ? null : URLDecoder.decode(bio + "", "UTF-8"));

		attendee.setCompany(company == null ? null : URLDecoder.decode(company + "", "UTF-8"));

		attendee.setDeviceToken(deviceToken == null ? null : URLDecoder.decode(deviceToken + "", "UTF-8"));

		attendee.setTitle(title == null ? null : URLDecoder.decode(title + "", "UTF-8"));

		attendee.setAppId(appId);

		attendee.setOrganizerId(organizerId);

		if (social_connected != null && !social_connected.equals("")) {

			attendee.setConnector(new Connector(social_connected, attendee.getProfileUrl(), accessToken, secretToken));

		}
		else {

			attendee.setConnector(null);
		}

		response.setMessageCode(Constants.SUCCESS_CODE);

		response.setMessage("Attendee Has Been Edited Successfuly !");

		Attendee editedAttendee = null;

		editedAttendee = attendeeService.edit(attendee, attendeeId);

		response.setRecordId(editedAttendee.getId());

		response.setRecord(editedAttendee);

		if (image_url != null) {

			url = image_url;

			updateDp = true;
		}

		try {

			if (updateDp) {

				new Thread(new UpdateFeedsThread(feedDao, dataChangeLogService, attendeeId, url)).start();

				new Thread(new UpdateCommentsThread(commentDao, feedDao, dataChangeLogService, attendeeId, url)).start();
			}

		}
		catch (Exception e) {

			e.printStackTrace();
		}

		return response;
	}

	// @RequestMapping(value = "/attendee", method = { RequestMethod.POST })
	// @ResponseStatus(HttpStatus.CREATED)
	// public @ResponseBody Attendee add(@RequestBody @Valid Attendee attendee,
	// @PathVariable("appid") String appId, @PathVariable("organizerId") String
	// organizerId, Model model, HttpServletRequest request) throws Exception {
	//
	// attendee.setAppId(appId);
	//
	// attendee.setOrganizerId(organizerId);
	//
	// return attendeeService.add(attendee);
	// }

	@RequestMapping(value = "/attendee", method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<MobileAttendee> get(@RequestBody(required = false) @Valid Criteria search, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to show all personnels");

		Pageable pageAble = new PageRequest(0, 5000);

		if (search != null) {
			if (search.getQuery() != null) {
				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());
				}
			}
		}

		Page<Attendee> attendee = attendeeService.get(pageAble, search);

		Page<MobileAttendee> attendeePage = new PageImpl<MobileAttendee>(MobileAttendee.toMobileAttendee(attendee.getContent()), pageAble, attendee.getTotalElements());

		return attendeePage;
	}

	@RequestMapping(value = "/attendee/{attendeeId}", method = { RequestMethod.GET })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseMessage getAttendee(@PathVariable(value = "attendeeId") String attendeeId, HttpServletRequest request, @PathVariable("appid") String appId, @PathVariable("organizerId") String organizerId) throws NotFoundException {

		logger.debug("Received request to show get attendee by id");

		Attendee attendee = attendeeService.get(attendeeId);

		ResponseMessage response = new ResponseMessage();

		AppConfiguration appConfiguration = appConfiguration_Service.get(appId, organizerId);

		String sinchAppId = appConfiguration.getSINCE_APP_ID();

		if (attendee == null) {

			response.setMessageCode(Constants.ERROR_CODE);

			response.setHttpCode(Constants.ERROR_CODE);

			response.setMessage("Attendee with id :" + attendeeId + " does not exist");

		}
		else {

			if (attendee.getPrimarySinchUser() == null) {

				try {
					attendee.setPrimarySinchUser(attendeeService.createSinchUser(sinchAppId));
					attendeeService.edit(attendee, attendeeId);
				}
				catch (ConstantNotExistsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			response.setRecordId(attendee.getId());

			response.setRecord(new MobileAttendee(attendee));

			response.setMessageCode(Constants.SUCCESS_CODE);

			response.setHttpCode(Constants.SUCCESS_CODE);
		}

		return response;
	}

	@RequestMapping(value = "/events/{eventId}/attendee/{id}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseMessage attachEvent(@PathVariable("appid") String appId, @PathVariable("eventId") String eventId, @PathVariable("organizerId") String organizerId, @PathVariable("id") String id, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to show all personnels");

		ResponseMessage response = new ResponseMessage();

		Attendee attendee = attendeeService.get(id);

		attendee.setEventId(eventId);

		attendee = attendeeService.edit(attendee);

		List<String> l = new ArrayList<String>();

		l.add(eventId);

		l.remove("");

		if (l.size() > 0) {

			dataChangeLogService.add(l, "EVENT", Constants.LOG_ATTENDEE_PROFILE, attendee.getId(), Constants.LOG_ACTION_ADD, Attendee.class.toString());
		}

		response.setRecordId(id);

		response.setRecord(attendee);

		response.setMessageCode(Constants.SUCCESS_CODE);

		return response;
	}

	@RequestMapping(value = "/events/{eventId}/attendee/{id}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseMessage getAttendeeById(@PathVariable("appid") String appId, @PathVariable("eventId") String eventId, @PathVariable("organizerId") String organizerId, @PathVariable("id") String id, Model model, HttpServletRequest request) {

		ResponseMessage response = new ResponseMessage();

		response.setRecordId(id);

		Attendee attendee = attendeeService.get(id);

		AttendeeDetail detail = null;

		if (attendee != null) {

			detail = new AttendeeDetail(attendee);
		}

		response.setRecord(detail);

		response.setHttpCode(Constants.SUCCESS_CODE);

		return response;
	}
}
