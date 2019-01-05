package com.revature.modules;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.models.Account;
import com.revature.services.AccountService;
import com.revature.utils.ReadInput;
import com.revature.utils.SessionManager;

public class RegularUser {
	private static final Logger logger = LogManager.getLogger(Welcome.class);
	private String[] validOptions = {"View","Create","Delete","Logout"};
	private ReadInput ri = new ReadInput(validOptions);
	private Account myAccount;
	private List<Account> myAccounts;
	private AccountService accountService = AccountService.getService();
	public void initiateRegularUserSession() {
		logger.traceEntry("entry handleRegularUserSessionHandler");
		logger.traceExit("exit handleRegularUserSessionHandler, calling promptRegularUserChoice");
		promptRegularUserChoice();
	}
	private void promptRegularUserChoice() {
		logger.traceEntry("entry promptRegularUserChoice");
		getAllAccounts();
		System.out.println("You are logged in as a regular user.");
		System.out.println("What would you like to do with your account(s)?");
		logger.info("calling ReadInput.readOptions");
		int choice = ri.promptThenReadOptions();
		System.out.printf("You have selected %s.%n", validOptions[choice-1]);
		logger.traceExit("exit promptRegularUserChoice, calling handleSuperUserRequest");
		handleRegularUserRequest(choice);
	}
	private void handleRegularUserRequest(Integer choice) {
		logger.traceEntry("entry handleRegularUserRequest");
		if (choice==1) {
			// View all accounts
			logger.traceExit("exit handleRegularUserRequest, calling");
			handleViewAccountRequest();
		} else if (choice==2) {
			// Create an account
			logger.traceExit("exit handleRegularUserRequest, calling handleCreateAccountRequest");
			handleCreateAccountRequest();
		} else if (choice==3) {
			// Delete an account
			logger.traceExit("exit handleRegularUserRequest, calling");
		} else if (choice==4) {
			// Logout
			logger.traceExit("exit handleRegularUserRequest, calling");
		}
		}
	private void handleViewAccountRequest() {
		logger.traceEntry("entry handleViewAccountRequest");
		System.out.println("Account viewer menu.");
		System.out.println("What would you like to view?");
		String[] lValidOptions = {"Individual","All"};
		ReadInput lRI = new ReadInput(lValidOptions);
		logger.info("calling ReadInput.readOptions");
		int lChoice = lRI.promptThenReadOptions();
		System.out.printf("You have selected %s.%n", lValidOptions[lChoice-1]);
		if (lChoice==1) {
			// View Individual Account
			logger.traceExit("exit handleViewAccountRequest, calling viewIndividualAccount");
			viewIndividualAccount();
		} else {
			// View all accounts
			logger.traceExit("exit handleViewAccountRequest, calling viewAllAccounts");
			viewAllAccounts();
		}
	}
	private void viewIndividualAccount() {
		logger.traceEntry("entry viewIndividualAccount");
		System.out.println("Please select a valid account id.");
		int lAccountID = selectIDPrompt();
		Account lAccount = null;
		for (Account a: myAccounts) {
			if (a.getAccountID()==lAccountID) {
				lAccount = a;
			}
		}
		System.out.printf("AccountID,	Balance%n");
		System.out.printf("%d,\t%d%n",lAccountID,lAccount.getBalance());
		individualAccountOptionsPrompt();
		logger.traceExit("exit viewIndividualAccount, calling");
	}
	private int selectIDPrompt() {
		logger.traceEntry("entry selectIDPrompt");
		String[] validAccountIDs = new String[myAccounts.size()];
		ReadInput myRI = new ReadInput(validAccountIDs);
		int accountChoice = myRI.promptThenReadOptions();
		return logger.traceExit(Integer.valueOf(validAccountIDs[accountChoice-1]));
	}
	private void individualAccountOptionsPrompt() {
		logger.traceEntry("entry individualAccountOptionsPrompt");
		System.out.println("Would you like to make a withdrawal or deposit, or go back to view all accounts?");
		String[] lValidOptions = {"Withdraw","Deposit","Back"};
		ReadInput myRI = new ReadInput(lValidOptions);
		int lChoice = myRI.promptThenReadOptions();
		System.out.printf("You chose %s.%n",lValidOptions[lChoice-1]);
		logger.traceExit("exit individualAccountOptionsPrompt, calling handleIndividualAccountOptions");
		handleIndividualAccountOptions(lChoice);
	}
	private void handleIndividualAccountOptions(int choice) {
		logger.traceEntry("entry handleIndividualAccountOptions");
		if (choice==1) {
			// Withdraw
		} else if (choice==2) {
			// Deposit
		} else {
			// Back to view all accounts
			logger.traceExit("exit handleIndividualAccountOptions, calling viewAllAccounts");
			viewAllAccounts();
		}
	}
	private void viewAllAccounts() {
		logger.traceEntry("entry viewAllAccounts");
		System.out.println("You are viewing all accounts.");
		System.out.println("AccountID	Balance");
		for (Account a: myAccounts) {
			System.out.printf("%d,\t%d%n",a.getAccountID(),a.getBalance());	
		}
		logger.traceExit("exit viewAllAccounts, calling promptRegularUserChoice");
		promptRegularUserChoice();
	}
	private void handleCreateAccountRequest() {
		logger.traceEntry("entry handleCreateAccountRequest");
		logger.traceExit("exit handleCreateAccountRequest, calling");
	}
	private void getAllAccounts() {
		logger.traceEntry("entry getAllAccounts");
		Optional<List<Account>> optionalAccounts = accountService.getAllAccounts(SessionManager.sessionUser.getUser_id());
		if (optionalAccounts.isPresent()) {
			myAccounts = optionalAccounts.get();
		} else {
			System.out.println("Server failed to find any accounts, try to create an account, and if that doesn't work then it is a server issue.");
			logger.info("Server failed to return any accounts.");
		}
		logger.traceExit("exit getAllAccounts");	
	}
}
