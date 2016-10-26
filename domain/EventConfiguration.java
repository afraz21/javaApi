package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.iqvis.nvolv3.mobile.bean.KeyValueString;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class EventConfiguration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int version;

	private List<KeyValueString> supported_languages;

	private KeyValueString default_lang;

	EventFeedConfiguration feed;

	private List<Tab> tabs;

	private Boolean activity_feedback_enabled;

	private String landing_screen;

	private CheckIn checkIn;

	

	public EventFeedConfiguration getFeed() {
		return feed;
	}

	public Boolean getActivity_feedback_enabled() {
		return activity_feedback_enabled;
	}

	public void setActivity_feedback_enabled(Boolean activity_feedback_enabled) {
		this.activity_feedback_enabled = activity_feedback_enabled;
	}

	public void setDefault_lang(KeyValueString default_lang) {
		this.default_lang = default_lang;
	}

	public void setFeed(EventFeedConfiguration feed) {
		this.feed = feed;
	}

	private boolean hasExpo;

	public boolean isHasExpo() {
		return hasExpo;
	}

	public void setHasExpo(boolean hasExpo) {
		this.hasExpo = hasExpo;
	}

	public EventConfiguration() {

	}

	public EventConfiguration(boolean f) {
		List<Tab> lis = new ArrayList<Tab>();
		Tab t = new Tab();
		t.setTab("ALERT");
		lis.add(t);

		t = new Tab();
		t.setTab("FEEDS");
		lis.add(t);

		t = new Tab();
		t.setTab("PROGRAM");
		lis.add(t);

		t = new Tab();
		t.setTab("EXPO");
		lis.add(t);

		t = new Tab();
		t.setTab("PERSONNEL");
		lis.add(t);

		t = new Tab();
		t.setTab("MAPS");
		lis.add(t);

		t = new Tab();
		t.setTab("RESOURCES");
		lis.add(t);

		t = new Tab();
		t.setTab("INFO");
		// t.setTemplate("LIST");
		lis.add(t);

		tabs = lis;

		this.landing_screen = "PROGRAM";

	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Tab> getTabs() {
		return tabs;
	}

	public void setTabs(List<Tab> tabs) {
		this.tabs = tabs;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@JsonProperty
	public List<KeyValueString> getSupported_languages() {
		return supported_languages;
	}

	@JsonIgnore
	public void setSupported_languages(List<KeyValueString> supported_languages) {
		this.supported_languages = supported_languages;
	}

	@JsonProperty
	public KeyValueString getDefault_lang() {
		return default_lang;
	}

	@JsonIgnore
	public void setDefault_lang(String default_lang) {
		default_lang = (default_lang == null ? "EN" : default_lang);
		KeyValueString t = new KeyValueString();
		t.setKey(default_lang);
		t.setText(new Locale(default_lang).getDisplayLanguage());
		this.default_lang = t;
	}

	public String getLanding_screen() {
		return landing_screen;
	}

	public void setLanding_screen(String landing_screen) {
		this.landing_screen = landing_screen;
	}

	public CheckIn getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(CheckIn checkIn) {
		this.checkIn = checkIn;
	}

	
}
