package org.iqvis.nvolv3.domain;

import java.io.Serializable;

import org.iqvis.nvolv3.audit.Audit;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A simple POJO representing a Media
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */
@Document
public class Media extends Audit implements Serializable {

	public Media() {
		super();
	}

	private static final long serialVersionUID = -1527566248002296042L;

	private String url;

	private String urlSmall;

	private String urlLarge;

	private String urlMedium;

	private String urlThumb;

	private String urlCustom;

	private String extention;

	private String dementions;

	private String type;

	private String mediaContainer;

	public String getMediaContainer() {
		return mediaContainer;
	}

	public void setMediaContainer(String mediaContainer) {
		this.mediaContainer = mediaContainer;
	}

	public Media(Media media) {
		super();
		this.setId(media.getId());
		this.setUrl(media.getUrl());
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrlSmall() {
		return urlSmall;
	}

	public void setUrlSmall(String urlSmall) {
		this.urlSmall = urlSmall;
	}

	public String getUrlLarge() {
		return urlLarge;
	}

	public void setUrlLarge(String urlLarge) {
		this.urlLarge = urlLarge;
	}

	public String getUrlMedium() {
		return urlMedium;
	}

	public void setUrlMedium(String urlMedium) {
		this.urlMedium = urlMedium;
	}

	public String getUrlThumb() {
		return urlThumb;
	}

	public void setUrlThumb(String urlThumb) {
		this.urlThumb = urlThumb;
	}

	public String getExtention() {
		return extention;
	}

	public void setExtention(String extention) {
		this.extention = extention;
	}

	public String getDementions() {
		return dementions;
	}

	public void setDementions(String dementions) {
		this.dementions = dementions;
	}

	public String getUrlCustom() {
		return urlCustom;
	}

	public void setUrlCustom(String urlCustom) {
		this.urlCustom = urlCustom;
	}


}
