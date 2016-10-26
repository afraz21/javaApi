package org.iqvis.nvolv3.exceptionHandler;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.User;
import org.iqvis.nvolv3.email.EmailServiceImpl;
import org.iqvis.nvolv3.email.ExceptionMessage;
import org.iqvis.nvolv3.service.UserService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.ISystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;

@SuppressWarnings("restriction")
@ControllerAdvice
public class GlobalExceptionHandler {

	protected static Logger logger = Logger.getLogger("controller");

	@Autowired
	protected EmailServiceImpl emailService;

	/*
	 * Exception handler
	 */
	@Resource(name = "userService")
	private UserService userService;

	@Autowired
	private String systemtExecutionEnvironment;

	@ExceptionHandler(NotFoundException.class)
	public @ResponseBody ResponseMessage handleNotFoundException(HttpServletRequest request, Exception ex) {

		ResponseMessage response = new ResponseMessage();

		response.setDetails_url(request.getRequestURL().toString());

		response.setMessage(ex.getMessage());

		response.setMessageCode(Constants.SUCCESS_CODE);

		response.setHttpCode(HttpStatus.NOT_FOUND.toString());

		logger.debug(request.getRequestURL().toString());
		// try {
		// if (!request.getRequestURL().toString().contains("localhost")) {
		// emailService.sendEmail("qasim.gulzar@iqvis.com", ex.getMessage(), new
		// ExceptionMessage(getStackTrace(ex),
		// request.getRequestURL().toString()).toString());
		// emailService.sendEmail("naveed.sharif@iqvis.com", ex.getMessage(),
		// new ExceptionMessage(getStackTrace(ex),
		// request.getRequestURL().toString()).toString());
		// emailService.sendEmail("zeeshan.omer@iqvis.com", ex.getMessage(), new
		// ExceptionMessage(getStackTrace(ex),
		// request.getRequestURL().toString()).toString());
		// }
		// }
		// catch (Exception e) {
		// e.printStackTrace();
		// }
		ex.printStackTrace();

		return response;
	}

	@ExceptionHandler(MediaTypeNotAllowedException.class)
	public @ResponseBody ResponseMessage handleMediaTypeNotAllowedException(HttpServletRequest request, Exception ex) {

		ResponseMessage response = new ResponseMessage();

		response.setDetails_url(request.getRequestURL().toString());

		response.setMessage(ex.getMessage());

		response.setHttpCode(HttpStatus.NOT_ACCEPTABLE.toString());

		logger.debug(request.getRequestURL().toString());

		// try {
		// if (!request.getRequestURL().toString().contains("localhost")) {
		// emailService.sendEmail("qasim.gulzar@iqvis.com", ex.getMessage(), new
		// ExceptionMessage(getStackTrace(ex),
		// request.getRequestURL().toString()).toString());
		// emailService.sendEmail("naveed.sharif@iqvis.com", ex.getMessage(),
		// new ExceptionMessage(getStackTrace(ex),
		// request.getRequestURL().toString()).toString());
		// emailService.sendEmail("zeeshan.omer@iqvis.com", ex.getMessage(), new
		// ExceptionMessage(getStackTrace(ex),
		// request.getRequestURL().toString()).toString());
		// }
		// }
		// catch (Exception e) {
		// e.printStackTrace();
		// }
		ex.printStackTrace();

		return response;
	}

	@ExceptionHandler(FileTypeNotAllowedException.class)
	public @ResponseBody ResponseMessage handleFileTypeNotAllowedException(HttpServletRequest request, Exception ex) {

		ResponseMessage response = new ResponseMessage();

		response.setDetails_url(request.getRequestURL().toString());

		response.setMessage(ex.getMessage());

		response.setHttpCode(HttpStatus.NOT_ACCEPTABLE.toString());

		logger.debug(request.getRequestURL().toString());

		// try {
		// if (!request.getRequestURL().toString().contains("localhost")) {
		// emailService.sendEmail("qasim.gulzar@iqvis.com", ex.getMessage(), new
		// ExceptionMessage(getStackTrace(ex),
		// request.getRequestURL().toString()).toString());
		// emailService.sendEmail("naveed.sharif@iqvis.com", ex.getMessage(),
		// new ExceptionMessage(getStackTrace(ex),
		// request.getRequestURL().toString()).toString());
		// emailService.sendEmail("zeeshan.omer@iqvis.com", ex.getMessage(), new
		// ExceptionMessage(getStackTrace(ex),
		// request.getRequestURL().toString()).toString());
		// }
		// }
		// catch (Exception e) {
		// e.printStackTrace();
		// }
		ex.printStackTrace();

		return response;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public @ResponseBody ResponseMessage handleBindingException(HttpServletRequest request, MethodArgumentNotValidException ex) {

		StringBuilder responseMessage = new StringBuilder();

		ResponseMessage response = new ResponseMessage();

		response.setDetails_url(request.getRequestURL().toString());

		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			responseMessage.append(error.getDefaultMessage() + " ,");

		}

		response.setMessage(responseMessage.toString());

		response.setMessageCode(ex.getLocalizedMessage());

		logger.debug(request.getRequestURL().toString());

		// try {
		// if (!request.getRequestURL().toString().contains("localhost")) {
		// emailService.sendEmail("qasim.gulzar@iqvis.com", ex.getMessage(), new
		// ExceptionMessage(getStackTrace(ex),
		// request.getRequestURL().toString()).toString());
		// emailService.sendEmail("naveed.sharif@iqvis.com", ex.getMessage(),
		// new ExceptionMessage(getStackTrace(ex),
		// request.getRequestURL().toString()).toString());
		// emailService.sendEmail("zeeshan.omer@iqvis.com", ex.getMessage(), new
		// ExceptionMessage(getStackTrace(ex),
		// request.getRequestURL().toString()).toString());
		// }
		// }
		// catch (Exception e) {
		// e.printStackTrace();
		// }
		ex.printStackTrace();

		return response;
	}

	@ExceptionHandler(Exception.class)
	public @ResponseBody ResponseMessage handleException(HttpServletRequest request, Exception ex) {

		ResponseMessage response = new ResponseMessage();

		response.setDetails_url(request.getRequestURL().toString());

		response.setMessage(ex.getMessage());

		response.setMessageCode(Constants.ERROR_CODE);

		logger.debug(request.getRequestURL().toString());

		try {
			if (ISystemConstant.ENVOIRNMENT_PRODUCTION.equals(systemtExecutionEnvironment)) {

				emailService.sendEmail("qasim.gulzar@iqvis.com", ex.getMessage(), new ExceptionMessage(getStackTrace(ex), request.getRequestURL().toString()).toString());

				emailService.sendEmail("naveed.sharif@iqvis.com", ex.getMessage(), new ExceptionMessage(getStackTrace(ex), request.getRequestURL().toString()).toString());

				emailService.sendEmail("zeeshan.omer@iqvis.com", ex.getMessage(), new ExceptionMessage(getStackTrace(ex), request.getRequestURL().toString()).toString());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		ex.printStackTrace();

		return response;
	}

	@ModelAttribute
	public void validateOrganizer(HttpServletRequest servletRequest) throws Exception {

		@SuppressWarnings("rawtypes")
		Map pathVariables = (Map) servletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

		if (pathVariables != null && pathVariables.containsKey("adminId")) {

			User adminUser = userService.get(pathVariables.get("adminId").toString(), Constants.USER_ADMIN);

			if (adminUser == null) {

				throw new Exception("User is not authorized to access restricted area");
			}
		}

		if (pathVariables != null && pathVariables.containsKey("organizerId")) {

			User organizer = userService.getInclude(pathVariables.get("organizerId").toString());

			// User organizer =
			// userService.getInclude(pathVariables.get("organizerId").toString());

			if (null == organizer) {
				logger.debug("Organizer not found with id " + pathVariables.get("organizerId").toString());

				throw new NotFoundException(pathVariables.get("organizerId").toString(), "Organizer");

			}
			else {

				// logger.debug("Loading organizer with id " +
				// pathVariables.get("organizerId").toString());
			}
		}
	}

	public String getStackTrace(final Throwable throwable) {

		StringBuilder sb = new StringBuilder();
		for (StackTraceElement element : throwable.getStackTrace()) {
			sb.append(element.toString());
			sb.append("<br>");
		}

		return sb.toString();
	}
}
