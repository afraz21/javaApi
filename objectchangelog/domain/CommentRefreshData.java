package org.iqvis.nvolv3.objectchangelog.domain;

import java.io.Serializable;

public class CommentRefreshData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommentRefreshData(Object feedData, Object commentList) {
		super();
		this.feedData = feedData;
		this.commentList = commentList;
	}

	private Object feedData;

	private Object commentList;

	public Object getFeedData() {
		return feedData;
	}

	public void setFeedData(Object feedData) {
		this.feedData = feedData;
	}

	public Object getCommentList() {
		return commentList;
	}

	public void setCommentList(Object commentList) {
		this.commentList = commentList;
	}

}
