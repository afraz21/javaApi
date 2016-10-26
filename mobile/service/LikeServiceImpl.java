package org.iqvis.nvolv3.mobile.service;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.dao.LikeDao;
import org.iqvis.nvolv3.domain.Like;
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

@Service(Constants.SERVICE_LIKE)
@Transactional
public class LikeServiceImpl implements LikeService {

	@Autowired
	private LikeDao likeDao;

	public Page<Like> getAll(Criteria userCriteria, Pageable pageAble, String organizerId) {

		return likeDao.getAll(Utils.parseQuery(userCriteria, "likedBy."), userCriteria, pageAble, organizerId);
	}

	public Like get(String id) throws NotFoundException {

		return likeDao.get(id);
	}

	public Like add(Like activity, String feedId) throws Exception {

		if (activity.getIsActive() == null) {

			activity.setIsActive(true);

		}

		if (activity.getIsDeleted() == null) {

			activity.setIsDeleted(false);
		}

		activity.setCreatedDate(new DateTime());

		return likeDao.add(activity, feedId);
	}

	public Boolean delete(String id) {

		return likeDao.delete(id);
	}

	public Like edit(Like activity) throws Exception, NotFoundException {

		Like existingLike = get(activity.getId());

		if (null == existingLike) {

			throw new NotFoundException(activity.getId(), "Like");

		}
		else {

			if (!existingLike.getId().equals(activity.getId())) {

				throw new NotFoundException(activity.getId(), "Like");
			}

			activity.setLastModifiedDate(new DateTime());

			activity.setLastModifiedBy(activity.getCreatedBy());

			if (activity.getIsDeleted() != null && !StringUtils.isEmpty(activity.getIsDeleted().toString())) {

				existingLike.setIsDeleted(activity.getIsDeleted());
			}

			if (activity.getIsActive() != null && !StringUtils.isEmpty(activity.getIsActive().toString())) {

				existingLike.setIsActive(activity.getIsActive());
			}

			existingLike.setLastModifiedBy(activity.getCreatedBy());

			if (existingLike.getVersion() != null) {

				existingLike.setVersion(existingLike.getVersion() + 1);
			}

		}

		existingLike = likeDao.edit(existingLike);

		return existingLike;
	}

	public String getLikeDetailUrl(Like activity, HttpServletRequest request) {

		return likeDao.getLikesDetailUrl(activity, request);
	}

}
