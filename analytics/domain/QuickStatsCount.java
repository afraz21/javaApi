package org.iqvis.nvolv3.analytics.domain;

import java.io.Serializable;

public class QuickStatsCount implements Serializable {

	public QuickStatsCount() {
		super();
	}

	public QuickStatsCount(Long event_data_downloads_count, Long event_visits_count, Long activities_in_schedule_count, Long alerts_views_count, Long feeds_activities_count, Long activities_feedback_collection_count) {
		super();
		this.event_data_downloads_count = event_data_downloads_count;
		this.event_visits_count = event_visits_count;
		this.activities_in_schedule_count = activities_in_schedule_count;
		this.alerts_views_count = alerts_views_count;
		this.feeds_activities_count = feeds_activities_count;
		this.activities_feedback_collection_count = activities_feedback_collection_count;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long event_data_downloads_count;

	private Long event_visits_count;

	private Long activities_in_schedule_count;

	private Long alerts_views_count;

	private Long feeds_activities_count;

	private Long activities_feedback_collection_count;
	
	private Long event_site_visits_count;

	public Long getEvent_site_visits_count() {
		return event_site_visits_count;
	}

	public void setEvent_site_visits_count(Long event_site_visits_count) {
		this.event_site_visits_count = event_site_visits_count;
	}

	public Long getEvent_data_downloads_count() {
		return event_data_downloads_count;
	}

	public void setEvent_data_downloads_count(Long event_data_downloads_count) {
		this.event_data_downloads_count = event_data_downloads_count;
	}

	public Long getEvent_visits_count() {
		return event_visits_count;
	}

	public void setEvent_visits_count(Long event_visits_count) {
		this.event_visits_count = event_visits_count;
	}

	public Long getActivities_in_schedule_count() {
		return activities_in_schedule_count;
	}

	public void setActivities_in_schedule_count(Long activities_in_schedule_count) {
		this.activities_in_schedule_count = activities_in_schedule_count;
	}

	public Long getAlerts_views_count() {
		return alerts_views_count;
	}

	public void setAlerts_views_count(Long alerts_views_count) {
		this.alerts_views_count = alerts_views_count;
	}

	public Long getFeeds_activities_count() {
		return feeds_activities_count;
	}

	public void setFeeds_activities_count(Long feeds_activities_count) {
		this.feeds_activities_count = feeds_activities_count;
	}

	public Long getActivities_feedback_collection_count() {
		return activities_feedback_collection_count;
	}

	public void setActivities_feedback_collection_count(Long activities_feedback_collection_count) {
		this.activities_feedback_collection_count = activities_feedback_collection_count;
	}

}
