package org.iqvis.nvolv3.service;

import java.util.List;

import org.iqvis.nvolv3.domain.AnswerObject;
import org.iqvis.nvolv3.domain.FeedBackReportResponse;
import org.iqvis.nvolv3.domain.UserFeedback;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

public interface UserFeedbackAnswerService {

	public UserFeedback add(UserFeedback userFeedbackQuestion) throws Exception;

	public List<FeedBackReportResponse> getUserFeedBackStats(String objectType, String objectId, String eventId, String activityId) throws NotFoundException;

	public Page<AnswerObject> getUserFeedBackStatsText(String objectType, String objectId, String eventId, String activityId, Pageable pageable, Criteria query) throws NotFoundException;

	public long getUserFeedBackCount(String objectType, String objectId) throws NotFoundException;

	public List<AnswerObject> getFeedBackRatingStartStats(String objectId, String objectType);
}
