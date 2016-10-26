package org.iqvis.nvolv3.bean;

public class ResponseMessage {

	private String recordId = "";

	private Object record;

	private String httpCode = "";

	private String message = "";

	private String messageCode = "";

	private String details_url = "";

	public Object getRecord() {
		return record;
	}

	public void setRecord(Object record) {
		this.record = record;
	}

	public String getHttpCode() {
		return httpCode;
	}

	public void setHttpCode(String httpCode) {
		this.httpCode = httpCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetails_url() {
		return details_url;
	}

	public void setDetails_url(String details_url) {
		this.details_url = details_url;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String message_code) {
		this.messageCode = message_code;
	}

}
