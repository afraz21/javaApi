package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.iqvis.nvolv3.audit.Audit;
import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.domain.Media;

/**
 * A simple POJO representing a EventTrack
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public class EventTrack extends Audit implements Serializable {

	private static final long serialVersionUID = -1527566248002296042L;

	private String organizerId;

	private String name;

	private Boolean isDeleted = false;

	private Media picture;

	private List<MultiLingual> multiLingual;

	private String sortOrder;

	private String colorCode;

	private String questionnaireType;

	private String questionnaireId;

	private List<Activity> sessions;
	
	private long activityCount;

	public long getActivityCount() {
		return activityCount;
	}

	public void setActivityCount(long activityCount) {
		this.activityCount = activityCount;
	}

	public String getQuestionnaireType() {
		return questionnaireType;
	}

	public void setQuestionnaireType(String questionnaireType) {
		this.questionnaireType = questionnaireType;
	}

	public String getQuestionnaireId() {
		return questionnaireId;
	}

	public void setQuestionnaireId(String questionnaireId) {
		this.questionnaireId = questionnaireId;
	}

	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Picture getPicture() {
		return this.picture != null ? new Picture(this.picture) : null;
	}

	@JsonIgnore
	public Media getPictureMedia() {
		return this.picture;
	}

	public void setPicture(Media picture) {
		this.picture = picture;
	}

	public List<MultiLingual> getMultiLingual() {
		return multiLingual;
	}

	public void setMultiLingual(List<MultiLingual> multiLingual) {
		this.multiLingual = multiLingual;
	}

	public Integer getSortOrder() {

		return sortOrder != null ? Integer.parseInt(sortOrder) : null;
		// return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	@Override
	@JsonIgnore
	public Integer getVersion() {
		// TODO Auto-generated method stub
		return super.getVersion();
	}

	@JsonIgnore
	public List<Activity> getSessions() {
		return sessions;
	}

	public void setSessions(List<Activity> sessions) {
		this.sessions = sessions;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.getId().equals(((EventTrack) obj).getId());
	}

}