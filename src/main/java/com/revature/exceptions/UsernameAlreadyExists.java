package com.revature.exceptions;

public class UsernameAlreadyExists extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5684103772847733640L;
	public UsernameAlreadyExists(String errorMessage) {
        super(errorMessage);
    }
}
