package com.revature.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.models.User;
import com.revature.modules.Login;
import com.revature.modules.Register;
import com.revature.modules.RegularUser;
import com.revature.modules.SuperUser;
import com.revature.modules.Welcome;

public class SessionManager {
	private static final Logger logger = LogManager.getLogger(SessionManager.class);
	public static User sessionUser;
	private static SessionManager sessionManager;
	private SessionManager() {
		
	}
	public static SessionManager getSessionManager() {
		if (sessionManager == null) {
			sessionManager = new SessionManager();
		}
		return sessionManager;
	}
	public void start() {
		logger.traceEntry("entry start");
		Welcome myWelcome = new Welcome();
		logger.traceExit("exit start, calling processWelcomeChoice(myWelcome.prompt())");
		processWelcomeChoice(myWelcome.prompt());	
	}
	private void processWelcomeChoice(int choice) {
		logger.traceEntry("entry processWelcomeChoice");
		if (choice == 1) {
			// Login prompt
			Login myLogin = new Login();
			logger.traceExit("exit processWelcomeChoice, calling myLogin.handleLoginRequest");
			myLogin.handleLoginRequest();
		} else {
			// Register prompt
			Register myRegister = new Register();
			logger.traceExit("exit processWelcomeChoice, calling myRegister.handleRegisterRequest");
			myRegister.handleRegisterRequest();
		}
	}
	public void processLoginRegister() {
		logger.traceEntry("entry processLoginRegister");
		System.out.println("Login/Register Succeeded");
		if (sessionUser.getAdmin()==1) {
			SuperUser mySuperUser = new SuperUser();
			logger.traceExit("exit processLoginRegister, calling mySuperUser.initiateSuperUserSession()");
			mySuperUser.initiateSuperUserSession();
		} else {
			//TODO: implement this path
			RegularUser myRegularUser = new RegularUser();
			logger.traceExit("exit processLoginRegister, calling myRegularUser.initiateRegularUserSession()");
			myRegularUser.initiateRegularUserSession();
		}
	}
}
