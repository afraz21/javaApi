package org.iqvis.nvolv3.email;

import org.hibernate.validator.constraints.NotEmpty;

public class Email {

	private String from;

	@NotEmpty(message = "to cannot be empty.")
	private String to;

	@NotEmpty(message = "subject cannot be empty.")
	private String subject;

	@NotEmpty(message = "body cannot be empty.")
	private String body;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
