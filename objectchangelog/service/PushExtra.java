package org.iqvis.nvolv3.objectchangelog.service;

import java.io.Serializable;

public class PushExtra implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String objectType;

	private String objectId;

	private String eventId;

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

}
