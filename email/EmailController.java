package org.iqvis.nvolv3.email;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.exceptionHandler.EmailSendException;
import org.iqvis.nvolv3.utils.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("restriction")
@Controller
@RequestMapping("/sendEmail")
public class EmailController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_EMAIL)
	private EmailService emailService;

	@RequestMapping(value = "", method = { RequestMethod.POST })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseMessage sendEmail(@RequestBody @Valid Email email, HttpServletRequest request) throws Exception {

		ResponseMessage response = new ResponseMessage();

		try {
			emailService.sendEmail(email.getTo(), email.getSubject(), email.getBody());

			response.setMessage("Email sent successfully");

			response.setRecordId(email.getTo());

			response.setDetails_url("");

			logger.debug("Email sent successfully to" + email.getTo());

		}
		catch (Exception e) {

			logger.debug("Exception while adding sending email ", e);

			if (e.getClass().equals(MailException.class)) {

				throw new EmailSendException(email.getTo());

			}

			throw e;
		}
		return response;
	}

}