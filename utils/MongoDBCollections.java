package org.iqvis.nvolv3.utils;

public enum MongoDBCollections {

	EVENT_CONFIGURATION, EVENT, TRACK, VENUE, PERSONNEL, ACTIVITY, USER, SPONSOR, MEDIA, LOCATION, REFERENCE_DATA, VENDOR, APP_CONFIGURATION, EVENT_ALERT, TIME_ZONE, EVENT_CAMPAIGN, USER_VERIFICATION, FEED, NEWS, EVENT_RESOURCES, Data_Change_Log, USER_FEEDBACK_ANSWERS;

	public String toString() {
		switch (this) {
		case EVENT:
			return "event";
		case TRACK:
			return "track";
		case VENUE:
			return "venue";
		case PERSONNEL:
			return "personnel";
		case ACTIVITY:
			return "activity";
		case USER:
			return "user";
		case SPONSOR:
			return "sponsor";
		case MEDIA:
			return "media";
		case LOCATION:
			return "location";
		case VENDOR:
			return "vendor";
		case REFERENCE_DATA:
			return "referenceData";
		case EVENT_ALERT:
			return "eventAlert";
		case TIME_ZONE:
			return "timezone";
		case APP_CONFIGURATION:
			return "appConfiguration";
		case FEED:
			return "feed";
		case EVENT_CAMPAIGN:
			return "eventCampaign";
		case USER_VERIFICATION:
			return "userVerification";
		case NEWS:
			return "news";
		case EVENT_RESOURCES:
			return "eventResource";
		case Data_Change_Log:
			return "dataChangeLog";
		case EVENT_CONFIGURATION:
			return "apiConfig";
		case USER_FEEDBACK_ANSWERS:
			return "userFeedBackAnswers";

		}

		return null;
	}

	public static MongoDBCollections valueOf(Class<MongoDBCollections> enumType, String value) {
		if (value.equalsIgnoreCase(EVENT.toString()))
			return MongoDBCollections.EVENT;
		else if (value.equalsIgnoreCase(TRACK.toString()))
			return MongoDBCollections.TRACK;
		else if (value.equalsIgnoreCase(VENUE.toString()))
			return MongoDBCollections.VENUE;
		else if (value.equalsIgnoreCase(PERSONNEL.toString()))
			return MongoDBCollections.PERSONNEL;
		else if (value.equalsIgnoreCase(ACTIVITY.toString()))
			return MongoDBCollections.ACTIVITY;
		else if (value.equalsIgnoreCase(USER.toString()))
			return MongoDBCollections.USER;
		else if (value.equalsIgnoreCase(SPONSOR.toString()))
			return MongoDBCollections.SPONSOR;
		else if (value.equalsIgnoreCase(MEDIA.toString()))
			return MongoDBCollections.MEDIA;
		else if (value.equalsIgnoreCase(LOCATION.toString()))
			return MongoDBCollections.LOCATION;
		else if (value.equalsIgnoreCase(REFERENCE_DATA.toString()))
			return MongoDBCollections.REFERENCE_DATA;
		else if (value.equalsIgnoreCase(VENDOR.toString()))
			return MongoDBCollections.VENDOR;
		else if (value.equalsIgnoreCase(APP_CONFIGURATION.toString()))
			return MongoDBCollections.APP_CONFIGURATION;
		else if (value.equalsIgnoreCase(EVENT_ALERT.toString()))
			return MongoDBCollections.EVENT_ALERT;
		else if (value.equalsIgnoreCase(TIME_ZONE.toString()))
			return MongoDBCollections.TIME_ZONE;
		else if (value.equalsIgnoreCase(EVENT_CAMPAIGN.toString()))
			return MongoDBCollections.EVENT_CAMPAIGN;
		else if (value.equalsIgnoreCase(FEED.toString()))
			return MongoDBCollections.FEED;
		else if (value.equalsIgnoreCase(USER_VERIFICATION.toString()))
			return MongoDBCollections.USER_VERIFICATION;
		else if (value.equalsIgnoreCase(NEWS.toString()))
			return MongoDBCollections.NEWS;
		else if (value.equalsIgnoreCase(EVENT_RESOURCES.toString()))
			return MongoDBCollections.EVENT_RESOURCES;
		else if (value.equalsIgnoreCase(Data_Change_Log.toString()))
			return MongoDBCollections.Data_Change_Log;
		else if (value.equalsIgnoreCase(EVENT_CONFIGURATION.toString()))
			return EVENT_CONFIGURATION;
		else if (value.equalsIgnoreCase(USER_FEEDBACK_ANSWERS.toString()))
			return USER_FEEDBACK_ANSWERS;
		else
			return null;
	}
}
