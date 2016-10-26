package org.iqvis.nvolv3.mobile.app.config.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.audit.Audit;
import org.iqvis.nvolv3.mobile.bean.KeyValue;
import org.iqvis.nvolv3.mobile.bean.KeyValueString;
import org.iqvis.nvolv3.mobile.bean.Menu;
import org.iqvis.nvolv3.mobile.bean.OrganizerInfo;
import org.iqvis.nvolv3.mobile.bean.ScreenText;
import org.iqvis.nvolv3.mobile.bean.TextContainer;
import org.iqvis.nvolv3.utils.Utils;

public class AppConfigurationOld extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2044227992186494537L;
	@NotEmpty(message = "OrganizerId cannot be empty.")
	private String organizerId;

	@NotEmpty(message = "Name cannot be empty.")
	private String name;

	private Boolean isDeleted = false;

	private List<String> events = new ArrayList<String>();

	private Boolean isActive = false;

	private List<KeyValueString> supported_languages;

	private TextContainer texts;

	private List<Menu> menus;

	private boolean show_news = false;

	private boolean show_profile_screen = false;

	private boolean profile_screen_skippable = false;

	private boolean show_organizer_logo_on_events_listing = false;

	private OrganizerInfo organizer_info;

	public void filterByLangCode(String code) throws Exception {

		if (texts != null) {

			List<ScreenText> listTemp = new ArrayList<ScreenText>();

			for (ScreenText st : texts.getScreens()) {

				List<KeyValue> t = new ArrayList<KeyValue>();

				for (KeyValue kv : st.getLabels()) {

					kv.setText(Utils.getMultiLingualByLangCode(kv.getText(), code));

					t.add(kv);
				}

				st.setLabels(t);

				listTemp.add(st);
			}
			texts.setScreens(listTemp);

		}
	}

	public List<String> getEvents() {
		return events;
	}

	public void setEvents(List<String> event) {
		this.events = event;
	}

	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public List<KeyValueString> getSupported_languages() {
		return supported_languages;
	}

	public void setSupported_languages(List<KeyValueString> supported_languages) {
		this.supported_languages = supported_languages;
	}

	public TextContainer getTexts() {
		return texts;
	}

	public void setTexts(TextContainer texts) {
		this.texts = texts;
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}

	public boolean isShow_news() {
		return show_news;
	}

	public void setShow_news(boolean show_news) {
		this.show_news = show_news;
	}

	public boolean isShow_profile_screen() {
		return show_profile_screen;
	}

	public void setShow_profile_screen(boolean show_profile_screen) {
		this.show_profile_screen = show_profile_screen;
	}

	public boolean isProfile_screen_skippable() {
		return profile_screen_skippable;
	}

	public void setProfile_screen_skippable(boolean profile_screen_skippable) {
		this.profile_screen_skippable = profile_screen_skippable;
	}

	public boolean isShow_organizer_logo_on_events_listing() {
		return show_organizer_logo_on_events_listing;
	}

	public void setShow_organizer_logo_on_events_listing(boolean show_organizer_logo_on_events_listing) {
		this.show_organizer_logo_on_events_listing = show_organizer_logo_on_events_listing;
	}

	public OrganizerInfo getOrganizer_info() {
		return organizer_info;
	}

	public void setOrganizer_info(OrganizerInfo organizer_info) {
		this.organizer_info = organizer_info;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
