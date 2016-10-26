package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.List;

import org.iqvis.nvolv3.mobile.bean.DataCMS;

public class EventConfigurationUpdate implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String themeId;
	
	private List<Tab> tabs;
	
	private String landing_screen;
	
	private List<Tab> tempTabs;
	
	private ActivityTimeGroup activities_list;
	
	private List<DataCMS> texts;
	
	private String branchIOURL;

	public String getBranchIOURL() {
		return branchIOURL;
	}

	public void setBranchIOURL(String branchIOURL) {
		this.branchIOURL = branchIOURL;
	}

	public String getLanding_screen() {
		return landing_screen;
	}

	public void setLanding_screen(String landing_screen) {
		this.landing_screen = landing_screen;
	}

	public List<Tab> getTempTabs() {
		return tempTabs;
	}

	public void setTempTabs(List<Tab> tempTabs) {
		this.tempTabs = tempTabs;
	}

	public String getThemeId() {
		return themeId;
	}

	public void setThemeId(String themeId) {
		this.themeId = themeId;
	}

	public List<Tab> getTabs() {
		return tabs;
	}

	public void setTabs(List<Tab> tabs) {
		this.tabs = tabs;
	}

	public List<DataCMS> getTexts() {
		return texts;
	}

	public ActivityTimeGroup getActivities_list() {
		return activities_list;
	}

	public void setActivities_list(ActivityTimeGroup activities_list) {
		this.activities_list = activities_list;
	}

	public void setTexts(List<DataCMS> texts) {
		this.texts = texts;
	}
	
	
}
