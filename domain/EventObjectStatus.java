package org.iqvis.nvolv3.domain;

import java.io.Serializable;

public class EventObjectStatus implements Serializable {

	public EventObjectStatus(boolean resources, boolean personnel, boolean alerts, boolean sponsors, boolean campaigns, boolean tracks, boolean activities, boolean vendors, boolean venue) {
		super();
		this.resources = resources;
		this.personnel = personnel;
		this.alerts = alerts;
		this.sponsors = sponsors;
		this.campaigns = campaigns;
		this.tracks = tracks;
		this.activities = activities;
		this.vendors = vendors;
		this.venue = venue;
	}

	public EventObjectStatus() {
		super();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean resources;

	private boolean personnel;

	private boolean alerts;

	private boolean sponsors;

	private boolean campaigns;

	private boolean tracks;

	private boolean activities;

	private boolean vendors;

	private boolean venue;

	public boolean isResources() {
		return resources;
	}

	public void setResources(boolean resources) {
		this.resources = resources;
	}

	public boolean isPersonnel() {
		return personnel;
	}

	public void setPersonnel(boolean personnel) {
		this.personnel = personnel;
	}

	public boolean isAlerts() {
		return alerts;
	}

	public void setAlerts(boolean alerts) {
		this.alerts = alerts;
	}

	public boolean isSponsors() {
		return sponsors;
	}

	public void setSponsors(boolean sponsors) {
		this.sponsors = sponsors;
	}

	public boolean isCampaigns() {
		return campaigns;
	}

	public void setCampaigns(boolean campaigns) {
		this.campaigns = campaigns;
	}

	public boolean isTracks() {
		return tracks;
	}

	public void setTracks(boolean tracks) {
		this.tracks = tracks;
	}

	public boolean isActivities() {
		return activities;
	}

	public void setActivities(boolean activities) {
		this.activities = activities;
	}

	public boolean isVendors() {
		return vendors;
	}

	public void setVendors(boolean vendors) {
		this.vendors = vendors;
	}

	public boolean isVenue() {
		return venue;
	}

	public void setVenue(boolean venue) {
		this.venue = venue;
	}

}
