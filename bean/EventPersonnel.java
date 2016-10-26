package org.iqvis.nvolv3.bean;

import java.io.Serializable;

import org.iqvis.nvolv3.utils.Constants;

public class EventPersonnel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer sortOrder;

	private String personnelId;

	private Boolean isFeatured;

	private String questionnaireType = Constants.INHERIT;

	private String questionnaireId;

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

	private int version = 0;

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getPersonnelId() {
		return personnelId;
	}

	public void setPersonnelId(String personnelId) {
		this.personnelId = personnelId;
	}

	public Boolean isFeatured() {
		return isFeatured;
	}

	public void setFeatured(Boolean isFeatured) {
		this.isFeatured = isFeatured;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{ personnelId : " + personnelId + ", isFeatured : " + isFeatured + " }";
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
