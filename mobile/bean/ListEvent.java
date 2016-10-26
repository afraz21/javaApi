package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.iqvis.nvolv3.bean.MultiLingual;
import org.joda.time.DateTime;

public class ListEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7441820111036605100L;

	private List<MultiLingual> multiLingual;

	private String id;
	private List<DateTime> eventDates = null;

	private String selected_language;

	private String organizerId;

	private String eventTimeZone;

	private boolean isLive = false;

	private boolean isDownloadable = false;

	private boolean isActive = true;

	private String descriptionLong;

	private List<LisTrack> tracks;

	private List<KeynotePersonnel> keynote_personnels;

	private Picture banner;

	private EventVenue venue;

	private boolean show_organizer_logo = true;

	private boolean show_contact_info = true;

	private int sortOrder;

	private Object eventConfiguration;

	private List<String> eventHashTag;

	private Boolean isProfileMust;

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getSelected_language() {
		return selected_language;
	}

	public void setSelected_language(String selected_language) {
		this.selected_language = selected_language;
	}

	public Object getEventConfiguration() {
		return eventConfiguration;
	}

	public void setEventConfiguration(Object eventConfiguration) {
		this.eventConfiguration = eventConfiguration;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<MultiLingual> getMultiLingual() {
		return multiLingual;
	}

	public void setMultiLingual(List<MultiLingual> multiLingual) {
		this.multiLingual = multiLingual;
	}

	public List<DateTime> getEventDates() {
		return eventDates;
	}

	public void setEventDates(List<DateTime> eventDates) {
		this.eventDates = eventDates;
	}

	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
	}

	public String getEventTimeZone() {
		return eventTimeZone;
	}

	public void setEventTimeZone(String eventTimeZone) {
		this.eventTimeZone = eventTimeZone;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public boolean isDownloadable() {
		return isDownloadable;
	}

	public void setDownloadable(boolean isDownloadable) {
		this.isDownloadable = isDownloadable;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getDescriptionLong() {
		return descriptionLong;
	}

	public void setDescriptionLong(String descriptionLong) {
		this.descriptionLong = descriptionLong;
	}

	public List<LisTrack> getTracks() {
		return tracks;
	}

	public void setTracks(List<LisTrack> tracks) {
		this.tracks = tracks;
	}

	public List<KeynotePersonnel> getKeynote_personnels() {

		return keynote_personnels;
	}

	public void setKeynote_personnels(List<KeynotePersonnel> keynote_personnels) {
		this.keynote_personnels = keynote_personnels;
	}

	public Picture getBanner() {
		return banner;
	}

	public void setBanner(Picture banner) {
		this.banner = banner;
	}

	public EventVenue getVenue() {
		return venue;
	}

	public void setVenue(EventVenue venue) {
		this.venue = venue;
	}

	public boolean isShow_organizer_logo() {
		return show_organizer_logo;
	}

	public void setShow_organizer_logo(boolean show_organizer_logo) {
		this.show_organizer_logo = show_organizer_logo;
	}

	public boolean isShow_contact_info() {
		return show_contact_info;
	}

	public void setShow_contact_info(boolean show_contact_info) {
		this.show_contact_info = show_contact_info;
	}

	@JsonIgnore
	private DateTime getStartDate() {

		return this.eventDates.get(0);
	}

	@JsonIgnore
	public DateTime getEndDate() {

		return this.eventDates.get(this.eventDates.size() - 1);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int compareTo(ListEvent o) {

		return this.getStartDate().compareTo(o.getStartDate()) > 0 ? -1 : 1;
	}

	public List<String> getEventHashTag() {
		return eventHashTag;
	}

	public void setEventHashTag(List<String> eventHashTag) {
		this.eventHashTag = eventHashTag;
	}

	public Boolean getIsProfileMust() {
		return isProfileMust;
	}

	public void setIsProfileMust(Boolean isProfileMust) {
		this.isProfileMust = isProfileMust;
	}

}
