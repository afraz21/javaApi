package org.iqvis.nvolv3.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The specified entity does not exist.")
public class AlreadyExistException extends Exception {

	private static final long serialVersionUID = -3332292346834265371L;

	public AlreadyExistException(String id, String recordType) {
		super(recordType + " already exit with id " + id.toString());
	}
}
