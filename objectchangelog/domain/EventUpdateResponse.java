package org.iqvis.nvolv3.objectchangelog.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.iqvis.nvolv3.serializer.DateTimeJsonSerializer;
import org.iqvis.nvolv3.utils.Constants;
import org.joda.time.DateTime;

public class EventUpdateResponse implements Serializable {

	public EventUpdateResponse(String selected_language, Object event, ChangeTrackLog tracks, ChangeTrackLog sponsors, ChangeTrackLog vendors, ChangeTrackLog personnels, ChangeTrackLog activities, Object personnelCategories, Object sponsorCategories, Object vendorCategories, Object maps, Object resources, Object venue, Object eventCampaigns, Object eventFeeds, List<String> campaigns, Object eventConfiguration, Object alerts, Object questionniars, Object eventAttendees) {
		super();
		this.selected_language = selected_language;
		this.event = event;
		this.tracks = tracks;
		this.sponsors = sponsors;
		this.vendors = vendors;
		this.personnels = personnels;
		this.activities = activities;
		this.personnelCategories = personnelCategories;
		this.sponsorCategories = sponsorCategories;
		this.vendorCategories = vendorCategories;
		this.maps = maps;
		this.resources = resources;
		this.venue = venue;
		this.eventCampaigns = eventCampaigns;
		this.eventFeeds = eventFeeds;
		this.campaigns = campaigns;
		this.eventConfiguration = eventConfiguration;
		this.alerts = alerts;
		this.questionniars = questionniars;
		this.eventAttendees = eventAttendees;
		
		this.message="Sync Successfull";
		this.messageCode=Constants.SUCCESS_CODE;
	}

	public EventUpdateResponse(String selected_language, Object event, ChangeTrackLog tracks, ChangeTrackLog sponsors, ChangeTrackLog vendors, ChangeTrackLog personnels, ChangeTrackLog activities, Object personnelCategories, Object sponsorCategories, Object vendorCategories, Object maps, Object resources, Object venue, Object eventCampaigns, Object eventFeeds, List<String> campaign, Object eventConfiguration, Object alerts, Object questionniars) {
		super();
		this.selected_language = selected_language;
		this.event = event;
		this.tracks = tracks;
		this.sponsors = sponsors;
		this.vendors = vendors;
		this.personnels = personnels;
		this.activities = activities;
		this.personnelCategories = personnelCategories;
		this.sponsorCategories = sponsorCategories;
		this.vendorCategories = vendorCategories;
		this.maps = maps;
		this.resources = resources;
		this.venue = venue;
		this.eventCampaigns = eventCampaigns;
		this.eventFeeds = eventFeeds;
		this.campaigns = campaign;
		this.eventConfiguration = eventConfiguration;
		this.alerts = alerts;
		this.questionniars = questionniars;
		
		this.message="Sync Successfull";
		this.messageCode=Constants.SUCCESS_CODE;
	}

	public EventUpdateResponse(String selected_lang, Object event, ChangeTrackLog tracks, ChangeTrackLog sponsors, ChangeTrackLog vendors, ChangeTrackLog personnels, ChangeTrackLog activities, Object personnelCategories, Object sponsorCategories, Object vendorCategories, Object maps, Object resources, Object venue, Object eventCampaigns, Object eventFeeds, List<String> campaign, Object eventConfiguration, Object alerts) {
		super();
		this.selected_language = selected_lang;
		this.event = event;
		this.tracks = tracks;
		this.sponsors = sponsors;
		this.vendors = vendors;
		this.personnels = personnels;
		this.activities = activities;
		this.personnelCategories = personnelCategories;
		this.sponsorCategories = sponsorCategories;
		this.vendorCategories = vendorCategories;
		this.maps = maps;
		this.resources = resources;
		this.venue = venue;
		this.eventCampaigns = eventCampaigns;
		this.eventFeeds = eventFeeds;
		this.campaigns = campaign;
		this.eventConfiguration = eventConfiguration;
		this.alerts = alerts;
	}

	public EventUpdateResponse(Object event, ChangeTrackLog tracks, ChangeTrackLog sponsors, ChangeTrackLog vendors, ChangeTrackLog personnels, ChangeTrackLog activities, Object personnelCategories, Object sponsorCategories, Object vendorCategories, Object maps, Object resources, Object venues, Object eventCampaigns, Object eventFeeds, List<String> campaign, Object eventConfiguration, Object alerts) {
		super();
		this.event = event;
		this.tracks = tracks;
		this.sponsors = sponsors;
		this.vendors = vendors;
		this.personnels = personnels;
		this.activities = activities;
		this.personnelCategories = personnelCategories;
		this.sponsorCategories = sponsorCategories;
		this.vendorCategories = vendorCategories;
		this.maps = maps;
		this.resources = resources;
		this.venue = venues;
		this.eventCampaigns = eventCampaigns;
		this.eventFeeds = eventFeeds;
		this.campaigns = campaign;
		this.eventConfiguration = eventConfiguration;
		this.alerts = alerts;
	}

	public EventUpdateResponse(Object event, ChangeTrackLog tracks, ChangeTrackLog sponsors, ChangeTrackLog vendors, ChangeTrackLog personnels, ChangeTrackLog activities, Object personnelCategories, Object sponsorCategories, Object vendorCategories, Object maps, Object resources, Object venues, Object eventCampaigns, Object feeds, List<String> campaign, Object eventConfiguration) {
		super();
		this.event = event;
		this.tracks = tracks;
		this.sponsors = sponsors;
		this.vendors = vendors;
		this.personnels = personnels;
		this.activities = activities;
		this.personnelCategories = personnelCategories;
		this.sponsorCategories = sponsorCategories;
		this.vendorCategories = vendorCategories;
		this.maps = maps;
		this.resources = resources;
		this.venue = venues;
		this.eventCampaigns = eventCampaigns;
		this.eventFeeds = feeds;
		this.campaigns = campaign;
		this.eventConfiguration = eventConfiguration;
		
		this.message="Sync Successfull";
		this.messageCode=Constants.SUCCESS_CODE;
	}

	public EventUpdateResponse(Object event, ChangeTrackLog tracks, ChangeTrackLog sponsors, ChangeTrackLog vendors, ChangeTrackLog personnels, ChangeTrackLog activities, Object personnelCategories, Object sponsorCategories, Object vendorCategories, Object maps, Object resources, Object venues, Object eventCampaigns, Object feeds, List<String> campaign) {
		super();
		this.event = event;
		this.tracks = tracks;
		this.sponsors = sponsors;
		this.vendors = vendors;
		this.personnels = personnels;
		this.activities = activities;
		this.personnelCategories = personnelCategories;
		this.sponsorCategories = sponsorCategories;
		this.vendorCategories = vendorCategories;
		this.maps = maps;
		this.resources = resources;
		this.venue = venues;
		this.eventCampaigns = eventCampaigns;
		this.eventFeeds = feeds;
		this.campaigns = campaign;
	}

	public EventUpdateResponse(Object event, ChangeTrackLog tracks, ChangeTrackLog sponsors, ChangeTrackLog vendors, ChangeTrackLog personnels, ChangeTrackLog activities, Object personnelCategories, Object sponsorCategories, Object vendorCategories, Object maps, Object resources, Object venues, Object campaignParticipents, Object feeds) {
		super();
		this.event = event;
		this.tracks = tracks;
		this.sponsors = sponsors;
		this.vendors = vendors;
		this.personnels = personnels;
		this.activities = activities;
		this.personnelCategories = personnelCategories;
		this.sponsorCategories = sponsorCategories;
		this.vendorCategories = vendorCategories;
		this.maps = maps;
		this.resources = resources;
		this.venue = venues;
		this.eventCampaigns = campaignParticipents;
		this.eventFeeds = feeds;
		
		this.message="Sync Successfull";
		this.messageCode=Constants.SUCCESS_CODE;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String selected_language;

	private Object event;

	private ChangeTrackLog tracks;

	private ChangeTrackLog sponsors;

	private ChangeTrackLog vendors;

	private ChangeTrackLog personnels;

	private ChangeTrackLog activities;

	private Object personnelCategories;

	private Object sponsorCategories;

	private Object vendorCategories;

	private Object maps;

	private Object resources;

	private Object venue;

	private Object eventCampaigns;

	private Object eventFeeds;

	private List<String> campaigns;

	private Object eventConfiguration;

	private Object alerts;

	private Object questionniars;
	
	private Object eventAttendees;
	
	private String message;
	
	private String messageCode;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public Object getEventAttendees() {
		return eventAttendees;
	}

	public void setEventAttendees(Object eventAttendees) {
		this.eventAttendees = eventAttendees;
	}

	public Object getQuestionniars() {
		return questionniars;
	}

	public void setQuestionniars(Object questionniars) {
		this.questionniars = questionniars;
	}

	public String getSelected_language() {
		return selected_language;
	}

	public void setSelected_language(String selected_lang) {
		this.selected_language = selected_lang;
	}

	public Object getAlerts() {
		return alerts;
	}

	public void setAlerts(Object alerts) {
		this.alerts = alerts;
	}

	public Object getEventConfiguration() {
		return eventConfiguration;
	}

	public void setEventConfiguration(Object eventConfiguration) {
		this.eventConfiguration = eventConfiguration;
	}

	public List<String> getCampaigns() {
		return campaigns;
	}

	public void setCampaigns(List<String> campaigns) {
		this.campaigns = campaigns;
	}

	@SuppressWarnings("static-access")
	@JsonProperty
	@JsonSerialize(using = DateTimeJsonSerializer.class)
	public DateTime getSyncTime() {
		return new DateTime().now();
	}

	@JsonIgnore
	public Object getFeeds() {
		return eventFeeds;
	}

	public Object getEventFeeds() {
		return eventFeeds;
	}

	@SuppressWarnings("unchecked")
	public Object getLocations() {

		ChangeDataTrackLogVenue venueTemp = (ChangeDataTrackLogVenue) getVenue();

		Object locations = null;

		List<Object> preComm = null;

		List<Object> postComm = null;

		if (venueTemp != null) {

			locations = venueTemp.getLocations();
		}
		if (locations != null) {
			preComm = new ArrayList<Object>();

			preComm.add("DELETE_ALL");
		}

		return new ChangeTrackLog(null, (List<Object>) locations, null, preComm, postComm);
	}

	public void setEventFeeds(Object eventFeeds) {
		this.eventFeeds = eventFeeds;
	}

	public void setFeeds(Object feeds) {
		this.eventFeeds = feeds;
	}

	public Object getEventCampaigns() {
		return eventCampaigns;
	}

	public void setEventCampaigns(Object eventCampaigns) {
		this.eventCampaigns = eventCampaigns;
	}

	public Object getVenue() {
		return venue;
	}

	public void setVenues(Object venues) {
		this.venue = venues;
	}

	public Object getMaps() {
		return maps;
	}

	public void setMaps(Object maps) {
		this.maps = maps;
	}

	public Object getResources() {
		return resources;
	}

	public void setResources(Object resources) {
		this.resources = resources;
	}

	public Object getPersonnelCategories() {
		return personnelCategories;
	}

	public void setPersonnelCategories(Object personnelCategories) {
		this.personnelCategories = personnelCategories;
	}

	public Object getSponsorCategories() {
		return sponsorCategories;
	}

	public void setSponsorCategories(Object sponsorCategories) {
		this.sponsorCategories = sponsorCategories;
	}

	public Object getVendorCategories() {
		return vendorCategories;
	}

	public void setVendorCategories(Object vendorCategories) {
		this.vendorCategories = vendorCategories;
	}

	public ChangeTrackLog getSponsors() {
		return sponsors;
	}

	public ChangeTrackLog getActivities() {
		return activities;
	}

	public void setActivities(ChangeTrackLog activities) {
		this.activities = activities;
	}

	public void setSponsors(ChangeTrackLog sponsors) {
		this.sponsors = sponsors;
	}

	public Object getEvent() {
		return event;
	}

	public void setEvent(Object event) {
		this.event = event;
	}

	public EventUpdateResponse(Object update) {
		super();
		this.event = update;
		
		if(update==null){
			this.message="Sync fail";
			this.messageCode=Constants.ERROR_CODE;
		}else{
			this.message="Sync Seccessfull";
			this.messageCode=Constants.SUCCESS_CODE;
		}
		
	}

	public EventUpdateResponse(Object update, ChangeTrackLog tracks, ChangeTrackLog sponsor) {
		super();
		this.event = update;
		this.tracks = tracks;
		this.sponsors = sponsor;
	}

	public EventUpdateResponse(Object event, ChangeTrackLog tracks, ChangeTrackLog sponsors, ChangeTrackLog vendors) {
		super();
		this.event = event;
		this.tracks = tracks;
		this.sponsors = sponsors;
		this.vendors = vendors;
	}

	public EventUpdateResponse(Object event, ChangeTrackLog tracks, ChangeTrackLog sponsors, ChangeTrackLog vendors, ChangeTrackLog personnels) {
		super();
		this.event = event;
		this.tracks = tracks;
		this.sponsors = sponsors;
		this.vendors = vendors;
		this.personnels = personnels;
	}

	public ChangeTrackLog getPersonnels() {
		return personnels;
	}

	public void setPersonnels(ChangeTrackLog personnels) {
		this.personnels = personnels;
	}

	public ChangeTrackLog getVendors() {
		return vendors;
	}

	public void setVendors(ChangeTrackLog vendors) {
		this.vendors = vendors;
	}

	public ChangeTrackLog getTracks() {
		return tracks;
	}

	public void setTracks(ChangeTrackLog tracks) {
		this.tracks = tracks;
	}

}
