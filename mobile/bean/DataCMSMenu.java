package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.bean.MultiLingual;

public class DataCMSMenu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -702309313707532835L;

	@NotEmpty(message = "Name cannot be empty.")
	private String key;

	@NotEmpty(message = "defaultText cannot be empty.")
	private String defaultText;

	private String url;

	private List<MultiLingual> multiLingual;

	public String getKey() {
		return key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setKey(String key) {
		this.key = key;
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
