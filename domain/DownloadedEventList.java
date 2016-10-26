package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.List;

public class DownloadedEventList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<String> eventId;

	public List<String> getEventId() {
		return eventId;
	}

	public void setEventId(List<String> eventId) {
		this.eventId = eventId;
	}
	

}
