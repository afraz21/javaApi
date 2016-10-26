package org.iqvis.nvolv3.service;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.dao.MediaDao;
import org.iqvis.nvolv3.domain.Media;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service(Constants.SERVICE_MEDIA)
@Transactional
public class MediaServiceImpl implements MediaService {

	@Autowired
	private MediaDao eventDao;

	public Media get(String id) {

		return eventDao.get(id);
	}

	public Media add(Media media) throws Exception {

		return eventDao.add(media);
	}

	public Media edit(Media media, String mediaID) throws Exception, NotFoundException {

		Media existingMedia = null;

		if (null != mediaID && !StringUtils.isEmpty(mediaID)) {

			existingMedia = eventDao.get(mediaID);
		}

		if (null == existingMedia) {

			throw new NotFoundException(mediaID, "Media");
		}

		if (null != media.getUrl() && !StringUtils.isEmpty(media.getUrl())) {
			existingMedia.setUrl(media.getUrl());
		}

		if (media.getType() != null && !StringUtils.isEmpty(media.getType())) {

			existingMedia.setType(media.getType());
		}

		existingMedia.setLastModifiedBy(media.getCreatedBy());

		return eventDao.edit(existingMedia);
	}

	public String getMediaDetailUrl(Media media, HttpServletRequest request) {

		return eventDao.getMediaDetailUrl(media, request);
	}

}
