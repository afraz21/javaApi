package org.iqvis.nvolv3.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.dao.LocationDao;
import org.iqvis.nvolv3.domain.Location;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Utils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service(Constants.SERVICE_LOCATION)
@Transactional
public class LocationServiceImpl implements LocationService {

	@Autowired
	LocationDao locationDao;

	public Location get(String id, String organizerId) throws NotFoundException {

		return locationDao.get(id, organizerId);
	}

	public Location add(Location location) throws Exception {

		if (location.getIsActive() == null) {

			location.setIsActive(true);
		}

		if (location.getIsDeleted() == null) {

			location.setIsDeleted(false);
		}

		return locationDao.add(location);
	}

	public Location edit(Location location, String organizerId) throws Exception {

		Location existingLocation = get(location.getId(), organizerId);

		if (null == existingLocation) {

			throw new NotFoundException(location.getId(), "Location");

		}
		else {

			existingLocation.setLastModifiedBy(location.getCreatedBy());

			existingLocation.setLastModifiedDate(new DateTime());

			if (location.getIsDeleted() != null && !StringUtils.isEmpty(location.getIsDeleted().toString())) {
				existingLocation.setIsDeleted(location.getIsDeleted());
			}

			if (location.getIsActive() != null && !StringUtils.isEmpty(location.getIsActive())) {
				existingLocation.setIsActive(location.getIsActive());
			}

			if (location.getName() != null && !StringUtils.isEmpty(location.getName())) {
				existingLocation.setName(location.getName());
			}

			if (location.getPicture() != null && !StringUtils.isEmpty(location.getPicture())) {
				existingLocation.setPicture(location.getPictureO());
			}

			if (location.getLongitude() != null && !StringUtils.isEmpty(location.getLongitude())) {
				existingLocation.setLongitude(location.getLongitude());
			}

			if (location.getLatitude() != null && !StringUtils.isEmpty(location.getLatitude())) {
				existingLocation.setLatitude(location.getLatitude());
			}

			if (location.getMultiLingual() != null && location.getMultiLingual().size() > 0) {

				List<MultiLingual> finalLanguages = new ArrayList<MultiLingual>();

				if (null != existingLocation.getMultiLingual()) {
					finalLanguages = Utils.updateMultiLingual(existingLocation.getMultiLingual(), location.getMultiLingual());

				}
				else {

					finalLanguages = location.getMultiLingual();
				}

				existingLocation.setMultiLingual(finalLanguages);

			}

		}

		return locationDao.edit(existingLocation, organizerId);
	}

	public Boolean delete(String id) {

		return locationDao.delete(id);
	}

	public String getLocationDetailUrl(Location location, String organizerId, HttpServletRequest request) {

		return locationDao.getLocationDetailUrl(location, organizerId, request);
	}

	public Page<Location> getAll(Criteria search, Pageable pageAble, String organizerId) {

		return locationDao.getAll(Utils.parseQuery(search, "venues.locations."), pageAble, organizerId);
	}

}
