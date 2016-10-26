package org.iqvis.nvolv3.utils;

public final class Urls {

	// UserFeedbackQuestionController URLs

	public static String ROOT = "/";

	public static final String USER_FEED_QUESTION = "feedback_question/{id}/question";

	public static final String USER_FEED_QUESTION_INNER_OBJECT = USER_FEED_QUESTION + "/{qId}";

	// Admin URLs

	public static final String ADMIN_USER_ROOT = "/admin/{adminId}";

	public static final String MAIN_ADMIN_LEVEL_USER_URL = "/users";

	// TOP LEVEL URLs
	public static final String TOP_LEVEL_ORGANIZER_ID = "{organizerId}";

	public static final String SECOND_LEVEL_DOMAIN_ID = "/{id}";

	public static final String THIRD_LEVEL_DOMAIN_ID = "/{tid}";

	// mobile
	public static final String APP_ID = "{appid}";

	public static final String MOBILE = "mobile/" + APP_ID;

	public static final String FEEDBACK_ANSWER = MOBILE + "/organizer/{organizerId}/answers";

	public static final String MOBILE_COMMENT_BASE = MOBILE + "/organizer/{organizerId}" + "/events" + "/{eventId}/";

	public static final String MOBILE_ANALYTICS_BASE = "organizer/{organizerId}" + "/analytics";

	public static final String MOBILE_PUSH_NOTIFICATION_BASE = MOBILE + "/organizer/{organizerId}" + "/push";

	public static final String TOP_LEVEL = "organizer/" + TOP_LEVEL_ORGANIZER_ID;

	// Event related URLS
	public static final String EVENT_BASE_URL = TOP_LEVEL + "/events";

	public static final String GET_EVENTS = "";

	public static final String ADD_EVENT = "";

	public static final String UPDATE_EVENT = SECOND_LEVEL_DOMAIN_ID;

	public static final String GET_EVENT = SECOND_LEVEL_DOMAIN_ID;

	public static final String GET_EVENTS_PERSONNELS = "/personnels";

	public static final String GET_EVENT_PERSONNELS = SECOND_LEVEL_DOMAIN_ID + "/personnels";

	public static final String GET_EVENT_SPONSORS = SECOND_LEVEL_DOMAIN_ID + "/sponsors";

	public static final String GET_EVENT_TRACKS = SECOND_LEVEL_DOMAIN_ID + "/tracks";

	// ////////////////////////////////////////////////////////////////////////////////////////

	public static final String GET_EVENT_ASSOCIATED_PERSONNELS = SECOND_LEVEL_DOMAIN_ID + "/event-personnels";

	public static final String ADD_EVENT_ASSOCIATED_PERSONNELS = SECOND_LEVEL_DOMAIN_ID + "/event-personnels";

	public static final String UPDATE_EVENT_ASSOCIATED_PERSONNELS = SECOND_LEVEL_DOMAIN_ID + "/event-personnels" + THIRD_LEVEL_DOMAIN_ID;

	public static final String GET_EVENT_PERSONNELS_ACTIVITIES = SECOND_LEVEL_DOMAIN_ID + "/event-personnels/{pid}/personnel-activities";

	// /////////////////////////////////////////////////////////////////////////////////////////////

	// /

	public static final String EVENT_RESOURCES_BASE_URL = EVENT_BASE_URL + SECOND_LEVEL_DOMAIN_ID + "/event-resources";

	public static final String GET_EVENT_RESOURCES = "";

	public static final String ADD_EVENT_RESOURCES = "";

	public static final String UPDATE_EVENT_RESOURCES = THIRD_LEVEL_DOMAIN_ID;

	public static final String GET_EVENT_RESOURCE = THIRD_LEVEL_DOMAIN_ID;

	// Event Campaigns URL

	public static final String EVENT_CAMPAIGN_BASE_URL = TOP_LEVEL + "/event-campaigns";

	public static final String GET_EVENT_CAMPAIGNS = "";

	public static final String ADD_EVENT_CAMPAIGN = "";

	public static final String UPDATE_EVENT_CAMPAIGN = SECOND_LEVEL_DOMAIN_ID;

	public static final String GET_EVENT_CAMPAIGN = SECOND_LEVEL_DOMAIN_ID;

	// / feeds

	public static final String EVENT_FEEDS_BASE_URL = "events" + SECOND_LEVEL_DOMAIN_ID + "/feeds";

	// public static final String FEEDS_EDIT = "feeds" + SECOND_LEVEL_DOMAIN_ID;

	public static final String FEEDS_EDIT = "events" + SECOND_LEVEL_DOMAIN_ID + "/feeds" + SECOND_LEVEL_DOMAIN_ID;

	public static final String ACTIVITY_FEEDS_BASE_URL = "activity" + SECOND_LEVEL_DOMAIN_ID + "/feeds";

	// comments

	public static final String FEEDS_COMMENT = "feeds" + SECOND_LEVEL_DOMAIN_ID + "/addcomment";
	public static final String FEEDS_LIKE = "feeds" + SECOND_LEVEL_DOMAIN_ID + "/like";

	// Event Campaigns URL

	public static final String EVENT_CAMPAIGN_PARTICIPANT_BASE_URL = EVENT_CAMPAIGN_BASE_URL + GET_EVENT_CAMPAIGN + "/event-campaigns-participants";

	public static final String GET_EVENT_CAMPAIGN_PARTICIPANTS = "";

	public static final String ADD_EVENT_CAMPAIGN_PARTICIPANT = "";

	public static final String UPDATE_EVENT_CAMPAIGN_PARTICIPANT = THIRD_LEVEL_DOMAIN_ID;

	public static final String GET_EVENT_CAMPAIGN_PARTICIPANT = THIRD_LEVEL_DOMAIN_ID;

	// Track related URLS
	public static final String TRACK_BASE_URL = TOP_LEVEL + "/tracks";

	public static final String GET_TRACKS = "";

	public static final String ADD_TRACK = "";

	public static final String UPDATE_TRACK = SECOND_LEVEL_DOMAIN_ID;

	public static final String GET_TRACK = SECOND_LEVEL_DOMAIN_ID;

	// Venue related URLS
	public static final String VENUE_BASE_URL = TOP_LEVEL + "/venues";

	public static final String GET_VENUES = "";

	public static final String ADD_VENUE = "";

	public static final String UPDATE_VENUE = SECOND_LEVEL_DOMAIN_ID;

	public static final String GET_VENUE = SECOND_LEVEL_DOMAIN_ID;

	// News related URLS
	public static final String News_BASE_URL = TOP_LEVEL + "/news";

	public static final String GET_NEWS_LIST = "";

	public static final String ADD_NEWS = "";

	public static final String UPDATE_NEWS = SECOND_LEVEL_DOMAIN_ID;

	public static final String GET_NEWS = SECOND_LEVEL_DOMAIN_ID;

	// Mobile News related URLS

	public static final String MOBILE_NEWS_BASE_URL = MOBILE + "/" + TOP_LEVEL + "/news";

	// Sponsors related URLS
	public static final String SPONSOR_BASE_URL = TOP_LEVEL + "/sponsors";

	public static final String GET_SPONSORS = "";

	public static final String ADD_SPONSOR = "";

	public static final String UPDATE_SPONSOR = SECOND_LEVEL_DOMAIN_ID;

	public static final String GET_SPONSOR = SECOND_LEVEL_DOMAIN_ID;

	// Sponsors related URLS
	public static final String VENDOR_BASE_URL = TOP_LEVEL + "/vendors";

	public static final String GET_VENDORS = "";

	public static final String ADD_VENDOR = "";

	public static final String UPDATE_VENDOR = SECOND_LEVEL_DOMAIN_ID;

	public static final String GET_VENDOR = SECOND_LEVEL_DOMAIN_ID;

	// locations related URLS
	public static final String LOCATION_BASE_URL = TOP_LEVEL + "/locations";

	public static final String GET_LOCATIONS = "";

	public static final String ADD_LOCATION = "";

	public static final String UPDATE_LOCATION = SECOND_LEVEL_DOMAIN_ID;

	public static final String GET_LOCATION = SECOND_LEVEL_DOMAIN_ID;

	// Activity related URLS
	// public static final String ACTIVITY_BASE_URL = TOP_LEVEL + "/activities";

	public static final String ACTIVITY_BASE_URL = EVENT_BASE_URL + THIRD_LEVEL_DOMAIN_ID + "/activities";

	public static final String GET_ACTIVITIES = "";

	public static final String ADD_ACTIVITY = "";

	public static final String UPDATE_ACTIVITY = SECOND_LEVEL_DOMAIN_ID;

	public static final String GET_ACTIVITY = SECOND_LEVEL_DOMAIN_ID;

	// User related URLS
	public static final String USER_BASE_URL = "/users";

	public static final String GET_USERS = "";

	public static final String ADD_USER = "";

	public static final String UPDATE_USER = SECOND_LEVEL_DOMAIN_ID;

	public static final String GET_USER = SECOND_LEVEL_DOMAIN_ID;

	// Personnel related URLS
	public static final String PERSONNEL_BASE_URL = TOP_LEVEL + "/personnels";

	public static final String GET_PERSONNELS = "";

	public static final String ADD_PERSONNEL = "";

	public static final String UPDATE_PERSONNEL = SECOND_LEVEL_DOMAIN_ID;

	public static final String GET_PERSONNEL = SECOND_LEVEL_DOMAIN_ID;

	public static final String GET_PERSONNEL_ACTIVITIES = SECOND_LEVEL_DOMAIN_ID + "/activities";

	// Authentication URL

	public static final String AUTH_BASE_URL = "/authentication";
	public static final String LOGIN = "login";

	public static final String CHANGE_PASSWORD = "change-password";

	public static final String FORGOT_PASSWORD = "forgot-password";

	public static final String EMAIL_VERFICATION = "verfication" + SECOND_LEVEL_DOMAIN_ID;

	// Media related URLS
	public static final String MEDIA_BASE_URL = "/media";

	public static final String ADD_MEDIA = "";

	public static final String ADD_RESOURCE = "/add-resource";

	public static final String ADD_CUSTUM_MEDIA = "/custom";

	public static final String GET_MEDIA = SECOND_LEVEL_DOMAIN_ID;

	// Reference Data URL
	public static final String REFERENCE_DATA_BASE_URL = TOP_LEVEL + "/reference-data";

	public static final String REFERENCE_DATA_LIST = "";

	public static final String REFERENCE_DATA_UPDATE = "/{type}";

	public static final String REFERENCE_DATA_GET = "/{type}";

	// Mobile Controller Event APIS

	public static final String MOBILE_APP_EVENTS = MOBILE + "/events";

	public static final String MOBILE_EVENT_BASE_URL = MOBILE + "/" + TOP_LEVEL + "/events";
	
	public static final String MOBILE_APP_ATTENDEE_BASE=MOBILE + "/" + TOP_LEVEL;

	public static final String MOBILE_GET_EVENTS = MOBILE_EVENT_BASE_URL + "";

	public static final String MOBILE_ADD_EVENT = MOBILE_EVENT_BASE_URL + "";

	public static final String MOBILE_UPDATE_EVENT = MOBILE_EVENT_BASE_URL + SECOND_LEVEL_DOMAIN_ID;

	public static final String MOBILE_GET_EVENT = MOBILE_EVENT_BASE_URL + SECOND_LEVEL_DOMAIN_ID;

	public static final String MOBILE_GET_EVENT_TRACKS = MOBILE_EVENT_BASE_URL + SECOND_LEVEL_DOMAIN_ID + "/tracks";

	public static final String MOBILE_GET_EVENT_ALERT = MOBILE_EVENT_BASE_URL + SECOND_LEVEL_DOMAIN_ID + "/alerts";

	public static final String MOBILE_GET_EVENT_PERSONNELS = MOBILE_EVENT_BASE_URL + SECOND_LEVEL_DOMAIN_ID + "/personnels";

	public static final String MOBILE_GET_EVENT_VENDORS = MOBILE_EVENT_BASE_URL + SECOND_LEVEL_DOMAIN_ID + "/vendors";

	public static final String MOBILE_GET_EVENT_SPONSORS = MOBILE_EVENT_BASE_URL + SECOND_LEVEL_DOMAIN_ID + "/sponsors";

	public static final String MOBILE_GET_EVENT_ACTIVITIES = MOBILE_EVENT_BASE_URL + SECOND_LEVEL_DOMAIN_ID + "/activities";

	public static final String MOBILE_EVENT_CONFIGURATION = "organizer/{organizerId}/event-config" + "/{id}";

	// Mobile App Config URL
	public static final String APP_CONFIG_DATA_BASE_URL = TOP_LEVEL + "/app-config";

	public static final String APP_CONFIG_LIST = "";

	public static final String Event_List_Object = "/{appId}/{code}/{version}";

	public static final String Mobile_APP_CONFIG_LIST = "/organizer/" + TOP_LEVEL_ORGANIZER_ID;;

	public static final String APP_CONFIG_UPDATE = "/{id}";

	public static final String APP_CONFIG_GET = "/{id}";

	public static final String MOBILE_APP_CONFIG_DATA_BASE_URL = MOBILE + "/app-configuration";

	public static final String MOBILE_APP_CONFIG_LIST = "";

	public static final String MOBILE_APP_CONFIG_UPDATE = "/{id}";

	public static final String MOBILE_APP_CONFIG_GET = "/{id}";

	// Event Advert
	public static final String EVENT_ADVERT_BASE_URL = TOP_LEVEL + "/eventAlerts";

	public static final String GET_EVENT_ADVERTS = "";

	public static final String ADD_EVENT_ADVERT = "";

	public static final String UPDATE_EVENT_ADVERT = SECOND_LEVEL_DOMAIN_ID;

	public static final String GET_EVENT_ADVERT = SECOND_LEVEL_DOMAIN_ID;

	public static final String MOBILE_EVENT_ALERT_BASE_URL = MOBILE + "/" + TOP_LEVEL + "/event-alerts";

	public static final String MOBILE_EVENT_ALERTS = SECOND_LEVEL_DOMAIN_ID;

	// Time zone
	public static final String TIME_ZONE_BASE_URL = "/timezones";

	public static final String ADD_TIME_ZONE = "";

	public static final String UPDATE_TIME_ZONE = SECOND_LEVEL_DOMAIN_ID;

	public static final String GET_TIME_ZONE = SECOND_LEVEL_DOMAIN_ID;

	public static final String GET_TIME_ZONES = "";

	// feeds url format

	public static final String FEED_BASE_URL = "mobile/{appId}/organizer/{organozerId}";

}
