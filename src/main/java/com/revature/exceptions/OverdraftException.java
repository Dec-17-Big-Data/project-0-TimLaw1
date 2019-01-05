package com.revature.exceptions;

public class OverdraftException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7168679456912742268L;

	public OverdraftException(String errorMessage) {
        super(errorMessage);
    }
}
