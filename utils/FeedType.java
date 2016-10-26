package org.iqvis.nvolv3.utils;

public enum FeedType {

	EVENT, ACTIVITY;

	public String toString() {
		switch (this) {
		case EVENT:
			return Constants.EVENT_FEED;
		case ACTIVITY:
			return Constants.ACTIVITY_FEED;
		}
		return null;
	}

	public static FeedType valueOf(Class<FeedType> enumType, String value) {
		if (value.equals(EVENT.toString()))
			return FeedType.EVENT;
		else if (value.equals(ACTIVITY.toString()))
			return FeedType.ACTIVITY;
		else
			return null;

	}

}
