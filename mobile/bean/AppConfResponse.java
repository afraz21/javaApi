package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class AppConfResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5922913507479773798L;

	private String organizerId;

	private Integer version;

	private String id;

	private Texts texts;

	private String current_lang;

	private List<KeyValueString> supported_languages;

	@JsonIgnore
	private Object ETC;

	@JsonProperty
	public Object getETC() {
		return ETC;
	}

	public void setETC(Object eTC) {
		ETC = eTC;
	}

	public String getCurrent_lang() {
		return current_lang;
	}

	public void setCurrent_lang(String current_lang) {
		this.current_lang = current_lang;
	}

	public List<KeyValueString> getSupported_languages() {
		return supported_languages;
	}

	public void setSupported_languages(List<KeyValueString> supported_languages) {
		this.supported_languages = supported_languages;
	}

	Boolean show_news = false;

	Boolean show_profile_screen = true;

	Boolean profile_screen_skippable = true;

	Boolean show_organizer_logo_on_events_listing = true;

	private Object organizer_info = null;

	public Boolean getShow_news() {
		return show_news;
	}

	public void setShow_news(Boolean show_news) {
		this.show_news = show_news;
	}

	public Boolean getShow_profile_screen() {
		return show_profile_screen;
	}

	public void setShow_profile_screen(Boolean show_profile_screen) {
		this.show_profile_screen = show_profile_screen;
	}

	public Boolean getProfile_screen_skippable() {
		return profile_screen_skippable;
	}

	public void setProfile_screen_skippable(Boolean profile_screen_skippable) {
		this.profile_screen_skippable = profile_screen_skippable;
	}

	public Boolean getShow_organizer_logo_on_events_listing() {
		return show_organizer_logo_on_events_listing;
	}

	public void setShow_organizer_logo_on_events_listing(Boolean show_organizer_logo_on_events_listing) {
		this.show_organizer_logo_on_events_listing = show_organizer_logo_on_events_listing;
	}

	public Object getOrganizer_info() {
		return organizer_info;
	}

	public void setOrganizer_info(Object organizer_info) {
		this.organizer_info = organizer_info;
	}

	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
	}

	public Texts getTexts() {
		return texts;
	}

	public void setTexts(Texts texts) {
		this.texts = texts;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
