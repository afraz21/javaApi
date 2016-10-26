package org.iqvis.nvolv3.mobile.app.config.beans;

import java.io.Serializable;
import java.util.List;

import org.iqvis.nvolv3.mobile.bean.DataCMS;
import org.iqvis.nvolv3.mobile.bean.DataCMSMenu;

/**
 * A simple POJO representing a ReferenceData
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public class General implements Serializable {

	private static final long serialVersionUID = -3527566248002296042L;

	private Boolean isMultiEvent = false;

	private Boolean showOrganizerNews = false;

	private List<DataCMS> labels = null;

	private List<DataCMS> texts = null;
	
	private List<DataCMS> tempTexts = null;

	private List<DataCMS> messages = null;

	private List<DataCMSMenu> menu = null;

	public General() {

	}

	public List<DataCMS> getTempTexts() {
		return tempTexts;
	}

	public void setTempTexts(List<DataCMS> tempTexts) {
		this.tempTexts = tempTexts;
	}

	public Boolean getIsMultiEvent() {
		return isMultiEvent;
	}

	public void setIsMultiEvent(Boolean isMultiEvent) {
		this.isMultiEvent = isMultiEvent;
	}

	public Boolean getShowOrganizerNews() {
		return showOrganizerNews;
	}

	public void setShowOrganizerNews(Boolean showOrganizerNews) {
		this.showOrganizerNews = showOrganizerNews;
	}

	public List<DataCMS> getLabels() {
		return labels;
	}

	public void setLabels(List<DataCMS> labels) {
		this.labels = labels;
	}

	public List<DataCMS> getTexts() {
		return texts;
	}

	public void setTexts(List<DataCMS> texts) {
		this.texts = texts;
	}

	public List<DataCMS> getMessages() {
		return messages;
	}

	public void setMessages(List<DataCMS> messages) {
		this.messages = messages;
	}

	public List<DataCMSMenu> getMenu() {
		return menu;
	}

	public void setMenu(List<DataCMSMenu> menu) {
		this.menu = menu;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
