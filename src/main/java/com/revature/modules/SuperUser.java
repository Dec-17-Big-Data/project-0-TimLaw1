package com.revature.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.exceptions.UsernameAlreadyExists;
import com.revature.models.User;
import com.revature.services.UserService;
import com.revature.utils.ReadInput;
import com.revature.utils.SessionManager;

public class SuperUser {
	private static final Logger logger = LogManager.getLogger(Welcome.class);
	private String[] validOptions = {"View","Create","Update","Delete","Logout"};
	private ReadInput ri = new ReadInput(validOptions);
	private List<User> allUsers;
	private UserService userService = UserService.getService();
	public void initiateSuperUserSession() {
		logger.traceEntry("entry handleSuperUserSessionHandler");
		logger.traceExit("exit handleSuperUserSessionHandler, calling promptSuperUserChoice");
		promptSuperUserChoice();
	}
	private void promptSuperUserChoice() {
		logger.traceEntry("entry promptSuperUserChoice");
		System.out.println("You are logged in as a superuser.");
		System.out.println("What would you like to do?");
		logger.info("calling ReadInput.readOptions");
		int choice = ri.promptThenReadOptions();
		System.out.printf("You have selected %s.%n", validOptions[choice-1]);
		logger.traceExit("exit promptSuperUserChoice, calling handleSuperUserRequest");
		handleSuperUserRequest(choice);
	}
	private void handleSuperUserRequest(int choice) {
		logger.traceEntry("entry handleSuperUserRequest");
		logger.info("Choice {}",choice);
		logger.info("calling loadAllUsers");
		loadAllUsers();
		if (choice==1) {
			// View all users
			System.out.println("You chose to view all users.");
			viewAllUsers();
		} else if (choice==2) {
			// Create a new user
			System.out.println("You chose to create a new user.");
			registerNewUser();
		} else if (choice==3) {
			// Update an existing user
			System.out.println("You chose to update an existing user.");
			updateExistingUserPrompt();
		} else if (choice==4) {
			// Delete an existing user
			System.out.println("You chose to delete an existing user.");
			deleteExistingUserPrompt();
		} else {
			// Logout
			System.out.println("You chose to logout of the super user account.");
			SessionManager mySM = SessionManager.getSessionManager();
			mySM.start();
		}
	}
	private void loadAllUsers() {
		logger.traceEntry("entry loadAllUsers");
		Optional<List<User>> potentialUsers = userService.getAllUsers();
		if (potentialUsers.isPresent()) {
			allUsers = potentialUsers.get();
		} else {
			System.out.println("Server failed to load users.");
		}
		logger.traceExit("exit loadAllUsers");
	}
	private void viewAllUsers() {
		logger.traceEntry("entry viewAllUsers");
		System.out.printf("User ID,\tUser name,\tUser password,\tIsSuperUser%n");
		for (User u: allUsers) {
			System.out.printf("%d,\t%s,\t%s,\t%d%n",u.getUser_id(),u.getUser_name(),u.getUser_password(),u.getAdmin());
		}
		logger.traceExit("exit viewAllUsers, call promptSuperUserChoice again");
		promptSuperUserChoice();
	}
	private void registerNewUser() {
		logger.traceEntry("entry registerNewUser");
		System.out.println("Type in your username, then hit enter.");
		logger.info("calling ReadInput.readUsernameOrPassword");
		String username = ri.readUsernameOrPassword();
		System.out.println("Type in your password, then hit enter.");
		logger.info("calling ReadInput.readUsernameOrPassword");
		String password = ri.readUsernameOrPassword();
		try {
			logger.info("Calling userService.registerUser with given username/password combination");
			Optional<User> optionalUser = userService.registerUser(username, password);
			if (optionalUser.isPresent()) {
				logger.info("Server responded with the new user. User successfully added to database. Adding new user to local array of users for future local viewing.");
				User newUser = optionalUser.get();
				allUsers.add(newUser);
			} else {
				logger.info("The server failed to respond with a user object.");
				System.out.println("Server failure, user may or may not have been registered, try again.");
			}
		} catch (UsernameAlreadyExists e) {
			// TODO Auto-generated catch block
			logger.catching(e);
			System.out.println("You entered a username that already exists, new user not registered.");
		}
		logger.traceExit("exit registerNewUser, call promptSuperUserChoice again");
		promptSuperUserChoice();
	}
	private void updateExistingUserPrompt() {
		logger.traceEntry("entry updateExistingUserPrompt");
		System.out.println("First, select a valid userID");
		int lUserID = getUserIDPrompt();	
		System.out.printf("You have selected UserID #%d.%n", lUserID);
		System.out.println("What would you like to change?");
		String[] validOptions = {"Username","Password","Both"};
		logger.info("calling local ReadInput.readOptions");
		ReadInput lri = new ReadInput(validOptions);
		int choice = lri.promptThenReadOptions();
		System.out.printf("You have selected to change %s.%n", validOptions[choice-1]);
		logger.traceExit("exit updateExistingUserPrompt, calling handleUpdateUserRequest");
		handleUpdateUserRequest(choice,lUserID);
	}
	private int getUserIDPrompt() {
		logger.traceEntry("entry getUserIDPrompt");
		List<String> validIDs = new ArrayList<>();
		for (User u: allUsers) {
			if (u.getUser_id()==SessionManager.sessionUser.getUser_id()) {
				continue;
			}
			validIDs.add(String.valueOf(u.getUser_id()));
		}
		ReadInput lri = new ReadInput(validIDs);
		logger.info("calling local ReadInput class method promptThenReadOptions");
		int userIDChoice = lri.promptThenReadOptions();
		logger.traceExit("exit getUserIDPrompt");
		return Integer.valueOf(validIDs.get(userIDChoice-1));
	}
	private void handleUpdateUserRequest(int choice,int lUserID) {
		logger.traceEntry("entry handleUpdateUserRequest");
		String username, password,database_password = null,database_username = null;
		Integer isSuperUser = null;
		for (User u: allUsers) {
			if (u.getUser_id()==lUserID) {
				database_username = u.getUser_name();
				database_password = u.getUser_password();
				isSuperUser = u.getAdmin();
			}
		}
		if (database_username==null||database_password==null||isSuperUser==null) {
			System.out.println("The user wasn't found locally, weird bug, should never happen.");
			logger.info("The user wasn't found locally, weird bug, should never happen.");
			System.out.println("Try from scratch.");
			logger.traceExit("retrying promptSuperUserChoice");
			promptSuperUserChoice();
		}
		if (choice == 1) {
			// only change username
			logger.info("reading new username");
			System.out.println("Type the username and hit enter.");
			username = ri.readUsernameOrPassword();
			password = database_password;
		} else if (choice == 2) {
			// only change password
			username = database_username;
			logger.info("reading new password");
			System.out.println("Type the password and hit enter.");
			password = ri.readUsernameOrPassword();
		} else {
			// change both
			logger.info("reading new username");
			System.out.println("Type the username and hit enter.");
			username = ri.readUsernameOrPassword();
			logger.info("reading new password");
			System.out.println("Type the password and hit enter.");
			password = ri.readUsernameOrPassword();
		}
		try {
			Optional<Boolean> updateSuccess = userService.updateUser(lUserID, username, password);
			if (updateSuccess.isPresent()) {
				System.out.println("Database connection works.");
				Boolean upSucc = updateSuccess.get();
				if (upSucc) {
					System.out.println("Database updated properly.");
					User updatedUser = new User(lUserID, username, password, isSuperUser);
					for (User u: allUsers) {
						if (u.getUser_id()==lUserID) {
							u = updatedUser; 
							System.out.println("The user has been updated successfully.");
							logger.info("Successfully updated user information after update request.");
						}
					}
				} else {
					System.out.println("The update didn't work for some reason, the user may or may not have been updated.");
					logger.info("The update didn't work for some reason, the user may or may not have been updated.");
				}
			} else {
				// Connection didn't work
				System.out.println("Database error occurred, update request failed.");
				logger.info("Database error occurred, update request failed.");
			}
		} catch (UsernameAlreadyExists e) {
			logger.catching(e);
			System.out.println("Username already exists, couldn't update database.");
		}
		logger.traceExit("exit handleUpdateUserRequest, calling promptSuperUserChoice");
		promptSuperUserChoice();
	}
	public void deleteExistingUserPrompt() {
		logger.traceEntry("entry deleteExistingUserPrompt");
		System.out.println("First, select a valid userID");
		int lUserID = getUserIDPrompt();	
		System.out.printf("You have selected UserID #%d.%n", lUserID);
		logger.traceExit("exit deleteExistingUserPrompt, calling handleDeleteUserRequest");
		handleDeleteUserRequest(lUserID);
	}
	public void handleDeleteUserRequest(int lUserID) {
		logger.traceEntry("entry handleDeleteUserRequest");
		System.out.printf("Deleting user with UserID %d.%n",lUserID);
		Optional<Boolean> deleteSuccess =userService.deleteUser(lUserID);
		if (deleteSuccess.isPresent()) {
			Boolean delSuc = deleteSuccess.get();
			if (delSuc) {
				int indexToRemove = -1;
				for(int i = 0;i<allUsers.size();i++) {
					if (allUsers.get(i).getUser_id()==lUserID) {
						indexToRemove = i;
					}
				}
				if (indexToRemove!=-1) {
					allUsers.remove(indexToRemove);
				} else {
					logger.info("Failed to find local user with userID that was deleted on the database, weird bug if this ever prints.");
					System.out.println("Failed to find local user with userID that was deleted on the database, weird bug if this ever prints.");
				}
			} else {
				System.out.println("Server failed to delete user, user may still be on database. Restart program to be sure.");
				logger.info("User delete request failed.");		
			}
		} else {
			System.out.println("Server failed to respond after request to delete user, user may still be on database. Restart program to be sure.");
			logger.info("Major error, should restart program now.");
		}
		logger.traceExit("exit handleDeleteUserRequest, calling promptSuperUserChoice");
		promptSuperUserChoice();
	}
}
