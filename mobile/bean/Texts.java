package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

public class Texts implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4923419660865005946L;

	private String languageCode;

	private List<LabelEntities> texts;

	private List<LabelEntities> messages;

	private List<LabelEntitiesMenu> menu;

	public List<LabelEntities> getMessages() {
		return messages;
	}

	public void setMessages(List<LabelEntities> messages) {
		this.messages = messages;
	}

	public List<LabelEntitiesMenu> getMenu() {
		return menu;
	}

	public void setMenu(List<LabelEntitiesMenu> menu) {
		this.menu = menu;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public List<LabelEntities> getTexts() {
		return texts;
	}

	public void setTexts(List<LabelEntities> texts) {
		this.texts = texts;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
