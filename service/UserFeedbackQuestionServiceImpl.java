package org.iqvis.nvolv3.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.iqvis.nvolv3.bean.QuestionMultiLingual;
import org.iqvis.nvolv3.dao.UserFeedbackQuestionDao;
import org.iqvis.nvolv3.domain.Question;
import org.iqvis.nvolv3.domain.UserFeedbackQuestion;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.exceptionHandler.OptionException;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.QuestionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("restriction")
@Service(Constants.USER_FEEDBACK_QUESTION_RESOURCE)
@Transactional
public class UserFeedbackQuestionServiceImpl implements UserFeedbackQuestionService {

	@Autowired
	UserFeedbackQuestionDao userFeedbackQuestionDao;

	@Resource(name = Constants.SERVICE_EVENT)
	EventService eventService;

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	public UserFeedbackQuestion add(UserFeedbackQuestion userFeedbackQuestion) throws Exception {

		if (userFeedbackQuestion.getQuestions() != null) {

			for (Question question : userFeedbackQuestion.getQuestions()) {

				if (null == QuestionType.valueOf(question.getType())) {

					throw new Exception("Question type " + question.getType() + " not allowed");
				}

			}
		}

		return userFeedbackQuestionDao.add(userFeedbackQuestion);
	}

	public UserFeedbackQuestion get(String id) {

		return userFeedbackQuestionDao.get(id);
	}

	public UserFeedbackQuestion edit(UserFeedbackQuestion userFeedbackQuestion, String id) throws Exception {

		UserFeedbackQuestion existingUserFeedbackQuestion = userFeedbackQuestionDao.get(id);

		if (existingUserFeedbackQuestion != null) {

			if (userFeedbackQuestion.getQuestions() != null) {

				for (Question question : userFeedbackQuestion.getQuestions()) {

					if (null == QuestionType.valueOf(question.getType())) {

						throw new Exception("Question type " + question.getType() + " not allowed");
					}

				}
			}

			if (userFeedbackQuestion.getTitle() != null) {
				existingUserFeedbackQuestion.setTitle(userFeedbackQuestion.getTitle());
			}

			if (userFeedbackQuestion.getQuestions() != null) {
				existingUserFeedbackQuestion.setQuestions(userFeedbackQuestion.getQuestions());
			}

			if (userFeedbackQuestion.getReferenceObject() != null) {
				existingUserFeedbackQuestion.setReferenceObject(userFeedbackQuestion.getReferenceObject());
			}

			if (userFeedbackQuestion.getReferenceObjectId() != null) {
				existingUserFeedbackQuestion.setReferenceObjectId(userFeedbackQuestion.getReferenceObjectId());
			}

			existingUserFeedbackQuestion = userFeedbackQuestionDao.edit(existingUserFeedbackQuestion, id);

			this.LOG_MY_ACTION(id, Constants.LOG_ACTION_UPDATE);

			return existingUserFeedbackQuestion;

		}

		throw new NotFoundException(userFeedbackQuestion.getId(), UserFeedbackQuestion.class.toString());

	}

	public List<UserFeedbackQuestion> getAll(String organizerId) throws NotFoundException {

		return userFeedbackQuestionDao.getAll(organizerId);
	}

	public UserFeedbackQuestion delete(String id) throws NotFoundException {

		UserFeedbackQuestion existingUserFeedbackQuestion = userFeedbackQuestionDao.get(id);

		if (existingUserFeedbackQuestion != null) {

			existingUserFeedbackQuestion.setIsDeleted(true);

			UserFeedbackQuestion temp = userFeedbackQuestionDao.edit(existingUserFeedbackQuestion, id);

			this.LOG_MY_ACTION(id, Constants.LOG_ACTION_DELETE);

			return temp;

		}

		throw new NotFoundException(id, UserFeedbackQuestion.class.toString());
	}

	public Question addMultiLingual(String id, String qId, QuestionMultiLingual multiLingual) throws NotFoundException, OptionException {

		UserFeedbackQuestion existingUserFeedbackQuestion = userFeedbackQuestionDao.get(id);

		Question questionContainer = null;

		if (existingUserFeedbackQuestion != null) {

			if (existingUserFeedbackQuestion.getQuestions() != null) {

				for (Question question : existingUserFeedbackQuestion.getQuestions()) {

					if ((question.getId() + "").equals(qId)) {

						List<QuestionMultiLingual> multiLinguals = new ArrayList<QuestionMultiLingual>(question.getMultilingual());

						if (Constants.MULTI_SELECT.equals(question.getType()) || Constants.SINGLE_SELECT.equals(question.getType())) {

							if (multiLingual.getAnswers() == null) {
								throw new OptionException("Options Should Not Be Empty");
							}

							if (multiLingual.getAnswers().size() <= 0) {
								throw new OptionException("Options Should Not Be Empty");
							}

						}

						multiLinguals.add(multiLingual);

						question.setMultilingual(multiLinguals);

						questionContainer = question;

						break;
					}
				}

			}

			userFeedbackQuestionDao.edit(existingUserFeedbackQuestion, id);

			this.LOG_MY_ACTION(id, Constants.LOG_ACTION_UPDATE);

			return questionContainer;

		}

		throw new NotFoundException(id, UserFeedbackQuestion.class.toString());

	}

	public Question editQuestion(String id, String qId, Question newQuestion) throws NotFoundException {
		UserFeedbackQuestion existingUserFeedbackQuestion = userFeedbackQuestionDao.get(id);

		Question questionContainer = null;

		if (existingUserFeedbackQuestion != null) {

			if (existingUserFeedbackQuestion.getQuestions() != null) {

				for (Question question : existingUserFeedbackQuestion.getQuestions()) {

					if ((question.getId() + "").equals(qId)) {

						if (newQuestion.getQuestionKey() != null) {
							question.setQuestionKey(newQuestion.getQuestionKey());
						}

						if (newQuestion.getMultilingual() != null) {
							question.setMultilingual(newQuestion.getMultilingual());
						}

						if (newQuestion.getType() != null) {
							question.setType(newQuestion.getType());
						}

						if (newQuestion.getUi() != null) {
							question.setUi(newQuestion.getUi());
						}

						questionContainer = question;

						break;
					}
				}

			}

			userFeedbackQuestionDao.edit(existingUserFeedbackQuestion, id);

			this.LOG_MY_ACTION(id, Constants.LOG_ACTION_UPDATE);

			return questionContainer;

		}

		throw new NotFoundException(id, UserFeedbackQuestion.class.toString());
	}

	public Question getQuestion(String id, String qId) throws NotFoundException {

		UserFeedbackQuestion existingUserFeedbackQuestion = userFeedbackQuestionDao.get(id);

		Question questionContainer = null;

		if (existingUserFeedbackQuestion != null) {

			if (existingUserFeedbackQuestion.getQuestions() != null) {

				for (Question question : existingUserFeedbackQuestion.getQuestions()) {

					if ((question.getId() + "").equals(qId)) {

						questionContainer = question;

						break;
					}
				}

			}

			return questionContainer;

		}

		throw new NotFoundException(id, UserFeedbackQuestion.class.toString());
	}

	public Question addQuestion(String id, Question question) throws NotFoundException {

		UserFeedbackQuestion existingUserFeedbackQuestion = userFeedbackQuestionDao.get(id);

		if (existingUserFeedbackQuestion != null) {

			List<Question> questions = new ArrayList<Question>(existingUserFeedbackQuestion.getQuestions());

			question.setId(UUID.randomUUID().toString());

			questions.add(question);

			existingUserFeedbackQuestion.setQuestions(questions);

			userFeedbackQuestionDao.edit(existingUserFeedbackQuestion, id);

			this.LOG_MY_ACTION(id, Constants.LOG_ACTION_UPDATE);

			return question;
		}

		throw new NotFoundException(id, UserFeedbackQuestion.class.toString());
	}

	public Question deleteQuestion(String id, String qId) throws NotFoundException {
		UserFeedbackQuestion existingUserFeedbackQuestion = userFeedbackQuestionDao.get(id);

		Question questionContainer = null;

		if (existingUserFeedbackQuestion != null) {

			if (existingUserFeedbackQuestion.getQuestions() != null) {

				List<Question> questions = new ArrayList<Question>();

				for (Question question : existingUserFeedbackQuestion.getQuestions()) {

					if ((question.getId() + "").equals(qId)) {

						questionContainer = question;

					}
					else {
						questions.add(question);
					}
				}

				existingUserFeedbackQuestion.setQuestions(questions);

			}

			userFeedbackQuestionDao.edit(existingUserFeedbackQuestion, id);

			this.LOG_MY_ACTION(id, Constants.LOG_ACTION_UPDATE);

		}

		if (questionContainer == null) {
			throw new NotFoundException(qId, "Question not found in Questionnaire " + id);
		}

		return questionContainer;

	}

	private void LOG_MY_ACTION(String id, String action) {

		List<String> l = new ArrayList<String>();

		l.addAll(eventService.getEventIdsByQuestionnair(id));
		System.out.println(l);
		if (l.size() > 0) {
			dataChangeLogService.add(l, "EVENT", Constants.LOG_USER_FEEDBACK_QUESTION, id, action, UserFeedbackQuestion.class.toString());

		}

	}

}