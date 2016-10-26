package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.iqvis.nvolv.sinch.SinchUser;
import org.iqvis.nvolv3.domain.Attendee;
import org.iqvis.nvolv3.domain.Connector;

public class AttendeeDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String name;

	private String title;

	private String company;

	private Picture picture;

	private String socialNetwork;

	private String email;

	private List<Connector> connectors;

	private String bio;

	private String imageUrl;

	private String profileUrl;

	private String accessToken;

	private String secretToken;

	private SinchUser primarySinchUser;
	
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
		this.secondarySinchId = secondarySinchId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public AttendeeDetail(LinkedinInformation info) {

		this.name = info.getFirstName() + " " + info.getLastName();

		this.title = info.getHeadline();

		this.email = info.getEmailAddress();

		this.socialNetwork = "Linkedin";

		this.bio = info.getSummary();

		this.imageUrl = info.getPictureUrl();

		this.profileUrl = info.getPublicProfileUrl();

		this.email = info.getEmailAddress();

		this.id = info.getId();
	}

	public AttendeeDetail(FacebookInformation info) {

		this.name = info.getName();

		this.title = info.getName();

		this.email = info.getEmail();

		this.socialNetwork = "Facebook";

		this.bio = "";

		if (info.getPicture() != null && info.getPicture().getData() != null) {

			this.imageUrl = info.getPicture().getData().getUrl();
		}
		this.profileUrl = info.getLink();

		this.id = info.getId();
	}

	public AttendeeDetail(TwitterInformation info) {

		this.name = info.getName();

		this.title = info.getName();

		this.email = "";

		this.socialNetwork = "Twitter";

		this.bio = info.getDescription();

		if (info.getProfile_image_url() != null) {

			this.imageUrl = info.getProfile_image_url();
		}
		this.profileUrl = "https://twitter.com/" + info.getScreen_name();

		this.id = info.getId();
	}

	public AttendeeDetail(Attendee attendee) {

		this.name = attendee.getName();

		this.profileUrl = attendee.getProfileUrl();

		this.connectors = attendee.getConnectors();

		this.name = attendee.getName();

		this.title = attendee.getTitle();

		this.company = attendee.getCompany();

		this.picture = (Picture) attendee.getPicture();

		this.socialNetwork = attendee.getSocialNetwork();

		this.bio = attendee.getBio();

		this.connectors = attendee.getConnectors();

		this.imageUrl = attendee.getImageUrl();

		this.email = attendee.getEmail();

		this.id = attendee.getId();
		
		this.primarySinchUser=attendee.getPrimarySinchUser();
		
		this.secondarySinchId=attendee.getSecondarySinchId();
		
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Picture getPicture() {
		return picture;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
	}

	public String getSocialNetwork() {
		return socialNetwork;
	}

	public void setSocialNetwork(String socialNetwork) {
		this.socialNetwork = socialNetwork;
	}

	public List<Connector> getConnectors() {
		return connectors;
	}

	public void setConnectors(List<Connector> connectors) {
		this.connectors = connectors;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getSecretToken() {
		return secretToken;
	}

	public void setSecretToken(String secretToken) {
		this.secretToken = secretToken;
	}

}
