package org.iqvis.nvolv3.mobile.service;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.dao.CommentDao;
import org.iqvis.nvolv3.domain.Comment;
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

@Service(Constants.SERVICE_COMMENT)
@Transactional
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentDao commentDao;

	public Page<Comment> getAll(Criteria userCriteria, Pageable pageAble, String organizerId) {

		return commentDao.getAll(Utils.parseQuery(userCriteria, "comments."), userCriteria, pageAble, organizerId);
	}

	public Comment get(String id) throws NotFoundException {

		return commentDao.get(id);
	}

	public Comment add(Comment activity, String feedId) throws Exception {

		if (activity.getIsActive() == null) {

			activity.setIsActive(true);

		}

		if (activity.getIsDeleted() == null) {

			activity.setIsDeleted(false);
		}

		activity.setCreatedDate(new DateTime());

		return commentDao.add(activity, feedId);
	}

	public Boolean delete(String id) {

		return commentDao.delete(id);
	}

	public Comment edit(Comment activity) throws Exception, NotFoundException {

		Comment existingComment = get(activity.getId());

		if (null == existingComment) {

			throw new NotFoundException(activity.getId(), "Comment");

		}
		else {

			if (!existingComment.getId().equals(activity.getId())) {

				throw new NotFoundException(activity.getId(), "Comment");
			}

			activity.setLastModifiedDate(new DateTime());

			activity.setLastModifiedBy(activity.getCreatedBy());

			if (activity.getIsDeleted() != null && !StringUtils.isEmpty(activity.getIsDeleted().toString())) {

				existingComment.setIsDeleted(activity.getIsDeleted());
			}

			if (activity.getIsActive() != null && !StringUtils.isEmpty(activity.getIsActive().toString())) {

				existingComment.setIsActive(activity.getIsActive());
			}

			existingComment.setLastModifiedBy(activity.getCreatedBy());

			if (existingComment.getVersion() != null) {

				existingComment.setVersion(existingComment.getVersion() + 1);
			}

		}

		existingComment = commentDao.edit(existingComment);

		return existingComment;
	}

	public String getCommentDetailUrl(Comment activity, HttpServletRequest request) {

		return commentDao.getCommentsDetailUrl(activity, request);
	}

}
