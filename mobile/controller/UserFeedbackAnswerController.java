package org.iqvis.nvolv3.mobile.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.UserFeedback;
import org.iqvis.nvolv3.service.UserFeedbackAnswerService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Urls;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("restriction")
@Controller
@RequestMapping(Urls.FEEDBACK_ANSWER)
public class UserFeedbackAnswerController {

	@Resource(name = Constants.USER_FEEDBACK_ANSWER_RESOURCE)
	private UserFeedbackAnswerService userFeedbackAnswerService;

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseMessage addUserFeedBackAnswers(@RequestBody @Valid UserFeedback userFeedback, @PathVariable("organizerId") String organizerId, HttpServletRequest request) throws Exception {

		ResponseMessage message = new ResponseMessage();

		userFeedback.getFeedBack().setOrganizerId(organizerId);

		UserFeedback addedQuestions = userFeedbackAnswerService.add(userFeedback);

		message.setDetails_url(request.getRequestURL().toString());

		message.setHttpCode(HttpStatus.CREATED.toString());

		message.setMessageCode(Constants.SUCCESS_CODE);

		message.setMessage("UserFeedbackQuestion added successfully !");

		message.setRecord(addedQuestions);

		message.setRecordId(addedQuestions.getId());

		return message;
	}

}
