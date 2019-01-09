package com.revature.modules;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.exceptions.IncorrectPasswordException;
import com.revature.models.User;
import com.revature.services.UserService;
import com.revature.utils.ReadInput;
import com.revature.utils.SessionManager;

public class Login {
	private static final Logger logger = LogManager.getLogger(Login.class);
	private ReadInput ri = new ReadInput();
	private UserService userService = UserService.getService();
	public void handleLoginRequest() {
		logger.traceEntry("entering public void class handleLoginRequest");
		logger.traceExit("Calling usernameAndPasswordPrompt");
		usernameAndPasswordPrompt();
	}
	private void usernameAndPasswordPrompt() {
		logger.traceEntry("entering private void usernameAndPasswordPrompt");
		System.out.println("Beginning of Login prompt...");
		System.out.println("Type your username (case-sensitive) and hit enter.");
		logger.info("Calling getUsernameOrPassword() method from usernameAndPasswordPrompt");
		String myUsername = getUsernameOrPassword();
		System.out.println("Type your password (case-sensitive) and hit enter.");
		logger.info("Calling getUsernameOrPassword() method from usernameAndPasswordPrompt");
		String myPassword = getUsernameOrPassword();
		logger.traceExit("calling authenticateUser");
		authenticateUser(myUsername,myPassword);
	}
	private String getUsernameOrPassword() {
		logger.traceEntry("entry getUsernameOrPassword");
		return logger.traceExit(ri.readUsernameOrPassword());
	}
	private void authenticateUser(String username, String password) {
		logger.traceEntry("entry authenticateUser(x,x)");
		Optional<User> potentialUser;
		InputStream in = null;
		Properties props = new Properties();
		String superUsername;
		String superPassword;
		try {
			in = new FileInputStream("C:\\Users\\IcedT\\Revature\\Java Workspace\\revature-project0\\src\\main\\resources\\superuser.properties");
			props.load(in);
			// Read root superuser name and password
			superUsername = props.getProperty("username");
			superPassword = props.getProperty("password");
			if (username.equals(superUsername) && password.equals(superPassword)) {
				SessionManager.sessionUser = new User(0,"","",1);
				SessionManager mySm = SessionManager.getSessionManager();
				logger.traceExit("Calling SessionManager.processLoginRegister() as root superuser");
				mySm.processLoginRegister();
			}
		} catch (FileNotFoundException e1) {
			logger.catching(e1);
			System.out.println("Couldn't open superuser.");
		} catch (IOException e) {
			logger.catching(e);
			System.out.println("Couldn't load superuser.");
		}
		try {
			potentialUser = userService.loginUser(username,password);
			if (potentialUser.isPresent()) {
				User u = potentialUser.get();
				SessionManager.sessionUser = u;
				SessionManager mySm = SessionManager.getSessionManager();
				logger.traceExit("Calling SessionManager.processLoginRegister()");
				mySm.processLoginRegister();
			} else {
				System.out.println("Server failed to respond, try again...");
				logger.traceExit("Calling usernameErrorPrompt");
				usernameErrorPrompt();
			}
		} catch (IncorrectPasswordException e) {
			logger.catching(e);
			System.out.println("Incorrect password, try again... ");
			logger.traceExit("Incorrect password, calling usernameErrorPrompt");
			usernameErrorPrompt();
		}
	}
	private void usernameErrorPrompt() {
		logger.traceEntry("entry usernameErrorPrompt()");
		String[] validOptions = {"Login","Register"};
		ReadInput uEPRI = new ReadInput(validOptions);
		Integer choice = uEPRI.promptThenReadOptions();
		if (choice==1) {
			// Prompt username password for login
			System.out.println("You are attempting to login again with an existing username");
			usernameAndPasswordPrompt();
		} else {
			// Prompt username password for register
			System.out.println("You are registering a new username/password on the database.");
			Register myRegister = new Register();
			myRegister.handleRegisterRequest();
		}	
		logger.traceExit();
	}
}
