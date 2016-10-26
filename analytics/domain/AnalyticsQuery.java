package org.iqvis.nvolv3.analytics.domain;

import java.io.Serializable;
import java.util.List;

public class AnalyticsQuery implements Serializable {

	public AnalyticsQuery() {
		super();
	}

	public AnalyticsQuery(String fromDate, String toDate) {
		super();
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	public AnalyticsQuery(String fromDate, String toDate, List<String> groupBy, List<Where> where, List<Object> aggregate, List<Object> orderBy, List<Object> _agroupBy) {
		super();
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.groupBy = groupBy;
		this.where = where;
		this.aggregate = aggregate;
		this.orderBy = orderBy;
		this._agroupBy = _agroupBy;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String fromDate;

	private String toDate;

	private List<String> groupBy;

	private List<Where> where;

	private List<Object> aggregate;

	private List<Object> orderBy;

	private List<Object> _agroupBy;
	
	private Object geoLocation;

	public Object getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(Object geoLocation) {
		this.geoLocation = geoLocation;
	}

	public List<Object> get_agroupBy() {
		return _agroupBy;
	}

	public void set_agroupBy(List<Object> _agroupBy) {
		this._agroupBy = _agroupBy;
	}

	public List<Where> getWhere() {
		return where;
	}

	public void setWhere(List<Where> where) {
		this.where = where;
	}

	public String getFromDate() {

		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {

		return toDate;

	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public List<String> getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(List<String> groupBy) {
		this.groupBy = groupBy;
	}

	public List<Object> getAggregate() {
		return aggregate;
	}

	public void setAggregate(List<Object> aggregate) {
		this.aggregate = aggregate;
	}

	public List<Object> getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(List<Object> orderBy) {
		this.orderBy = orderBy;
	}

	public String getToDateTime() {

		return toDate;
	}

	public String getFromDateTime() {

		return fromDate;
	}

}
