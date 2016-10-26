package org.iqvis.nvolv3.domain;

import java.io.Serializable;

public class EventLanguage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String selected_language;

	private String eventId;

	public String getSelected_language() {
		return selected_language;
	}

	public void setSelected_language(String selected_language) {
		this.selected_language = selected_language;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	@Override
	public boolean equals(Object obj) {

		EventLanguage tempLang = (EventLanguage) obj;

		return (this.selected_language + "").equals(tempLang.getSelected_language()) && (this.eventId + "").equals(tempLang.getEventId());
	}

}
