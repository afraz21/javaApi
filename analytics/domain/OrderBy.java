package org.iqvis.nvolv3.analytics.domain;

import java.io.Serializable;

public class OrderBy implements Serializable {

	public OrderBy() {
		super();
	}

	public OrderBy(String field, String dir) {
		super();
		this.field = field;
		this.dir = dir;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String field;

	private String dir;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

}
