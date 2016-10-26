package org.iqvis.nvolv3.service;

import javax.annotation.Resource;

import org.iqvis.nvolv3.exceptionHandler.ConstantNotExistsException;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.ISystemConstant;
import org.iqvis.nvolv3.utils.SystemConstantEnum;
import org.springframework.stereotype.Service;

@Service(Constants.FACTORY_SYSTEM_CONSTANT)
public class ConstantFactoryImpl implements ConstantFactory {

	@Resource(name = Constants.SERVICE_SYSTEMT_CONSTANT)
	SystemConstantService systemtConstantService;

	public static String SINCH_SIGNUP_API_URL;

	public static String APP_CONFIG_OAUTH_TWITTER_CALLBACK;

	public static String APP_CONFIG_OAUTH_TWITTER_API_SECRET;

	public static String APP_CONFIG_OAUTH_TWITTER_API_KEY;

	public static String APP_CONFIG_OAUTH_FACEBOOK_CALLBACK;

	public static String APP_CONFIG_OAUTH_FACEBOOK_API_SECRET;

	public static String APP_CONFIG_OAUTH_FACEBOOK_API_KEY;

	public static String APP_CONFIG_OAUTH_LINKEDIN_CALLBACK;

	public static String APP_CONFIG_OAUTH_LINKEDIN_API_SECRET;

	public static String APP_CONFIG_OAUTH_LINKEDIN_API_KEY;

	public static String ANALYTICS_GEO_LOCATION_AGGREGATION_REPORTING_URL;

	public static String ANALYTICS_CUSTOM_AGGREGATION_REPORTING_URL;

	public static String ANALYTICS_PRIMARY_URL;

	public static String ANALYTICS_CLIENT_ID;

	public static Integer APP_STARTUP_PREVIOUS_DAYS;

	public static Integer EVENT_SEARCH_PREVIOUS_DAYS;

	public static String DOWNLOAD_FILE_URL;

	public static Integer TWITTER_FEED_FETCH_COUNT;
	
	public static String SALES_EMAIL;

	public void fillConstats() throws ConstantNotExistsException {

		this.getValue(SystemConstantEnum.ENUM_SINCH_SIGNUP_API_URL);

		this.getValue(SystemConstantEnum.ENUM_APP_CONFIG_OAUTH_TWITTER_CALLBACK);

		this.getValue(SystemConstantEnum.ENUM_APP_CONFIG_OAUTH_TWITTER_API_SECRET);

		this.getValue(SystemConstantEnum.ENUM_APP_CONFIG_OAUTH_TWITTER_API_KEY);

		this.getValue(SystemConstantEnum.ENUM_APP_CONFIG_OAUTH_FACEBOOK_CALLBACK);

		this.getValue(SystemConstantEnum.ENUM_APP_CONFIG_OAUTH_FACEBOOK_API_SECRET);

		this.getValue(SystemConstantEnum.ENUM_APP_CONFIG_OAUTH_FACEBOOK_API_KEY);

		this.getValue(SystemConstantEnum.ENUM_APP_CONFIG_OAUTH_LINKEDIN_CALLBACK);

		this.getValue(SystemConstantEnum.ENUM_APP_CONFIG_OAUTH_LINKEDIN_API_SECRET);

		this.getValue(SystemConstantEnum.ENUM_APP_CONFIG_OAUTH_LINKEDIN_API_KEY);

		this.getValue(SystemConstantEnum.ENUM_ANALYTICS_GEO_LOCATION_AGGREGATION_REPORTING_URL);

		this.getValue(SystemConstantEnum.ENUM_ANALYTICS_CUSTOM_AGGREGATION_REPORTING_URL);

		this.getValue(SystemConstantEnum.ENUM_ANALYTICS_PRIMARY_URL);

		this.getValue(SystemConstantEnum.ENUM_ANALYTICS_CLIENT_ID);

		this.getValue(SystemConstantEnum.ENUM_APP_STARTUP_PREVIOUS_DAYS);

		this.getValue(SystemConstantEnum.ENUM_EVENT_SEARCH_PREVIOUS_DAYS);

		this.getValue(SystemConstantEnum.ENUM_DOWNLOAD_FILE_URL);
		
		this.getValue(SystemConstantEnum.ENUM_TWITTER_FEED_FETCH_COUNT);
		
		this.getValue(SystemConstantEnum.ENUM_SALES_EMAIL);
	}

	public Object getValue(SystemConstantEnum constant) throws ConstantNotExistsException {

		switch (constant) {

		case ENUM_SINCH_SIGNUP_API_URL:

			return SINCH_SIGNUP_API_URL == null ? SINCH_SIGNUP_API_URL = systemtConstantService.get(ISystemConstant.SINCH_SIGNUP_API_URL).toString() : SINCH_SIGNUP_API_URL;

		case ENUM_APP_CONFIG_OAUTH_TWITTER_CALLBACK:

			return APP_CONFIG_OAUTH_TWITTER_CALLBACK == null ? APP_CONFIG_OAUTH_TWITTER_CALLBACK = systemtConstantService.get(ISystemConstant.APP_CONFIG_OAUTH_TWITTER_CALLBACK).toString() : APP_CONFIG_OAUTH_TWITTER_CALLBACK;

		case ENUM_APP_CONFIG_OAUTH_TWITTER_API_SECRET:

			return APP_CONFIG_OAUTH_TWITTER_API_SECRET == null ? APP_CONFIG_OAUTH_TWITTER_API_SECRET = systemtConstantService.get(ISystemConstant.APP_CONFIG_OAUTH_TWITTER_API_SECRET).toString() : APP_CONFIG_OAUTH_TWITTER_API_SECRET;

		case ENUM_APP_CONFIG_OAUTH_TWITTER_API_KEY:

			return APP_CONFIG_OAUTH_TWITTER_API_KEY == null ? APP_CONFIG_OAUTH_TWITTER_API_KEY = systemtConstantService.get(ISystemConstant.APP_CONFIG_OAUTH_TWITTER_API_KEY).toString() : APP_CONFIG_OAUTH_TWITTER_API_KEY;

		case ENUM_APP_CONFIG_OAUTH_FACEBOOK_CALLBACK:

			return APP_CONFIG_OAUTH_FACEBOOK_CALLBACK == null ? APP_CONFIG_OAUTH_FACEBOOK_CALLBACK = systemtConstantService.get(ISystemConstant.APP_CONFIG_OAUTH_FACEBOOK_CALLBACK).toString() : APP_CONFIG_OAUTH_FACEBOOK_CALLBACK;

		case ENUM_APP_CONFIG_OAUTH_FACEBOOK_API_SECRET:

			return APP_CONFIG_OAUTH_FACEBOOK_API_SECRET == null ? APP_CONFIG_OAUTH_FACEBOOK_API_SECRET = systemtConstantService.get(ISystemConstant.APP_CONFIG_OAUTH_FACEBOOK_API_SECRET).toString() : APP_CONFIG_OAUTH_FACEBOOK_API_SECRET;

		case ENUM_APP_CONFIG_OAUTH_FACEBOOK_API_KEY:

			return APP_CONFIG_OAUTH_FACEBOOK_API_KEY == null ? APP_CONFIG_OAUTH_FACEBOOK_API_KEY = systemtConstantService.get(ISystemConstant.APP_CONFIG_OAUTH_FACEBOOK_API_KEY).toString() : APP_CONFIG_OAUTH_FACEBOOK_API_KEY;

		case ENUM_APP_CONFIG_OAUTH_LINKEDIN_CALLBACK:

			return APP_CONFIG_OAUTH_LINKEDIN_CALLBACK == null ? APP_CONFIG_OAUTH_LINKEDIN_CALLBACK = systemtConstantService.get(ISystemConstant.APP_CONFIG_OAUTH_LINKEDIN_CALLBACK).toString() : APP_CONFIG_OAUTH_LINKEDIN_CALLBACK;

		case ENUM_APP_CONFIG_OAUTH_LINKEDIN_API_SECRET:

			return APP_CONFIG_OAUTH_LINKEDIN_API_SECRET == null ? APP_CONFIG_OAUTH_LINKEDIN_API_SECRET = systemtConstantService.get(ISystemConstant.APP_CONFIG_OAUTH_LINKEDIN_API_SECRET).toString() : APP_CONFIG_OAUTH_LINKEDIN_API_SECRET;

		case ENUM_APP_CONFIG_OAUTH_LINKEDIN_API_KEY:

			return APP_CONFIG_OAUTH_LINKEDIN_API_KEY == null ? APP_CONFIG_OAUTH_LINKEDIN_API_KEY = systemtConstantService.get(ISystemConstant.APP_CONFIG_OAUTH_LINKEDIN_API_KEY).toString() : APP_CONFIG_OAUTH_LINKEDIN_API_KEY;

		case ENUM_ANALYTICS_GEO_LOCATION_AGGREGATION_REPORTING_URL:

			return ANALYTICS_GEO_LOCATION_AGGREGATION_REPORTING_URL == null ? ANALYTICS_GEO_LOCATION_AGGREGATION_REPORTING_URL = systemtConstantService.get(ISystemConstant.ANALYTICS_GEO_LOCATION_AGGREGATION_REPORTING_URL).toString() : ANALYTICS_GEO_LOCATION_AGGREGATION_REPORTING_URL;

		case ENUM_ANALYTICS_CUSTOM_AGGREGATION_REPORTING_URL:

			return ANALYTICS_CUSTOM_AGGREGATION_REPORTING_URL == null ? ANALYTICS_CUSTOM_AGGREGATION_REPORTING_URL = systemtConstantService.get(ISystemConstant.ANALYTICS_CUSTOM_AGGREGATION_REPORTING_URL).toString() : ANALYTICS_CUSTOM_AGGREGATION_REPORTING_URL;

		case ENUM_ANALYTICS_PRIMARY_URL:

			return ANALYTICS_PRIMARY_URL == null ? ANALYTICS_PRIMARY_URL = systemtConstantService.get(ISystemConstant.ANALYTICS_PRIMARY_URL).toString() : ANALYTICS_PRIMARY_URL;

		case ENUM_ANALYTICS_CLIENT_ID:

			return ANALYTICS_CLIENT_ID == null ? ANALYTICS_CLIENT_ID = systemtConstantService.get(ISystemConstant.ANALYTICS_CLIENT_ID).toString() : ANALYTICS_CLIENT_ID;

		case ENUM_EVENT_SEARCH_PREVIOUS_DAYS:

			return EVENT_SEARCH_PREVIOUS_DAYS == null ? EVENT_SEARCH_PREVIOUS_DAYS = systemtConstantService.get(ISystemConstant.EVENT_SEARCH_PREVIOUS_DAYS).toInteger() : EVENT_SEARCH_PREVIOUS_DAYS;

		case ENUM_APP_STARTUP_PREVIOUS_DAYS:

			return APP_STARTUP_PREVIOUS_DAYS == null ? APP_STARTUP_PREVIOUS_DAYS = systemtConstantService.get(ISystemConstant.APP_STARTUP_PREVIOUS_DAYS).toInteger() : APP_STARTUP_PREVIOUS_DAYS;

		case ENUM_DOWNLOAD_FILE_URL:

			return DOWNLOAD_FILE_URL == null ? DOWNLOAD_FILE_URL = systemtConstantService.get(ISystemConstant.DOWNLOAD_FILE_URL).toString() : DOWNLOAD_FILE_URL;

		case ENUM_TWITTER_FEED_FETCH_COUNT:

			return TWITTER_FEED_FETCH_COUNT == null ? TWITTER_FEED_FETCH_COUNT = systemtConstantService.get(ISystemConstant.TWITTER_FEED_FETCH_COUNT).toInteger() : TWITTER_FEED_FETCH_COUNT;
			
		case ENUM_SALES_EMAIL:
			
			return SALES_EMAIL == null ? SALES_EMAIL = systemtConstantService.get(ISystemConstant.SALES_EMAIL).toString() : SALES_EMAIL;

		default:

			return null;
		}

	}

}
