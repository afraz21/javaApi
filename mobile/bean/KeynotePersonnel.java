package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

import org.iqvis.nvolv3.bean.MultiLingualPersonnelInformation;

public class KeynotePersonnel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -519175388988527066L;

	private String id;

	private Picture picture;

	private List<MultiLingualPersonnelInformation> multiLingual;

	private Boolean keynote;

	private Integer sortOrder;

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Boolean getKeynote() {
		return keynote;
	}

	public void setKeynote(Boolean keynote) {
		this.keynote = keynote;
	}

	public List<MultiLingualPersonnelInformation> getMultiLingual() {
		return multiLingual;
	}

	public void setMultiLingual(List<MultiLingualPersonnelInformation> multiLingual) {
		this.multiLingual = multiLingual;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Picture getPicture() {
		return picture;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
