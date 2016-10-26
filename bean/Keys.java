package org.iqvis.nvolv3.bean;

import java.io.Serializable;

public class Keys implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5419303290283531709L;
	private String label;
	private String defaultText;
	private Boolean isActive;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDefaultText() {
		return defaultText;
	}

	public void setDefaultText(String defaultText) {
		this.defaultText = defaultText;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
