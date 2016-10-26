package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;

public class LabelEntitiesMenu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6938769910667639589L;

	private String key;

	private String text;

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
