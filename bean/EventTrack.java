package org.iqvis.nvolv3.bean;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.utils.Constants;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A simple POJO representing a EventTrack
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */
@Document(collection = "tracks")
public class EventTrack implements Serializable {

	private static final long serialVersionUID = -1527566248002296042L;

	private int version = 0;

	@NotEmpty(message = "Track id cannot be empty.")
	private String trackId;

	@NotNull(message = "sortOrder cannot be empty.")
	private Integer sortOrder;

	@NotEmpty(message = "Color code cannot be empty.")
	private String colorCode;

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

	public String getTrackId() {
		return trackId;
	}

	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
