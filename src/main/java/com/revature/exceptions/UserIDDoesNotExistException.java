package com.revature.exceptions;

public class UserIDDoesNotExistException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4558520483394234335L;

	public UserIDDoesNotExistException(String errorMessage) {
        super(errorMessage);
    }
}
