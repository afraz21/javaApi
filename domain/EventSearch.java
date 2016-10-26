package org.iqvis.nvolv3.domain;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.iqvis.nvolv3.serializer.DateTimeJsonDeSerializer;
import org.iqvis.nvolv3.utils.Constants;
import org.joda.time.DateTime;

public class EventSearch implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String text;

	private String language = Constants.APPLICATION_DEFAULT_LANGUAGE;

	private String partnerId;

	private String location;

	private String appId;

	private Integer pageNumber;

	private Integer pageSize = 20;

	public Integer getPageNumber() {

		if (pageNumber == null)
			return null;

		return pageNumber < 1 ? 1 : pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@JsonDeserialize(using = DateTimeJsonDeSerializer.class)
	private DateTime fromDateTime;

	@JsonDeserialize(using = DateTimeJsonDeSerializer.class)
	private DateTime toDateTime;

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public DateTime getFromDateTime() {
		return fromDateTime;
	}

	public void setFromDateTime(DateTime fromDateTime) {
		this.fromDateTime = fromDateTime;
	}

	public DateTime getToDateTime() {
		return toDateTime;
	}

	public void setToDateTime(DateTime toDateTime) {
		this.toDateTime = toDateTime;
	}

}
