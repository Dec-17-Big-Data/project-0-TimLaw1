package com.revature.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.exceptions.AccountNotEmptyException;
import com.revature.exceptions.OverdraftException;
import com.revature.exceptions.UserIDDoesNotExistException;
import com.revature.models.Account;
import com.revature.models.Transaction;
import com.revature.services.AccountService;
import com.revature.services.TransactionService;
import com.revature.utils.ReadInput;
import com.revature.utils.SessionManager;

import oracle.sql.TIMESTAMP;

public class RegularUser {
	private static final Logger logger = LogManager.getLogger(Welcome.class);
	private String[] validOptions = {"View","Create","Logout"};
	private ReadInput ri = new ReadInput(validOptions);
	private Account myAccount;
	private List<Account> myAccounts = new ArrayList<>();
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
			// Logout
			logger.traceExit("exit handleRegularUserRequest, calling SessionManager.start");
			SessionManager mySM = SessionManager.getSessionManager();
			mySM.start();
		}
	}
	private void handleViewAccountRequest() {
		logger.traceEntry("entry handleViewAccountRequest");
		System.out.println("Account viewer menu.");
		if (myAccounts==null) {
			System.out.println("You have no accounts yet, would you like to create an account now?");
			String[] ValidOptions2 = {"Yes","No"};
			ReadInput lRI2 = new ReadInput(ValidOptions2);
			int choice2 = lRI2.promptThenReadOptions();
			if (choice2==1) {
				System.out.println("You are going to the create an account menu.");
				logger.traceExit("exit handleViewAccountRequest, calling handleCreateAccountRequest");
				handleCreateAccountRequest();
			} else {
				System.out.println("You will be returned to the main account choice prompt.");
				logger.traceExit("exit handleViewAccountRequest, calling promptRegularUserChoice");
				promptRegularUserChoice();
			}
		}
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
		for (Account a: myAccounts) {
			if (a.getAccountID()==lAccountID) {
				myAccount = a;
			}
		}
		System.out.printf("AccountID,	Balance%n");
		System.out.printf("%d,\t%d%n",lAccountID,myAccount.getBalance());
		individualAccountOptionsPrompt();
		logger.traceExit("exit viewIndividualAccount, calling");
	}
	private int selectIDPrompt() {
		logger.traceEntry("entry selectIDPrompt");
		String[] validAccountIDs = new String[myAccounts.size()];
		for (int i=0;i<myAccounts.size();i++) {
			validAccountIDs[i] = String.valueOf(myAccounts.get(i).getAccountID());
		}
		ReadInput myRI = new ReadInput(validAccountIDs);
		int accountChoice = myRI.promptThenReadOptions();
		System.out.printf("You selected account number %s.%n",validAccountIDs[accountChoice-1]);
		return logger.traceExit(Integer.valueOf(validAccountIDs[accountChoice-1]));
	}
	private void individualAccountOptionsPrompt() {
		logger.traceEntry("entry individualAccountOptionsPrompt");
		System.out.println("Would you like to make a withdrawal or deposit, view transaction history, delete the account, or go back to view all accounts?");
		String[] lValidOptions = {"Withdraw","Deposit","Transactions","Delete","Back"};
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
			logger.traceExit("exit handleIndividualAccountOptions, calling withdrawFromAccount");
			withdrawFromAccount();
		} else if (choice==2) {
			// Deposit
			logger.traceExit("exit handleIndividualAccountOptions, calling depositToAccount");
			depositToAccount();
		} else if (choice==3) {
			// View transactions
			logger.traceExit("exit handleIndividualAccountOptions, calling viewAccountTransactions");
			viewIndividualAccountTransactions();
		} else if (choice==4) {
			// Delete account
			logger.traceExit("exit handleIndividualAccountOptions, calling deleteAccount");
			deleteAccount();
		} else {
			// Back to view all accounts
			logger.traceExit("exit handleIndividualAccountOptions, calling viewAllAccounts");
			viewAllAccounts();
		}
	}
	private void viewIndividualAccountTransactions() {
		logger.traceEntry("entry viewIndividualAccountTransactions");
		logger.info("calling getTransactionsByAccountID");
		List<Transaction> thisAccountsTransactions = getTransactionsByAccountID();
		if (thisAccountsTransactions!=null) {
			logger.traceExit("exit viewIndividualAccountTransactions, calling printListOfTransactions(thisAccountsTransactions)");
			printListOfTransactions(thisAccountsTransactions);
		} else {
			System.out.println("Problem getting transactions list, sending you back to account view.");
			logger.traceExit("exit viewIndividualAccountTransactions, calling individualAccountOptionsPrompt");
			individualAccountOptionsPrompt();
		}
	}
	private void printListOfTransactions(List<Transaction> myTransactionList) {
		System.out.println("Transaction ID,	Amount,	Withdrawal, Date of purchase");
		for (Transaction t: myTransactionList) {
			System.out.printf("%d, %d, %s, %s%n",t.getTransaction_id(),t.getAmount(),t.getWithdrawal() == 1 ? "true":"false",t.getDate_of_purchase().toString());
		}
		System.out.println("Going back to account view.");
		logger.traceExit("exit printListOfTransactions, calling individualAccountOptionsPrompt");
		individualAccountOptionsPrompt();
	}
	private List<Transaction> getTransactionsByAccountID() {
		logger.traceEntry("entry getTransactionsByAccountID");
		TransactionService transactionService = TransactionService.getService();
		Optional<List<Transaction>> optionalTransactions = transactionService.getAllTransactionsByAccountID(myAccount.getAccountID());
		if (optionalTransactions.isPresent()) {
			logger.traceExit("exit getTransactionsByAccountID, returning transaction list to calling function.");
			return optionalTransactions.get();
		} else {
			System.out.println("Server failed to connect, transactions not received.");
			System.out.println("Going back to account view.");
			logger.traceExit("exit getTransactionsByAccountID, calling individualAccountOptionsPrompt");
			individualAccountOptionsPrompt();
		}
		return null;
	}
	private void deleteAccount() {
		logger.traceEntry("entry deleteAccount");
		System.out.printf("You are deleting account number %d.%n",myAccount.getAccountID());
		try {
			Optional<Boolean> success = accountService.deleteAccount(myAccount.getAccountID());
			if (success.isPresent()) {
				if (success.get()) {
					int indexToRemove = 0;
					Boolean foundIt = false;
					for (int i =0; i<myAccounts.size();i++) {
						if (myAccounts.get(i).getAccountID()==myAccount.getAccountID()) {
							foundIt = true;
							indexToRemove = i;
							break;
						}
					}
					if (foundIt) {
						myAccounts.remove(indexToRemove);
						myAccount = null;
					} else {
						System.out.println("The local list of accounts didn't have the account you are trying to delete, very weird.");
					}
					System.out.println("Going back to regular user options.");
					logger.traceExit("exit deleteAccount, calling promptRegularUserChoice");
					promptRegularUserChoice();
				} else {
					System.out.println("User may or may not have been deleted. Checking with new query.");
					Optional<List<Account>> optionalAccounts = accountService.getAllAccounts(SessionManager.sessionUser.getUser_id());
					if (optionalAccounts.isPresent()) {
						myAccounts = optionalAccounts.get();
						for (Account a: myAccounts) {
							if (a.getAccountID()==myAccount.getAccountID()) {
								System.out.println("System failed to delete the account, going back to account options.");
								logger.traceExit("exit deleteAccount, calling promptRegularUserChoice");
								viewIndividualAccount();
							}
						}
					} else {
						System.out.println("Database is having issues, the request to see if accounts are present failed. This may be because you have no accounts left or it may be that the server failed.");						
					}
				}
				System.out.println("Going back to user menu.");
				logger.traceExit("exit deleteAccount, calling promptRegularUserChoice");
				promptRegularUserChoice();
			} else {
				System.out.println("Connection not available, account not deleted, taking you back to individual account options.");
				logger.traceExit("exit deleteAccount, calling individualAccountOptionsPrompt");
				viewIndividualAccount();
			}
		} catch (AccountNotEmptyException e) {
			logger.catching(e);
			System.out.println("That account isn't empty, taking you back to individual account options.");
			logger.traceExit("exit deleteAccount, calling individualAccountOptionsPrompt");
			viewIndividualAccount();
		}
	}
	private void depositToAccount() {
		logger.traceEntry("entry depositToAccount");
		System.out.printf("You are making a withdrawal from account number %d.%n",myAccount.getAccountID());
		System.out.printf("The current balance is %d.%n",myAccount.getBalance());
		System.out.println("How much would you like to deposit into the account?");
		int depositAmount = ri.readPositiveInteger();
		Optional<Boolean> optionalSuccess = accountService.depositToAccount(myAccount.getAccountID(), depositAmount);
		if (optionalSuccess.isPresent()) {
			if (optionalSuccess.get()) {
				System.out.println("Deposit successful.");
				myAccount.setBalance(myAccount.getBalance() + depositAmount);
			} else {
				System.out.println("Something went wrong with the request, deposit may or may not have gone through. Going back to account options.");
			}
		} else {
			System.out.println("No connection is available currently. Going back to account options.");
		}
		logger.traceExit("exit depositToAccount, calling individualAccountOptionsPrompt");
		viewIndividualAccount();
	}
	private void withdrawFromAccount() {
		logger.traceEntry("entry withdrawFromAccount");
		System.out.printf("You are making a withdrawal from account number %d.%n",myAccount.getAccountID());
		System.out.printf("The current balance is %d.%n",myAccount.getBalance());
		System.out.println("How much would you like to withdraw from the account?");
		int withdrawalAmount = ri.readPositiveInteger();
		try {
			Optional<Boolean> success = accountService.withdrawFromAccount(myAccount.getAccountID(), withdrawalAmount);
			if (success.isPresent()) {
				if (success.get()) {
					logger.traceExit("exit withdrawFromAccount, calling individualAccountOptionsPrompt");
					System.out.println("Withdrawal successful.");
					myAccount.setBalance(myAccount.getBalance() - withdrawalAmount);
					viewIndividualAccount();
				} else {
					System.out.println("Something went wrong with the request, withdrawal may or may not have gone through. Going back to account options.");
					logger.traceExit("exit withdrawFromAccount, calling individualAccountOptionsPrompt");
					viewIndividualAccount();
				}
			} else {
				System.out.println("No connection is available currently. Going back to account options.");
				logger.traceExit("exit withdrawFromAccount, calling individualAccountOptionsPrompt");
				viewIndividualAccount();
			}
		} catch (OverdraftException e) {
			logger.catching(e);
			System.out.println("Withdrawal failed, you can try again.");
			logger.traceExit("exit withdrawFromAccount, calling withdrawFromAccount");
			withdrawFromAccount();
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
		System.out.println("You are making a new account.");
		System.out.println("How much would you like to deposit into the new account?");
		int sBalance = ri.readPositiveInteger();
		try {
			Optional<Account> myAcc = accountService.registerAccount(SessionManager.sessionUser.getUser_id(), sBalance);
			if (myAcc.isPresent()) {
				myAccount = myAcc.get();
				myAccounts.add(myAccount);
				System.out.println("The new account has been created successfully. Going to account view.");
				logger.traceExit("exit handleCreateAccountRequest, calling individualAccountOptionsPrompt");
				viewIndividualAccount();
			} else {
				System.out.println("Uh-oh, the new account wasn't made for some reason, server issue.");
				logger.info("Server couldn't add new account.");
			}
		} catch (UserIDDoesNotExistException e) {
			System.out.println("uh-oh, somehow this user doesn't exist in the database.");
			logger.catching(e);
		}
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
