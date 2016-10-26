package org.iqvis.nvolv3.service;

import java.util.List;

import org.iqvis.nvolv3.bean.QuestionMultiLingual;
import org.iqvis.nvolv3.domain.Question;
import org.iqvis.nvolv3.domain.UserFeedbackQuestion;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.exceptionHandler.OptionException;

public interface UserFeedbackQuestionService {

	public UserFeedbackQuestion add(UserFeedbackQuestion userFeedbackQuestion) throws Exception;

	public UserFeedbackQuestion get(String id);

	public UserFeedbackQuestion edit(UserFeedbackQuestion userFeedbackQuestion, String id) throws Exception;

	public UserFeedbackQuestion delete(String id) throws NotFoundException;

	public List<UserFeedbackQuestion> getAll(String organizerId) throws NotFoundException;

	public Question addMultiLingual(String id, String qId, QuestionMultiLingual multiLingual) throws NotFoundException, OptionException;

	public Question editQuestion(String id, String qId, Question question) throws NotFoundException;

	public Question getQuestion(String id, String qId) throws NotFoundException;

	public Question deleteQuestion(String id, String qId) throws NotFoundException;

	public Question addQuestion(String id, Question question) throws NotFoundException;

}
