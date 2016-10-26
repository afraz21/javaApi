package org.iqvis.nvolv3.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.iqvis.nvolv3.bean.QuestionMultiLingual;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.Question;
import org.iqvis.nvolv3.domain.UserFeedbackQuestion;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.exceptionHandler.OptionException;
import org.iqvis.nvolv3.service.EventConfigurationService;
import org.iqvis.nvolv3.service.UserFeedbackQuestionService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.QuestionType;
import org.iqvis.nvolv3.utils.Urls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("restriction")
@RequestMapping(Urls.TOP_LEVEL)
@Controller
public class UserFeedbackQuestionController {

	@Resource(name = Constants.USER_FEEDBACK_QUESTION_RESOURCE)
	private UserFeedbackQuestionService userFeedbackQuestionService;

	@Autowired
	EventConfigurationService eventConfigurationService;

	@RequestMapping(value = "feedback_question", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseMessage addUserFeedBackQuestions(@RequestBody @Valid UserFeedbackQuestion userFeedbackQuestion, @PathVariable("organizerId") String organizerId, HttpServletRequest request) throws Exception {

		ResponseMessage message = new ResponseMessage();

		userFeedbackQuestion.setOrganizerId(organizerId);

		UserFeedbackQuestion addedQuestions = userFeedbackQuestionService.add(userFeedbackQuestion);

		message.setDetails_url(request.getRequestURL().toString());

		message.setHttpCode(HttpStatus.CREATED.toString());

		message.setMessageCode(Constants.SUCCESS_CODE);

		message.setMessage("UserFeedbackQuestion added successfully !");

		message.setRecord(addedQuestions);

		message.setRecordId(addedQuestions.getId());

		return message;
	}

	@RequestMapping(value = "feedback_question", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Page<UserFeedbackQuestion> getUserFeedBackQuestions(@PathVariable("organizerId") String organizerId, HttpServletRequest request) throws NotFoundException {

		List<UserFeedbackQuestion> list = userFeedbackQuestionService.getAll(organizerId);

		return new PageImpl<UserFeedbackQuestion>(list);
	}

	@RequestMapping(value = "feedback_question/{id}", method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ResponseMessage editUserFeedBackQuestions(@RequestBody UserFeedbackQuestion userFeedbackQuestion, @PathVariable("id") String id, HttpServletRequest request) throws Exception {

		ResponseMessage message = new ResponseMessage();

		UserFeedbackQuestion editedQuestions = userFeedbackQuestionService.edit(userFeedbackQuestion, id);

		message.setDetails_url(request.getRequestURL().toString());

		message.setHttpCode(HttpStatus.OK.toString());

		message.setMessageCode(Constants.SUCCESS_CODE);

		message.setMessage("UserFeedbackQuestion edited successfully !");

		message.setRecord(editedQuestions);

		message.setRecordId(editedQuestions.getId());

		return message;
	}

	@RequestMapping(value = "feedback_question/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ResponseMessage deleteUserFeedBackQuestions(@PathVariable("id") String id, HttpServletRequest request) throws NotFoundException {

		ResponseMessage message = new ResponseMessage();

		UserFeedbackQuestion editedQuestions = userFeedbackQuestionService.delete(id);

		message.setDetails_url(request.getRequestURL().toString());

		message.setHttpCode(HttpStatus.OK.toString());

		message.setMessageCode(Constants.SUCCESS_CODE);

		message.setMessage("UserFeedbackQuestion deleted successfully !");

		message.setRecord(editedQuestions);

		message.setRecordId(id);

		return message;
	}

	@RequestMapping(value = "feedback_question/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ResponseMessage deleteUserFeedBackQuestionSingle(@PathVariable("id") String id, HttpServletRequest request) throws NotFoundException {

		ResponseMessage message = new ResponseMessage();

		UserFeedbackQuestion existingQuestions = userFeedbackQuestionService.get(id);

		message.setDetails_url(request.getRequestURL().toString());

		message.setHttpCode(HttpStatus.OK.toString());

		message.setMessageCode(Constants.SUCCESS_CODE);

		message.setMessage("UserFeedbackQuestion retrieved successfully !");

		message.setRecord(existingQuestions);

		message.setRecordId(existingQuestions.getId());

		return message;
	}

	@RequestMapping(value = Urls.USER_FEED_QUESTION, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ResponseMessage addUserFeedBackQuestion(@RequestBody @Valid Question question, @PathVariable("id") String id, HttpServletRequest request) throws Exception {

		ResponseMessage message = new ResponseMessage();

		if (null == QuestionType.valueOf(question.getType())) {

			throw new Exception("Question type " + question.getType() + " not allowed");
		}

		question.setType(eventConfigurationService.getQuestionType(question.getUi().getUiControl()));

		Question existingQuestions = userFeedbackQuestionService.addQuestion(id, question);

		message.setDetails_url(request.getRequestURL().toString());

		message.setHttpCode(HttpStatus.OK.toString());

		message.setMessageCode(Constants.SUCCESS_CODE);

		message.setMessage("UserFeedbackQuestion added successfully !");

		message.setRecord(existingQuestions);

		message.setRecordId(existingQuestions.getId());

		return message;
	}

	@RequestMapping(value = Urls.USER_FEED_QUESTION_INNER_OBJECT, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ResponseMessage addUserFeedBackQuestionMultiLingual(@RequestBody QuestionMultiLingual multiLingual, @PathVariable("id") String id, @PathVariable("qId") String qId, HttpServletRequest request) throws NotFoundException, OptionException {

		ResponseMessage message = new ResponseMessage();

		Question existingQuestions = userFeedbackQuestionService.addMultiLingual(id, qId, multiLingual);

		message.setDetails_url(request.getRequestURL().toString());

		message.setHttpCode(HttpStatus.OK.toString());

		message.setMessageCode(Constants.SUCCESS_CODE);

		message.setMessage("UserFeedbackQuestion retrieved successfully !");

		message.setRecord(existingQuestions);

		message.setRecordId(existingQuestions.getId());

		return message;
	}

	@RequestMapping(value = Urls.USER_FEED_QUESTION_INNER_OBJECT, method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ResponseMessage editUserFeedBackQuestion(@RequestBody Question question, @PathVariable("id") String id, @PathVariable("qId") String qId, HttpServletRequest request) throws NotFoundException {

		ResponseMessage message = new ResponseMessage();

		question.setType(eventConfigurationService.getQuestionType(question.getUi().getUiControl()));

		Question existingQuestions = userFeedbackQuestionService.editQuestion(id, qId, question);

		message.setDetails_url(request.getRequestURL().toString());

		message.setHttpCode(HttpStatus.OK.toString());

		message.setMessageCode(Constants.SUCCESS_CODE);

		message.setMessage("UserFeedbackQuestion retrieved successfully !");

		message.setRecord(existingQuestions);

		message.setRecordId(existingQuestions.getId());

		return message;
	}

	@RequestMapping(value = Urls.USER_FEED_QUESTION_INNER_OBJECT, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ResponseMessage getUserFeedBackQuestion(@PathVariable("id") String id, @PathVariable("qId") String qId, HttpServletRequest request) throws NotFoundException {

		ResponseMessage message = new ResponseMessage();

		Question existingQuestions = userFeedbackQuestionService.getQuestion(id, qId);

		message.setDetails_url(request.getRequestURL().toString());

		message.setHttpCode(HttpStatus.OK.toString());

		message.setMessageCode(Constants.SUCCESS_CODE);

		message.setMessage("UserFeedbackQuestion retrieved successfully !");

		message.setRecord(existingQuestions);

		message.setRecordId(existingQuestions.getId());

		return message;
	}

	@RequestMapping(value = Urls.USER_FEED_QUESTION_INNER_OBJECT, method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ResponseMessage deleteUserFeedBackQuestion(@PathVariable("id") String id, @PathVariable("qId") String qId, HttpServletRequest request) throws NotFoundException {

		ResponseMessage message = new ResponseMessage();

		Question existingQuestions = userFeedbackQuestionService.deleteQuestion(id, qId);

		message.setDetails_url(request.getRequestURL().toString());

		message.setHttpCode(HttpStatus.OK.toString());

		message.setMessageCode(Constants.SUCCESS_CODE);

		message.setMessage("UserFeedbackQuestion deleted successfully !");

		message.setRecord(existingQuestions);

		message.setRecordId(existingQuestions.getId());

		return message;
	}

}
