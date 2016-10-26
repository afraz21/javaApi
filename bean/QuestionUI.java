package org.iqvis.nvolv3.bean;

import java.io.Serializable;

public class QuestionUI implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer sortOrder;

	private String uiControl;

	private Integer pageNumber;

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getUiControl() {
		return uiControl;
	}

	public void setUiControl(String uiControl) {
		this.uiControl = uiControl;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

}
