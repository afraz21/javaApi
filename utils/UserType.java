package org.iqvis.nvolv3.utils;

public enum UserType {

	SPONSOR, ORGANIZER, PERSONNEL, ADMIN, PARTNER;

	@Override
	public String toString() {

		switch (this) {
		case SPONSOR:

			return Constants.USER_SPONSOR;

		case ORGANIZER:

			return Constants.USER_ORGANIZER;

		case PERSONNEL:

			return Constants.USER_PERSONNEL;
		case ADMIN:

			return Constants.USER_ADMIN;

		case PARTNER:

			return Constants.USER_PARTNER;

		}

		return null;
	}

	public static UserType valueOf(Class<UserType> enumType, String value) {

		if (SPONSOR.toString().equals(value)) {

			return UserType.SPONSOR;
		}
		else if (PERSONNEL.toString().equals(value)) {

			return UserType.PERSONNEL;
		}

		else if (ORGANIZER.toString().equals(value)) {

			return UserType.ORGANIZER;
		}
		
		else if (ADMIN.toString().equals(value)) {

			return UserType.ADMIN;
		}
		
		else if (PARTNER.toString().equals(value)) {

			return UserType.PARTNER;
		}
		
		

		return null;

	}

}
