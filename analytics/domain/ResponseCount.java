package org.iqvis.nvolv3.analytics.domain;

import java.io.Serializable;

public class ResponseCount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long count;

	private String activityId;

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.activityId.equals(((ResponseCount) obj).getActivityId());
	}

}
