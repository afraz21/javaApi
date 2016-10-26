package org.iqvis.nvolv3.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The specified data already exist.")
public class ReferenceDataAlreadyExistException extends Exception {

	private static final long serialVersionUID = -3332292346834265371L;

	public ReferenceDataAlreadyExistException(String id, String recordType) {
		super(recordType + " for organizer " + id.toString() + " already created");
	}
}
