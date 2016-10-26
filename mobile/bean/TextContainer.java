package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

public class TextContainer implements Serializable {
	/**
	 * 
	 */

	private static final long serialVersionUID = -4434257722166988991L;

	private List<ScreenText> screens;

	private Messages messages;

	public List<ScreenText> getScreens() {
		return screens;
	}

	public void setScreens(List<ScreenText> screens) {
		this.screens = screens;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Messages getMessages() {
		return messages;
	}

	public void setMessages(Messages messages) {
		this.messages = messages;
	}

}
