package com.revature.dao;

import java.util.List;
import java.util.Optional;

import com.revature.exceptions.UserIDDoesNotExistException;
import com.revature.models.Account;

public interface AccountDao {
	Optional<List<Account>> getAllAccounts(Integer userID);
	Optional<Account> registerAccount(Integer userID, Integer balance) throws UserIDDoesNotExistException;
	Optional<Boolean> deleteAccount(Integer accountID);
	Optional<Boolean> depositAccount(Integer accountID);
	Optional<Boolean> withdrawAccount(Integer accountID);
}
