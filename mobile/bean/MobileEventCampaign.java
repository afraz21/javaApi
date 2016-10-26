package org.iqvis.nvolv3.mobile.bean;

import java.util.List;

public class MobileEventCampaign {

	private String name;

	private List<MobileEventCampaignParticipants> participants;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MobileEventCampaignParticipants> getParticipants() {
		return participants;
	}

	public void setParticipants(List<MobileEventCampaignParticipants> participants) {
		this.participants = participants;
	}

}
