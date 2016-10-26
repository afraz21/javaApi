package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

import org.iqvis.nvolv3.bean.MultiLingual;

public class LisTrack implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -519175388988527066L;

	private String id;

	private List<MultiLingual> multiLingual;

	private Picture picture;

	private String colorCode;

	private Integer sortOrder;

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<MultiLingual> getMultiLingual() {
		return multiLingual;
	}

	public void setMultiLingual(List<MultiLingual> multiLingual) {
		this.multiLingual = multiLingual;
	}

	public Picture getPicture() {
		return picture;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
