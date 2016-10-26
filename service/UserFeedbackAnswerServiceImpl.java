package org.iqvis.nvolv3.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.iqvis.nvolv3.dao.UserFeedbackAnswerDao;
import org.iqvis.nvolv3.domain.Activity;
import org.iqvis.nvolv3.domain.AnswerObject;
import org.iqvis.nvolv3.domain.FeedBackAnswerCount;
import org.iqvis.nvolv3.domain.FeedBackReportResponse;
import org.iqvis.nvolv3.domain.Question;
import org.iqvis.nvolv3.domain.SelectAnswerCodeStat;
import org.iqvis.nvolv3.domain.UserFeedback;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.QuestionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(Constants.USER_FEEDBACK_ANSWER_RESOURCE)
@Transactional
public class UserFeedbackAnswerServiceImpl implements UserFeedbackAnswerService {

	@Autowired
	UserFeedbackAnswerDao userFeedbackAnswerDao;

	@Resource(name = Constants.USER_FEEDBACK_QUESTION_RESOURCE)
	private UserFeedbackQuestionService userFeedbackQuestionService;

	@Resource(name = Constants.SERVICE_ACTIVITY)
	private ActivityService activityService;

	public UserFeedback add(UserFeedback userFeedback) throws Exception {

		if (userFeedback.getFeedBack().getQuestions() != null) {

			for (Question question : userFeedback.getFeedBack().getQuestions()) {

				if (null == QuestionType.valueOf(question.getType())) {

					throw new Exception("Question type " + question.getType() + " not allowed");
				}

			}
		}

		return userFeedbackAnswerDao.add(userFeedback);
	}

	public long getUserFeedBackCount(String objectType, String objectId) throws NotFoundException {

		return userFeedbackAnswerDao.getTotalFeedBackCount(objectType, objectId);
	}

	public List<FeedBackReportResponse> getUserFeedBackStats(String objectType, String objectId, String eventId, String activityId) throws NotFoundException {

		Activity existingActivity = activityService.get(activityId, eventId);

		List<FeedBackReportResponse> report = new ArrayList<FeedBackReportResponse>();

		List<FeedBackAnswerCount> list = userFeedbackAnswerDao.getFeedBackStats(objectId, objectType);

		List<FeedBackAnswerCount> listSecond = new ArrayList<FeedBackAnswerCount>(list);

		for (FeedBackAnswerCount feedBackAnswerCount : listSecond) {

			List<SelectAnswerCodeStat> stats = new ArrayList<SelectAnswerCodeStat>();

			for (FeedBackAnswerCount innerFeedBackAnswerCount : list) {

				if ((feedBackAnswerCount.get_id().getQuestionId() + "").equals(innerFeedBackAnswerCount.get_id().getQuestionId())) {

					stats.add(new SelectAnswerCodeStat(innerFeedBackAnswerCount.get_id().getAnswerCode(), innerFeedBackAnswerCount.getCount()));

				}
			}

			FeedBackReportResponse temp = new FeedBackReportResponse(feedBackAnswerCount.get_id().getQuestionId(), feedBackAnswerCount.get_id().getQuestionType(), stats);

			temp.setQuestionKey(feedBackAnswerCount.get_id().getQuestionKey());

			if (!report.contains(temp) && !Constants.INPUT_TEXT.equals(temp.getQuestionType()) && !Constants.RATING_STARS.equals(temp.getQuestionType())) {
				report.add(temp);
			}
		}

		List<FeedBackReportResponse> tempReport = new ArrayList<FeedBackReportResponse>(report);

		List<FeedBackReportResponse> tempList = new ArrayList<FeedBackReportResponse>();

		for (FeedBackReportResponse feedBackReportResponse : tempReport) {

			List<SelectAnswerCodeStat> stats = new ArrayList<SelectAnswerCodeStat>();

			if (Constants.MULTI_SELECT.equals(feedBackReportResponse.getQuestionType())) {

				report.remove(feedBackReportResponse);

				for (SelectAnswerCodeStat selectAnswerCodeStat : feedBackReportResponse.getSelections()) {

					String[] answerCodes = (selectAnswerCodeStat.getAnswerCode() + "").split(",");
					for (String string : answerCodes) {

						stats.add(new SelectAnswerCodeStat(string, selectAnswerCodeStat.getCount()));

					}

				}

				FeedBackReportResponse tempCon = new FeedBackReportResponse(feedBackReportResponse.getQuestionId(), feedBackReportResponse.getQuestionType(), stats);

				tempCon.setQuestionKey(feedBackReportResponse.getQuestionKey());

				tempList.add(tempCon);

			}
		}

		List<String> check = new ArrayList<String>();

		for (FeedBackReportResponse feedBackReportResponse : tempList) {

			List<SelectAnswerCodeStat> listSelect = new ArrayList<SelectAnswerCodeStat>(feedBackReportResponse.getSelections());

			List<SelectAnswerCodeStat> listNewSelect = new ArrayList<SelectAnswerCodeStat>();

			for (SelectAnswerCodeStat selectAnswerCodeStat : feedBackReportResponse.getSelections()) {

				float sum = 0;

				for (SelectAnswerCodeStat innerSelectAnswerCodeStat : listSelect) {

					if (innerSelectAnswerCodeStat.getAnswerCode().equals(selectAnswerCodeStat.getAnswerCode())) {

						sum += innerSelectAnswerCodeStat.getCount();

					}

				}

				if (!check.contains(selectAnswerCodeStat.getAnswerCode())) {
					listNewSelect.add(new SelectAnswerCodeStat(selectAnswerCodeStat.getAnswerCode(), sum));
				}

				check.add(selectAnswerCodeStat.getAnswerCode());

			}

			report.add(new FeedBackReportResponse(feedBackReportResponse.getQuestionId(), feedBackReportResponse.getQuestionType(), feedBackReportResponse.getQuestionKey(), listNewSelect));

		}

		List<AnswerObject> ratingStarList = userFeedbackAnswerDao.getFeedBackRatingStartStats(objectId, objectType);

		List<AnswerObject> tempRatingStarList = userFeedbackAnswerDao.getFeedBackRatingStartStats(objectId, objectType);

		List<String> checkList = new ArrayList<String>();

		List<FeedBackReportResponse> ratingReport = new ArrayList<FeedBackReportResponse>();

		for (AnswerObject feedBackAnswerCount : ratingStarList) {

			int count = 0, sum = 0;

			float avg = 0.0f;

			for (AnswerObject feedBackAnswerCount2 : tempRatingStarList) {
				try {
					if (feedBackAnswerCount.getQuestionId().equals(feedBackAnswerCount2.getQuestionId())) {

						count++;

						sum += Integer.parseInt(feedBackAnswerCount2.getAnswerCode());

					}
				}
				catch (Exception e) {

					e.printStackTrace();
				}
			}

			if (!checkList.contains(feedBackAnswerCount.getQuestionId())) {

				if (count > 0) {

					avg = sum / count;
					List<SelectAnswerCodeStat> selections = new ArrayList<SelectAnswerCodeStat>();

					selections.add(new SelectAnswerCodeStat("RATING", avg));

					ratingReport.add(new FeedBackReportResponse(feedBackAnswerCount.getQuestionId(), Constants.RATING_STARS, feedBackAnswerCount.getQuestionKey(), selections));

				}
			}

			checkList.add(feedBackAnswerCount.getQuestionId());

		}

		List<FeedBackAnswerCount> textQuestions = userFeedbackAnswerDao.getFeedBackStatsInputText(activityId, objectType);

		for (FeedBackAnswerCount feedBackAnswerCount : textQuestions) {

			List<SelectAnswerCodeStat> selections = new ArrayList<SelectAnswerCodeStat>();

			selections.add(new SelectAnswerCodeStat(null, feedBackAnswerCount.getCount()));

			ratingReport.add(new FeedBackReportResponse(feedBackAnswerCount.get_id().getQuestionId(), Constants.INPUT_TEXT, feedBackAnswerCount.get_id().getQuestionKey(), selections));
		}

		report.addAll(ratingReport);

		return report;
	}

	public Page<AnswerObject> getUserFeedBackStatsText(String objectType, String objectId, String eventId, String activityId, Pageable pageAble, Criteria query) throws NotFoundException {

		Activity existingActivity = activityService.get(activityId, eventId);

		Page<AnswerObject> page = userFeedbackAnswerDao.getFeedBackStatsInputText(objectId, objectType, pageAble, query);

		return page;
	}

	public List<AnswerObject> getFeedBackRatingStartStats(String objectId, String objectType) {
		// TODO Auto-generated method stub
		return userFeedbackAnswerDao.getFeedBackRatingStartStats(objectId, objectType);
	}

}
