package org.iqvis.nvolv3.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The specified Media is not allowed.")
public class MediaTypeNotAllowedException extends Exception {

	private static final long serialVersionUID = -3332292346834265371L;

	public MediaTypeNotAllowedException(String type) {
		super("Media type(" + type + ") not allowed");
	}
}
