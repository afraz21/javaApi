package org.iqvis.nvolv3.utils;

public enum QuestionType {

	SINGLE_SELECT, MULTI_SELECT, RATING_STARS, INPUT_TEXT;

	@Override
	public String toString() {
		// TODO Auto-generated method stub

		switch (this) {
		case SINGLE_SELECT:
			return Constants.SINGLE_SELECT;

		case MULTI_SELECT:
			return Constants.MULTI_SELECT;

		case RATING_STARS:
			return Constants.RATING_STARS;

		case INPUT_TEXT:
			return Constants.INPUT_TEXT;

		}

		return super.toString();
	}

	public static QuestionType valueOf(Class<QuestionType> enumType, String value) {

		if (value.toString().equals(SINGLE_SELECT.toString())) {
			return SINGLE_SELECT;
		}
		else if (value.toString().equals(MULTI_SELECT.toString())) {
			return MULTI_SELECT;
		}
		else if (value.toString().equals(RATING_STARS.toString())) {
			return RATING_STARS;
		}
		else if (value.toString().equals(INPUT_TEXT.toString())) {
			return INPUT_TEXT;
		}
		else
			return null;

	}

}
