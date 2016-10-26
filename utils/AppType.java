package org.iqvis.nvolv3.utils;

public enum AppType {

	GENERAL, ORGANIZER_SPECIFIC, EVENT_SPECIFIC, WHITELABEL, PARTNER;

	public String toString() {
		switch (this) {
		case GENERAL:
			return Constants.APP_TYPE_GENERAL;
		case ORGANIZER_SPECIFIC:
			return Constants.APP_TYPE_ORGANIZER_SPECIFIC;
		case EVENT_SPECIFIC:
			return Constants.APP_TYPE_EVENT_SPECIFIC;
		case WHITELABEL:
			return Constants.APP_TYPE_WHITELABEL;
		case PARTNER:
			return Constants.APP_TYPE_PARTNER;
		}
		return null;
	}

	public static AppType valueOf(Class<AppType> enumType, String value) {
		if (value.equals(GENERAL.toString()))
			return AppType.GENERAL;
		else if (value.equals(ORGANIZER_SPECIFIC.toString()))
			return AppType.ORGANIZER_SPECIFIC;
		else if (value.equals(EVENT_SPECIFIC.toString()))
			return AppType.EVENT_SPECIFIC;
		else if (value.equals(WHITELABEL.toString()))
			return AppType.WHITELABEL;
		else if (value.equals(PARTNER.toString()))
			return AppType.PARTNER;
		else
			return null;
	}
}
