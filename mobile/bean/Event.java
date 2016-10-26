package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.iqvis.nvolv3.audit.Audit;
import org.iqvis.nvolv3.bean.Data;
import org.iqvis.nvolv3.bean.EventPersonnel;
import org.iqvis.nvolv3.bean.FeedbackConfiguration;
import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.domain.Comment;
import org.iqvis.nvolv3.domain.EventAlert;
import org.iqvis.nvolv3.domain.EventCampaign;
import org.iqvis.nvolv3.domain.EventCampaignParticipant;
import org.iqvis.nvolv3.domain.Feed;
import org.iqvis.nvolv3.domain.Location;
import org.iqvis.nvolv3.domain.Media;
import org.iqvis.nvolv3.domain.Personnel;
import org.iqvis.nvolv3.domain.ReferenceData;
import org.iqvis.nvolv3.domain.UserFeedbackQuestion;
import org.iqvis.nvolv3.domain.Venue;
import org.iqvis.nvolv3.objectchangelog.domain.MobileReferenceData;
import org.iqvis.nvolv3.serializer.DateTimeJsonSerializer;
import org.iqvis.nvolv3.utils.Constants;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * A simple POJO representing a Event
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public class Event extends Audit implements Serializable {

	private static final long serialVersionUID = -5527566248002296042L;

	private String name;

	private List<DateTime> eventDates = null;

	private DateTime startDate;

	private DateTime eventStartDateGMT;

	private DateTime endDate;

	private DateTime eventEndDateGMT;

	private String organizerId;

	private String eventTimeZone;

	private Boolean isDeleted = false;

	private Boolean isLive = false;

	private Boolean isDownloadable = false;

	private Boolean isActive = false;

	private List<MultiLingual> multiLingual;

	private List<EventTrack> tracks;

	private List<EventSponsor> sponsors;

	private List<EventVendor> vendors;

	private List<EventAlert> eventAlerts;

	private List<String> testOrganizers = new ArrayList<String>();

	private List<Personnel> personnels;

	private Boolean hasExpo = false;

	private Boolean showFeed = false;

	private Boolean isfeedModerated = false;

	private Boolean isOverlayCampaignActive = false;

	private Venue venue;

	private Object eventConfiguration;

	private String selected_language;

	private List<UserFeedbackQuestion> questionniars;

	private FeedbackConfiguration feedback_configuration;

	private List<MobileAttendee> eventAttendees;

	private List<String> eventHashTag;

	public List<String> getEventHashTag() {

		return eventHashTag == null ? new ArrayList<String>() : eventHashTag;
	}

	public void setEventHashTag(List<String> eventHashTag) {
		this.eventHashTag = eventHashTag;
	}

	@JsonIgnore
	public List<MobileAttendee> getEventAttendees() {
		return eventAttendees;
	}

	public void setEventAttendees(List<MobileAttendee> attendeeList) {
		this.eventAttendees = attendeeList;
	}

	public FeedbackConfiguration getFeedback_configuration() {
		return feedback_configuration;
	}

	public void setFeedback_configuration(FeedbackConfiguration feedback_configuration) {
		this.feedback_configuration = feedback_configuration;
	}

	public List<UserFeedbackQuestion> getQuestionniars() {
		return questionniars;
	}

	public void setQuestionniars(List<UserFeedbackQuestion> questionnairs) {
		this.questionniars = questionnairs;
	}

	public String getSelected_language() {
		return selected_language;
	}

	public void setSelected_language(String selected_language) {
		this.selected_language = selected_language;
	}

	@JsonProperty
	public Object getEventConfiguration() {
		return eventConfiguration;
	}

	public void setEventConfiguration(Object eventConfig) {
		this.eventConfiguration = eventConfig;
	}

	@SuppressWarnings("unused")
	private List<String> campaigns;

	@DBRef
	private Media picture;

	@DBRef
	private Media banner;

	ReferenceData personnelTypes;

	List<EventCampaign> eventCampaigns;

	List<Feed> eventFeeds;

	private List<Activity> orpActivities = new ArrayList<Activity>();

	public void setOrpActivities(List<Activity> orpActivities) {
		this.orpActivities = orpActivities;
	}

	private List<MobileEventAlert> alerts;

	public List<MobileEventAlert> getAlerts() {
		return alerts;
	}

	public void setAlerts(List<MobileEventAlert> alerts) {
		this.alerts = alerts;
	}

	@SuppressWarnings("unused")
	private List<Activity> activities;

	private List<MobileEventResource> maps;

	private List<MobileEventResource> resources;

	private List<EventPersonnel> eventPersonnelList;

	@JsonIgnore
	public List<EventPersonnel> getEventPersonnelList() {
		return eventPersonnelList;
	}

	public void setEventPersonnelList(List<EventPersonnel> eventPersonnelList) {
		this.eventPersonnelList = eventPersonnelList;
	}

	public List<MobileEventResource> getMaps() {
		return maps;
	}

	public void setMaps(List<MobileEventResource> maps) {
		this.maps = maps;
	}

	public List<MobileEventResource> getResources() {
		return resources;
	}

	public void setResources(List<MobileEventResource> resources) {
		this.resources = resources;
	}

	public List<String> getCampaigns() {

		@SuppressWarnings("unused")
		List<MobileEventCampaign> temp = new ArrayList<MobileEventCampaign>();

		List<String> listCampaign = new ArrayList<String>();

		if (eventCampaigns != null) {
			for (EventCampaign campaign : eventCampaigns) {

				listCampaign.add(campaign.getCampaignType());

			}
		}

		return listCampaign;
	}

	public void setCampaigns(List<String> campaigns) {
		this.campaigns = campaigns;
	}

	public List<Activity> getActivities() {
		List<Activity> temp = new ArrayList<Activity>();

		temp.addAll(orpActivities);

		return temp;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getEventDates() {

		List<String> eventDatesList = new ArrayList<String>();

		if (eventDates != null) {

			for (DateTime dateTime : eventDates) {

				eventDatesList.add(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").print(dateTime));

			}

		}
		return eventDatesList;
	}

	public void setEventDates(List<DateTime> eventDates) {
		this.eventDates = eventDates;
	}

	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
	}

	@JsonIgnore
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@JsonIgnore
	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public List<MultiLingual> getMultiLingual() {
		return multiLingual;
	}

	public void setMultiLingual(List<MultiLingual> multiLingual) {
		this.multiLingual = multiLingual;
	}

	public List<EventTrack> getTracks() {
		return tracks;
	}

	public void setTracks(List<EventTrack> tracks) {
		this.tracks = tracks;
	}

	public EventVenue getVenue() {

		if (venue == null) {
			return null;
		}

		return new EventVenue(venue, "EN", "EN");
	}

	@JsonProperty
	public List<MobileLocation> getLocations() {

		if (venue != null && venue.getLocations() != null) {

			List<Location> list = venue.getLocations();

			List<MobileLocation> temp = new ArrayList<MobileLocation>();

			if (list != null) {

				for (Location location : list) {

					temp.add(new MobileLocation(location));

				}
			}

			return temp;

		}

		return null;

	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}

	@JsonIgnore
	public Media getPicture() {
		return picture;
	}

	@JsonProperty
	public Picture getLogo() {
		return new Picture(this.picture);
	}

	public void setPicture(Media picture) {
		this.picture = picture;
	}

	public String getEventTimeZone() {
		return eventTimeZone;
	}

	public void setEventTimeZone(String eventTimeZone) {
		this.eventTimeZone = eventTimeZone;
	}

	@JsonIgnore
	public Boolean getIsLive() {
		return isLive;
	}

	public void setIsLive(Boolean isLive) {
		this.isLive = isLive;
	}

	public Boolean getIsDownloadable() {
		return isDownloadable;
	}

	public void setIsDownloadable(Boolean isDownloadable) {
		this.isDownloadable = isDownloadable;
	}

	@JsonIgnore
	public List<String> getTestOrganizers() {
		return testOrganizers;
	}

	public void setTestOrganizers(List<String> testOrganizers) {
		this.testOrganizers = testOrganizers;
	}

	public List<PersonnelResponse> getPersonnels() {

		List<PersonnelResponse> temp = new ArrayList<PersonnelResponse>();

		if (personnels != null) {
			for (Personnel r : personnels) {
				temp.add(new PersonnelResponse(r, this.eventPersonnelList));
			}

		}
		return temp;
	}

	public void setPersonnels(List<Personnel> personnels) {
		this.personnels = personnels;
	}

	public Picture getBanner() {
		return new Picture(banner);
	}

	public void setBanner(Media banner) {
		this.banner = banner;
	}

	@JsonIgnore
	public DateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}

	public DateTime getEventStartDateGMT() {
		return eventStartDateGMT;
	}

	public void setEventStartDateGMT(DateTime eventStartDateGMT) {
		this.eventStartDateGMT = eventStartDateGMT;
	}

	@JsonIgnore
	public DateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}

	public DateTime getEventEndDateGMT() {
		return eventEndDateGMT;
	}

	public void setEventEndDateGMT(DateTime eventEndDateGMT) {
		this.eventEndDateGMT = eventEndDateGMT;
	}

	@JsonIgnore
	public Boolean getHasExpo() {
		return hasExpo;
	}

	public void setHasExpo(Boolean hasExpo) {
		this.hasExpo = hasExpo;
	}

	@JsonIgnore
	public Boolean getShowFeed() {
		return showFeed;
	}

	public void setShowFeed(Boolean showFeed) {
		this.showFeed = showFeed;
	}

	@JsonIgnore
	public Boolean getIsfeedModerated() {
		return isfeedModerated;
	}

	public void setIsfeedModerated(Boolean isfeedModerated) {
		this.isfeedModerated = isfeedModerated;
	}

	@JsonIgnore
	public Boolean getIsOverlayCampaignActive() {
		return isOverlayCampaignActive;
	}

	public void setIsOverlayCampaignActive(Boolean isOverlayCampaignActive) {
		this.isOverlayCampaignActive = isOverlayCampaignActive;
	}

	public List<EventSponsor> getSponsors() {
		return sponsors;
	}

	public void setSponsors(List<EventSponsor> sponsors) {
		this.sponsors = sponsors;
	}

	public List<EventVendor> getVendors() {
		return vendors;
	}

	public void setVendors(List<EventVendor> vendors) {
		this.vendors = vendors;
	}

	@JsonIgnore
	public List<EventAlert> getEventAlerts() {
		return eventAlerts;
	}

	public void setEventAlerts(List<EventAlert> eventAlerts) {
		this.eventAlerts = eventAlerts;
	}

	public List<MobileReferenceData> getPersonnelTypes() {

		List<MobileReferenceData> temp = new ArrayList<MobileReferenceData>();

		if (personnelTypes != null && personnelTypes.getData() != null) {

			for (Data d : personnelTypes.getData()) {
				MobileReferenceData t = new MobileReferenceData();

				t.setId(d.getId());

				t.setMultilingual(d.getMultiLingual());

				t.setSortOrder(d.getSortOrder());

				temp.add(t);

			}

		}

		return temp;
	}

	public void setPersonnelTypes(ReferenceData personnelTypes) {
		this.personnelTypes = personnelTypes;
	}

	public void setEventCampaigns(List<EventCampaign> eventCampaigns) {
		this.eventCampaigns = eventCampaigns;
	}

	@SuppressWarnings("static-access")
	public MobileEventFeedListObject getEventFeeds() {
		int i = 0, j = 0;

		if (eventFeeds == null)
			return null;

		List<MobileEventFeed> list = new ArrayList<MobileEventFeed>();

		if (eventFeeds.size() == 0) {

			return null;

		}

		for (Feed feed : eventFeeds) {

			MobileEventFeed tempFeed = new MobileEventFeed();

			if (i > 10)
				break;

			tempFeed.setEventId(feed.getEventId());

			tempFeed.setCommentsCount(feed.getCommentCount());

			tempFeed.setCreatedDate(feed.getCreatedDate());

			tempFeed.setId(feed.getId());

			tempFeed.setLastModifiedDate(feed.getLastModifiedDate());

			tempFeed.setLikes(feed.getLikes());

			tempFeed.setSource(feed.getSource());

			tempFeed.setAttendeeId(feed.getAttendeeId());

			tempFeed.setDp(feed.getDp());

			if (feed != null) {
				tempFeed.setLikes(feed.getLikes());
			}

			if (feed.getPicture() != null) {

				// tempFeed.setLogo(new Picture(feed.getPicture()));
				tempFeed.setLogo(new Picture(feed.getPictureO()));

			}
			tempFeed.setText(feed.getDescription());

			tempFeed.setType(feed.getType());

			tempFeed.setTypeId(feed.getTypeId());

			tempFeed.setCreatedByEmail(feed.getCreatedByEmail());

			tempFeed.setCreatedByname(feed.getCreatedByname());

			List<MobileEventFeedComment> comments = new ArrayList<MobileEventFeedComment>();

			if (feed.getComments() != null) {

				j = 0;

				for (Comment comment : feed.getComments()) {

					MobileEventFeedComment com = new MobileEventFeedComment();

					if (j > 10)
						break;

					com.setCreatedByname(comment.getCreatedByname());

					com.setCreatedDate(comment.getCreatedDate());

					com.setId(comment.getId());

					com.setText(comment.getText());
					
					com.setDp(comment.getDp());
					
					com.setAttendeeId(comment.getAttendeeId());

					comments.add(com);

					j = j + 1;

				}

			}

			tempFeed.setComments(comments);

			if (feed.isPrepared() && feed.getIsActive() && Constants.FEED_STATUS_APPROVED.equals(feed.getFeedStatus())) {

				list.add(tempFeed);

				i = i + 1;
			}
		}

		MobileEventFeedListObject feedsObject = new MobileEventFeedListObject();

		feedsObject.setCurrentTime(new DateTime().now());

		feedsObject.setFeeds(list);

		return feedsObject;
	}

	public void setEventFeeds(List<Feed> eventFeeds) {
		this.eventFeeds = eventFeeds;
	}

	@SuppressWarnings("static-access")
	@JsonProperty
	@JsonSerialize(using = DateTimeJsonSerializer.class)
	public DateTime getSyncTime() {

		return new DateTime().now();
	}

	@JsonProperty
	public List<MobileReferenceData> getSponsorCategories() {
		List<MobileReferenceData> temp = new ArrayList<MobileReferenceData>();

		List<String> catIds = new ArrayList<String>();

		if (sponsors != null) {
			for (EventSponsor eventSponsor : sponsors) {

				MobileReferenceData t = new MobileReferenceData();

				if (eventSponsor.getSponsorCategory() != null) {

					t.setId(eventSponsor.getSponsorCategory().getId());

					t.setMultilingual(eventSponsor.getSponsorCategory().getMultiLingual());

					t.setSortOrder(eventSponsor.getSponsorCategory().getSortOrder());

					if (!catIds.contains(eventSponsor.getSponsorCategory().getId())) {

						catIds.add(eventSponsor.getSponsorCategory().getId());

						temp.add(t);

					}
				}
			}
		}

		return temp;
	}

	public List<MobileReferenceData> getVendorCategories() {

		List<MobileReferenceData> temp = new ArrayList<MobileReferenceData>();

		List<String> catIds = new ArrayList<String>();

		if (vendors != null) {
			for (EventVendor eventSp : vendors) {
				MobileReferenceData t = new MobileReferenceData();

				if (eventSp.getVendorCategory() != null) {

					t.setId(eventSp.getVendorCategory().getId());

					t.setMultilingual(eventSp.getVendorCategory().getMultiLingual());

					t.setSortOrder(eventSp.getVendorCategory().getSortOrder());

					if (!catIds.contains(eventSp.getVendorCategory().getId())) {

						catIds.add(eventSp.getVendorCategory().getId());

						temp.add(t);
					}
				}
			}
		}

		return temp;
	}

	public List<MobileEventCampaign> getEventCampaigns() {

		List<MobileEventCampaign> mec = new ArrayList<MobileEventCampaign>();

		for (EventCampaign campaign : eventCampaigns) {

			// TODO Event Campaign Loop

			MobileEventCampaign tempCamp = new MobileEventCampaign();

			List<MobileEventCampaignParticipants> tempPartList = new ArrayList<MobileEventCampaignParticipants>();

			tempCamp.setName(campaign.getCampaignType());

			if (campaign.getParticipants() != null) {

				for (EventCampaignParticipant tempPart : campaign.getParticipants()) {

					// TODO Event Campaign Participant Loop

					System.out.println("Participant Name : " + tempPart.getName());

					if (!tempPart.isPrepared()) {

						continue;
					}

					if (tempPart.getIs_end() == true) {

						continue;
					}

					if (tempPart.getIs_start() == null) {

						tempPart.setIs_start(false);
					}

					if (tempPart.getIs_end() == null) {

						tempPart.setIs_end(false);
					}

					if (tempPart.getIsActive() == true && tempPart.getIsDeleted() == false) {

						MobileEventCampaignParticipants t = new MobileEventCampaignParticipants();

						t.setSortOrder(tempPart.getSortOrder());

						t.setCampaignId(tempPart.getCampaignId());

						t.setLogo(new Picture(tempPart.getPictureO()));

						t.setMultiLingual(tempPart.getMultiLingual());

						t.setOverlayLandscape(new Picture(tempPart.getOverlayLandscapeO()));

						t.setOverlayPotrait(new Picture(tempPart.getOverlayPotraitO()));

						t.setParticipantId(tempPart.getId());

						t.setSponsorId(tempPart.getSponsors());

						tempPart.setIs_start(true);

						tempPartList.add(t);
					}

				}

				tempCamp.setParticipants(tempPartList);

			}
			else {

				tempCamp.setParticipants(new ArrayList<MobileEventCampaignParticipants>());
			}

			mec.add(tempCamp);
		}

		return mec;
	}

	@JsonIgnore
	public List<MobileEventCampaign> getEventCampaigns1() {

		List<MobileEventCampaign> temp = new ArrayList<MobileEventCampaign>();

		List<String> listCampaign = new ArrayList<String>();

		/*
		 * Current Date Time Start
		 */

		// System.out.println("--------------------------------------------------------------------------------------------");

		if (listCampaign != null) {

			// System.out.println("Event ID " + getId() +
			// " | EventCampaign List Size : " + eventCampaigns.size());

			for (EventCampaign campaign : eventCampaigns) {

				boolean psFlag = false;

				try {

					List<MobileEventCampaignParticipants> tempPartList = new ArrayList<MobileEventCampaignParticipants>();

					MobileEventCampaign tempCamp = new MobileEventCampaign();

					if (campaign.getIsActive() == true && campaign.getIsDeleted() == false) {

						tempCamp.setName(campaign.getCampaignType());

						listCampaign.add(campaign.getCampaignType());

						if (campaign.getParticipants() != null) {

							for (EventCampaignParticipant tempPart : campaign.getParticipants()) {

								// System.out.println("Participant Name : " +
								// tempPart.getName());

								psFlag = false;

								if (tempPart.getIs_start() == null) {

									tempPart.setIs_start(false);
								}

								if (tempPart.getIs_end() == null) {

									tempPart.setIs_end(false);
								}

								if (tempPart.getIsActive() == true && tempPart.getIsDeleted() == false) {

									MobileEventCampaignParticipants t = new MobileEventCampaignParticipants();

									if (tempPart.getIs_start() == true && tempPart.getIs_end() == false) {

										psFlag = true;
									}

									if (psFlag == true) {

										t.setSortOrder(tempPart.getSortOrder());

										t.setCampaignId(tempPart.getCampaignId());

										t.setLogo(new Picture(tempPart.getPictureO()));

										t.setMultiLingual(tempPart.getMultiLingual());

										t.setOverlayLandscape(new Picture(tempPart.getOverlayLandscapeO()));

										t.setOverlayPotrait(new Picture(tempPart.getOverlayPotraitO()));

										t.setParticipantId(tempPart.getId());

										t.setSponsorId(tempPart.getSponsors());

										tempPart.setIs_start(true);

										tempPartList.add(t);
									}

								}
							}
						}

					}

					else if (campaign.getParticipants() != null) {

						for (EventCampaignParticipant tempPart : campaign.getParticipants()) {

							System.out.println("Participant Name : " + tempPart.getName());

							psFlag = false;

							if (tempPart.getIs_start() == null) {

								tempPart.setIs_start(false);
							}

							if (tempPart.getIs_end() == null) {

								tempPart.setIs_end(false);
							}

							if (tempPart.getIsActive() == true && tempPart.getIsDeleted() == false) {

								MobileEventCampaignParticipants t = new MobileEventCampaignParticipants();

								if (tempPart.getIs_start() == true && tempPart.getIs_end() == false) {

									psFlag = true;
								}

								if (psFlag == true) {

									t.setSortOrder(tempPart.getSortOrder());

									t.setCampaignId(tempPart.getCampaignId());

									t.setLogo(new Picture(tempPart.getPictureO()));

									t.setMultiLingual(tempPart.getMultiLingual());

									t.setOverlayLandscape(new Picture(tempPart.getOverlayLandscapeO()));

									t.setOverlayPotrait(new Picture(tempPart.getOverlayPotraitO()));

									t.setParticipantId(tempPart.getId());

									t.setSponsorId(tempPart.getSponsors());

									tempPart.setIs_start(true);

									tempPartList.add(t);
								}

							}

						}

					}

					// if (csFlag == true) {

					tempCamp.setParticipants(tempPartList);

					temp.add(tempCamp);
					// }

				}

				catch (Exception ex) {

					ex.printStackTrace();
					// System.out.println("Ecxeption" + ex);

				}
			}
		}

		campaigns = listCampaign;

		return temp;
	}

}
