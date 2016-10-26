package org.iqvis.nvolv3.domain;

import java.io.Serializable;

import org.iqvis.nvolv3.audit.Audit;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A simple POJO representing a Business Category
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

@Document
public class BusinessCategory extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

	private String summary;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

}