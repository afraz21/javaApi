package org.iqvis.nvolv3.domain;

import java.io.Serializable;

public class FeedBackAnswerCount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private AnswerObject _id;

	private Integer count;

	public AnswerObject get_id() {
		return _id;
	}

	public void set_id(AnswerObject _id) {
		this._id = _id;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
