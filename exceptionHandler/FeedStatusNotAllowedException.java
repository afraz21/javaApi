package org.iqvis.nvolv3.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The feed status is not allowed.")
public class FeedStatusNotAllowedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FeedStatusNotAllowedException(String feedStatus) {
		super("Feed Status (" + feedStatus + ") not allowed)");
	}

}
