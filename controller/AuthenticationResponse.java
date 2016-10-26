package org.iqvis.nvolv3.controller;

import java.io.Serializable;
import java.util.List;

public class AuthenticationResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Boolean organizerApproved;
	
	private Boolean sponsorApproved; 
	
	private List<String> userType;
	
	private boolean authenticated=false;

	public Boolean getOrganizerApproved() {
		return organizerApproved==null?false:organizerApproved;
	}

	public void setOrganizerApproved(Boolean organizerApproved) {
		this.organizerApproved = organizerApproved;
	}

	public Boolean getSponsorApproved() {
		return sponsorApproved==null?false:sponsorApproved;
	}

	public void setSponsorApproved(Boolean sponsorApproved) {
		this.sponsorApproved = sponsorApproved;
	}

	public List<String> getUserType() {
		return userType;
	}

	public void setUserType(List<String> userType) {
		this.userType = userType;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	} 
}
