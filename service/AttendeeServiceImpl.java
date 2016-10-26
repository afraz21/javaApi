package org.iqvis.nvolv3.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

import org.iqvis.nvolv.sinch.Sinch;
import org.iqvis.nvolv.sinch.SinchUser;
import org.iqvis.nvolv3.dao.AttendeeDao;
import org.iqvis.nvolv3.domain.Attendee;
import org.iqvis.nvolv3.domain.Connector;
import org.iqvis.nvolv3.exceptionHandler.AttendeeProfileExistsAlready;
import org.iqvis.nvolv3.exceptionHandler.ConstantNotExistsException;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;
import org.iqvis.nvolv3.mobile.app.config.service.AppConfigService;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

@Service(Constants.SERVICE_ATTENDEE)
public class AttendeeServiceImpl implements AttendeeService {

	@Resource(name = Constants.SERVICE_SYSTEMT_CONSTANT)
	SystemConstantService systemtConstantService;

	@Resource(name = Constants.SERVICE_APP_CONFIG)
	private AppConfigService appConfiguration_Service;

	// @Autowired
	private String sinchSignUpUrl = ConstantFactoryImpl.SINCH_SIGNUP_API_URL;

	// private static String SINCH_SIGNUP_API_URL;

	@Autowired
	AttendeeDao attendeeDao;

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	public Attendee addAttendee(Attendee attendee) throws AttendeeProfileExistsAlready, ConstantNotExistsException {

		boolean verified = (attendee.getSocialNetwork() != null && !attendee.getSocialNetwork().equals("")) ? true : false;

		attendee.setVerified(verified);

		String organizerId = attendee.getOrganizerId() == null ? "nvolv" : attendee.getOrganizerId();

		String appId = attendee.getAppId() == null ? "nvolv" : attendee.getAppId();

		AppConfiguration appConfiguration = appConfiguration_Service.getAppObject(appId);

		String sinchAppId = appConfiguration.getSINCE_APP_ID();

		List<Attendee> attendeeList = this.get("email", attendee.getEmail());

		boolean exists = attendeeList != null && attendeeList.size() > 0 ? true : false;

		if (!exists) {

			if (attendee.getPrimarySinchUser() == null) {

				attendee.setPrimarySinchUser(this.createSinchUser(sinchAppId));
			}

			Attendee addedAttendee = this.add(attendee);

			return addedAttendee;
		}

		if (verified) {

			for (Attendee attendee2 : attendeeList) {
				if (attendee2.isVerified()) {
					// TODO current attendee profile is verified and there is
					// already an verified profile with same email [MERGE]

					attendee = Attendee.merge(attendee, attendee2);

					attendeeDao.delete(attendee2.getId());
				}
				else if (attendee.getDeviceToken().size() > 0 && attendee2.getDeviceToken().contains(attendee.getDeviceToken().get(0))) {
					// TODO current attendee profile is verified and there is
					// already an unverified profile with same email [MERGE]

					attendee = Attendee.merge(attendee, attendee2);

					attendeeDao.delete(attendee2.getId());
				}
			}
			// TODO create new attendee profile if no device token repeat or
			// create new profile in any case [intentionally]
			if (attendee.getPrimarySinchUser() == null) {

				attendee.setPrimarySinchUser(this.createSinchUser(sinchAppId));
			}

			Attendee addedAttendee = this.add(attendee);

			return addedAttendee;

		}
		else {

			for (Attendee attendee2 : attendeeList) {
				if (attendee.getDeviceToken().size() > 0 && attendee2.getDeviceToken().contains(attendee.getDeviceToken().get(0))) {
					// TODO current attendee profile is verified and there is
					// already an unverified profile with same email [MERGE]
					attendee = Attendee.merge(attendee, attendee2);
					attendeeDao.delete(attendee2.getId());
				}
			}

			// TODO create new attendee profile if no device token repeat or
			// create new profile in any case [intentionally]
			if (attendee.getPrimarySinchUser() == null) {
				attendee.setPrimarySinchUser(this.createSinchUser(sinchAppId));
			}
			Attendee addedAttendee = this.add(attendee);

			return addedAttendee;

		}
	}

	public SinchUser createSinchUser(String sinchAppId) throws ConstantNotExistsException {

		// if (SINCH_SIGNUP_API_URL == null) {
		//
		// SINCH_SIGNUP_API_URL =
		// systemtConstantService.get(ISystemConstant.SINCH_SIGNUP_API_URL).toString();
		// }

		SinchUser sinchUser = new SinchUser();

		sinchUser.setApp_key(sinchAppId);

		Client client = Client.create();

		WebResource resource = client.resource(sinchSignUpUrl);

		Sinch sinch = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(Sinch.class, sinchUser);

		if (sinch.getResponse().getHttpCode() == 200) {

			sinchUser.setSinchId(sinch.getResponse().getContents().getSinchId());
		}

		return sinchUser;
	}

	public Attendee add(Attendee attendee) throws AttendeeProfileExistsAlready {

		return attendeeDao.add(attendee);
	}

	public List<Attendee> get(List<String> ids) {

		return attendeeDao.get(ids);
	}

	public Page<Attendee> get(Pageable pageAble, List<String> eventIds) {

		return attendeeDao.get(pageAble, eventIds);
	}

	public Page<Attendee> get(Pageable pageAble, Criteria search) {

		return attendeeDao.get(pageAble, Utils.parseCriteria(search, ""));
	}

	public Attendee edit(Attendee attendee) throws NotFoundException {

		Attendee editAttendee = attendeeDao.get(attendee.getId());

		editAttendee.setEvents(attendee.getEvents());

		return attendeeDao.edit(editAttendee);
	}

	public Attendee edit(Attendee attendee, String id) throws NotFoundException {

		Attendee existingAttendee = this.get(id);

		if (attendee.getProfileUrl() != null && !"".equals(attendee.getProfileUrl())) {
			existingAttendee.setProfileUrl(attendee.getProfileUrl());
		}

		if (attendee.getAppId() != null && !attendee.getAppId().equals("")) {
			existingAttendee.setAppId(attendee.getAppId());
		}

		if (attendee.getBio() != null && !attendee.getBio().equals("")) {
			existingAttendee.setBio(attendee.getBio());
		}

		if (attendee.getCompany() != null && !attendee.getCompany().equals("")) {
			existingAttendee.setCompany(attendee.getCompany());
		}

		if (attendee.getConnectors() != null && attendee.getConnectors().size() != 0) {

			if (existingAttendee.getConnectors() != null && existingAttendee.getConnectors().size() > 0) {

				for (Connector connector : attendee.getConnectors()) {

					existingAttendee.setConnector(connector);

				}

			}
			else {

				existingAttendee.setConnectors(attendee.getConnectors());
			}
		}

		if (attendee.getPrimarySinchUser() != null) {

			existingAttendee.setPrimarySinchUser(attendee.getPrimarySinchUser());
		}

		if (attendee.getCreatedBy() != null && attendee.getCreatedBy() != "") {
			existingAttendee.setCreatedBy(attendee.getCreatedBy());
		}

		if (attendee.getCreatedDate() != null) {
			existingAttendee.setCreatedDate(attendee.getCreatedDate());
		}

		if (attendee.getDob() != null) {
			existingAttendee.setDob(attendee.getDob());
		}

		if (attendee.getImageUrl() != null && !attendee.getImageUrl().equals("") && !attendee.getImageUrl().equalsIgnoreCase("null")) {
			existingAttendee.setImageUrl(attendee.getImageUrl());
		}

		if (attendee.getLastModifiedBy() != null && attendee.getLastModifiedBy() != "") {
			existingAttendee.setLastModifiedBy(attendee.getLastModifiedBy());
		}

		if (attendee.getName() != null && !attendee.getName().equals("")) {
			existingAttendee.setName(attendee.getName());
		}

		if (attendee.getPicture() != null) {
			existingAttendee.setPicture(attendee.getPictureO());
		}

		// System.out.println("+--------"+attendee.getProfileUrl()+"-------+");
		//
		// if (attendee.getProfileUrl() != null &&
		// !StringUtils.isEmpty(attendee.getProfileUrl() )) {
		// existingAttendee.setProfileUrl(attendee.getProfileUrl());
		// }

		if (attendee.getSocialNetwork() != null && !attendee.getSocialNetwork().equals("") && !attendee.getSocialNetwork().equalsIgnoreCase("null")) {
			existingAttendee.setSocialNetwork(attendee.getSocialNetwork());
		}

		if (attendee.getTitle() != null && !attendee.getTitle().equals("")) {
			existingAttendee.setTitle(attendee.getTitle());
		}

		if (attendee.getUsername() != null && !attendee.getUsername().equals("")) {
			existingAttendee.setUsername(attendee.getUsername());
		}

		Attendee editedAttendee = attendeeDao.edit(existingAttendee);

		List<String> l = new ArrayList<String>();

		l.add(editedAttendee.getId());

		l.remove("");

		if (l.size() > 0) {

			dataChangeLogService.add(l, "EVENT", Constants.LOG_ATTENDEE_PROFILE, attendee.getId(), Constants.LOG_ACTION_UPDATE, Attendee.class.toString());
		}

		return editedAttendee;
	}

	public Attendee get(String id) {

		return attendeeDao.get(id);
	}

	public List<Attendee> get(String selector, String value) {

		return attendeeDao.get(selector, value);
	}

	public List<Attendee> get(Map<String, Object> map) {

		return attendeeDao.get(map);
	}

}
