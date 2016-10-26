package org.iqvis.nvolv3.bean;

import java.io.Serializable;

public class AppConfigKeys implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6613435884579345803L;

	private AppConfigKeyContainer general;

	private AppConfigKeyContainer signup;

	private Object etc;

	public Object getEtc() {
		return etc;
	}

	public void setEtc(Object etc) {
		this.etc = etc;
	}

	public AppConfigKeyContainer getGeneral() {
		return general;
	}

	public void setGeneral(AppConfigKeyContainer general) {
		this.general = general;
	}

	public AppConfigKeyContainer getSignup() {
		return signup;
	}

	public void setSignup(AppConfigKeyContainer signup) {
		this.signup = signup;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
