package com.revature.services;

import java.util.List;
import java.util.Optional;

import com.revature.dao.AccountDao;
import com.revature.dao.AccountOracle;
import com.revature.exceptions.AccountNotEmptyException;
import com.revature.exceptions.OverdraftException;
import com.revature.exceptions.UserIDDoesNotExistException;
import com.revature.models.Account;
import com.revature.models.User;

public class AccountService {
	private static AccountService accountService;
	final static AccountDao accountDao = AccountOracle.getDao();
	
	private AccountService() {
		
	}
	
	public static AccountService getService() {
		if (accountService == null) {
			accountService = new AccountService();
		}
		return accountService;
	}

	public Optional<List<Account>> getAllAccounts(int userID) {
		return accountDao.getAllAccounts(userID);
	}

	public Optional<Account> registerAccount(Integer userID, Integer balance) throws UserIDDoesNotExistException {
		return accountDao.registerAccount(userID,balance);
	}
	
	public Optional<Boolean> deleteAccount(Integer accountID) throws AccountNotEmptyException{
		return accountDao.deleteAccount(accountID);
	}
	
	public Optional<Boolean> withdrawFromAccount(Integer accountID, Integer amount) throws OverdraftException {
		return accountDao.withdrawFromAccount(accountID,amount);
	}
	
	public Optional<Boolean> depositToAccount(Integer accountID, Integer amount) {
		return accountDao.depositToAccount(accountID,amount);
	}
}
