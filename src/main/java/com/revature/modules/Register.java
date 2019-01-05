package com.revature.modules;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.exceptions.UsernameAlreadyExists;
import com.revature.models.User;
import com.revature.services.UserService;
import com.revature.utils.ReadInput;
import com.revature.utils.SessionManager;

public class Register {
	private static final Logger logger = LogManager.getLogger(Register.class);
	private ReadInput ri = new ReadInput();
	private UserService userService = UserService.getService();
	public void handleRegisterRequest() {
		logger.traceEntry("entry handleRegisterRequest");
		logger.traceExit("exit, calling usernameAndPasswordPrompt");
		usernameAndPasswordPrompt();
	}
	private void usernameAndPasswordPrompt() {
		logger.traceEntry("entry usernameAndPasswordPrompt");
		System.out.println("Beginning of Registration prompt...");
		System.out.println("Type a new username (case-sensitive) and hit enter.");
		logger.info("calling getUsernameOrPassword");
		String myUsername = getUsernameOrPassword();
		System.out.println("Type a new password (case-sensitive) and hit enter.");
		logger.info("calling getUsernameOrPassword");
		String myPassword = getUsernameOrPassword();
		logger.traceExit("exit, calling registerNewUser");
		registerNewUser(myUsername,myPassword);	
	}
	private String getUsernameOrPassword() {
		logger.traceEntry();
		return logger.traceExit(ri.readUsernameOrPassword());
	}
	private void registerNewUser(String username, String password) {
		logger.traceEntry("entry registerNewUser");
		System.out.println("Registering new user...");
		Optional<User> potentialUser;
		try {
			potentialUser = userService.registerUser(username,password);
			if (potentialUser.isPresent()) {
				User u = potentialUser.get();
				SessionManager.sessionUser = u;
				SessionManager mySm = SessionManager.getSessionManager();
				logger.traceExit("New user registered, calling session manager to handle showing the user admin/not-admin views.");
				mySm.processLoginRegister();
			} else {
				System.out.println("Server failed to respond, try again...");
				logger.traceExit("Server failed to respond, calling registration error prompt");
				registrationErrorPrompt();
			}
		} catch (UsernameAlreadyExists e) {
			logger.catching(e);
			System.out.println("That username is already taken, what would you like to do?");
			logger.traceExit("Username already exists, calling registration error prompt");
			registrationErrorPrompt();
		}
	}
	private void registrationErrorPrompt() {
		logger.traceEntry("entry registrationErrorPrompt");
		String[] validOptions = {"Register","Login"};
		ReadInput myRI = new ReadInput(validOptions);
		Integer choice = myRI.promptThenReadOptions();
		if (choice == 1) {
			System.out.println("You are attempting to register again.");
			logger.traceExit("exit usernameAndPasswordPrompt");
			usernameAndPasswordPrompt();
		} else {
			System.out.println("You are attempting to Login.");
			Login myLogin = new Login();
			logger.traceExit("exit myLogin.handleLoginRequest");
			myLogin.handleLoginRequest();
		}
	
	}
}
