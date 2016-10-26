package org.iqvis.nvolv3.utils;

public class Constants {

	// TODO System Constents

	public static String BADGE = "BADGE";

	// TODO Social Media/Sources Constants

	public static String FACEBOOK = "Facebook";

	public static String TWITTER = "Twitter";

	public static String LINKED_IN = "LinkedIn";

	public static String APP = "APP";

	// TODO User Type Constants

	public static String USER_ADMIN = "ADMIN";

	public static String USER_PERSONNEL = "PERSONNEL";

	public static String USER_ORGANIZER = "ORGANIZER";

	public static String USER_SPONSOR = "SPONSOR";

	public static String USER_NVOLV_STAFF = "NVOLV_STAFF";

	public static String USER_PARTNER = "PARTNER";

	// TODO Question Type Constants

	public static String FEEDBACK_RADIO_CONTROL = "RADIO";

	public static String FEEDBACK_CHECKBOX_CONTROL = "CHECKBOX";

	public static String FEEDBACK_DROPDOWN_CONTROL = "DROPDOWN";

	public static String FEEDBACK_TEXTVIEW_CONTROL = "TEXTVIEW";

	public static String FEEDBACK_TEXTFIELD_CONTROL = "TEXTFIELD";

	public static String FEEDBACK_RATING_CONTROL = "RATING";

	// TODO ANALYTICS API CONSTANTS
	public static final String PUSH_NOFICATION_URL = "https://api.parse.com/1/push";

	public static final String NVOLV3_ANALYTICS_API_KEY = "55115f690a769795e415e8a7";

	public static final String NVOLV_ANALYTICS_REPORTING_API_URL = "http://analytics.iqvis.net:81/reporting/custom/aggregate/" + NVOLV3_ANALYTICS_API_KEY;

	public static String GOOGLE_TIMEZONE_API_URL = "https://maps.googleapis.com/maps/api/timezone/json?";

	// TODO Questionnaire Type

	public static String INHERIT = "INHERIT";

	public static String CUSTOM = "CUSTOM";

	public static String DISABLED = "DISABLED";

	// TODO Question Types

	public static String SINGLE_SELECT = "SINGLE_SELECT";

	public static String MULTI_SELECT = "MULTI_SELECT";

	public static String RATING_STARS = "RATING_STARS";

	public static String INPUT_TEXT = "INPUT_TEXT";

	// TODO AppType Constants

	public static String APP_TYPE_GENERAL = "GENERAL";

	public static String APP_TYPE_ORGANIZER_SPECIFIC = "ORGANIZER_SPECIFIC";

	public static String APP_TYPE_EVENT_SPECIFIC = "EVENT_SPECIFIC";

	public static String APP_TYPE_WHITELABEL = "WHITELABEL";

	public static String APP_TYPE_PARTNER = "PARTNER";

	public static String EVENT_TYPE = "EVENT_TYPE";
	/**
	 * TODO Logging Keys
	 * */

	public static final String LOG_USER_FEEDBACK_QUESTION = "USER_FEEDBACK_QUESTION";

	public static final String LOG_ATTENDEE_PROFILE = "LOG_ATTENDEE_PROFILE";

	public static String TRACK_LOG_KEY = "Track";

	public static String EVENT_ALERT_LOG_KEY = "EVENT_ALERT";

	public static String EVENT_SPONSOR_LOG_KEY = "EventSponsor";

	public static String EVENT_CONFIGURATION_LOG_KEY = "EventConfiguration";

	public static String EVENT_CAMPAIGN_PARTICIPANT_LOG_KEY = "eventCampaignParticipant";

	public static String EVENT_CAMPAIGN__LOG_KEY = "eventCampaign";

	public static String EVENT_PERSONNEL_LOG_KEY = "EventPersonnel";

	public static String EVENT_RESOURCE_MAP_LOG_KEY = "ResourceMap";

	public static String EVENT_OTHER_RESOURCE_MAP_LOG_KEY = "Resource";

	public static String EVENT_VENDOR_LOG_KEY = "Vendor";

	public static String VENDOR_LOG_KEY = "Vendor";

	public static String ACTIVITY_LOG_KEY = "Activity";

	public static String VENUE_LOG_KEY = "Venue";

	public static String FEED_LOG_KEY = "FEED";

	public static String FEED_STATUS_APPROVED = "APPROVED";

	public static String FEED_STATUS_PENDING = "PENDING";

	public static String FEED_STATUS_REJECTED = "REJECTED";

	public static String FEED_STATUS_DEFERRED = "DEFERRED";

	public static String EVENT_FEED = "EVENT";

	public static String ACTIVITY_FEED = "ACTIVITY";

	/**
	 * 
	 * TODO Feeds Counstants
	 * */

	public static final int THRESHOLD = 100;

	public static final int FEED_LIMIT = 10;

	/**
	 * TODO Logging Action Keys
	 * */
	public static String LOG_ACTION_ADD = "ADD";

	public static String LOG_ACTION_UPDATE = "Update";

	public static String LOG_ACTION_DELETE = "delete";

	/**
	 * TODO Sponsor_Type Selector Keys For Utlity selector on reference data
	 * */

	public static String LOG_UTLITY_KEY_SPONSOR_TYPE = "Sponsor_Type";

	public static String LOG_UTLITY_KEY_VENDOR_BUSINESS_CATEGORY = "Vendor_Business_Category";

	public static String LOG_UTLITY_KEY_PERSONNEL_TYPE = "Personnel_Type";

	// TODO CASE IN-Sensitive Regex
	public static String CASE_IN_SENSITIVE_PRE = "/^";
	public static String CASE_IN_SENSITIVE_POST = "$/i";

	/*
	 * TODO CODE REFERENCES
	 */
	public static String SUCCESS_CODE = "1000";

	public static String ERROR_CODE = "1001";

	public static String FOUND_CODE = "1200";

	public static String NOT_FOUND_CODE = "1404";

	public static String UNAUTHORIZED_USER_CODE = "1414";

	public static String AUTHORIZED_USER_CODE = "1415";

	/*
	 * TODO EVENT CAMPAIGNS CONSTANTS
	 */

	public static String PHOTO_OVERLAY = "PHOTO_OVERLAY";

	/*
	 * TODO For Sponsor Controller
	 */

	public static String DEFAULT_PASSWORD = "123123";

	public static String USER_TYPE_SPONSOR = "Sponsor";

	/*
	 * TODO Services Constants
	 */

	public static final String USER_FEEDBACK_QUESTION_RESOURCE = "userFeedbackQuestionService";

	public static final String USER_FEEDBACK_ANSWER_RESOURCE = "userFeedbackAnswerService";

	public static final String SERVICE_EVENT_CONFIGURATION = "eventConfigurationService";

	public static final String SERVICE_USER = "userService";

	public static final String SERVICE_USER_VERIFICATION = "userVerificationService";

	public static final String SERVICE_VENUE = "venueService";

	public static final String SERVICE_EVENT_ALERT = "eventAlertService";

	public static final String SERVICE_EVENT = "eventService";

	public static final String SERVICE_EVENT_RESOURCE = "eventResourceService";

	public static final String SERVICE_FEED = "feedService";

	public static final String SERVICE_EVENT_CAMPAIGN = "eventCampaignService";

	public static final String SERVICE_EVENT_TRACK = "eventTrackService";

	public static final String SERVICE_ACTIVITY = "activityService";

	public static final String SERVICE_COMMENT = "commentService";

	public static final String SERVICE_LIKE = "likeService";

	public static final String SERVICE_PERSONNEL = "personnelService";

	public static final String SERVICE_LOCATION = "locationService";

	public static final String SERVICE_SPONSOR = "sponsorService";

	public static final String SERVICE_VENDOR = "vendorService";

	public static final String SERVICE_TRACK = "trackService";

	public static final String SERVICE_EVENT_CAMPAIGN_PARTICIPANT = "eventCampaignParticipantService";

	public static final String SERVICE_MEDIA = "mediaService";

	public static final String SERVICE_EMAIL = "emailService";

	public static final String SERVICE_REFERENCE_DATA = "referencedataService";

	public static final String SERVICE_APP_CONFIG = "appConfigService";

	public static final String SERVICE_USER_DEVICE_INFO_SERVICE = "userDeviceInfoService";

	public static final String MOBILE_SERVICE_EVENT = "mobileEventService";

	public static final String SERVICE_NEWS = "newsService";

	public static final String PUSH_LOGGING_SERVICE = "pushLoggingService";

	public static final String SERVICE_TIME_ZONE = "timeZoneService";

	public static final String SERVICE_DATA_CHAGE_LOG_SERVCIE = "dataChangeLogService";

	public static final String SERVICE_ATTENDEE = "attendeeService";

	public static final String FACTORY_SYSTEM_CONSTANT = "factorySystemConstant";

	public static final String SERVICE_SYSTEMT_CONSTANT = "systemConstantService";

	public static final String SERVICE_EMAIL_TEMPLATE = "emailTemplateService";

	// TODO Activity TYPES
	public static final String ACTIVITY_TYPE_KEY_NOTE = "Keynote";

	public static final String ACTIVITY_TYPE_SESSION = "Session";

	public static final String APPLICATION_DEFAULT_LANGUAGE = "EN";

	public static String VERFICATION_EMAIL_TEMPLATE = "<html><body><h4>Dear [NAME],</h4><p>Someone, possibly you, requested to reset your Nvolv password. Please visit the link below to finish resetting your password.</p></p>The link will take you a to a web page where you can create a new password.</p><p>[LINK]</p><p>If you weren't trying to reset your password, just ignore this email. Please do not reply to this email.</p><p>Thanks,</p><strong><p>Nvolv Team</p></strong></body></html>";

	public static String VERIFICAITON_FORM = "http://cms.nvolv.co/member/resetpassword/";

	// TODO EMAIL TEMPLATE CONSTANTS
	public static final String SALES_EMAIL_ADDRESS = "sales@getnvolv.com";

	public static final String MAIL_FOR_INDIVIDUAL = "INDIVIDUAL";

	// TODO personnel types
	public static final String PERSONNEL_TYPE = "Personnel_Type";

	public static final String ATTACH_EVENT_EMAIL_TEMPLATE_FOR_SALES = "ATTACH_EVENT_EMAIL_TEMPLATE_FOR_SALES";
	
	public static final String NVOLV_APP_APPROVED_EMAIL_TEMPLATE = "nvolv_app_approved_email_template";
	
	public static final String NVOLV_APP_SALES_EMAIL_TEMPLATE = "ATTACH_EVENT_EMAIL_TEMPLATE_FOR_SALES_OF_PARTNER";

}
