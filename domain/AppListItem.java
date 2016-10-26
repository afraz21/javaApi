package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;

public class AppListItem implements Serializable {

	public AppListItem(String id, String name, String appType) {
		super();
		this.id = id;
		this.name = name;
		this.appType = appType;
	}

	public AppListItem(String name, String appType) {
		super();
		this.name = name;
		this.appType = appType;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String name;

	private String appType;

	private String appName;

	private String appDescription;

	private Object appLogo;

	private String organizerId;

	private Boolean linkEventModeration;

	private Boolean show_news = false;

	private Boolean show_profile_screen = true;

	private Boolean profile_screen_skippable = true;

	private Boolean show_organizer_logo_on_events_listing = true;

	public Boolean getLinkEventModeration() {
		return linkEventModeration;
	}

	public void setLinkEventModeration(Boolean linkEventModeration) {
		this.linkEventModeration = linkEventModeration;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppDescription() {
		return appDescription;
	}

	public void setAppDescription(String appDescription) {
		this.appDescription = appDescription;
	}

	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public Object getAppLogo() {
		return appLogo;
	}

	public void setAppLogo(Object appLogo) {
		this.appLogo = appLogo;
	}

	public Boolean getShow_news() {
		return show_news;
	}

	public void setShow_news(Boolean show_news) {
		this.show_news = show_news;
	}

	public Boolean getShow_profile_screen() {
		return show_profile_screen;
	}

	public void setShow_profile_screen(Boolean show_profile_screen) {
		this.show_profile_screen = show_profile_screen;
	}

	public Boolean getProfile_screen_skippable() {
		return profile_screen_skippable;
	}

	public void setProfile_screen_skippable(Boolean profile_screen_skippable) {
		this.profile_screen_skippable = profile_screen_skippable;
	}

	public Boolean getShow_organizer_logo_on_events_listing() {
		return show_organizer_logo_on_events_listing;
	}

	public void setShow_organizer_logo_on_events_listing(Boolean show_organizer_logo_on_events_listing) {
		this.show_organizer_logo_on_events_listing = show_organizer_logo_on_events_listing;
	}

	public static List<AppListItem> toList(List<AppConfiguration> applicationList, boolean isOrganizer) {

		List<AppListItem> list = new ArrayList<AppListItem>();
		if (applicationList != null) {

			for (AppConfiguration appConfiguration : applicationList) {

				// AppListItem appItem = new
				// AppListItem(appConfiguration.getId(),
				// appConfiguration.getName(), isOrganizer == true ?
				// "WHITELABEL" : "GENERAL");

				AppListItem appItem = new AppListItem(appConfiguration.getId(), appConfiguration.getName(), appConfiguration.getAppType());

				appItem.setAppDescription(appConfiguration.getAppDescription());

				appItem.setAppLogo(appConfiguration.getAppLogo());

				appItem.setAppName(appConfiguration.getName());

				appItem.setOrganizerId(appConfiguration.getOrganizerId());

				appItem.setLinkEventModeration(appConfiguration.getLinkEventModeration());

				appItem.setShow_news(appConfiguration.getShow_news());

				appItem.setShow_profile_screen(appConfiguration.getShow_profile_screen());

				appItem.setProfile_screen_skippable(appConfiguration.getProfile_screen_skippable());

				appItem.setShow_organizer_logo_on_events_listing(appConfiguration.getShow_organizer_logo_on_events_listing());

				list.add(appItem);
			}
		}
		return list;
	}

	public static List<AppListItem> toListPartner(List<AppConfiguration> applicationList, String partnerId) {

		List<AppListItem> list = new ArrayList<AppListItem>();

		if (applicationList != null) {

			for (AppConfiguration appConfiguration : applicationList) {

				// AppListItem appItem = new
				// AppListItem(appConfiguration.getId(),
				// appConfiguration.getName(),
				// !appConfiguration.getOrganizerId().equals(partnerId) == true
				// ? "WHITELABEL" : "GENERAL");

				AppListItem appItem = new AppListItem(appConfiguration.getId(), appConfiguration.getName(), appConfiguration.getAppType());

				appItem.setAppDescription(appConfiguration.getAppDescription());

				appItem.setAppLogo(appConfiguration.getAppLogo());

				appItem.setAppName(appConfiguration.getName());

				appItem.setOrganizerId(appConfiguration.getOrganizerId());

				appItem.setLinkEventModeration(appConfiguration.getLinkEventModeration());

				appItem.setShow_news(appConfiguration.getShow_news());

				appItem.setShow_profile_screen(appConfiguration.getShow_profile_screen());

				appItem.setProfile_screen_skippable(appConfiguration.getProfile_screen_skippable());

				appItem.setShow_organizer_logo_on_events_listing(appConfiguration.getShow_organizer_logo_on_events_listing());

				list.add(appItem);
			}
		}
		return list;
	}

	public static List<AppListItem> toList(List<AppConfiguration> applicationList) {

		List<AppListItem> list = new ArrayList<AppListItem>();
		if (applicationList != null) {

			for (AppConfiguration appConfiguration : applicationList) {

				AppListItem appItem = new AppListItem(appConfiguration.getId(), appConfiguration.getName(), appConfiguration.getAppType());

				appItem.setAppDescription(appConfiguration.getAppDescription());

				appItem.setAppLogo(appConfiguration.getAppLogo());

				appItem.setAppName(appConfiguration.getName());

				appItem.setOrganizerId(appConfiguration.getOrganizerId());

				appItem.setLinkEventModeration(appConfiguration.getLinkEventModeration());

				appItem.setShow_news(appConfiguration.getShow_news());

				appItem.setShow_profile_screen(appConfiguration.getShow_profile_screen());

				appItem.setProfile_screen_skippable(appConfiguration.getProfile_screen_skippable());

				appItem.setShow_organizer_logo_on_events_listing(appConfiguration.getShow_organizer_logo_on_events_listing());

				list.add(appItem);
			}
		}
		return list;
	}

	@Override
	public boolean equals(Object obj) {

		AppListItem object = (AppListItem) obj;

		return this.id.equals(object.getId());
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

}
