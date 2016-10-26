package org.iqvis.nvolv3.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hamcrest.Matchers;
import org.iqvis.nvolv3.bean.EventTrack;
import org.iqvis.nvolv3.domain.Activity;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.EventCampaign;
import org.iqvis.nvolv3.domain.EventCampaignParticipant;
import org.iqvis.nvolv3.domain.Feed;
import org.iqvis.nvolv3.domain.FeedActivityData;
import org.iqvis.nvolv3.domain.FeedActivityTrackData;
import org.iqvis.nvolv3.domain.FeedCampaignData;
import org.iqvis.nvolv3.domain.Sponsor;
import org.iqvis.nvolv3.domain.Track;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.service.ActivityService;
import org.iqvis.nvolv3.service.EventCampaignParticipantService;
import org.iqvis.nvolv3.service.EventCampaignService;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.service.SponsorService;
import org.iqvis.nvolv3.service.TrackService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.MongoDBCollections;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.joda.time.DateTime;

import ch.lambdaj.Lambda;

@SuppressWarnings("restriction")
@Repository
public class FeedDao {

	protected static Logger logger = Logger.getLogger("dao");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	@Resource(name = Constants.SERVICE_ACTIVITY)
	private ActivityService activityService;

	@Resource(name = Constants.SERVICE_TRACK)
	private TrackService trackService;

	@Resource(name = Constants.SERVICE_EVENT)
	private EventService eventService;

	@Resource(name = Constants.SERVICE_EVENT_CAMPAIGN_PARTICIPANT)
	private EventCampaignParticipantService eventCampaignParticipantService;

	@Resource(name = Constants.SERVICE_SPONSOR)
	private SponsorService sponserService;

	@Resource(name = Constants.SERVICE_EVENT_CAMPAIGN)
	private EventCampaignService eventCampaignService;

	public void updateFeedsAttendeeDp(String attendeeId, String imageUrl) {

		Query query = new Query();

		query.addCriteria(Criteria.where("attendeeId").is(attendeeId));

		Update update = new Update();

		update.set("dp", imageUrl);

		mongoTemplate.updateMulti(query, update, Feed.class);
	}

	public List<String> getListOfFeedIds(String attendeeId) {
		
		List<String> ids=new ArrayList<String>();

		Query query = new Query();

		query.addCriteria(Criteria.where("attendeeId").is(attendeeId));

		List<Feed> list=mongoTemplate.find(query, Feed.class);
		
		if(list!=null){
			
			for (Feed feed : list) {
				
				if(!ids.contains(feed.getId())){
					
					ids.add(feed.getId());
				}
			}
		}
		
		return ids;
	}

	/**
	 * Retrieves all Events
	 * 
	 * @param Search
	 *            eventSearch
	 * @param HttpServletRequest
	 *            request
	 * @return List<Feeds>
	 */
	public Page<Feed> getAllCMS(Query query, Pageable pageAble, String type, String typeId) {

		logger.debug("Retrieving all feeds");

		List<Feed> feeds = null;

		if (null != pageAble) {

			query.with(pageAble);
		}

		// query.addCriteria(Criteria.where("type").is(type).and("typeId").is(typeId).and("isDeleted").is(false));

		// query.addCriteria(Criteria.where("type").is(type).and("typeId").is(typeId).and("isDeleted").exists(false));

		// query.addCriteria(Criteria.where("type").is(type).and("typeId").is(typeId));

		query.addCriteria(Criteria.where("eventId").is(typeId));

		feeds = mongoTemplate.find(query, Feed.class);

		for (Feed f : feeds) {

			Event ev = eventService.get(typeId);

			List<EventTrack> etList = ev.getTracks();

			String org = ev.getOrganizerId();

			FeedActivityData fad = new FeedActivityData();

			Activity act = new Activity();

			// FeedActivityTrackData fatd = new FeedActivityTrackData();

			FeedCampaignData fcd = new FeedCampaignData();

			if (f.getType() != null && f.getType().equals(Constants.ACTIVITY_FEED)) {

				List<Activity> activity = Lambda.select(ev.getActivities(), Lambda.having(Lambda.on(Activity.class).getId(), Matchers.equalTo(f.getTypeId())));

				if (activity.size() > 0) {

					act = activity.get(0);

					if (act.getName() != null) {

						fad.setActivityName(act.getName());

					}

					if (act.getStartTime() != null) {

						fad.setStartTime(act.getStartTime());

					}

					if (act.getEndTime() != null) {

						fad.setEndTime(act.getEndTime());

					}

					// List<Track> activityTrack =
					// Lambda.select(act.getTracks(),
					// Lambda.having(Lambda.on(Track.class).getId(),
					// Matchers.equalTo(act.getTracks())));

					List<String> activityTrackList = act.getTracks() == null ? new ArrayList<String>() : act.getTracks();

					List<FeedActivityTrackData> fatdList = new ArrayList<FeedActivityTrackData>();

					for (String tId : activityTrackList) {

						FeedActivityTrackData feedATD = new FeedActivityTrackData();

						Track t = new Track();

						String tName = "";

						String tColor = "";

						try {
							if (!t.equals("") && t != null) {

								t = trackService.get(tId, org);

								tName = t.getName();

								for (EventTrack eventTrack : etList) {

									if (!eventTrack.equals("") && eventTrack != null) {

										if (tId.equals(eventTrack.getTrackId())) {

											tColor = eventTrack.getColorCode();

											break;
										}

									}
								}

							}
						}
						catch (NotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						feedATD.setTrackame(tName);

						feedATD.setTrackColorCode(tColor);

						fatdList.add(feedATD);
					}

					fad.setFeedActivityTrackDataList(fatdList);

					f.setFeedActivityData(fad);

				}

			}

			if (f.getParticipantId() != null) {

				List<EventCampaign> ecpList = eventCampaignService.getAllEventCampaign(ev.getId());

				String pID = "";

				String pName = "";

				String cID = "";

				String cName = "";

				String sName = "";

				// List<FeedCampaignData> feedCampaignDataList = new
				// ArrayList<FeedCampaignData>();

				Sponsor sponser = new Sponsor();

				EventCampaign eventCampaign = new EventCampaign();

				EventCampaignParticipant ecp = new EventCampaignParticipant();

				for (EventCampaign eCampaign : ecpList) {

					List<EventCampaignParticipant> ecParticipantList = eCampaign.getParticipants();

					boolean flag = false;

					if (ecParticipantList != null && ecParticipantList.size() > 0) {

						for (EventCampaignParticipant ecPart : ecParticipantList) {

							if (f.getParticipantId().equals(ecPart.getId())) {

								ecp = ecPart;

								pID = ecp.getId();

								pName = ecp.getName();

								cID = ecp.getCampaignId();

								eventCampaign = eventCampaignService.get(cID, org);

								cName = eventCampaign.getName();

								sponser = sponserService.get(ecp.getSponsors().get(0));

								sName = sponser.getName();

								flag = true;

								break;

							}
						}

					}

					if (flag) {

						break;

					}
				}

				// for (EventCampaign ecp : ecpList) {
				//
				// if (f.get.equals(ecp.getId())) {
				//
				// pID = ecp.getId();
				//
				// pName = ecp.getName();
				//
				// cID = ecp.getCampaignId();
				//
				// eventCampaign = eventCampaignService.get(cID, org);
				//
				// cName = eventCampaign.getName();
				//
				// sponser = sponserService.get(ecp.getSponsors().get(0));
				//
				// sName = sponser.getName();
				//
				// System.out.println("PID : " + pID + " | pName : " + pName +
				// " | cID : " + cID + " | cName : " + cName + " | sName : " +
				// sName);
				//
				//
				//
				// break;
				// }
				// }

				fcd.setParticipantId(pID);

				fcd.setParticipantName(pName);

				fcd.setCampaignId(cID);

				fcd.setCampaignName(cName);

				fcd.setSponserName(sName);

				f.setFeedCampaignData(fcd);

			}

		}

		long total = mongoTemplate.count(query, Feed.class);

		// to print the mongodb query for debug purposes

		Page<Feed> eventPage = new PageImpl<Feed>(feeds, pageAble, total);

		return eventPage;
	}

	// for mobile
	public Page<Feed> getAll(Query query, Pageable pageAble, String type, String typeId, String eventId) {

		logger.debug("Retrieving all feeds");

		List<Feed> feeds = null;

		if (null != pageAble) {

			query.with(pageAble);
		}

		// query.addCriteria(Criteria.where("type").is(type).and("typeId").is(typeId).and("isActive").is(true).and("isDeleted").is(false));

		// query.addCriteria(Criteria.where("eventId").is(eventId).and("isActive").is(true));

		query.addCriteria(Criteria.where("eventId").is(eventId).and("feedStatus").is(org.iqvis.nvolv3.utils.Constants.FEED_STATUS_APPROVED));

		// String str_case =
		// org.iqvis.nvolv3.utils.Constants.FEED_STATUS_APPROVED.toLowerCase();
		//
		// query.addCriteria(Criteria.where("eventId").is(eventId).and("status").is(str_case));

		feeds = mongoTemplate.find(query, Feed.class);

		long total = mongoTemplate.count(query, Feed.class);

		// to print the mongodb query for debug purposes

		Page<Feed> eventPage = new PageImpl<Feed>(feeds, pageAble, total);

		return eventPage;
	}

	public List<Feed> getAll(String type, String typeId) {

		logger.debug("Retrieving all feeds");

		List<Feed> feeds = null;

		Query query = new Query();

		query.addCriteria(Criteria.where("type").is(type).and("typeId").is(typeId).and("isActive").is(true));

		feeds = mongoTemplate.find(query, Feed.class);

		return feeds;
	}

	public Feed getLatestTwitterFeed() {

		logger.debug("Retrieving all feeds");

		Query query = new Query();

		query.addCriteria(Criteria.where("socialMediaType").is(Constants.TWITTER));

		query.with(new Sort(Sort.Direction.DESC, "createdDate"));

		query.limit(1);

		logger.debug(query);

		List<Feed> feeds = mongoTemplate.find(query, Feed.class);

		return feeds != null && feeds.size() > 0 ? feeds.get(0) : null;
	}

	public Feed getSocialMediaFeed(long socialMediaId, String eventId) {

		logger.info("Retrieving feed with socialMediaId : " + socialMediaId + " and eventId : " + eventId);

		Query query = new Query();

		query.addCriteria(Criteria.where("socialMediaId").is(socialMediaId).and("eventId").is(eventId));

		logger.info(query);

		return mongoTemplate.findOne(query, Feed.class);
	}

	public List<Feed> getAllFeeds(String eventId) {

		logger.debug("Retrieving all feeds");

		List<Feed> feeds = null;

		Query query = new Query();

		query.addCriteria(Criteria.where("eventId").is(eventId).and("isActive").is(true));

		query.with(new Sort(Sort.Direction.DESC, "createdDate"));

		feeds = mongoTemplate.find(query, Feed.class);

		return feeds;
	}

	/**
	 * Retrieves a single feed
	 * 
	 * @param String
	 *            id
	 * @return feed
	 */

	public Feed get(String id) {

		logger.debug("Retrieving an existing feed");

		Query query = new Query(Criteria.where("id").is(id).and("isDeleted").is(false));

		logger.debug(query.getQueryObject());

		Feed feed = mongoTemplate.findOne(query, Feed.class, MongoDBCollections.FEED.toString());

		FeedActivityData fad = new FeedActivityData();

		String eid = feed.getEventId();

		Event e = eventService.get(eid);

		String org = e.getOrganizerId();

		List<EventTrack> etList = e.getTracks();

		Activity act = new Activity();

		if (feed != null) {

			if (feed.getType() != null && feed.getType().equalsIgnoreCase(Constants.ACTIVITY_FEED)) {

				List<Activity> activity = Lambda.select(e.getActivities(), Lambda.having(Lambda.on(Activity.class).getId(), Matchers.equalTo(feed.getTypeId())));

				if (activity.size() > 0) {

					act = activity.get(0);

					if (act.getName() != null) {

						fad.setActivityName(act.getName());
					}

					if (act.getStartTime() != null) {

						fad.setStartTime(act.getStartTime());

					}

					if (act.getEndTime() != null) {

						fad.setEndTime(act.getEndTime());

					}

					List<String> activityTrackList = act.getTracks() == null ? new ArrayList<String>() : act.getTracks();

					List<FeedActivityTrackData> fatdList = new ArrayList<FeedActivityTrackData>();

					for (String tId : activityTrackList) {

						FeedActivityTrackData feedATD = new FeedActivityTrackData();

						Track t = new Track();

						String tName = "";

						String tColor = "";

						try {

							if (!t.equals("") && t != null) {

								t = trackService.get(tId, org);

								tName = t.getName();

								for (EventTrack eventTrack : etList) {

									if (!eventTrack.equals("") && eventTrack != null) {

										if (tId.equals(eventTrack.getTrackId())) {

											tColor = eventTrack.getColorCode();

											break;
										}

									}

								}

							}

						}
						catch (NotFoundException ex) {
							// TODO Auto-generated catch block

							ex.printStackTrace();
						}
						feedATD.setTrackame(tName);

						feedATD.setTrackColorCode(tColor);

						fatdList.add(feedATD);
					}

					fad.setFeedActivityTrackDataList(fatdList);

					feed.setFeedActivityData(fad);

				}

			}

		}

		FeedCampaignData fcd = new FeedCampaignData();

		if (feed.getParticipantId() != null) {

			List<EventCampaign> ecpList = eventCampaignService.getAllEventCampaign(e.getId());

			String pID = "";

			String pName = "";

			String cID = "";

			String cName = "";

			String sName = "";

			Sponsor sponser = new Sponsor();

			EventCampaign eventCampaign = new EventCampaign();

			EventCampaignParticipant ecp = new EventCampaignParticipant();

			for (EventCampaign eCampaign : ecpList) {

				List<EventCampaignParticipant> ecParticipantList = eCampaign.getParticipants();

				boolean flag = false;

				if (ecParticipantList != null && ecParticipantList.size() > 0) {

					for (EventCampaignParticipant ecPart : ecParticipantList) {

						if (feed.getParticipantId().equals(ecPart.getId())) {

							ecp = ecPart;

							pID = ecp.getId();

							pName = ecp.getName();

							cID = ecp.getCampaignId();

							eventCampaign = eventCampaignService.get(cID, org);

							cName = eventCampaign.getName();

							sponser = sponserService.get(ecp.getSponsors().get(0));

							sName = sponser.getName();

							flag = true;

							break;

						}
					}

				}

				if (flag) {

					break;

				}
			}

			fcd.setParticipantId(pID);

			fcd.setParticipantName(pName);

			fcd.setCampaignId(cID);

			fcd.setCampaignName(cName);

			fcd.setSponserName(sName);

			feed.setFeedCampaignData(fcd);

		}

		return feed;
	}

	/**
	 * Adds a new feed
	 * 
	 * @param Feed
	 *            feed
	 * @return Feed
	 */
	public Feed add(Feed feed) throws Exception {
		logger.debug("Adding a new feed");

		try {

			mongoTemplate.insert(feed, MongoDBCollections.FEED.toString());

			return feed;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to add new feed", e);

			throw e;
		}
	}

	/**
	 * Edits an existing Feed
	 * 
	 * @param Feed
	 *            feed
	 * @param String
	 *            eventId
	 * @return Feed
	 */
	public Feed edit(Feed feed) throws Exception, NotFoundException {

		logger.debug("Editing existing feed");

		try {

			mongoTemplate.save(feed);

			return feed;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to edit existing feed", e);

			throw e;
		}

	}

	public String getFeedDetailUrl(Feed feed, HttpServletRequest request) {
		String detailUrl = "";

		String replaceUrlToken = Urls.EVENT_FEEDS_BASE_URL;

		// replaceUrlToken = replaceUrlToken.replace("id", newChar)

		detailUrl = Utils.getURLWithContextPath(request) + replaceUrlToken;

		return detailUrl;
	}

	public List<Feed> getAll(String eventId) {
		logger.debug("Retrieving all feeds");

		List<Feed> feeds = null;

		Query query = new Query();

		query.addCriteria(Criteria.where("eventId").is(eventId).and("isActive").is(true).and("isDeleted").is(false));

		feeds = mongoTemplate.find(query, Feed.class);

		return feeds;
	}
	
	public List<Feed> getOlderByDate(String eventId, DateTime date) {
		logger.debug("Retrieving older feeds by date");

		List<Feed> feeds = null;

		Query query = new Query();

		query.addCriteria(Criteria.where("eventId").is(eventId).and("isActive").is(true).and("isDeleted").is(false).and("createdDate").lt(date));
		query.with(new Sort(Sort.Direction.DESC, "createdDate"));
		query.limit(Constants.FEED_LIMIT);

		feeds = mongoTemplate.find(query, Feed.class);

		return feeds;
	}

}
