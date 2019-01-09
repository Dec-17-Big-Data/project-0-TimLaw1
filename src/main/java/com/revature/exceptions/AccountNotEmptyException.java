package com.revature.exceptions;

public class AccountNotEmptyException  extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7172478170158789935L;

	public AccountNotEmptyException(String errorMessage) {
		super(errorMessage);
    }
}
