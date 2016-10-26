package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;

public class CMSResponseAppConfiguration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2711203046507105324L;
	private AppConfiguration app_config;

	private List<ListEvent> events_list;

	/*
	 * private String message="";
	 * 
	 * public String getMessage() { return message; }
	 * 
	 * public void setMessage(String message) { this.message = message; }
	 */

	public List<ListEvent> getEvents_list() {
		return events_list;
	}

	public AppConfiguration getApp_config() {
		return app_config;
	}

	public void setApp_config(AppConfiguration app_config) {
		this.app_config = app_config;
	}

	public void setEvents_list(List<ListEvent> events_list) {
		this.events_list = events_list;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
