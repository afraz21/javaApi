package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.iqvis.nvolv3.audit.Audit;
import org.iqvis.nvolv3.bean.Data;
import org.iqvis.nvolv3.bean.EventPersonnel;
import org.iqvis.nvolv3.bean.EventSponsor;
import org.iqvis.nvolv3.bean.EventTrack;
import org.iqvis.nvolv3.bean.FeedbackConfiguration;
import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.mobile.bean.KeyValueString;
import org.iqvis.nvolv3.mobile.bean.Picture;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;

/**
 * A simple POJO representing a Event
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

@Document
public class Event extends Audit implements Serializable {

	private static final long serialVersionUID = -5527566248002296042L;

	private String name;

	private List<DateTime> eventDates;

	private List<String> eventHashTag;

	private List<String> socialMediaHashTags = new ArrayList<String>();

	private DateTime eventStartDateGMT;

	private DateTime eventEndDateGMT;

	private String organizerId;

	private String eventTypeId;

	private Boolean isMultiDate;

	private Boolean isDeleted;

	private Boolean isActive;

	private String partnerId;

	private List<MultiLingual> multiLingual;

	private List<EventSponsor> sponsors;

	private List<EventPersonnel> eventPersonnels;

	private List<EventTrack> tracks;

	private String venueId;

	private List<String> languages;

	private Boolean isLive;

	private Boolean isDownloadable;

	private List<EventLink> linkedApp;

	@TextScore
	Float score;

	@JsonIgnore
	private Venue venue;

	@DBRef(lazy = true)
	private Media picture;

	@DBRef(lazy = true)
	private Media banner;

	private String contact_Email;

	private String contact_Phone_Primary;

	private String contact_Phone_Secondary;

	private String contact_Fax_Primary;

	private String contact_Fax_Secondary;

	private String default_lang;

	private Boolean website_isExternal;

	private String event_Website;

	private String timeZone;

	private Boolean dayLightSaving;

	private String timeZoneLabel;

	private String appType;

	private Boolean hasExpo;

	private String expoLocationId;

	private Boolean showFeed;

	private Boolean isfeedModerated;

	private Boolean isOverlayCampaignActive;

	private List<String> eventApps;

	private List<SocialMedia> socialMediaLinks;

	private Data eventTypeDetail;

	private FeedbackConfiguration feedback_configuration;

	private Boolean live;

	private Boolean goLiveModeration;

	private DateTime twitterFeedPullStartDate;

	private DateTime twitterFeedPullEndDate;

	private Boolean isProfileMust;

	public Boolean isDayLightSaving() {
		return dayLightSaving;
	}

	public void setDayLightSaving(Boolean dayLightSaving) {
		this.dayLightSaving = dayLightSaving;
	}

	public DateTime getTwitterFeedPullStartDate() {
		return twitterFeedPullStartDate;
	}

	public void setTwitterFeedPullStartDate(DateTime twitterFeedPullStartDate) {
		this.twitterFeedPullStartDate = twitterFeedPullStartDate;
	}

	public DateTime getTwitterFeedPullEndDate() {
		return twitterFeedPullEndDate;
	}

	public void setTwitterFeedPullEndDate(DateTime twitterFeedPullEndDate) {
		this.twitterFeedPullEndDate = twitterFeedPullEndDate;
	}

	public List<String> getSocialMediaHashTags() {
		return socialMediaHashTags;
	}

	public void setSocialMediaHashTags(List<String> socialMediaHashTags) {
		this.socialMediaHashTags = socialMediaHashTags;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public Event() {
		// TODO Auto-generated constructor stub
	}

	public Event(String name, List<DateTime> eventDates, DateTime eventStartDateGMT, DateTime eventEndDateGMT, String organizerId, String eventTypeId, Boolean isMultiDate, Boolean isDeleted, Boolean isActive, String partnerId, List<MultiLingual> multiLingual, List<EventSponsor> sponsors, List<EventPersonnel> eventPersonnels, List<EventTrack> tracks, String venueId, List<String> languages, Boolean isLive, Boolean isDownloadable, List<EventLink> linkedApp, Venue venue, Media picture, Media banner, String contact_Email, String contact_Phone_Primary, String contact_Phone_Secondary, String contact_Fax_Primary, String contact_Fax_Secondary, String default_lang, Boolean website_isExternal, String event_Website, String timeZone, String timeZoneLabel, String appType, Boolean hasExpo,
			String expoLocationId, Boolean showFeed, Boolean isfeedModerated, Boolean isOverlayCampaignActive, List<String> eventApps, List<SocialMedia> socialMediaLinks, Data eventTypeDetail, FeedbackConfiguration feedback_configuration, Boolean live, Boolean goLiveModeration, List<KeyValueString> supported_languages, List<Activity> activities, List<String> testOrganizersEmails, Object eventConfiguration) {
		super();
		this.name = name;
		this.eventDates = eventDates;
		this.eventStartDateGMT = eventStartDateGMT;
		this.eventEndDateGMT = eventEndDateGMT;
		this.organizerId = organizerId;
		this.eventTypeId = eventTypeId;
		this.isMultiDate = isMultiDate;
		this.isDeleted = isDeleted;
		this.isActive = isActive;
		this.partnerId = partnerId;
		this.multiLingual = multiLingual;
		this.sponsors = sponsors;
		this.eventPersonnels = eventPersonnels;
		this.tracks = tracks;
		this.venueId = venueId;
		this.languages = languages;
		this.isLive = isLive;
		this.isDownloadable = isDownloadable;
		this.linkedApp = linkedApp;
		this.venue = venue;
		this.picture = picture;
		this.banner = banner;
		this.contact_Email = contact_Email;
		this.contact_Phone_Primary = contact_Phone_Primary;
		this.contact_Phone_Secondary = contact_Phone_Secondary;
		this.contact_Fax_Primary = contact_Fax_Primary;
		this.contact_Fax_Secondary = contact_Fax_Secondary;
		this.default_lang = default_lang;
		this.website_isExternal = website_isExternal;
		this.event_Website = event_Website;
		this.timeZone = timeZone;
		this.timeZoneLabel = timeZoneLabel;
		this.appType = appType;
		this.hasExpo = hasExpo;
		this.expoLocationId = expoLocationId;
		this.showFeed = showFeed;
		this.isfeedModerated = isfeedModerated;
		this.isOverlayCampaignActive = isOverlayCampaignActive;
		this.eventApps = eventApps;
		this.socialMediaLinks = socialMediaLinks;
		this.eventTypeDetail = eventTypeDetail;
		this.feedback_configuration = feedback_configuration;
		this.live = live;
		this.goLiveModeration = goLiveModeration;
		this.supported_languages = supported_languages;
		this.activities = activities;
		this.testOrganizersEmails = testOrganizersEmails;
		this.eventConfiguration = eventConfiguration;
	}

	// private Boolean appLevelChange;
	//
	// private Boolean eventLevelChange;

	// public Boolean isAppLevelChange() {
	// return appLevelChange;
	// }
	//
	// public void setAppLevelChange(Boolean appLevelChange) {
	// this.appLevelChange = appLevelChange;
	// }
	//
	// public Boolean isEventLevelChange() {
	// return eventLevelChange;
	// }
	//
	// public void setEventLevelChange(Boolean eventLevelChange) {
	// this.eventLevelChange = eventLevelChange;
	// }

	public Boolean getLive() {
		return live;
	}

	public void setLive(Boolean live) {
		this.live = live;
	}

	public Boolean getGoLiveModeration() {
		return goLiveModeration;
	}

	public void setGoLiveModeration(Boolean goLiveModeration) {
		this.goLiveModeration = goLiveModeration;
	}

	public List<EventLink> getLinkedApp() {
		return linkedApp;
	}

	public void linkApp(EventLink app) {
		if (app == null) {

			return;
		}

		if (this.linkedApp == null) {

			this.linkedApp = new ArrayList<EventLink>();
		}

		if (!this.linkedApp.contains(app)) {

			this.linkedApp.add(app);
		}
	}

	public void deLinkApp(EventLink app) {
		if (app == null || this.linkedApp == null)
			return;

		if (this.linkedApp.contains(app)) {

			this.linkedApp.remove(app);
		}
	}

	public void setLinkedApp(List<EventLink> linkedApp) {
		this.linkedApp = linkedApp;
	}

	@JsonIgnore
	public List<String> getLinkedAppIds() {
		List<String> ids = new ArrayList<String>();

		if (this.linkedApp != null) {

			for (EventLink App : this.linkedApp) {

				ids.add(App.getAppId());
			}
		}

		return ids;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public FeedbackConfiguration getFeedback_configuration() {
		return feedback_configuration;
	}

	public void setFeedback_configuration(FeedbackConfiguration feedback_configuration) {
		this.feedback_configuration = feedback_configuration;
	}

	@JsonProperty
	public Data getEventTypeDetail() {
		return eventTypeDetail;
	}

	@JsonIgnore
	public void setEventTypeDetail(Data eventTypeDetail) {
		this.eventTypeDetail = eventTypeDetail;
	}

	public List<SocialMedia> getSocialMediaLinks() {
		return socialMediaLinks;
	}

	public void setSocialMediaLinks(List<SocialMedia> socialMediaLinks) {
		this.socialMediaLinks = socialMediaLinks;
	}

	public String getTimeZoneLabel() {
		return timeZoneLabel;
	}

	public void setTimeZoneLabel(String timeZoneLabel) {
		this.timeZoneLabel = timeZoneLabel;
	}

	private List<KeyValueString> supported_languages;

	public List<KeyValueString> getSupported_languages() {
		return supported_languages;
	}

	public void setSupported_languages(List<KeyValueString> supported_languages) {
		this.supported_languages = supported_languages;
	}

	@JsonIgnore
	public String getDefault_lang() {
		return default_lang;
	}

	public void setDefault_lang(String default_lang) {
		this.default_lang = default_lang;
	}

	@org.codehaus.jackson.annotate.JsonIgnore
	private List<Activity> activities;

	private List<String> testOrganizersEmails;

	private Object eventConfiguration;

	@JsonIgnore
	public Object getEventConfiguration() {
		return eventConfiguration;
	}

	public List<String> getEventHashTag() {
		return eventHashTag;
	}

	public void setEventHashTag(List<String> eventHashTag) {
		this.eventHashTag = eventHashTag;
	}

	public void setEventConfiguration(Object eventConfig) {
		this.eventConfiguration = eventConfig;
	}

	@JsonProperty
	public Venue getVenue() {
		return venue;
	}

	@JsonIgnore
	public void setVenue(Venue venue) {
		this.venue = venue;
	}

	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;

	}

	public String getEventTypeId() {
		return eventTypeId;
	}

	public void setEventTypeId(String eventTypeId) {
		this.eventTypeId = eventTypeId;
	}

	public Boolean isMultiDate() {
		return isMultiDate;
	}

	public void setMultiDate(Boolean isMultiDate) {
		this.isMultiDate = isMultiDate;
	}

	public List<MultiLingual> getMultiLingual() {
		return multiLingual;
	}

	public void setMultiLingual(List<MultiLingual> multiLingual) {
		this.multiLingual = multiLingual;
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

	public Boolean getIsMultiDate() {
		return isMultiDate;
	}

	public void setIsMultiDate(Boolean isMultiDate) {
		this.isMultiDate = isMultiDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DateTime> getEventDates() {
		return eventDates;
	}

	public void setEventDates(List<DateTime> eventDates) {
		this.eventDates = eventDates;
	}

	public String getVenueId() {
		return venueId;
	}

	public void setVenueId(String venuId) {
		this.venueId = venuId;

	}

	public List<EventTrack> getTracks() {
		return tracks;
	}

	public void setTracks(List<EventTrack> tracks) {
		this.tracks = tracks;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public List<String> getLanguages() {
		return languages;
	}

	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}

	public String getContact_Email() {
		return contact_Email;
	}

	public void setContact_Email(String contact_Email) {
		this.contact_Email = contact_Email;
	}

	public String getContact_Phone_Primary() {
		return contact_Phone_Primary;
	}

	public void setContact_Phone_Primary(String contact_Phone_Primary) {
		this.contact_Phone_Primary = contact_Phone_Primary;
	}

	public String getContact_Phone_Secondary() {
		return contact_Phone_Secondary;
	}

	public void setContact_Phone_Secondary(String contact_Phone_Secondary) {
		this.contact_Phone_Secondary = contact_Phone_Secondary;
	}

	public String getContact_Fax_Primary() {
		return contact_Fax_Primary;
	}

	public void setContact_Fax_Primary(String contact_Fax_Primary) {
		this.contact_Fax_Primary = contact_Fax_Primary;
	}

	public String getContact_Fax_Secondary() {
		return contact_Fax_Secondary;
	}

	public void setContact_Fax_Secondary(String contact_Fax_Secondary) {
		this.contact_Fax_Secondary = contact_Fax_Secondary;
	}

	public String getEvent_Website() {
		return event_Website;
	}

	public void setEvent_Website(String event_Website) {
		this.event_Website = event_Website;
	}

	public Boolean isWebsite_isExternal() {
		return website_isExternal;
	}

	public void setWebsite_isExternal(Boolean website_isExternal) {
		this.website_isExternal = website_isExternal;
	}

	@JsonIgnore
	public Media getPictureO() {
		return picture;
	}

	public Object getPicture() {
		if (picture == null)
			return null;

		return new Picture(picture);
	}

	public void setPicture(Media picture) {
		this.picture = picture;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public Boolean getIsLive() {
		return isLive;
	}

	public void setIsLive(Boolean isLive) {
		this.isLive = isLive;
	}

	public Boolean getIsDownloadable() {
		return isDownloadable;
	}

	public void setIsDownloadable(Boolean isDownloadable) {
		this.isDownloadable = isDownloadable;
	}

	@JsonIgnore
	public Media getBannerO() {
		return banner;
	}

	public Object getBanner() {

		if (banner == null)
			return null;

		return new Picture(banner);
	}

	public void setBanner(Media banner) {
		this.banner = banner;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public Boolean getHasExpo() {
		return hasExpo;
	}

	public void setHasExpo(Boolean hasExpo) {
		this.hasExpo = hasExpo;
	}

	public String getExpoLocationId() {
		return expoLocationId;
	}

	public void setExpoLocationId(String expoLocationId) {
		this.expoLocationId = expoLocationId;
	}

	public List<String> getTestOrganizersEmails() {
		return testOrganizersEmails;
	}

	public void setTestOrganizersEmails(List<String> testOrganizersEmails) {
		this.testOrganizersEmails = testOrganizersEmails;
	}

	public Boolean getShowFeed() {
		return showFeed;
	}

	public void setShowFeed(Boolean showFeed) {
		this.showFeed = showFeed;
	}

	public Boolean getIsfeedModerated() {
		return isfeedModerated;
	}

	public void setIsfeedModerated(Boolean isfeedModerated) {
		this.isfeedModerated = isfeedModerated;
	}

	public Boolean getIsOverlayCampaignActive() {
		return isOverlayCampaignActive;
	}

	public void setIsOverlayCampaignActive(Boolean isOverlayCampaignActive) {
		this.isOverlayCampaignActive = isOverlayCampaignActive;
	}

	public List<EventSponsor> getSponsors() {
		return sponsors;
	}

	public void setSponsors(List<EventSponsor> sponsors) {
		this.sponsors = sponsors;
	}

	public List<String> getEventApps() {
		return eventApps;
	}

	public void setEventApps(List<String> eventApps) {
		this.eventApps = eventApps;
	}

	public List<EventPersonnel> getEventPersonnels() {
		return eventPersonnels;
	}

	public void setEventPersonnels(List<EventPersonnel> eventPersonnels) {
		this.eventPersonnels = eventPersonnels;
	}

	public DateTime getEventStartDateGMT() {
		return eventStartDateGMT;
	}

	public void setEventStartDateGMT(DateTime eventStartDateGMT) {
		this.eventStartDateGMT = eventStartDateGMT;
	}

	public DateTime getEventEndDateGMT() {
		return eventEndDateGMT;
	}

	public void setEventEndDateGMT(DateTime eventEndDateGMT) {
		this.eventEndDateGMT = eventEndDateGMT;
	}

	@JsonIgnore
	private DateTime getStartDate() {

		return this.eventDates.get(0);
	}

	@JsonIgnore
	public DateTime getEndDate() {

		return this.eventDates.get(this.eventDates.size() - 1);
	}

	public int compareTo(Event o) {

		return this.getStartDate().compareTo(o.getStartDate()) > 0 ? -1 : 1;
	}

	public Boolean getIsProfileMust() {
		return isProfileMust;
	}

	public void setIsProfileMust(Boolean isProfileMust) {
		this.isProfileMust = isProfileMust;
	}

}
