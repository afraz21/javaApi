package org.iqvis.nvolv3.exceptionHandler;

import org.iqvis.nvolv3.utils.Utils;

public class UserTypeNotAllowedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserTypeNotAllowedException(String userType){
		
		super(Utils.isEmptyOrNull(userType)?"Empty or Null User Type is Not Allowed":"UserType ( "+userType+" ) Is Not Allowed Please Use Valid User Type user Creation Process");
		
	}

}
