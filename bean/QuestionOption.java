package org.iqvis.nvolv3.bean;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author iqvis
 *
 */
public class QuestionOption implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;

	private String text;

	private Integer sortOrder;

	private Boolean isSelected;

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	@JsonIgnore
	public Boolean getIsSelected() {
		return isSelected;
	}

	@JsonProperty
	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
