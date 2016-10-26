package org.iqvis.nvolv3.utils;

public enum FeesStatusType {

	APPROVED, PENDING, REJECTED, DEFERRED;

	public String toString() {
		switch (this) {
		case APPROVED:
			return Constants.FEED_STATUS_APPROVED;
		case PENDING:
			return Constants.FEED_STATUS_PENDING;
		case REJECTED:
			return Constants.FEED_STATUS_REJECTED;
		case DEFERRED:
			return Constants.FEED_STATUS_DEFERRED;
		}
		return null;
	}

	public static FeesStatusType valueOf(Class<FeesStatusType> enumType, String value) {
		if (value.equals(APPROVED.toString()))
			return FeesStatusType.APPROVED;
		else if (value.equals(PENDING.toString()))
			return FeesStatusType.PENDING;
		else if (value.equals(REJECTED.toString()))
			return FeesStatusType.REJECTED;
		else if (value.equals(DEFERRED.toString()))
			return FeesStatusType.DEFERRED;
		else
			return null;
	}

}
