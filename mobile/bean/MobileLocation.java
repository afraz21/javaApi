package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.domain.Location;

public class MobileLocation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private List<MultiLingual> multilingual;

	public MobileLocation(Location l) {
		if (l != null) {
			this.id = l.getId();
			this.multilingual = l.getMultiLingual();
		}
	}

	public MobileLocation() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<MultiLingual> getMultilingual() {
		return multilingual;
	}

	public void setMultilingual(List<MultiLingual> multilingual) {
		this.multilingual = multilingual;
	}

}
