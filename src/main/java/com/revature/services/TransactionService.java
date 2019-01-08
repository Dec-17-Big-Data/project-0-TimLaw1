package com.revature.services;

import java.util.List;
import java.util.Optional;

import com.revature.dao.TransactionDao;
import com.revature.dao.TransactionOracle;
import com.revature.models.Transaction;

public class TransactionService {
	private static TransactionService transactionService;
	final static TransactionDao transactionDao = TransactionOracle.getDao();
	
	private TransactionService() {
		
	}
	
	public static TransactionService getService() {
		if (transactionService == null) {
			transactionService = new TransactionService();
		}
		return transactionService;
	}
	
	public Optional<List<Transaction>> getAllTransactionsByAccountID(Integer accountID) {
		return transactionDao.getAllTransactionsByAccountID(accountID);
	}
}
