package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

import org.iqvis.nvolv3.bean.MultiLingual;

public class KeyValue implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5374873437459853620L;

	private String key;

	private List<MultiLingual> text;

	public List<MultiLingual> getText() {
		return text;
	}

	public void setText(List<MultiLingual> text) {
		this.text = text;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
