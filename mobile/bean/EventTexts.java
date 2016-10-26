package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

public class EventTexts implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String languageCode;

	private List<LabelEntities> texts;

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

}
