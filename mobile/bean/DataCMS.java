package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.bean.MultiLingual;

public class DataCMS implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -702309313707532835L;

	@NotEmpty(message = "Name cannot be empty.")
	private String key;

	@NotEmpty(message = "Type cannot be empty.")
	private String type;

	@NotEmpty(message = "defaultText cannot be empty.")
	private String defaultText;

	private List<MultiLingual> multiLingual;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDefaultText() {
		return defaultText;
	}

	public void setDefaultText(String defaultText) {
		this.defaultText = defaultText;
	}

	public List<MultiLingual> getMultiLingual() {
		return multiLingual;
	}

	public void setMultiLingual(List<MultiLingual> multiLingual) {
		this.multiLingual = multiLingual;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
