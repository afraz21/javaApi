package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.iqvis.nvolv3.serializer.JodaDateTimeJsonSerializerFeeds;
import org.joda.time.DateTime;

public class MobileEventFeedComment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String text;

	private String createdByname;
	
	private String dp;
	
	private String attendeeId;

	@JsonSerialize(using = JodaDateTimeJsonSerializerFeeds.class)
	private DateTime createdDate;

	private String createdByEmail;

	public String getDp() {
		return dp;
	}

	public void setDp(String dp) {
		this.dp = dp;
	}

	public String getAttendeeId() {
		return attendeeId;
	}

	public void setAttendeeId(String attendeeId) {
		this.attendeeId = attendeeId;
	}

	public String getCreatedByEmail() {
		return createdByEmail;
	}

	public void setCreatedByEmail(String createdByEmail) {
		this.createdByEmail = createdByEmail;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCreatedByname() {
		return createdByname;
	}

	public void setCreatedByname(String createdByname) {
		this.createdByname = createdByname;
	}

	public DateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.getId().equals(((MobileEventFeedComment) obj).getId());
	}

}
