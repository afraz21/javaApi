package org.iqvis.nvolv3.controller;

import java.io.Serializable;
import java.util.List;

import org.iqvis.nvolv3.mobile.bean.DataCMS;

public class ConfigurationResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Object eventConfigurationTexts;
	
	private List<DataCMS> applicationConfigurationTexts;

	public Object getEventConfigurationTexts() {
		return eventConfigurationTexts;
	}

	public void setEventConfigurationTexts(Object eventConfigurationTexts) {
		this.eventConfigurationTexts = eventConfigurationTexts;
	}

	public Object getApplicationConfigurationTexts() {
		return applicationConfigurationTexts;
	}

	public void setApplicationConfigurationTexts(List<DataCMS> applicationConfigurationTexts) {
		this.applicationConfigurationTexts = applicationConfigurationTexts;
	}

}
