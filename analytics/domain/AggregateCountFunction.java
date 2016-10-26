package org.iqvis.nvolv3.analytics.domain;

import java.io.Serializable;

public class AggregateCountFunction implements Serializable {

	public AggregateCountFunction(Object count) {
		super();
		this.count = count;
	}

	public Object getCount() {
		return count;
	}

	public void setCount(Object count) {
		this.count = count;
	}

	private static final long serialVersionUID = 1L;

	private Object count;

}
