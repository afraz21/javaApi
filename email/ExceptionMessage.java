package org.iqvis.nvolv3.email;

public class ExceptionMessage {

	public ExceptionMessage(String exception, String uRL) {
		super();
		this.exception = exception;
		URL = uRL;
	}

	private String exception;

	private String URL;

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Exception-StackTrace : <br><pre style='background:#FFFFE0;'>" + exception + "</pre><br><br><span style='color:RED;'>Exception-URL : " + URL + "</span>";
	}

}
