package com.revature.dao;

import java.util.List;
import java.util.Optional;

import com.revature.exceptions.AccountNotEmptyException;
import com.revature.exceptions.OverdraftException;
import com.revature.exceptions.UserIDDoesNotExistException;
import com.revature.models.Account;

public interface AccountDao {
	Optional<List<Account>> getAllAccounts(Integer userID);
	Optional<Account> registerAccount(Integer userID, Integer balance) throws UserIDDoesNotExistException;
	Optional<Boolean> deleteAccount(Integer accountID) throws AccountNotEmptyException;
	Optional<Boolean> depositToAccount(Integer accountID, Integer amount);
	Optional<Boolean> withdrawFromAccount(Integer accountID, Integer amount) throws OverdraftException;
}
