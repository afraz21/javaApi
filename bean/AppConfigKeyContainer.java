package org.iqvis.nvolv3.bean;

import java.io.Serializable;
import java.util.List;

public class AppConfigKeyContainer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6797548073039535221L;

	List<Keys> labels;

	List<Keys> texts;

	List<Keys> messages;

	public List<Keys> getLabels() {
		return labels;
	}

	public void setLabels(List<Keys> labels) {
		this.labels = labels;
	}

	public List<Keys> getTexts() {
		return texts;
	}

	public void setTexts(List<Keys> texts) {
		this.texts = texts;
	}

	public List<Keys> getMessages() {
		return messages;
	}

	public void setMessages(List<Keys> messages) {
		this.messages = messages;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
