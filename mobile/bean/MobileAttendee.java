package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.iqvis.nvolv.sinch.SinchUser;
import org.iqvis.nvolv3.domain.Attendee;

public class MobileAttendee implements Serializable {

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

	private String imageUrl;

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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MobileAttendee(Attendee attendee) {

		this.id = attendee.getId();

		this.name = attendee.getName();

		this.title = attendee.getTitle();

		this.company = attendee.getCompany();

		this.picture = (Picture) attendee.getPicture();

		this.socialNetwork = attendee.getSocialNetwork();

		this.imageUrl = attendee.getImageUrl();

		this.primarySinchUser = attendee.getPrimarySinchUser();

		this.secondarySinchId = attendee.getSecondarySinchId();

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

	public static List<MobileAttendee> toMobileAttendee(List<Attendee> attendies) {

		List<MobileAttendee> mobileAttendeeList = new ArrayList<MobileAttendee>();

		if (attendies == null || attendies.size() == 0) {
			return mobileAttendeeList;
		}

		for (Attendee attendee : attendies) {

			mobileAttendeeList.add(new MobileAttendee(attendee));
		}

		return mobileAttendeeList;
	}

}
