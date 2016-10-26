package org.iqvis.nvolv3.objectchangelog.domain;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.iqvis.nvolv3.serializer.JodaDateTimeJsonSerializer;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dataChangeLog")
public class DataChangeLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@JsonSerialize(using = JodaDateTimeJsonSerializer.class)
	private DateTime timestamp;

	private List<String> eventIds;

	private String eventObject;

	private String action;

	private String subObject;

	private String subObjectId;

	private String subObjectAction;

	private String subObjectType;

	public DataChangeLog(DateTime timestamp, List<String> eventIds, String eventObject, String action, String subObject, String subObjectId, String subObjectAction, String subObjectType) {
		super();
		this.timestamp = timestamp;
		this.eventIds = eventIds;
		this.eventObject = eventObject;
		this.action = action;
		this.subObject = subObject;
		this.subObjectId = subObjectId;
		this.subObjectAction = subObjectAction;
		this.subObjectType = subObjectType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}

	public List<String> getEventIds() {
		return eventIds;
	}

	public void setEventIds(List<String> eventIds) {
		this.eventIds = eventIds;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Object getSubObject() {
		return subObject;
	}

	public String getSubObjectAction() {
		return subObjectAction;
	}

	public void setSubObjectAction(String subObjectAction) {
		this.subObjectAction = subObjectAction;
	}

	public String getSubObjectType() {
		return subObjectType;
	}

	public void setSubObjectType(String subObjectType) {
		this.subObjectType = subObjectType;
	}

	public String getEventObject() {
		return eventObject;
	}

	public void setEventObject(String eventObject) {
		this.eventObject = eventObject;
	}

	public String getSubObjectId() {
		return subObjectId;
	}

	public void setSubObjectId(String subObjectId) {
		this.subObjectId = subObjectId;
	}

	public void setSubObject(String subObject) {
		this.subObject = subObject;
	}

	@Override
	public boolean equals(Object obj) {
		
		return this.subObjectId.equals(((DataChangeLog)obj).getSubObjectId());
	}
	
	

}
