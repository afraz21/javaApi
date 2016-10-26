package org.iqvis.nvolv3.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The feed type is not allowed.")
public class FeedTypeNotAllowedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7381684169892894101L;

	public FeedTypeNotAllowedException(String feedType) {

		super("Feed Type (" + feedType + ") not allowed)");
	}

}
