package org.iqvis.nvolv3.exceptionHandler;

public class AccountIdAlreadyExists extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AccountIdAlreadyExists(){
		
		super(org.iqvis.nvolv3.utils.Messages.USER_ACCOUNT_ID_ALREADY_EXISTS);
	}
	
	public AccountIdAlreadyExists(String accountId){
	
		super("AccountId "+accountId+" Already Exists !");
	}

}
