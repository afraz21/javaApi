package org.iqvis.nvolv3.exceptionHandler;

public class EmailAlreadyExists extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EmailAlreadyExists(String email){
		
		super("Email "+email+"Already Exists !");
	}
	
	public EmailAlreadyExists(){
		
		super(org.iqvis.nvolv3.utils.Messages.USER_EMAIL_ALREADY_EXISTS);
	}
}
