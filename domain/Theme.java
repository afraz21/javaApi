package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="eventThemes")
public class Theme implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;
	
	private String theme;
	
	private List<ThemeColor> colours;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public List<ThemeColor> getColours() {
		return colours;
	}

	public void setColours(List<ThemeColor> colours) {
		this.colours = colours;
	}
	
}
