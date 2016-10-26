package org.iqvis.nvolv3.bean;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A simple POJO representing a MultiLingual
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */
@Document
public class MultiLingual implements Serializable {

	private static final long serialVersionUID = -4527566248002296042L;

	private String languageCode;

	private String title;

	private String descriptionShort;

	private String descriptionLong;

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescriptionShort() {
		return descriptionShort;
	}

	public void setDescriptionShort(String descriptionShort) {
		this.descriptionShort = descriptionShort;
	}

	public String getDescriptionLong() {
		return descriptionLong;
	}

	public void setDescriptionLong(String descriptionLong) {
		this.descriptionLong = descriptionLong;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}

		MultiLingual guest = (MultiLingual) obj;

		return languageCode.equalsIgnoreCase(guest.languageCode);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((languageCode == null) ? 0 : languageCode.hashCode());
		return result;

	}
}
