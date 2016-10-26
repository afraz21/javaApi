package org.iqvis.nvolv3.utils;

public enum QuestionUIType {

	FEEDBACK_RADIO_CONTROL, FEEDBACK_CHECKBOX_CONTROL, FEEDBACK_DROPDOWN_CONTROL, FEEDBACK_TEXTVIEW_CONTROL, FEEDBACK_TEXTFIELD_CONTROL, FEEDBACK_RATING_CONTROL;

	@Override
	public String toString() {
		// TODO Auto-generated method stub

		switch (this) {
		case FEEDBACK_RADIO_CONTROL:
			return Constants.FEEDBACK_RADIO_CONTROL;
		case FEEDBACK_CHECKBOX_CONTROL:
			return Constants.FEEDBACK_CHECKBOX_CONTROL;
		case FEEDBACK_DROPDOWN_CONTROL:
			return Constants.FEEDBACK_DROPDOWN_CONTROL;
		case FEEDBACK_TEXTVIEW_CONTROL:
			return Constants.FEEDBACK_TEXTVIEW_CONTROL;
		case FEEDBACK_TEXTFIELD_CONTROL:
			return Constants.FEEDBACK_TEXTFIELD_CONTROL;
		case FEEDBACK_RATING_CONTROL:
			return Constants.FEEDBACK_RATING_CONTROL;
		}

		return super.toString();
	}

	public static QuestionUIType valueOf(Class<QuestionType> enumType, String value) {

		if (value.equals(FEEDBACK_RADIO_CONTROL.toString())) {
			return FEEDBACK_RADIO_CONTROL;
		}
		else if (value.equals(FEEDBACK_CHECKBOX_CONTROL.toString())) {
			return FEEDBACK_CHECKBOX_CONTROL;
		}
		else if (value.equals(FEEDBACK_DROPDOWN_CONTROL.toString())) {
			return FEEDBACK_DROPDOWN_CONTROL;
		}
		else if (value.equals(FEEDBACK_TEXTVIEW_CONTROL.toString())) {
			return FEEDBACK_TEXTVIEW_CONTROL;
		}
		else if (value.equals(FEEDBACK_TEXTFIELD_CONTROL.toString())) {
			return FEEDBACK_TEXTFIELD_CONTROL;
		}
		else if (value.equals(FEEDBACK_RATING_CONTROL.toString())) {
			return FEEDBACK_RATING_CONTROL;
		}

		return null;
	}
}
