package org.iqvis.nvolv3.utils;

import java.util.ArrayList;
import java.util.List;

import org.iqvis.nvolv3.analytics.domain.AggregateCountFunction;
import org.iqvis.nvolv3.analytics.domain.AnalyticsQuery;
import org.iqvis.nvolv3.analytics.domain.Field;
import org.iqvis.nvolv3.analytics.domain.OrderBy;
import org.iqvis.nvolv3.analytics.domain.Where;

public class AnalyticQueryUtils {

	public static AnalyticsQuery event_data_downloads_count_query(AnalyticsQuery query, String eventId, String organizerId) {

		List<Where> where = query.getWhere();

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

		return query;
	}

	public static AnalyticsQuery event_visits_count_query(AnalyticsQuery query, String eventId, String organizerId) {

		List<Where> where = query.getWhere();

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

		return query;
	}

	public static AnalyticsQuery activities_in_schedule_count_query(AnalyticsQuery query, String eventId, String organizerId) {

		List<Where> where = query.getWhere();

		if (where == null)
			where = new ArrayList<Where>();

		Where w = new Where();

		List<Field> fieldList = new ArrayList<Field>();

		fieldList.add(new Field("object", "ACTIVITY_ADD_TO_FAVORITES", "eq"));

		fieldList.add(new Field("eventId", eventId, "eq"));

		fieldList.add(new Field("organizerId", organizerId, "eq"));

		fieldList.add(new Field("generator", "USER", "eq"));

		w.setAnd(fieldList);

		where.add(w);

		query.setWhere(where);

		List<Object> aggregateList = new ArrayList<Object>();

		aggregateList.add(new AggregateCountFunction(true));

		query.setAggregate(aggregateList);

		return query;
	}

	public static AnalyticsQuery alerts_views_count_query(AnalyticsQuery query, String eventId, String organizerId) {

		List<Where> where = query.getWhere();

		if (where == null)
			where = new ArrayList<Where>();

		Where w = new Where();

		List<Field> fieldList = new ArrayList<Field>();

		fieldList.add(new Field("action", "SHOW_SCREEN", "eq"));

		fieldList.add(new Field("eventId", eventId, "eq"));

		fieldList.add(new Field("screenName", "ALERT_DETAIL", "eq"));

		fieldList.add(new Field("organizerId", organizerId, "eq"));

		fieldList.add(new Field("generator", "SYSTEM", "eq"));

		w.setAnd(fieldList);

		where.add(w);

		query.setWhere(where);

		List<Object> aggregateList = new ArrayList<Object>();

		aggregateList.add(new AggregateCountFunction(true));

		query.setAggregate(aggregateList);

		return query;
	}

	public static AnalyticsQuery feeds_activities_count_query(AnalyticsQuery query, String eventId, String organizerId) {

		List<Where> where = query.getWhere();

		if (where == null)
			where = new ArrayList<Where>();

		Where w = new Where();

		List<Field> fieldList = new ArrayList<Field>();

		fieldList.add(new Field("eventId", eventId, "eq"));

		List<String> screenSelections = new ArrayList<String>();

		screenSelections.add("FEEDS_LIST");

		screenSelections.add("FEED_DETAIL");

		screenSelections.add("FEED");

		fieldList.add(new Field("screenSection", screenSelections, "in"));

		fieldList.add(new Field("organizerId", organizerId, "eq"));

		fieldList.add(new Field("generator", "USER", "eq"));

		w.setAnd(fieldList);

		where.add(w);

		query.setWhere(where);

		List<Object> aggregateList = new ArrayList<Object>();

		aggregateList.add(new AggregateCountFunction(true));

		query.setAggregate(aggregateList);

		return query;
	}

	public static AnalyticsQuery activities_feedback_collection_count_query(AnalyticsQuery query, String eventId, String organizerId) {

		List<Where> where = query.getWhere();

		if (where == null)
			where = new ArrayList<Where>();

		Where w = new Where();

		List<Field> fieldList = new ArrayList<Field>();

		fieldList.add(new Field("eventId", eventId, "eq"));

		fieldList.add(new Field("object", "POST_ACTIVITY_FEEDBACK", "eq"));

		fieldList.add(new Field("organizerId", organizerId, "eq"));

		fieldList.add(new Field("generator", "USER", "eq"));

		w.setAnd(fieldList);

		where.add(w);

		query.setWhere(where);

		List<Object> aggregateList = new ArrayList<Object>();

		aggregateList.add(new AggregateCountFunction(true));

		query.setAggregate(aggregateList);

		return query;
	}

	public static AnalyticsQuery view_activities_query(AnalyticsQuery query, String eventId, String organizerId) {

		List<Where> where = query.getWhere();

		List<String> groupBy = new ArrayList<String>();

		groupBy.add("activityId");

		query.setGroupBy(groupBy);

		if (where == null)
			where = new ArrayList<Where>();

		Where w = new Where();

		List<Field> fieldList = new ArrayList<Field>();

		fieldList.add(new Field("action", "SHOW_SCREEN", "eq"));

		fieldList.add(new Field("screenName", "ACTIVITY_DETAIL", "eq"));

		fieldList.add(new Field("eventId", eventId, "eq"));

		fieldList.add(new Field("organizerId", organizerId, "eq"));

		fieldList.add(new Field("generator", "SYSTEM", "eq"));

		w.setAnd(fieldList);

		where.add(w);

		query.setWhere(where);

		List<Object> aggregateList = new ArrayList<Object>();

		aggregateList.add(new AggregateCountFunction(1));

		query.setAggregate(aggregateList);

		List<Object> orderBy = new ArrayList<Object>();

		orderBy.add(new OrderBy("count", "desc"));

		query.setOrderBy(orderBy);

		return query;
	}

	public static AnalyticsQuery favorites_activities_query(AnalyticsQuery query, String eventId, String organizerId) {

		List<Where> where = query.getWhere();

		List<String> groupBy = new ArrayList<String>();

		groupBy.add("activityId");

		query.setGroupBy(groupBy);

		if (where == null)
			where = new ArrayList<Where>();

		Where w = new Where();

		List<Field> fieldList = new ArrayList<Field>();

		fieldList.add(new Field("object", "ACTIVITY_ADD_TO_FAVORITES", "eq"));

		fieldList.add(new Field("eventId", eventId, "eq"));

		fieldList.add(new Field("organizerId", organizerId, "eq"));

		fieldList.add(new Field("generator", "USER", "eq"));

		w.setAnd(fieldList);

		where.add(w);

		query.setWhere(where);

		List<Object> aggregateList = new ArrayList<Object>();

		aggregateList.add(new AggregateCountFunction(2));

		query.setAggregate(aggregateList);

		List<Object> orderBy = new ArrayList<Object>();

		orderBy.add(new OrderBy("count", "desc"));

		query.setOrderBy(orderBy);

		return query;
	}
	
	public static AnalyticsQuery event_site_visits_count_query(AnalyticsQuery query, String eventId, String organizerId) {

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

		aggregateList.add(new AggregateCountFunction(1));

		query.setAggregate(aggregateList);

		return query;
	}

}
