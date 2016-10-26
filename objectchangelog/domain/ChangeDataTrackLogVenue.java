package org.iqvis.nvolv3.objectchangelog.domain;

import org.codehaus.jackson.annotate.JsonIgnore;

public class ChangeDataTrackLogVenue extends ChangeTrackLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Object locations;

	@JsonIgnore
	public Object getLocations() {
		return locations;
	}

	public void setLocations(Object locations) {
		this.locations = locations;
	}

}
