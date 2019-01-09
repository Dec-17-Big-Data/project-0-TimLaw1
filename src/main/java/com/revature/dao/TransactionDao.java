package com.revature.dao;

import java.util.List;
import java.util.Optional;

import com.revature.models.Transaction;

public interface TransactionDao {
	Optional<List<Transaction>> getAllTransactionsByAccountID(Integer accountID);
}
