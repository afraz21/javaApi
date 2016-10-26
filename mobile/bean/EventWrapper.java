package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;

public class EventWrapper implements Serializable {
	public EventWrapper(Object content) {
		super();
		this.content = content;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Object content;

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

}
