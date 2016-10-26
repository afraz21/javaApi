package org.iqvis.nvolv3.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The specified file type is not allowed.")
public class FileTypeNotAllowedException extends Exception {

	private static final long serialVersionUID = -3332292346834265371L;

	public FileTypeNotAllowedException() {
		super("This file type is not allowed");
	}
}
