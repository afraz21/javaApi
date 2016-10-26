package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.iqvis.nvolv3.domain.EventAlert;
import org.iqvis.nvolv3.serializer.JodaDateTimeJsonSerializer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class MobileEventAlert implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String eventId;

	private String title;

	private String description;

	private String summary;

	private String linkType;

	private String linkId;

	private String gmtTime;

	@JsonIgnore
	public String getGmtTime() {
		return gmtTime;
	}

	public void setGmtTime(String gmtTime) {
		this.gmtTime = gmtTime;
	}

	@JsonSerialize(using = JodaDateTimeJsonSerializer.class)
	private DateTime startDate;

	public Picture logo;

	public Picture getLogo() {
		return logo;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.id.equals(((MobileEventAlert) obj).getId());
	}

	public void setLogo(Picture logo) {
		this.logo = logo;
	}

	@SuppressWarnings("deprecation")
	public MobileEventAlert(EventAlert ea) throws ParseException {
		if (ea != null) {
			id = ea.getId();
			eventId = ea.getEventId();
			title = ea.getTitle();
			description = ea.getDescription();
			summary = ea.getSummary();
			if (ea.getActivityId() != null && !ea.getActivityId().trim().equalsIgnoreCase("")) {
				linkId = ea.getActivityId();
				linkType = "Activity";
			}
			else if (ea.getPersonnelId() != null && !ea.getPersonnelId().trim().equalsIgnoreCase("")) {
				linkId = ea.getPersonnelId();
				linkType = "Personnel";
			}
			else if (ea.getSponsorId() != null && !ea.getSponsorId().trim().equalsIgnoreCase("")) {
				linkId = ea.getSponsorId();
				linkType = "Sponsor";
			}

			Calendar c = Calendar.getInstance();

			String s = ea.getStartTime();

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

			if (s != null) {
				Date startTime = simpleDateFormat.parse(s);

				c.setTime(ea.getStartDate().toDate());

				c.set(Calendar.HOUR, startTime.getHours());

				c.set(Calendar.MINUTE, startTime.getMinutes());

				c.set(Calendar.SECOND, startTime.getSeconds());

				startDate = new DateTime(c.getTime());
			}
			// System.out.println(startTime);

			// c.setTimeZone(TimeZone.getTimeZone("UTC"));

			// if (ea.getPicture() != null)
			// logo = new Picture(ea.getPicture());

			if (ea.getPictureO() != null)
				logo = new Picture(ea.getPictureO());

		}
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	@JsonProperty
	private boolean getLinked() {
		return (linkId != null && !linkId.trim().equalsIgnoreCase("") ? true : false);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public DateTime getStartDate() {

		if (gmtTime != null && gmtTime != "") {

			DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");

			return format.parseDateTime(gmtTime);

		}

		return startDate;
	}

	public void setStartDate(DateTime startDate) {

		this.startDate = startDate;

	}

}
