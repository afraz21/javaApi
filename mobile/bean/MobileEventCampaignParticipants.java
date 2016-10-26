package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

import org.iqvis.nvolv3.bean.MultiLingual;

public class MobileEventCampaignParticipants implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String campaignId;

	private String participantId;

	private List<String> sponsorId;

	private List<MultiLingual> multiLingual;

	private Picture logo;

	private Picture overlayLandscape;

	private Picture overlayPotrait;

	private Integer sortOrder;

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public List<String> getSponsorId() {
		return sponsorId;
	}

	public void setSponsorId(List<String> sponsorId) {
		this.sponsorId = sponsorId;
	}

	public List<MultiLingual> getMultiLingual() {
		return multiLingual;
	}

	public void setMultiLingual(List<MultiLingual> multiLingual) {
		this.multiLingual = multiLingual;
	}

	public Picture getLogo() {
		return logo;
	}

	public void setLogo(Picture logo) {
		this.logo = logo;
	}

	public Picture getOverlayLandscape() {
		return overlayLandscape;
	}

	public void setOverlayLandscape(Picture overlayLandscape) {
		this.overlayLandscape = overlayLandscape;
	}

	public Picture getOverlayPotrait() {
		return overlayPotrait;
	}

	public void setOverlayPotrait(Picture overlayPotrait) {
		this.overlayPotrait = overlayPotrait;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
	
		if(obj==null)
			return true;
				
		return this.getCampaignId().equals(((MobileEventCampaignParticipants) obj).getCampaignId());
	}

}
