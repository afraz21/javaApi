package org.iqvis.nvolv3.analytics.domain;

import java.io.Serializable;

import org.iqvis.nvolv3.domain.Activity;

public class ActivityClass implements Serializable {

	public ActivityClass() {
	}

	public ActivityClass(Activity activity) {

		this.activityId = activity.getId();

		this.activityName = activity.getName();

		this.count = activity.getPopulerCount();
	}

	public ActivityClass(String activityName, Integer count, String activityId) {
		super();
		this.activityName = activityName;
		this.count = count;
		this.activityId = activityId;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String activityName;

	private long count;

	private String activityId;

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

}
