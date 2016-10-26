package org.iqvis.nvolv3.tracker;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.core.MediaType;

import org.iqvis.nvolv3.analytics.domain.ActivityClass;
import org.iqvis.nvolv3.analytics.domain.AggregateCountFunction;
import org.iqvis.nvolv3.analytics.domain.AnalyticsQuery;
import org.iqvis.nvolv3.analytics.domain.AnalyticsServerResponse;
import org.iqvis.nvolv3.analytics.domain.Field;
import org.iqvis.nvolv3.analytics.domain.OrderBy;
import org.iqvis.nvolv3.analytics.domain.QuickStatsCount;
import org.iqvis.nvolv3.analytics.domain.ResponseCount;
import org.iqvis.nvolv3.analytics.domain.Where;
import org.iqvis.nvolv3.domain.Activity;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.service.ConstantFactoryImpl;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.tracker.beans.ResponseMessage;
import org.iqvis.nvolv3.tracker.beans.Track;
import org.iqvis.nvolv3.utils.AnalyticQueryUtils;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Urls;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

@SuppressWarnings("restriction")
@RequestMapping(Urls.MOBILE_ANALYTICS_BASE)
@Controller
public class AnalyticsDataCollectionController {

	@Resource(name = Constants.SERVICE_EVENT)
	private EventService eventService;

//	@Autowired
	private String analyticsServerUrl=ConstantFactoryImpl.ANALYTICS_CUSTOM_AGGREGATION_REPORTING_URL;

//	@Autowired
	private String geoLocationAnalyticsServerUrl=ConstantFactoryImpl.ANALYTICS_GEO_LOCATION_AGGREGATION_REPORTING_URL;

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	public ResponseMessage add(@RequestBody Track track, HttpServletRequest request) {

		Client client = Client.create();

		WebResource resource = client.resource("http://analytics.iqvis.net/" + Constants.NVOLV3_ANALYTICS_API_KEY + "/track/");

		ResponseMessage bean = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(ResponseMessage.class, track);

		return bean;
	}

	@RequestMapping(value = "/reporting", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	public Object get(@RequestBody(required = true) @Valid AnalyticsQuery query, @PathVariable("organizerId") String organizerId, HttpServletRequest request) {

		Client client = Client.create();

		List<Where> where = query.getWhere();

		if (where == null)
			where = new ArrayList<Where>();

		where.add(new Where("organizerId", organizerId, "eq"));

		query.setWhere(where);

		WebResource resource = client.resource(analyticsServerUrl);

		Object bean = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(Object.class, query);

		return bean;
	}

	@RequestMapping(value = "/reporting/app_downloads/{appId}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	public Object getInstalled(@RequestBody(required = true) @Valid AnalyticsQuery query, @PathVariable("organizerId") String organizerId, @PathVariable("appId") String appId, HttpServletRequest request) {

		Client client = Client.create();

		List<Where> where = query.getWhere();

		List<String> groupBy = new ArrayList<String>();

		groupBy.add("device.OS");

		groupBy.add("device.type");

		query.setGroupBy(groupBy);

		if (where == null)
			where = new ArrayList<Where>();

		Where w = new Where();

		List<Field> fieldList = new ArrayList<Field>();

		fieldList.add(new Field("action", "APP_INSTALL", "eq"));

		fieldList.add(new Field("appId", appId, "eq"));

		// fieldList.add(new Field("organizerId", organizerId, "eq"));

		w.setAnd(fieldList);

		where.add(w);

		query.setWhere(where);

		List<Object> aggregateList = new ArrayList<Object>();

		aggregateList.add(new AggregateCountFunction(true));

		query.setAggregate(aggregateList);

		WebResource resource = client.resource(analyticsServerUrl);

		Object bean = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(Object.class, query);

		return bean;
	}

	// TODO event_data_downloads_per_platform
	@RequestMapping(value = "/reporting/event_data_downloads_per_platform/{eventId}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	public Object getEventDataDownload(@RequestBody(required = true) @Valid AnalyticsQuery query, @PathVariable("organizerId") String organizerId, @PathVariable("eventId") String eventId, HttpServletRequest request) {

		Client client = Client.create();

		List<Where> where = query.getWhere();

		List<String> groupBy = new ArrayList<String>();

		groupBy.add("device.OS");

		query.setGroupBy(groupBy);

		if (where == null)
			where = new ArrayList<Where>();

		Where w = new Where();

		List<Field> fieldList = new ArrayList<Field>();

		fieldList.add(new Field("action", "EVENT_DATA_DOWNLOAD", "eq"));

		fieldList.add(new Field("eventId", eventId, "eq"));

		fieldList.add(new Field("organizerId", organizerId, "eq"));

		fieldList.add(new Field("generator", "SYSTEM", "eq"));

		w.setAnd(fieldList);

		where.add(w);

		query.setWhere(where);

		List<Object> aggregateList = new ArrayList<Object>();

		aggregateList.add(new AggregateCountFunction(true));

		query.setAggregate(aggregateList);

		WebResource resource = client.resource(analyticsServerUrl);

		Object bean = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(Object.class, query);

		return bean;
	}

	@RequestMapping(value = "/reporting/app_usage/{appId}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	public Object getUsageStats(@RequestBody(required = true) @Valid AnalyticsQuery query, @PathVariable("organizerId") String organizerId, @PathVariable("appId") String appId, HttpServletRequest request) {

		Client client = Client.create();

		List<Where> where = query.getWhere();

		List<String> groupBy = new ArrayList<String>();

		groupBy.add("device.OS");

		groupBy.add("device.type");

		query.setGroupBy(groupBy);

		List<Object> _agroupBy = new ArrayList<Object>();

		_agroupBy.add("date");

		query.set_agroupBy(_agroupBy);

		if (where == null)
			where = new ArrayList<Where>();

		Where w = new Where();

		List<Field> fieldList = new ArrayList<Field>();

		fieldList.add(new Field("action", "APP_OPEN", "eq"));

		fieldList.add(new Field("appId", appId, "eq"));

		// fieldList.add(new Field("organizerId", organizerId, "eq"));

		w.setAnd(fieldList);

		where.add(w);

		query.setWhere(where);

		List<Object> aggregateList = new ArrayList<Object>();

		aggregateList.add(new AggregateCountFunction(true));

		query.setAggregate(aggregateList);

		List<Object> orderBy = new ArrayList<Object>();

		orderBy.add(new OrderBy("date.year", "asc"));

		orderBy.add(new OrderBy("date.month", "asc"));

		orderBy.add(new OrderBy("date.day", "asc"));

		query.setOrderBy(orderBy);

		WebResource resource = client.resource(analyticsServerUrl);

		Object bean = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(Object.class, query);

		return bean;
	}

	// --------------------------------------Event Site Stat
	// Api---------------------------------------------------------

	@RequestMapping(value = "/reporting/event_site_visits_per_day/{eventId}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	public Object getEvent_site_visits_per_day(@RequestBody(required = true) @Valid AnalyticsQuery query, @PathVariable("organizerId") String organizerId, @PathVariable("eventId") String eventId, HttpServletRequest request) {

		Client client = Client.create();

		List<Where> where = query.getWhere();

		List<Object> _agroupBy = new ArrayList<Object>();

		_agroupBy.add("date");

		query.set_agroupBy(_agroupBy);

		if (where == null)
			where = new ArrayList<Where>();

		Where w = new Where();

		List<Field> fieldList = new ArrayList<Field>();

		fieldList.add(new Field("organizerId", organizerId, "eq"));

		fieldList.add(new Field("action", "PAGE_VISIT", "eq"));

		fieldList.add(new Field("screenName", "HOME", "eq"));

		fieldList.add(new Field("app", "EVENT_SITE", "eq"));

		fieldList.add(new Field("eventId", eventId, "eq"));

		fieldList.add(new Field("generator", "USER", "eq"));

		w.setAnd(fieldList);

		where.add(w);

		query.setWhere(where);

		List<Object> aggregateList = new ArrayList<Object>();

		aggregateList.add(new AggregateCountFunction(true));

		query.setAggregate(aggregateList);

		List<Object> orderBy = new ArrayList<Object>();

		orderBy.add(new OrderBy("date.year", "asc"));

		orderBy.add(new OrderBy("date.month", "asc"));

		orderBy.add(new OrderBy("date.day", "asc"));

		query.setOrderBy(orderBy);

		WebResource resource = client.resource(analyticsServerUrl);

		Object bean = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(Object.class, query);

		return bean;
	}

	@RequestMapping(value = "/reporting/event_data_downloads_per_day/{eventId}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	public Object getEventDataDownloadPerDay(@RequestBody(required = true) @Valid AnalyticsQuery query, @PathVariable("organizerId") String organizerId, @PathVariable("eventId") String eventId, HttpServletRequest request) {

		Client client = Client.create();

		List<Where> where = query.getWhere();

		List<String> groupBy = new ArrayList<String>();

		groupBy.add("device.OS");

		groupBy.add("device.type");

		query.setGroupBy(groupBy);

		List<Object> _agroupBy = new ArrayList<Object>();

		_agroupBy.add("date");

		query.set_agroupBy(_agroupBy);

		if (where == null)
			where = new ArrayList<Where>();

		Where w = new Where();

		List<Field> fieldList = new ArrayList<Field>();

		fieldList.add(new Field("action", "EVENT_DATA_DOWNLOAD", "eq"));

		fieldList.add(new Field("eventId", eventId, "eq"));

		fieldList.add(new Field("organizerId", organizerId, "eq"));

		fieldList.add(new Field("generator", "SYSTEM", "eq"));

		w.setAnd(fieldList);

		where.add(w);

		query.setWhere(where);

		List<Object> aggregateList = new ArrayList<Object>();

		aggregateList.add(new AggregateCountFunction(true));

		query.setAggregate(aggregateList);

		List<Object> orderBy = new ArrayList<Object>();

		orderBy.add(new OrderBy("date.year", "asc"));

		orderBy.add(new OrderBy("date.month", "asc"));

		orderBy.add(new OrderBy("date.day", "asc"));

		query.setOrderBy(orderBy);

		WebResource resource = client.resource(analyticsServerUrl);

		Object bean = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(Object.class, query);

		return bean;
	}

	@RequestMapping(value = "/reporting/event_open_per_day/{eventId}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	public Object getEventDataOpenPerDay(@RequestBody(required = true) @Valid AnalyticsQuery query, @PathVariable("organizerId") String organizerId, @PathVariable("eventId") String eventId, HttpServletRequest request) {

		Client client = Client.create();

		List<Where> where = query.getWhere();

		List<Object> _agroupBy = new ArrayList<Object>();

		_agroupBy.add("date");

		query.set_agroupBy(_agroupBy);

		if (where == null)
			where = new ArrayList<Where>();

		Where w = new Where();

		List<Field> fieldList = new ArrayList<Field>();

		fieldList.add(new Field("action", "SHOW_EVENT", "eq"));

		fieldList.add(new Field("eventId", eventId, "eq"));

		fieldList.add(new Field("organizerId", organizerId, "eq"));

		fieldList.add(new Field("generator", "SYSTEM", "eq"));

		w.setAnd(fieldList);

		where.add(w);

		query.setWhere(where);

		List<Object> aggregateList = new ArrayList<Object>();

		aggregateList.add(new AggregateCountFunction(true));

		query.setAggregate(aggregateList);

		List<Object> orderBy = new ArrayList<Object>();

		orderBy.add(new OrderBy("date.year", "asc"));

		orderBy.add(new OrderBy("date.month", "asc"));

		orderBy.add(new OrderBy("date.day", "asc"));

		query.setOrderBy(orderBy);

		WebResource resource = client.resource(analyticsServerUrl);

		Object bean = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(Object.class, query);

		return bean;
	}

	@RequestMapping(value = "/reporting/app_screen_visits/{eventId}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	public Object getAppScreenVists(@RequestBody(required = true) @Valid AnalyticsQuery query, @PathVariable("organizerId") String organizerId, @PathVariable("eventId") String eventId, HttpServletRequest request) {

		Client client = Client.create();

		List<Where> where = query.getWhere();

		List<String> groupBy = new ArrayList<String>();

		groupBy.add("screenName");

		query.setGroupBy(groupBy);

		if (where == null)
			where = new ArrayList<Where>();

		Where w = new Where();

		List<Field> fieldList = new ArrayList<Field>();

		fieldList.add(new Field("action", "SHOW_SCREEN", "eq"));

		fieldList.add(new Field("eventId", eventId, "eq"));

		fieldList.add(new Field("organizerId", organizerId, "eq"));

		fieldList.add(new Field("generator", "SYSTEM", "eq"));

		w.setAnd(fieldList);

		where.add(w);

		query.setWhere(where);

		List<Object> aggregateList = new ArrayList<Object>();

		aggregateList.add(new AggregateCountFunction(true));

		query.setAggregate(aggregateList);

		List<Object> orderBy = new ArrayList<Object>();

		orderBy.add(new OrderBy("count", "desc"));

		query.setOrderBy(orderBy);

		WebResource resource = client.resource(analyticsServerUrl);

		Object bean = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(Object.class, query);

		return bean;
	}

	// ----------------------------------------------------------------------------------------------------------------------

	@RequestMapping(value = "/reporting/event_site_visits_by_geolocation/{eventId}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	public Object getEventSiteVisitsByLocation(@RequestBody(required = true) @Valid AnalyticsQuery query, @PathVariable("organizerId") String organizerId, @PathVariable("eventId") String eventId, HttpServletRequest request) {

		Client client = Client.create();

		List<Where> where = query.getWhere();

		if (where == null)
			where = new ArrayList<Where>();

		Where w = new Where();

		List<Field> fieldList = new ArrayList<Field>();

		fieldList.add(new Field("organizerId", organizerId, "eq"));

		fieldList.add(new Field("action", "PAGE_VISIT", "eq"));

		fieldList.add(new Field("app", "EVENT_SITE", "eq"));

		fieldList.add(new Field("eventId", eventId, "eq"));

		fieldList.add(new Field("generator", "USER", "eq"));

		w.setAnd(fieldList);

		where.add(w);

		query.setWhere(where);

		List<Object> aggregateList = new ArrayList<Object>();

		aggregateList.add(new AggregateCountFunction(true));

		query.setAggregate(aggregateList);

		List<Object> orderBy = new ArrayList<Object>();

		orderBy.add(new OrderBy("count", "desc"));

		query.setOrderBy(orderBy);

		WebResource resource = client.resource(geoLocationAnalyticsServerUrl);

		Object bean = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(Object.class, query);

		return bean;
	}

	@RequestMapping(value = "/reporting/populer_activities/{eventId}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	public AnalyticsServerResponse getPopulerActivities(@RequestBody(required = true) @Valid AnalyticsQuery query, @PathVariable("organizerId") String organizerId, @PathVariable("eventId") String eventId, HttpServletRequest request) {

		Client client = Client.create();

		Gson gson = new Gson();

		List<ResponseCount> viewActivityList = new ArrayList<ResponseCount>();

		List<ResponseCount> favoritesActivityList = new ArrayList<ResponseCount>();

		List<ResponseCount> tempActivityList = new ArrayList<ResponseCount>();

		Type listType = new TypeToken<List<ResponseCount>>() {
		}.getType();

		WebResource resource = client.resource(analyticsServerUrl);

		AnalyticsQuery view_activities_query = AnalyticQueryUtils.view_activities_query(new AnalyticsQuery(query.getFromDateTime(), query.getToDateTime()), eventId, organizerId);

		AnalyticsServerResponse view_activities_query_response = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(AnalyticsServerResponse.class, view_activities_query);

		if (view_activities_query_response.getHttpCode() == 200) {

			List<Object> content = view_activities_query_response.getContents();

			String json = gson.toJson(content);

			System.out.println(json);

			viewActivityList = (List<ResponseCount>) gson.fromJson(json, listType);

			if (viewActivityList != null && !viewActivityList.isEmpty()) {

				tempActivityList = new ArrayList<ResponseCount>(viewActivityList);
			}

		}

		AnalyticsQuery favorites_activities_query = AnalyticQueryUtils.favorites_activities_query(new AnalyticsQuery(query.getFromDateTime(), query.getToDateTime()), eventId, organizerId);

		AnalyticsServerResponse favorites_activities_query_response = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(AnalyticsServerResponse.class, favorites_activities_query);

		if (favorites_activities_query_response.getHttpCode() == 200) {

			List<Object> content = favorites_activities_query_response.getContents();

			String json = gson.toJson(content);

			System.out.println(json);

			favoritesActivityList = (List<ResponseCount>) gson.fromJson(json, listType);

			if (favoritesActivityList != null && !favoritesActivityList.isEmpty()) {

				if (viewActivityList == null || viewActivityList.isEmpty()) {

					tempActivityList = new ArrayList<ResponseCount>(favoritesActivityList);
				}
				else {
					List<ResponseCount> temp = new ArrayList<ResponseCount>(favoritesActivityList);

					List<ResponseCount> tempNew = new ArrayList<ResponseCount>();

					for (ResponseCount responseCount : tempActivityList) {

						for (ResponseCount responseCount2 : temp) {

							if (responseCount.getActivityId().equals(responseCount2.getActivityId())) {

								responseCount.setCount(responseCount.getCount() + responseCount2.getCount());

							}
							else if (!tempActivityList.contains(responseCount2) && !tempNew.contains(responseCount2)) {

								tempNew.add(responseCount2);
							}
						}

					}

					tempActivityList.addAll(tempNew);
				}
			}

		}

		List<Activity> finalList = new ArrayList<Activity>();

		if (tempActivityList != null && !tempActivityList.isEmpty()) {

			String json = gson.toJson(tempActivityList);

			System.out.println(json);

			Event event = eventService.get(eventId, organizerId);

			List<Activity> activityList = new ArrayList<Activity>(event.getActivities());

			for (Activity activity : activityList) {

				for (ResponseCount responseCount : tempActivityList) {

					if (activity.getId().equals(responseCount.getActivityId())) {

						activity.setPopulerCount(responseCount.getCount());

						finalList.add(activity);

					}
				}
			}

		}

		AnalyticsServerResponse message = new AnalyticsServerResponse();

		message.setHttpCode(200);

		Collections.sort(finalList, new ActivitySortDesc());

		List<Object> res = new ArrayList<Object>();

		for (Activity activity : finalList) {

			res.add(new ActivityClass(activity));
		}

		message.setContents(res);

		return message;
	}

	@RequestMapping(value = "/reporting/event_quick_states/{eventId}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	public AnalyticsServerResponse getEventQuickStats(@RequestBody(required = true) @Valid AnalyticsQuery query, @PathVariable("organizerId") String organizerId, @PathVariable("eventId") String eventId, HttpServletRequest request) {

		Client client = Client.create();

		Gson gson = new Gson();

		Type listType = new TypeToken<List<ResponseCount>>() {
		}.getType();

		QuickStatsCount quickStats = new QuickStatsCount();

		WebResource resource = client.resource(analyticsServerUrl);

		AnalyticsQuery event_data_downloads_count_query = AnalyticQueryUtils.event_data_downloads_count_query(new AnalyticsQuery(query.getFromDateTime(), query.getToDateTime()), eventId, organizerId);

		AnalyticsServerResponse event_data_downloads_count_response = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(AnalyticsServerResponse.class, event_data_downloads_count_query);

		if (event_data_downloads_count_response.getHttpCode() == 200) {

			List<Object> content = event_data_downloads_count_response.getContents();

			String json = gson.toJson(content);

			System.out.println(json);

			List<ResponseCount> countList = (List<ResponseCount>) gson.fromJson(json, listType);

			if (countList != null && !countList.isEmpty()) {

				ResponseCount count = (ResponseCount) countList.get(0);

				quickStats.setEvent_data_downloads_count(count.getCount());

			}

		}

		AnalyticsQuery event_visits_count_query = AnalyticQueryUtils.event_visits_count_query(new AnalyticsQuery(query.getFromDateTime(), query.getToDateTime()), eventId, organizerId);

		AnalyticsServerResponse event_visits_count_response = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(AnalyticsServerResponse.class, event_visits_count_query);

		if (event_visits_count_response.getHttpCode() == 200) {

			List<Object> content = event_visits_count_response.getContents();

			String json = gson.toJson(content);

			List<ResponseCount> countList = (List<ResponseCount>) gson.fromJson(json, listType);

			if (countList != null && !countList.isEmpty()) {

				ResponseCount count = countList.get(0);

				quickStats.setEvent_visits_count(count.getCount());

			}

		}

		AnalyticsQuery activities_in_schedule_count_query = AnalyticQueryUtils.activities_in_schedule_count_query(new AnalyticsQuery(query.getFromDateTime(), query.getToDateTime()), eventId, organizerId);

		AnalyticsServerResponse activities_in_schedule_count_response = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(AnalyticsServerResponse.class, activities_in_schedule_count_query);

		if (activities_in_schedule_count_response.getHttpCode() == 200) {

			List<Object> content = activities_in_schedule_count_response.getContents();

			String json = gson.toJson(content);

			List<ResponseCount> countList = (List<ResponseCount>) gson.fromJson(json, listType);

			if (countList != null && !countList.isEmpty()) {

				ResponseCount count = countList.get(0);

				quickStats.setActivities_in_schedule_count(count.getCount());

			}

		}

		AnalyticsQuery alerts_views_count_query = AnalyticQueryUtils.alerts_views_count_query(new AnalyticsQuery(query.getFromDateTime(), query.getToDateTime()), eventId, organizerId);

		AnalyticsServerResponse alerts_views_count_response = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(AnalyticsServerResponse.class, alerts_views_count_query);

		if (alerts_views_count_response.getHttpCode() == 200) {

			List<Object> content = alerts_views_count_response.getContents();

			String json = gson.toJson(content);

			List<ResponseCount> countList = (List<ResponseCount>) gson.fromJson(json, listType);

			if (countList != null && !countList.isEmpty()) {

				ResponseCount count = countList.get(0);

				quickStats.setAlerts_views_count(count.getCount());

			}

		}

		AnalyticsQuery feeds_activities_count_query = AnalyticQueryUtils.feeds_activities_count_query(new AnalyticsQuery(query.getFromDateTime(), query.getToDateTime()), eventId, organizerId);

		AnalyticsServerResponse feeds_activities_count_response = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(AnalyticsServerResponse.class, feeds_activities_count_query);

		if (feeds_activities_count_response.getHttpCode() == 200) {

			List<Object> content = feeds_activities_count_response.getContents();

			String json = gson.toJson(content);

			List<ResponseCount> countList = (List<ResponseCount>) gson.fromJson(json, listType);

			if (countList != null && !countList.isEmpty()) {

				ResponseCount count = countList.get(0);

				quickStats.setFeeds_activities_count(count.getCount());

			}

		}

		AnalyticsQuery activities_feedback_collection_count_query = AnalyticQueryUtils.activities_feedback_collection_count_query(new AnalyticsQuery(query.getFromDateTime(), query.getToDateTime()), eventId, organizerId);

		AnalyticsServerResponse activities_feedback_collection_count_response = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(AnalyticsServerResponse.class, activities_feedback_collection_count_query);

		if (activities_feedback_collection_count_response.getHttpCode() == 200) {

			List<Object> content = activities_feedback_collection_count_response.getContents();

			String json = gson.toJson(content);

			List<ResponseCount> countList = (List<ResponseCount>) gson.fromJson(json, listType);

			if (countList != null && !countList.isEmpty()) {

				ResponseCount count = countList.get(0);

				quickStats.setActivities_feedback_collection_count(count.getCount());

			}

		}

		AnalyticsQuery event_site_visits_count_query = AnalyticQueryUtils.event_site_visits_count_query(new AnalyticsQuery(query.getFromDateTime(), query.getToDateTime()), eventId, organizerId);

		AnalyticsServerResponse event_site_visits_count_response = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(AnalyticsServerResponse.class, event_site_visits_count_query);

		if (event_site_visits_count_response.getHttpCode() == 200) {

			List<Object> content = event_site_visits_count_response.getContents();

			String json = gson.toJson(content);

			List<ResponseCount> countList = (List<ResponseCount>) gson.fromJson(json, listType);

			if (countList != null && !countList.isEmpty()) {

				ResponseCount count = countList.get(0);

				quickStats.setEvent_site_visits_count(count.getCount());

			}

		}

		AnalyticsServerResponse message = new AnalyticsServerResponse();

		message.setHttpCode(200);

		List<Object> res = new ArrayList<Object>();

		res.add(quickStats);

		message.setContents(res);

		return message;
	}

	class ActivitySortDesc implements Comparator<Activity> {

		public int compare(Activity o1, Activity o2) {
			// write comparison logic here like below , it's just a sample
			return o1.compareTo(o2);
		}
	}

}
