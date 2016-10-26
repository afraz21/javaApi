package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;

public class FacebookData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String url;

	private Boolean is_silhouette;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getIs_silhouette() {
		return is_silhouette;
	}

	public void setIs_silhouette(Boolean is_silhouette) {
		this.is_silhouette = is_silhouette;
	}

}
