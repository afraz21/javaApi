package org.iqvis.nvolv3.objectchangelog.domain;

import java.io.Serializable;
import java.util.List;

public class ChangeTrackLog implements Serializable {

	public ChangeTrackLog(List<Object> insert, List<Object> update, List<Object> delete, List<Object> preCommand, List<Object> postCommand) {
		super();
		this.insert = insert;
		this.update = update;
		this.delete = delete;
		this.preCommand = preCommand;
		this.postCommand = postCommand;
	}

	public ChangeTrackLog() {
		super();
	}

	public ChangeTrackLog(List<Object> insert, List<Object> update, List<Object> delete) {
		super();
		this.insert = insert;
		this.update = update;
		this.delete = delete;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Object> insert;

	private List<Object> update;

	private List<Object> delete;

	private List<Object> preCommand;

	private List<Object> postCommand;

	public Object getInsert() {
		return insert == null || insert.size() == 0 ? null : insert;
	}

	public List<Object> getPreCommand() {
		return preCommand;
	}

	public void setPreCommand(List<Object> preCommand) {
		this.preCommand = preCommand;
	}

	public List<Object> getPostCommand() {
		return postCommand;
	}

	public void setPostCommand(List<Object> postCommand) {
		this.postCommand = postCommand;
	}

	public void setInsert(List<Object> insert) {
		this.insert = insert;
	}

	// public Object getCategories() {
	// return categories;
	// }
	//
	// public void setCategories(Object categories) {
	//
	// this.categories=categories;
	// }

	public List<Object> getUpdate() {
		return update == null || update.size() == 0 ? null : update;
	}

	public void setUpdate(List<Object> update) {
		if (update != null)
			update.remove(null);

		this.update = update;
	}

	public List<Object> getDelete() {
		return delete == null || delete.size() == 0 ? null : delete;
	}

	public void setDelete(List<Object> delete) {
		this.delete = delete;
	}

}
