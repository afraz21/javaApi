package org.iqvis.nvolv3.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.bean.MultiLingualAddresses;
import org.iqvis.nvolv3.dao.VenueDao;
import org.iqvis.nvolv3.domain.Venue;
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

@Service(Constants.SERVICE_VENUE)
@Transactional
public class VenueServiceImpl implements VenueService {

	@Autowired
	VenueDao venueDao;

	public Venue get(String id, String organizerId) throws NotFoundException {

		return venueDao.get(id, organizerId);
	}

	public Venue add(Venue venue) throws Exception {

		venue.setCreatedDate(new DateTime());

		if (venue.getIsActive() == null) {

			venue.setIsActive(true);
		}

		if (venue.getIsDeleted() == null) {

			venue.setIsDeleted(false);
		}

		if (venue.getIsEventVenue() == null) {

			venue.setIsEventVenue(true);
		}

		if (venue.getLongitude() == null) {
			venue.setLongitude(0.0);
		}

		if (venue.getLatitude() == null) {
			venue.setLatitude(0.0);
		}

		return venueDao.add(venue);
	}

	public Venue edit(Venue venue) throws Exception {

		Venue existingVenue = get(venue.getId(), venue.getOrganizerId());

		if (null == existingVenue) {

			throw new NotFoundException(venue.getId(), "Venue");

		}
		else {

			existingVenue.setLastModifiedDate(new DateTime());

			existingVenue.setLastModifiedBy(venue.getCreatedBy());

			if (venue.getLatitude() != null) {
				existingVenue.setLatitude(venue.getLatitude());
			}
			if (venue.getLongitude() != null) {
				existingVenue.setLongitude(venue.getLongitude());
			}

			if (venue.getIsDeleted() != null && !StringUtils.isEmpty(venue.getIsDeleted().toString())) {
				existingVenue.setIsDeleted(venue.getIsDeleted());
			}

			if (venue.getIsActive() != null && !StringUtils.isEmpty(venue.getIsActive())) {
				existingVenue.setIsActive(venue.getIsActive());
			}

			if (venue.getGoogleMapLink() != null && !StringUtils.isEmpty(venue.getGoogleMapLink())) {

				existingVenue.setGoogleMapLink(venue.getGoogleMapLink());
			}

			if (venue.getName() != null && !StringUtils.isEmpty(venue.getName())) {

				existingVenue.setName(venue.getName());
			}

			if (venue.getPicture() != null && !StringUtils.isEmpty(venue.getPicture())) {

				existingVenue.setPicture(venue.getPictureO());
			}

			if (venue.getIsEventVenue() != null) {

				existingVenue.setIsEventVenue(venue.getIsEventVenue());
			}

			if (venue.getLocations() != null && venue.getLocations().size() > 0) {
				existingVenue.setLocations(venue.getLocations());
			}

			if (venue.getMultiLingual() != null && venue.getMultiLingual().size() > 0) {

				List<MultiLingualAddresses> finalLanguages = new ArrayList<MultiLingualAddresses>();

				if (null != existingVenue.getMultiLingual()) {
					finalLanguages = Utils.updateMultiLingualAddress(existingVenue.getMultiLingual(), venue.getMultiLingual());

				}
				else {

					finalLanguages = venue.getMultiLingual();
				}

				existingVenue.setMultiLingual(finalLanguages);

			}

			if (existingVenue.getVersion() != null) {

				existingVenue.setVersion(existingVenue.getVersion() + 1);
			}

		}

		return venueDao.edit(existingVenue);
	}

	public Boolean delete(String id) {

		return venueDao.delete(id);
	}

	public String getVenueDetailUrl(Venue venue, HttpServletRequest request) {

		return venueDao.getVenueDetailUrl(venue, request);
	}

	public Page<Venue> getAll(Criteria userCriteria, Pageable pageAble, String organizerId) {

		return venueDao.getAll(Utils.parseQuery(userCriteria, "venues."), userCriteria, pageAble, organizerId);
	}

	public void onVenueDelete(String venueId) {

		venueDao.onVenueDelete(venueId);

	}

}
