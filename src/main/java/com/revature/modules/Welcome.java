package com.revature.modules;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.utils.ReadInput;

public class Welcome {
	private static final Logger logger = LogManager.getLogger(Welcome.class);
	private String[] validOptions = {"Login","Register","Exit"};
	private ReadInput ri = new ReadInput(validOptions);
	public int prompt(int ... optionalArgs) {
		logger.traceEntry("Params {}", optionalArgs);
		//Optional Arguments so you can do integration test without the view/scanner
		if (optionalArgs.length == 1) {
			logger.info("Entered one variable argument");
			if(optionalArgs[0]==1) {
				logger.info("Chose login");
				return logger.traceExit(1);
			} else if (optionalArgs[0]==2) {
				logger.info("Chose Register");
				return logger.traceExit(2);
			}
		}
		System.out.println("Welcome to the Project 0 Oracle Database Bank App");
		System.out.println("Do you want to login to an existing account or register a new bank account?");
		int choice = ri.promptThenReadOptions();
		System.out.printf("You have selected %s.%n", validOptions[choice-1]);
		return logger.traceExit(choice);
	}
}