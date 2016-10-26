package org.iqvis.nvolv3.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The specified App is not allowed.")
public class AppTypeNotAllowedException extends Exception {

	private static final long serialVersionUID = -3332292346834265371L;

	public AppTypeNotAllowedException(String type) {
		super("App type(" + type + ") not allowed");
	}
}
