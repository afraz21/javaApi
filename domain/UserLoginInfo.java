package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.utils.Utils;

public class UserLoginInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="accountId should not be empty")
	@NotNull(message="accountId should not be null")
	private String accountId;;
	
	@NotEmpty(message="password should not be empty")
	@NotNull(message="password should not be null")
	private String password;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getPassword() throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {
		
		return Utils.getSHA512Hash(this.password);
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
