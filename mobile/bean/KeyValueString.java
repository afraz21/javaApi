package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;

public class KeyValueString implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5374873437459853620L;
	private String key;

	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
