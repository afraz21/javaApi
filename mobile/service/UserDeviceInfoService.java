package org.iqvis.nvolv3.mobile.service;

import java.util.List;

import org.iqvis.nvolv3.domain.DeviceInfo;
import org.iqvis.nvolv3.domain.PushNotificationConfiguration;
import org.iqvis.nvolv3.push.Query;

public interface UserDeviceInfoService {
	public DeviceInfo add(DeviceInfo deviceInfo);

	public DeviceInfo get(String deviceToken);

	public List<DeviceInfo> get(String selector, Object value);

	public List<String> get(String selector, String value,boolean isFeed);

	public Query getQuery(String selector, String value, String deviceType,boolean isFeed);

	public List<DeviceInfo> get(List<String> ids);

	public DeviceInfo editNotificationConfiguration(PushNotificationConfiguration pushNotificationConfiguration, String deviceToken);
}
