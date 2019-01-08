package com.revature.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.models.Transaction;
import com.revature.utils.ConnectionUtil;

import oracle.jdbc.OracleTypes;

public class TransactionOracle implements TransactionDao {
	private static final Logger logger = LogManager.getLogger(TransactionOracle.class);
	private static TransactionOracle transactionOracle;
	final static TransactionDao transactionDao = TransactionOracle.getDao();
	
	private TransactionOracle() {
		
	}
	
	public static TransactionOracle getDao() {
		if (transactionOracle == null) {
			transactionOracle = new TransactionOracle();
		}
		return transactionOracle;
	}
	
	@Override
	public Optional<List<Transaction>> getAllTransactionsByAccountID(Integer accountID) {
Connection con = ConnectionUtil.getConnection();
		
		CallableStatement cstmt = null;
	    if (con == null) {
			return Optional.empty();
		}
	    List<Transaction> listOfTransactions = new ArrayList<>();
	    Boolean getAllSuccess = false;
		try {
			String SQL = "call getAllTransactionsByAccountID(?, ?)";
			cstmt = con.prepareCall(SQL);
			cstmt.setInt(1, accountID);
			cstmt.registerOutParameter(2, OracleTypes.CURSOR);
			cstmt.execute();
			ResultSet rs = (ResultSet) cstmt.getObject(2);

			while (rs.next()) {
				listOfTransactions.add(new Transaction(rs.getInt("transaction_id"),rs.getInt("user_id"),rs.getInt("account_id"),rs.getInt("amount"),rs.getInt("withdrawal"),rs.getTimestamp("date_of_purchase")));
			}
			Collections.sort(listOfTransactions);
			getAllSuccess = true;
		} catch (SQLException e) {
			logger.catching(e);
		} finally {
			try {
				cstmt.close();
			} catch (SQLException e) {
				logger.catching(e);
			}
		}
		if (getAllSuccess) {
			return Optional.of(listOfTransactions);		
		} else {
			return Optional.empty();
		}
	}

}
