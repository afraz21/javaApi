package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

public class ResponseClass implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 219699006655550154L;

	private AppConfResponse app_config;

	private List<ListEvent> events_list;

	/*
	 * private String message="";
	 * 
	 * public String getMessage() { return message; }
	 * 
	 * public void setMessage(String message) { this.message = message; }
	 */

	public AppConfResponse getApp_config() {
		return app_config;
	}

	public void setApp_config(AppConfResponse app_config) {
		this.app_config = app_config;
	}

	public List<ListEvent> getEvents_list() {
		return events_list;
	}

	public void setEvents_list(List<ListEvent> events_list) {
		this.events_list = events_list;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
