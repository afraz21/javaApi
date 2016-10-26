package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hamcrest.Matchers;
import org.iqvis.nvolv.sinch.SinchUser;
import org.iqvis.nvolv3.audit.Audit;
import org.iqvis.nvolv3.mobile.bean.Picture;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import ch.lambdaj.Lambda;

@Document(collection = "attendee")
public class Attendee extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	private String title;

	private String company;

	private String socialNetwork;

	private Date dob;

	private String username;

	private boolean isDeleted;

	private List<String> events;

	private String organizerId;

	private String appId;

	private String bio;

	private String email;

	private String imageUrl;

	private String profileUrl;

	private List<String> deviceToken = new ArrayList<String>();

	private SinchUser primarySinchUser;

	private List<String> appList = new ArrayList<String>();

	public List<String> getAppList() {
		return appList;
	}

	public void setAppList(List<String> appList) {
		appList = appList == null ? new ArrayList<String>() : appList;

		for (String apid : appList) {

			this.appList.remove(apid);
		}

		this.appList.addAll(appList);
	}

	@JsonIgnore
	public void setAppList(String appId) {

		appId = appId == null ? "" : appId;

		appList.remove(appId);

		if (appId != "") {
			appList.add(appId);
		}
	}

	private List<SinchUser> secondarySinchId = new ArrayList<SinchUser>();

	public SinchUser getPrimarySinchUser() {
		return primarySinchUser;
	}

	public void setPrimarySinchUser(SinchUser primarySinchUser) {
		this.primarySinchUser = primarySinchUser;
	}

	public List<SinchUser> getSecondarySinchId() {
		return secondarySinchId;
	}

	public void setSecondarySinchId(List<SinchUser> secondarySinchId) {

		secondarySinchId = secondarySinchId == null ? new ArrayList<SinchUser>() : secondarySinchId;

		for (SinchUser sinchId : secondarySinchId) {

			this.secondarySinchId.remove(sinchId);
		}

		this.secondarySinchId.addAll(secondarySinchId);
	}

	private boolean isVerified;

	@JsonIgnore
	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public List<String> getDeviceToken() {
		return deviceToken;
	}

	@JsonIgnore
	public void setDeviceToken(String deviceToken) {

		if (this.deviceToken == null) {

			this.deviceToken = new ArrayList<String>();
		}

		if (deviceToken != null && !deviceToken.equals("") && !this.deviceToken.contains(deviceToken) ) {

			this.deviceToken.add(deviceToken);
		}
	}

	public void setDeviceToken(List<String> deviceToken) {
		deviceToken = deviceToken == null ? new ArrayList<String>() : deviceToken;
		for (String dt : deviceToken) {

			this.deviceToken.remove(dt);
		}
		this.deviceToken.addAll(deviceToken);
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	private List<Connector> connectors;

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public List<Connector> getConnectors() {
		return connectors;
	}

	public void setConnectors(List<Connector> connectors) {
		this.connectors = connectors;
	}

	public void setConnector(Connector connector) {

		if (connector != null) {

			if (this.connectors == null) {

				this.connectors = new ArrayList<Connector>();
			}

			this.connectors.remove(connector);

			this.connectors.add(connector);
		}

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getSocialNetwork() {
		return socialNetwork;
	}

	public void setSocialNetwork(String socialNetwork) {
		this.socialNetwork = socialNetwork;
	}

	public List<String> getEvents() {
		return events;
	}

	public void setEvents(List<String> events) {
		this.events = events;
	}

	public void setEventId(String eventId) {

		if (events == null)
			events = new ArrayList<String>();

		if (!events.contains(eventId)) {
			events.add(eventId);
		}

	}

	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@DBRef(lazy = true)
	private Media picture;

	@JsonIgnore
	public Media getPictureO() {
		return picture;
	}

	public Object getPicture() {
		if (picture == null)
			return null;

		return new Picture(picture);
	}

	public void setPicture(Media picture) {
		this.picture = picture;
	}

	@JsonIgnore
	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Connector getConnecterByName(String name) {

		if (this.connectors != null && this.connectors.size() > 0) {

			List<Connector> conList = Lambda.select(this.connectors, Lambda.having(Lambda.on(Connector.class).getName(), Matchers.equalToIgnoringCase(name + "")));

			if (conList != null && conList.size() > 0) {

				return conList.get(0);
			}

		}
		return null;
	}

	@JsonIgnore
	public Map<String, Object> getMap() {

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("email", this.getEmail());

		map.put("deviceToken", this.getDeviceToken());

		return map;
	}

	public static Attendee merge(Attendee primery, Attendee secondary) {

		if (primery.getTitle()==null || primery.getTitle().equals("")) {

			primery.setTitle(secondary.getTitle());
		}

		primery.setAppList(secondary.getAppId());

		primery.setAppList(secondary.getAppList());

		primery.setBio(primery.getBio() == null || primery.getBio() == "" ? secondary.getBio() : primery.getBio());

		primery.setCompany(primery.getCompany() == null && primery.getCompany() == "" ? secondary.getCompany() : primery.getCompany());

		if (secondary.getConnectors() != null) {

			for (Connector connector : secondary.getConnectors()) {

				primery.setConnector(connector);
			}
		}

		primery.setDeviceToken(secondary.getDeviceToken());

		if (secondary.getEvents() != null) {

			for (String eventId : secondary.getEvents()) {

				primery.setEventId(eventId);
			}
		}

		primery.setVerified(secondary.isVerified() ? secondary.isVerified() : primery.isVerified());

		primery.setImageUrl(primery.getImageUrl() == null || primery.getImageUrl() == "" ? secondary.getImageUrl() : primery.getImageUrl());

		if (primery.getPictureO() == null) {

			primery.setPicture(secondary.getPictureO());
		}

		primery.setSecondarySinchId(secondary.getSecondarySinchId());

		if (primery.getPrimarySinchUser() == null) {

			primery.setPrimarySinchUser(secondary.getPrimarySinchUser());
		}

		return primery;
	}
}
