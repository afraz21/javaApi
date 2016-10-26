package org.iqvis.nvolv3.objectchangelog.domain;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.iqvis.nvolv3.serializer.DateTimeJsonDeSerializer;
import org.joda.time.DateTime;

public class FeedQuery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(message = "syncDateTime cant be NULL")
	@JsonDeserialize(using = DateTimeJsonDeSerializer.class)
	private DateTime syncDateTime;

	@NotNull(message = "eventId cant be NULL")
	private String eventId;

	@NotNull(message = "organizerId cant be NULL")
	private String organizerId;

	private List<FeedObject> list;

	public List<FeedObject> getList() {
		return list;
	}

	public void setList(List<FeedObject> list) {
		this.list = list;
	}

	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
	}

	public DateTime getSyncDateTime() {
		return syncDateTime;
	}

	public void setSyncDateTime(DateTime syncDateTime) {
		this.syncDateTime = syncDateTime;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

}
