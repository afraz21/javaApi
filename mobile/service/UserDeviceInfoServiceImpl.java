package org.iqvis.nvolv3.mobile.service;

import java.util.ArrayList;
import java.util.List;

import org.iqvis.nvolv3.dao.DeviceInfoDao;
import org.iqvis.nvolv3.domain.DeviceInfo;
import org.iqvis.nvolv3.domain.PushNotificationConfiguration;
import org.iqvis.nvolv3.push.DeviceToken;
import org.iqvis.nvolv3.push.Query;
import org.iqvis.nvolv3.push.Where;
import org.iqvis.nvolv3.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(Constants.SERVICE_USER_DEVICE_INFO_SERVICE)
@Transactional
public class UserDeviceInfoServiceImpl implements UserDeviceInfoService {

	@Autowired
	private DeviceInfoDao deviceInfoDao;

	public DeviceInfo add(DeviceInfo deviceInfo) {

		return deviceInfoDao.add(deviceInfo);
	}

	public DeviceInfo editNotificationConfiguration(PushNotificationConfiguration pushNotificationConfiguration, String deviceToken) {

		DeviceInfo deviceInfo = this.get(deviceToken);

		deviceInfo.setPushNotificationConfiguration(pushNotificationConfiguration);

		return deviceInfoDao.add(deviceInfo);
	}

	public DeviceInfo get(String deviceToken) {
		return deviceInfoDao.get(deviceToken);
	}

	public List<DeviceInfo> get(String selector, Object value) {

		return deviceInfoDao.get(selector, value);
	}

	public List<String> get(String selector, String value,boolean isFeed) {

		List<DeviceInfo> temp = deviceInfoDao.get(selector, value);

		List<String> deviceToken = new ArrayList<String>();
		if (temp != null) {

			for (DeviceInfo deviceInfo : temp) {
				
				if(isFeed==true){
					
					if(deviceInfo.getPushNotificationConfiguration().isNewFeed()==true){
						
						deviceToken.add(deviceInfo.getDeviceToken());
					}
				}
				else{
					deviceToken.add(deviceInfo.getDeviceToken());
				}
			}
			
			deviceToken.remove("");
		}

		return deviceToken;
	}

	public Query getQuery(String selector, String value, String deviceType,boolean isFeed) {

		List<String> deviceTokens = this.get(selector, value,isFeed);

		Query query = new Query();

		Where where = new Where();

		DeviceToken deviceToken = new DeviceToken();

		deviceToken.set$in(deviceTokens);

		where.setDeviceToken(deviceToken);

		where.setDeviceType(deviceType);

		query.setWhere(where);

		return query;
	}

	public List<DeviceInfo> get(List<String> ids) {

		return deviceInfoDao.get(ids);
	}

}
