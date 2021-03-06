package com.revature.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.exceptions.UserIDDoesNotExistException;
import com.revature.exceptions.UsernameAlreadyExists;
import com.revature.models.Account;
import com.revature.models.User;
import com.revature.utils.ConnectionUtil;

import oracle.jdbc.OracleTypes;

public class AccountOracle implements AccountDao{
	private static final Logger logger = LogManager.getLogger(ChampionOracle.class);
	private static AccountOracle accountOracle;
	final static AccountDao accountDao = AccountOracle.getDao();
	
	private AccountOracle() {
		
	}
	
	public static AccountOracle getDao() {
		if (accountOracle == null) {
			accountOracle = new AccountOracle();
		}
		return accountOracle;
	}
	
	@Override
	public Optional<List<Account>> getAllAccounts(Integer userID) {
		Connection con = ConnectionUtil.getConnection();
		
		CallableStatement cstmt = null;
	    if (con == null) {
			return Optional.empty();
		}
	    List<Account> listOfAccounts = new ArrayList<>();
	    Boolean getAllSuccess = false;
		try {
			String SQL = "call getAllUsers(?, ?)";
			cstmt = con.prepareCall(SQL);
			cstmt.setInt(1, userID);
			cstmt.registerOutParameter(2, OracleTypes.CURSOR);
			cstmt.execute();
			ResultSet rs = (ResultSet) cstmt.getObject(1);

			while (rs.next()) {
				listOfAccounts.add(new Account(rs.getInt("account_id"),userID,rs.getInt("balance")));
			}
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
			return Optional.of(listOfAccounts);		
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Optional<Account> registerAccount(Integer userID, Integer balance) throws UserIDDoesNotExistException {
		Connection con = ConnectionUtil.getConnection();
		if (con == null) {
			return Optional.empty();
		}
		CallableStatement cstmt = null;
		Account myAccount = null;
	    try {
	       String SQL = "call registerAccount(?, ?, ?, ?)";
	       cstmt = con.prepareCall(SQL);
	       cstmt.setInt(1, userID);
	       cstmt.setInt(2, balance);
	       cstmt.registerOutParameter(3, java.sql.Types.INTEGER);
	       cstmt.registerOutParameter(4, java.sql.Types.INTEGER);
	       cstmt.execute();
	       Integer registerSuccess = cstmt.getInt(4);
	       if (registerSuccess==1) {
	    	   Integer accountID = cstmt.getInt(3);
	    	   myAccount = new Account(accountID,userID,balance);
	       } else {
	    	   throw new UserIDDoesNotExistException("That userID does not exist in the database.");
	       }
	       
	    } catch (SQLException e) {
	       logger.catching(e);
	    } finally {
	    	try {
				cstmt.close();
			} catch (SQLException e) {
				logger.catching(e);
			}
	    }
	    return Optional.of(myAccount);
	}

	@Override
	public Optional<Boolean> deleteAccount(Integer accountID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Boolean> depositAccount(Integer accountID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Boolean> withdrawAccount(Integer accountID) {
		// TODO Auto-generated method stub
		return null;
	}

}
