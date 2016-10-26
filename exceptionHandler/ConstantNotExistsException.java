package org.iqvis.nvolv3.exceptionHandler;

public class ConstantNotExistsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConstantNotExistsException(String constantName) {
		 super("Constant "+constantName+" Does Not Exists In Database Collection ! \n Please Verify Your Constant Name Mentioned In Code ");
	}
}
