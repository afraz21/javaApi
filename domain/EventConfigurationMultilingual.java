package org.iqvis.nvolv3.domain;

import java.io.Serializable;

public class EventConfigurationMultilingual implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String languageCode;

	private String title;

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
