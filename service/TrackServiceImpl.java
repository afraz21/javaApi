package org.iqvis.nvolv3.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.dao.TracksDao;
import org.iqvis.nvolv3.domain.Track;
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

@Service(Constants.SERVICE_TRACK)
@Transactional
public class TrackServiceImpl implements TrackService {

	@Autowired
	TracksDao trackDao;

	public Track get(String id, String organizerId) throws NotFoundException {

		return trackDao.get(id, organizerId);

	}

	public Track add(Track track) throws Exception {

		track.setCreatedDate(new DateTime());

		return trackDao.add(track);

	}

	public Track edit(Track track) throws Exception {

		Track existingTrack = get(track.getId(), track.getOrganizerId());

		if (null == existingTrack) {

			throw new NotFoundException(track.getId(), "Track");

		}
		else {

			if (track.getIsDeleted() != null && !StringUtils.isEmpty(track.getIsDeleted().toString())) {

				System.out.println("isDeleted : " + track.getIsDeleted());

				existingTrack.setIsDeleted(track.getIsDeleted());
			}

			if (track.getName() != null && !StringUtils.isEmpty(track.getName())) {

				existingTrack.setName(track.getName());
			}

			existingTrack.setLastModifiedBy(track.getCreatedBy());

			existingTrack.setLastModifiedDate(new DateTime());

			if (existingTrack.getVersion() != null) {

				existingTrack.setVersion(existingTrack.getVersion() + 1);
			}

			if (track.getPictureO() != null) {

				existingTrack.setPicture(track.getPictureO());
			}

			if (null != track.getMultiLingual() && track.getMultiLingual().size() > 0) {

				List<MultiLingual> finalLanguages = new ArrayList<MultiLingual>();

				if (null != existingTrack.getMultiLingual()) {

					finalLanguages = Utils.updateMultiLingual(existingTrack.getMultiLingual(), track.getMultiLingual());

				}
				else {

					finalLanguages = track.getMultiLingual();

				}

				existingTrack.setMultiLingual(finalLanguages);

			}

		}

		return trackDao.edit(existingTrack);

	}

	public Boolean delete(String id) {

		return trackDao.delete(id);

	}

	public String getTrackDetailUrl(Track track, HttpServletRequest request) {

		return trackDao.getTrackDetailUrl(track, request);

	}

	public Page<Track> getAll(Criteria search, Pageable pageAble, String organizerId) {

		return trackDao.getAll(Utils.parseQuery(search, "tracks."), search, pageAble, organizerId);

	}

	public List<Track> getOrganizerTracks(String organizerId) {

		return trackDao.getOrganizerTracks(organizerId);
	}

}
