package org.iqvis.nvolv3.bean;

import java.io.Serializable;

public class FeedbackConfiguration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String event_feedback;

	private String track_feedback;

	private String personnel_feedback;

	private String activity_feedback;

	public String getEvent_feedback() {
		return event_feedback;
	}

	public void setEvent_feedback(String event_feedback) {
		this.event_feedback = event_feedback;
	}

	public String getTrack_feedback() {
		return track_feedback;
	}

	public void setTrack_feedback(String track_feedback) {
		this.track_feedback = track_feedback;
	}

	public String getPersonnel_feedback() {
		return personnel_feedback;
	}

	public void setPersonnel_feedback(String personnel_feedback) {
		this.personnel_feedback = personnel_feedback;
	}

	public String getActivity_feedback() {
		return activity_feedback;
	}

	public void setActivity_feedback(String activity_feedback) {
		this.activity_feedback = activity_feedback;
	}

	@Override
	public boolean equals(Object obj) {
		FeedbackConfiguration configuration = (FeedbackConfiguration) obj;

		if (obj == null)
			return false;

		if (!(this.getActivity_feedback() + "").equals(configuration.getActivity_feedback())) {
			return false;
		}

		if (!(this.getEvent_feedback() + "").equals(configuration.getEvent_feedback())) {
			return false;
		}

		if (!(this.getPersonnel_feedback() + "").equals(configuration.getPersonnel_feedback())) {
			return false;
		}

		if (!(this.getTrack_feedback() + "").equals(configuration.getTrack_feedback())) {
			return false;
		}

		return super.equals(obj);
	}

}
