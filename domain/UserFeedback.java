package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

public class UserFeedback implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String objectType;

	private String objectId;
	
	private String eventId;
	private String activityName;
	private String objectName;


	private List<AnswerObject> answers;

	private UserFeedbackAnswer feedBack;

	@CreatedDate
	private DateTime createdDate;

	public DateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	
	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<AnswerObject> getAnswers() {
		return answers;
	}

	public void setAnswers(List<AnswerObject> answers) {
		this.answers = answers;
	}

	public UserFeedbackAnswer getFeedBack() {
		return feedBack;
	}

	public void setFeedBack(UserFeedbackAnswer feedBack) {
		this.feedBack = feedBack;
	}

}
