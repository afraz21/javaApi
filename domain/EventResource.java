package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.List;

import org.iqvis.nvolv3.audit.Audit;
import org.iqvis.nvolv3.bean.MultiLingual;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A simple POJO representing a Event
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

@Document
public class EventResource extends Audit implements Serializable {

	private static final long serialVersionUID = -5527566248002296042L;

	private String name;

	private String type;

	private String organizerId;

	private String eventId;

	private Boolean isDeleted;

	private Boolean isActive;

	private List<MultiLingual> multiLingual;

	private String attachmentURL;

	@DBRef(lazy = true)
	private Media picture;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Media getPicture() {
		return picture;
	}

	public void setPicture(Media picture) {
		this.picture = picture;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MultiLingual> getMultiLingual() {
		return multiLingual;
	}

	public void setMultiLingual(List<MultiLingual> multiLingual) {
		this.multiLingual = multiLingual;
	}

	public String getAttachmentURL() {
		return attachmentURL;
	}

	public void setAttachmentURL(String attachmentURL) {
		this.attachmentURL = attachmentURL;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.getId().equals(((EventResource) obj).getId());
	}

}
