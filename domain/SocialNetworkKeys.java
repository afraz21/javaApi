package org.iqvis.nvolv3.domain;

import java.io.Serializable;

public class SocialNetworkKeys implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String apikey;
	
	private String apisecret;
	
	private String callback;
	
	private String socialMediaName;

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public String getApisecret() {
		return apisecret;
	}

	public void setApisecret(String apisecret) {
		this.apisecret = apisecret;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public String getSocialMediaName() {
		return socialMediaName;
	}

	public void setSocialMediaName(String socialMediaName) {
		this.socialMediaName = socialMediaName;
	}

}
