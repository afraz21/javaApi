package org.iqvis.nvolv3.push;

import java.io.Serializable;

public class PushData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String alert;

	private Object extra;

	private String sound = "default";

	private String badge;

	public String getBadge() {
		return badge;
	}

	public void setBadge(String badge) {
		this.badge = badge;
	}

	public String getSound() {
		return sound;
	}

	public void setSound(String sound) {
		this.sound = sound;
	}

	public Object getExtra() {
		return extra;
	}

	public void setExtra(Object extra) {
		this.extra = extra;
	}

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

}
