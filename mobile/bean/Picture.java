package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;

import org.iqvis.nvolv3.domain.Media;

public class Picture implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1851256088675981503L;

	private String id;

	private String url;

	private String urlSmall;

	private String urlLarge;

	private String urlMedium;

	private String urlThumb;

	public Picture() {

	}

	public Picture(Media media) {

		if (media != null) {

			this.id = media.getId();

			this.url = media.getUrl();

			this.urlSmall = media.getUrlSmall();

			this.urlLarge = media.getUrlLarge();

			this.urlMedium = media.getUrlMedium();

			this.urlThumb = media.getUrlThumb();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {

		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
