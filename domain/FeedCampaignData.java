package org.iqvis.nvolv3.domain;

import java.io.Serializable;

public class FeedCampaignData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6323293903779235814L;

	private String campaignId;

	private String campaignName;

	private String participantId;

	private String participantName;

	private String sponserName;

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	public String getSponserName() {
		return sponserName;
	}

	public void setSponserName(String sponserName) {
		this.sponserName = sponserName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
