package org.iqvis.nvolv3.mobile.app.config.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.audit.Audit;
import org.iqvis.nvolv3.domain.Media;
import org.iqvis.nvolv3.domain.PushNotificationConfiguration;
import org.iqvis.nvolv3.domain.SocialNetworkKeys;
import org.iqvis.nvolv3.mobile.bean.KeyValueString;
import org.iqvis.nvolv3.mobile.bean.Picture;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class AppConfiguration extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "OrganizerId cannot be empty.")
	private String organizerId;

	private List<String> events;

	@NotEmpty(message = "Name cannot be empty.")
	private String name;

	private General general;

	private SignUp signup;

	private Sponsorship sponsorship;

	private Boolean isDeleted = false;

	private Boolean isActive = false;

	private Boolean show_news = false;

	private Boolean show_profile_screen = true;

	private Boolean profile_screen_skippable = true;

	private Boolean show_organizer_logo_on_events_listing = true;

	private Object organizer_info = null;

	private List<KeyValueString> supported_languages;

	private List<String> testOrganizersEmails;

	private String current_lang;

	private Object etc;

	private String X_PARSE_APPLICATION_ID;

	private String X_PARSE_REST_API_KEY;

	private String partnerId;

	@DBRef(lazy = true)
	private Media appLogo;

	@DBRef(lazy = true)
	private Media appBigLogo;

	@DBRef(lazy = true)
	private Media splashImage;

	@DBRef(lazy = true)
	private Media aboutBanner;

	private String appDescription;

	private String appType;

	private String branchIOSDKey;

	private String iosAppUrl;

	private String androidAppUrl;
	
	private Boolean linkEventModeration=false;
	
	private Boolean showProfileScreenOnStart;
	
	private Boolean userProfileIsModrated;
	
	private List<SocialNetworkKeys> socialNetowrkKeys;
	
	private String SINCE_APP_ID;
	
	private String SINCH_APP_SECRET;
	
	private PushNotificationConfiguration pushNotificationConfiguration = new PushNotificationConfiguration();

	public String getSINCE_APP_ID() {
		return SINCE_APP_ID;
	}

	public void setSINCE_APP_ID(String sINCE_APP_ID) {
		SINCE_APP_ID = sINCE_APP_ID;
	}

	public String getSINCH_APP_SECRET() {
		return SINCH_APP_SECRET;
	}

	public void setSINCH_APP_SECRET(String SINCH_APP_SECRET) {
		
		this.SINCH_APP_SECRET = SINCH_APP_SECRET;
	}

	@JsonIgnore
	public List<SocialNetworkKeys> getSocialNetowrkKeys() {
		return socialNetowrkKeys;
	}
	
	@JsonIgnore
	public SocialNetworkKeys getSocialMediaKeysMyName(String name){

		for (SocialNetworkKeys socialNetworkKeys : socialNetowrkKeys) {
			
			if(socialNetworkKeys.getSocialMediaName().equals(name)){
				
				return socialNetworkKeys; 
			}
		}
		
		return null;
	}

	public void setSocialNetowrkKeys(List<SocialNetworkKeys> socialNetowrkKeys) {
		
		this.socialNetowrkKeys = socialNetowrkKeys;
	}

	public PushNotificationConfiguration getPushNotificationConfiguration() {
		return pushNotificationConfiguration;
	}

	public void setPushNotificationConfiguration(PushNotificationConfiguration pushNotificationConfiguration) {
		this.pushNotificationConfiguration = pushNotificationConfiguration;
	}

	public Boolean getShowProfileScreenOnStart() {
		return showProfileScreenOnStart;
	}

	public void setShowProfileScreenOnStart(Boolean showProfileScreenOnStart) {
		this.showProfileScreenOnStart = showProfileScreenOnStart;
	}

	public Boolean getUserProfileIsModrated() {
		return userProfileIsModrated;
	}

	public void setUserProfileIsModrated(Boolean userProfileIsModrated) {
		this.userProfileIsModrated = userProfileIsModrated;
	}

	public Boolean getLinkEventModeration() {
		return linkEventModeration;
	}

	public void setLinkEventModeration(Boolean linkEventModeration) {
		this.linkEventModeration = linkEventModeration;
	}

	@JsonIgnore
	public Media getAppBigLogoO() {
		return appBigLogo;
	}

	public Object getAppBigLogo() {

		return appBigLogo == null ? null : new Picture(appBigLogo);
	}

	public void setAppBigLogo(Media appBigLogo) {
		this.appBigLogo = appBigLogo;
	}

	@JsonIgnore
	public Media getSplashImageO() {
		return splashImage;
	}

	public Object getSplashImage() {

		return splashImage == null ? null : new Picture(splashImage);
	}

	public void setSplashImage(Media splashImage) {
		this.splashImage = splashImage;
	}

	@JsonIgnore
	public Media getAboutBannerO() {
		return aboutBanner;
	}
	
	public Object getAboutBanner() {
		return aboutBanner==null?null:new Picture(aboutBanner);
	}

	public void setAboutBanner(Media aboutBanner) {
		this.aboutBanner = aboutBanner;
	}

	public String getAppDescription() {
		return appDescription;
	}

	public void setAppDescription(String appDescription) {
		this.appDescription = appDescription;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getBranchIOSDKey() {
		return branchIOSDKey;
	}

	public void setBranchIOSDKey(String branchIOSDKey) {
		this.branchIOSDKey = branchIOSDKey;
	}

	public String getIosAppUrl() {
		return iosAppUrl;
	}

	public void setIosAppUrl(String iosAppUrl) {
		this.iosAppUrl = iosAppUrl;
	}

	public String getAndroidAppUrl() {
		return androidAppUrl;
	}

	public void setAndroidAppUrl(String androidAppUrl) {
		this.androidAppUrl = androidAppUrl;
	}

	@JsonIgnore
	public Media getAppLogoO() {
		return appLogo;
	}

	public Object getAppLogo() {
		if (appLogo == null)
			return null;

		return new Picture(appLogo);
	}
	
	public void setAppLogo(Media appLogo) {
		this.appLogo = appLogo;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getX_PARSE_APPLICATION_ID() {
		return X_PARSE_APPLICATION_ID;
	}

	public void setX_PARSE_APPLICATION_ID(String x_PARSE_APPLICATION_ID) {
		X_PARSE_APPLICATION_ID = x_PARSE_APPLICATION_ID;
	}

	public String getX_PARSE_REST_API_KEY() {
		return X_PARSE_REST_API_KEY;
	}

	public void setX_PARSE_REST_API_KEY(String x_PARSE_REST_API_KEY) {
		X_PARSE_REST_API_KEY = x_PARSE_REST_API_KEY;
	}

	public Object getEtc() {
		return etc;
	}

	public void setEtc(Object etc) {
		this.etc = etc;
	}

	public String getCurrent_lang() {
		return current_lang;
	}

	public void setCurrent_lang(String current_lang) {
		this.current_lang = current_lang;
	}

	@JsonProperty
	public List<KeyValueString> getSupported_languages() {
		return supported_languages;
	}

	public void setLanguages(List<String> language) {
		if (language != null) {
			List<KeyValueString> list = new ArrayList<KeyValueString>();

			for (String string : language) {

				String default_lang = string;

				KeyValueString t = new KeyValueString();

				t.setKey(default_lang);

				t.setText(new Locale(default_lang).getDisplayLanguage());

				list.add(t);

			}

			supported_languages = list;
		}

	}

	public void setSupported_languages(List<KeyValueString> supported_languages) {
		this.supported_languages = supported_languages;
	}

	public List<String> getTestOrganizersEmails() {
		return testOrganizersEmails;
	}

	public void setTestOrganizersEmails(List<String> testOrganizersEmails) {
		this.testOrganizersEmails = testOrganizersEmails;
	}

	public Object getOrganizer_info() {
		return organizer_info;
	}

	public void setOrganizer_info(Object organizer_info) {

		this.organizer_info = organizer_info;
	}

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

	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
	}

	public General getGeneral() {
		return general;
	}

	public void setGeneral(General general) {
		this.general = general;
	}

	public SignUp getSignup() {
		return signup;
	}

	public void setSignup(SignUp signup) {
		this.signup = signup;
	}

	public Sponsorship getSponsorship() {
		return sponsorship;
	}

	public void setSponsorship(Sponsorship sponsorship) {
		this.sponsorship = sponsorship;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public List<String> getEvents() {
		return events;
	}

	public void setEvents(List<String> events) {
		this.events = events;
	}

	public void linkEvent(String eventId) {

		if (eventId == null || eventId == "")
			return;

		if (this.events == null)
			this.events = new ArrayList<String>();

		if (!this.events.contains(eventId)) {

			this.events.add(eventId);
		}

	}

	public void deLinkEvent(String eventId) {

		if (eventId == null || eventId == "")
			return;

		if (this.events == null)
			this.events = new ArrayList<String>();

		if (this.events.contains(eventId)) {

			this.events.remove(eventId);
		}
	}

}
