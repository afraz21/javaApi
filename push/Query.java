package org.iqvis.nvolv3.push;

import java.io.Serializable;

public class Query implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Where where;

	private Object data;

	public Where getWhere() {
		return where;
	}

	public void setWhere(Where where) {
		this.where = where;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
