package com.revature.services;

import java.util.List;
import java.util.Optional;

import com.revature.dao.AccountDao;
import com.revature.dao.AccountOracle;
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
}
