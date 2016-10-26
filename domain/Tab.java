package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.List;

public class Tab implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String tab;

	private String template;

	private List<String> additional_attributes;

	private List<EventConfigurationMultilingual> multiLingual;

	public List<EventConfigurationMultilingual> getMultiLingual() {
		return multiLingual;
	}

	public void setMultiLingual(List<EventConfigurationMultilingual> multiLingual) {
		this.multiLingual = multiLingual;
	}

	public String getTab() {
		return tab;
	}

	public void setTab(String tab) {
		this.tab = tab;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public List<String> getAdditional_attributes() {
		return additional_attributes;
	}

	public void setAdditional_attributes(List<String> additional_attributes) {
		this.additional_attributes = additional_attributes;
	}

}
