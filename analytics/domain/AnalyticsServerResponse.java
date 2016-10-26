package org.iqvis.nvolv3.analytics.domain;

import java.io.Serializable;
import java.util.List;

public class AnalyticsServerResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer httpCode;

	private String message;

	private List<Object> contents;

	private Object aggregate;

	public Integer getHttpCode() {
		return httpCode;
	}

	public void setHttpCode(Integer httpCode) {
		this.httpCode = httpCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Object> getContents() {
		return contents;
	}

	public void setContents(List<Object> contents) {
		this.contents = contents;
	}

	public Object getAggregate() {
		return aggregate;
	}

	public void setAggregate(Object aggregate) {
		this.aggregate = aggregate;
	}
}
