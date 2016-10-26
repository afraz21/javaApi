package org.iqvis.nvolv3.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "apiConfig")
public class EventConfigurationWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private Object eventConfiguration;

	private Boolean mobileAPI;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Object getEventConfiguration() {
		return eventConfiguration;
	}

	public void setEventConfiguration(EventConfiguration eventConfiguration) {
		this.eventConfiguration = eventConfiguration;
	}

	public Boolean getMobileAPI() {
		return mobileAPI;
	}

	public void setMobileAPI(Boolean mobileAPI) {
		this.mobileAPI = mobileAPI;
	}

}
