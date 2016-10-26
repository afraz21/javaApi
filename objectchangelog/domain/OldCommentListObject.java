package org.iqvis.nvolv3.objectchangelog.domain;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.iqvis.nvolv3.serializer.FeeDateTimeJsonDeSerializer;
import org.joda.time.DateTime;

public class OldCommentListObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String commentId;

	@JsonDeserialize(using = FeeDateTimeJsonDeSerializer.class)
	private DateTime creationDate;

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public DateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(DateTime creationDate) {
		this.creationDate = creationDate;
	}

	public int compareTo(OldCommentListObject o) {
		// TODO Auto-generated method stub
		return this.creationDate.compareTo(o.getCreationDate()) > 0 ? -1 : 1;
	}

}
