package org.iqvis.nvolv3.objectchangelog.domain;

import java.io.Serializable;

public class SyncDataObject implements Serializable {

	public SyncDataObject() {
	}

	public SyncDataObject(Object insert, Object update, Object delete) {

		this.insert = insert;

		this.update = update;

		this.delete = delete;

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Object insert;

	private Object update;

	private Object delete;

	public Object getInsert() {
		return insert;
	}

	public void setInsert(Object insert) {
		this.insert = insert;
	}

	public Object getUpdate() {
		return update;
	}

	public void setUpdate(Object update) {
		this.update = update;
	}

	public Object getDelete() {
		return delete;
	}

	public void setDelete(Object delete) {
		this.delete = delete;
	}

}
