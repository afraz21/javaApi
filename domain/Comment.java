package org.iqvis.nvolv3.domain;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.audit.Audit;
import org.iqvis.nvolv3.serializer.JodaDateTimeJsonSerializerFeeds;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A simple POJO representing a Personnel
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

@Document
public class Comment extends Audit implements Serializable {

	private static final long serialVersionUID = -2527566248002296042L;

	@NotEmpty(message = "text cannot be empty.")
	private String text;

	private Boolean isDeleted;

	private Boolean isActive;

	private String createdByname;

	private String createdByEmail;

	private String deviceToken;
	
	private String dp;
	
	private String attendeeId;

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

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getCreatedByname() {
		return createdByname;
	}

	public void setCreatedByname(String createdByname) {
		this.createdByname = createdByname;
	}

	public String getCreatedByEmail() {
		return createdByEmail;
	}

	public void setCreatedByEmail(String createdByEmail) {
		this.createdByEmail = createdByEmail;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	@JsonSerialize(using = JodaDateTimeJsonSerializerFeeds.class)
	public DateTime getCreatedDate() {
		// TODO Auto-generated method stub
		return super.getCreatedDate();
	}

	@Override
	@JsonSerialize(using = JodaDateTimeJsonSerializerFeeds.class)
	public DateTime getLastModifiedDate() {
		// TODO Auto-generated method stub
		return super.getLastModifiedDate();
	}

	public int compareTo(Comment o) {
		// TODO Auto-generated method stub
		return this.getCreatedDate().compareTo(o.getCreatedDate()) > 0 ? -1 : 1;
	}
}
