package org.iqvis.nvolv3.domain;

import java.io.Serializable;

public class EventLink implements Serializable{
	
	public EventLink() {
		super();
	}

	public EventLink(String appId, String ownedBy) {
		super();
		this.appId = appId;
		this.ownedBy = ownedBy;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String appId;
	
	private String ownedBy;
	
	private String branchIOurl;

	public String getBranchIOurl() {
		return branchIOurl;
	}

	public void setBranchIOurl(String branchIOurl) {
		this.branchIOurl = branchIOurl;
	}

	public String getOwnedBy() {
		return ownedBy;
	}

	public void setOwnedBy(String ownedBy) {
		this.ownedBy = ownedBy;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		
		if(obj==null){
			return false;
		}
		
		EventLink link=(EventLink) obj;
		
		return this.appId.equals(link.getAppId());
	}
	
	

}
