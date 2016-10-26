package org.iqvis.nvolv3.mobile.app.config.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.AppListItem;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;
import org.iqvis.nvolv3.mobile.app.config.dao.AppConfigDao;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.search.OrderBy;
import org.iqvis.nvolv3.search.QueryOperator;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@SuppressWarnings("restriction")
@Service(Constants.SERVICE_APP_CONFIG)
@Transactional
public class AppConfigServiceImpl implements AppConfigService {

	protected static Logger logger = Logger.getLogger("service");

	// Daos

	@Autowired
	private AppConfigDao appConfigurationDao;

	// /Services

	@Resource(name = Constants.SERVICE_EVENT)
	private EventService eventService;

	public Page<AppConfiguration> getAll(Criteria appConfigurationSearch, String organizerId, HttpServletRequest request, Pageable pageAble) {

		return appConfigurationDao.getAll(Utils.parseCriteria(appConfigurationSearch, ""), organizerId, pageAble);
	}

	public AppConfiguration get(String appId, String organizerId) {

		return appConfigurationDao.get(appId, organizerId);
	}

	public AppConfiguration add(AppConfiguration appConfiguration) throws Exception {

		if (appConfiguration.getSignup().getShowSignUpScreen() == true) {

			appConfiguration.setShow_profile_screen(true);

			if (appConfiguration.getSignup().getSignUpManadatory() == true) {

				appConfiguration.setProfile_screen_skippable(false);
			}
			else {

				appConfiguration.setProfile_screen_skippable(true);
			}
		}
		else {

			appConfiguration.setShow_profile_screen(false);

			appConfiguration.setProfile_screen_skippable(false);
		}

		AppConfiguration appConfig = appConfigurationDao.add(appConfiguration);

		updateEventApps(appConfig);

		return appConfig;
	}

	public Boolean delete(String id) {

		return appConfigurationDao.delete(id);
	}

	public AppConfiguration edit(AppConfiguration appConfiguration, String referenceDataType) throws Exception, NotFoundException {

		AppConfiguration existingAppConfiguration = null;

		if (null != referenceDataType && !StringUtils.isEmpty(referenceDataType)) {

			existingAppConfiguration = get(referenceDataType, appConfiguration.getOrganizerId());
		}

		if (null == existingAppConfiguration) {

			throw new NotFoundException(referenceDataType, "AppConfiguration");
		}

		if (null != appConfiguration.getName()) {

			existingAppConfiguration.setName(appConfiguration.getName());
		}

		if (null != appConfiguration.getAppLogoO()) {

			existingAppConfiguration.setAppLogo(appConfiguration.getAppLogoO());
		}

		if (null != appConfiguration.getAppDescription()) {

			existingAppConfiguration.setAppDescription(appConfiguration.getAppDescription());
		}

		if (appConfiguration.getShowProfileScreenOnStart() != null) {

			existingAppConfiguration.setShowProfileScreenOnStart(appConfiguration.getShowProfileScreenOnStart());
		}

		if (appConfiguration.getUserProfileIsModrated() != null) {

			existingAppConfiguration.setUserProfileIsModrated(appConfiguration.getUserProfileIsModrated());
		}

		if (null != appConfiguration.getAppType()) {

			existingAppConfiguration.setAppType(appConfiguration.getAppType());
		}

		if (null != appConfiguration.getBranchIOSDKey()) {

			existingAppConfiguration.setBranchIOSDKey(appConfiguration.getBranchIOSDKey());
		}

		if (null != appConfiguration.getAndroidAppUrl()) {

			existingAppConfiguration.setAndroidAppUrl(appConfiguration.getAndroidAppUrl());
		}

		if (null != appConfiguration.getIosAppUrl()) {

			existingAppConfiguration.setIosAppUrl(appConfiguration.getIosAppUrl());
		}

		if (null != appConfiguration.getOrganizerId()) {

			existingAppConfiguration.setOrganizerId(appConfiguration.getOrganizerId());
		}

		if (appConfiguration.getIsDeleted() != null) {

			existingAppConfiguration.setIsDeleted(appConfiguration.getIsDeleted());
		}

		if (appConfiguration.getAppBigLogoO() != null) {

			existingAppConfiguration.setAppBigLogo(appConfiguration.getAppBigLogoO());
		}

		if (appConfiguration.getSplashImageO() != null) {

			existingAppConfiguration.setSplashImage(appConfiguration.getSplashImageO());
		}

		if (appConfiguration.getAboutBannerO() != null) {

			existingAppConfiguration.setAboutBanner(appConfiguration.getAboutBannerO());
		}

		if (appConfiguration.getLinkEventModeration() != null) {

			existingAppConfiguration.setLinkEventModeration(appConfiguration.getLinkEventModeration());
		}

		/*
		 * if (null != appConfiguration.getMenus()) {
		 * existingAppConfiguration.setMenus(appConfiguration.getMenus()); }
		 * 
		 * if (null != appConfiguration.getTexts()) {
		 * 
		 * existingAppConfiguration.setTexts(appConfiguration.getTexts()); }
		 * 
		 * if (null != appConfiguration.getSupported_languages()) {
		 * 
		 * existingAppConfiguration.setSupported_languages(appConfiguration.
		 * getSupported_languages()); }
		 * 
		 * if (null != appConfiguration.getOrganizer_info()) {
		 * 
		 * existingAppConfiguration.setOrganizer_info(appConfiguration.
		 * getOrganizer_info()); }
		 */
		if (null != appConfiguration.getIsDeleted()) {

			existingAppConfiguration.setIsDeleted(appConfiguration.getIsDeleted());
		}

		if (null != appConfiguration.getIsActive()) {

			existingAppConfiguration.setIsActive(appConfiguration.getIsActive());
		}

		if (null != appConfiguration.getGeneral()) {

			if (existingAppConfiguration.getGeneral() == null) {

				existingAppConfiguration.setGeneral(appConfiguration.getGeneral());
			}
			else {

				if (null != appConfiguration.getGeneral().getIsMultiEvent()) {

					existingAppConfiguration.getGeneral().setIsMultiEvent(appConfiguration.getGeneral().getIsMultiEvent());
				}

				if (null != appConfiguration.getGeneral().getLabels()) {

					existingAppConfiguration.getGeneral().setLabels(appConfiguration.getGeneral().getLabels());
				}

				if (null != appConfiguration.getGeneral().getMenu()) {

					existingAppConfiguration.getGeneral().setMenu(appConfiguration.getGeneral().getMenu());
				}

				if (null != appConfiguration.getGeneral().getMessages()) {

					existingAppConfiguration.getGeneral().setMessages(appConfiguration.getGeneral().getMessages());
				}

				if (null != appConfiguration.getGeneral().getShowOrganizerNews()) {

					existingAppConfiguration.getGeneral().setShowOrganizerNews(appConfiguration.getGeneral().getShowOrganizerNews());
				}

				if (null != appConfiguration.getGeneral().getTempTexts()) {

					existingAppConfiguration.getGeneral().setTempTexts(appConfiguration.getGeneral().getTempTexts());
				}

				if (null != appConfiguration.getGeneral().getTexts()) {

					existingAppConfiguration.getGeneral().setTexts(appConfiguration.getGeneral().getTexts());
				}

			}

			// existingAppConfiguration.setGeneral(appConfiguration.getGeneral());
		}

		if (null != appConfiguration.getSignup()) {
			existingAppConfiguration.setSignup(appConfiguration.getSignup());
		}

		if (null != appConfiguration.getEvents()) {
			existingAppConfiguration.setEvents(appConfiguration.getEvents());
		}

		if (null != appConfiguration.getTestOrganizersEmails()) {

			existingAppConfiguration.setTestOrganizersEmails(appConfiguration.getTestOrganizersEmails());
		}

		if (appConfiguration.getSignup() != null && appConfiguration.getSignup().getShowSignUpScreen() == true) {

			existingAppConfiguration.setShow_profile_screen(true);

			if (appConfiguration.getSignup().getSignUpManadatory() == true) {

				existingAppConfiguration.setProfile_screen_skippable(false);
			}
			else {

				existingAppConfiguration.setProfile_screen_skippable(true);
			}

		}
		else {

			existingAppConfiguration.setShow_profile_screen(false);

			existingAppConfiguration.setProfile_screen_skippable(false);
		}

		// if (appConfiguration.getShow_profile_screen() != null) {
		//
		// existingAppConfiguration.setShow_profile_screen(appConfiguration.getShow_profile_screen());
		// }
		//
		// if (appConfiguration.getProfile_screen_skippable() != null &&
		// appConfiguration.getShow_profile_screen() == true) {
		//
		// existingAppConfiguration.setProfile_screen_skippable(false);
		// }
		// else if (appConfiguration.getProfile_screen_skippable() != null &&
		// appConfiguration.getShow_profile_screen() == false) {
		//
		// existingAppConfiguration.setProfile_screen_skippable(true);
		// }

		AppConfiguration appConfig = appConfigurationDao.edit(existingAppConfiguration);

		// updateEventApps(appConfig);

		return appConfig;
	}

	public String getAppConfigurationDetailUrl(AppConfiguration referenceData, HttpServletRequest request) {

		return appConfigurationDao.getAppConfigurationDetailUrl(referenceData, request);
	}

	public AppConfiguration getAppObject(String appId) {
		return appConfigurationDao.getAppObject(appId);
	}

	public void updateEventApps(AppConfiguration appConfig) {

		logger.debug("Updating app events");

		if (appConfig.getEvents() != null && appConfig.getEvents().size() > 0) {

			for (String eventId : appConfig.getEvents()) {

				if (!eventId.equals("")) {

					org.iqvis.nvolv3.domain.Event event = eventService.get(eventId, appConfig.getOrganizerId());

					if (event.getEventApps() != null) {

						if (!event.getEventApps().contains(appConfig.getId())) {

							event.getEventApps().add(appConfig.getId());
						}

					}
					else {

						List<String> apps = new ArrayList<String>();

						apps.add(appConfig.getId());

						event.setEventApps(apps);
					}

					try {
						eventService.edit(event, event.getId(), event.getOrganizerId());
					}
					catch (NotFoundException e) {

						e.printStackTrace();
					}
					catch (Exception e) {

						e.printStackTrace();
					}
				}
			}
		}

	}

	public Object addETC(String appConfigurationId, String organizerId, Object appConfiguration) {

		return appConfigurationDao.addETC(appConfigurationId, organizerId, appConfiguration);
	}

	public List<AppConfiguration> get(List<String> keys, List<Object> values) {
		// TODO Auto-generated method stub

		keys.add("isDeleted");

		values.add(false);

		return appConfigurationDao.get(keys, values);
	}
	
	public org.springframework.data.domain.Page<AppListItem> getWithSearch(List<String> keys, List<Object> values,Criteria search,Pageable pageAble,String organizerId) {
		// TODO Auto-generated method stub

		keys.add("isDeleted");

		values.add(false);
		
		org.springframework.data.mongodb.core.query.Criteria criteria=Utils.parseQuery(search, "");
		
		List<Order> orderByList = new ArrayList<Order>();
		
		if (null != search && null != search.getQuery().getOrderBy() && search.getQuery().getOrderBy().size() > 0) {

			for (OrderBy orderBy : search.getQuery().getOrderBy()) {

				if (orderBy.getDirection().equalsIgnoreCase(QueryOperator.ASC)) {

					orderByList.add(new Order(Direction.ASC, orderBy.getField()));

				}
				else {

					orderByList.add(new Order(Direction.DESC, orderBy.getField()));
				}
			}
		
		}
		
		return appConfigurationDao.get(keys, values,criteria,orderByList,pageAble,organizerId);
	}

	public List<String> getIdsByEvent(String eventId, String organizerId) {

		List<String> keys = new ArrayList<String>();

		List<Object> values = new ArrayList<Object>();

		keys.add("events");

		values.add(eventId);

		keys.add("organizerId");

		values.add(organizerId);

		List<AppConfiguration> list = this.get(keys, values);

		List<String> ids = new ArrayList<String>();

		if (list != null) {

			for (AppConfiguration appConfiguration : list) {

				ids.add(appConfiguration.getId());
			}
		}

		return ids;
	}
	
	public List<AppConfiguration> get(List<String> ids){
		
		return appConfigurationDao.get(ids);
	}

}
