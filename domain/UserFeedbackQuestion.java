package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "userFeedbackQuestions")
public class UserFeedbackQuestion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String title;

	private String referenceObject;

	private String referenceObjectId;

	private String eventId;

	private String organizerId;

	private boolean isDeleted;

	private List<Question> questions;

	@CreatedDate
	private DateTime createdDate;

	@LastModifiedDate
	private DateTime lastModifiedDate;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}

	public DateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(DateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getReferenceObject() {
		return referenceObject;
	}

	public void setReferenceObject(String referenceObject) {
		this.referenceObject = referenceObject;
	}

	public String getReferenceObjectId() {
		return referenceObjectId;
	}

	public void setReferenceObjectId(String referenceObjectId) {
		this.referenceObjectId = referenceObjectId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	@Override
	public boolean equals(Object obj) {

		return this.id.equals(((UserFeedbackQuestion) obj).getId());
	}

}
