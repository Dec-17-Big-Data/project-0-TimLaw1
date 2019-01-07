package com.revature.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.exceptions.UserIDDoesNotExistException;
import com.revature.models.Account;
import com.revature.models.Champion;
import com.revature.models.User;
import com.revature.modules.RegularUser;
import com.revature.modules.SuperUser;
import com.revature.services.AccountService;
import com.revature.services.ChampionService;
import com.revature.services.UserService;


public class JDBCBank {
	private static final Logger logger = LogManager.getLogger(JDBCBank.class);
	public static final Scanner inStream = new Scanner(System.in);
	public static void main(String[] args) {
		logger.traceEntry("entry main");
//		testUserService();
//		testSuperUserView();
//		testRegularUserView();
//		testGetAllAccounts();
		SessionManager sm = SessionManager.getSessionManager();
		sm.start();
		logger.traceExit("exit main");
	}
	public static void testGetAllAccounts () {
		List<Account> myAccounts = new ArrayList<>();
		AccountService accountService = AccountService.getService();
		Optional<List<Account>> optionalAccounts = accountService.getAllAccounts(61);
		if (optionalAccounts.isPresent()) {
			myAccounts = optionalAccounts.get();
			for (Account a: myAccounts) {
				System.out.println(a.getAccountID());
				System.out.println(a.getBalance());
				System.out.println(a.getUserID());
			}
		} else {
			System.out.println("Server failed to find any accounts, try to create an account, and if that doesn't work then it is a server issue.");
			logger.info("Server failed to return any accounts.");
		}
	}
	public static void testUserService() {
		AccountService myAS = AccountService.getService();
		Optional<Account> optional;
		try {
			optional = myAS.registerAccount(25, 1000);
			if (optional.isPresent()) {
				Account b = optional.get();
				System.out.println(b.getAccountID());
				System.out.println(b.getBalance());
			}
		} catch (UserIDDoesNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void testRegularUserView() {
		RegularUser myRU = new RegularUser();
		myRU.initiateRegularUserSession();
	}
	public static void testSuperUserView() {
		SuperUser mySU = new SuperUser();
		mySU.initiateSuperUserSession();
	}
	public static void testGetAllUsers() {
		UserService myUS = UserService.getService();
		Optional<List<User>> optionalUsers = myUS.getAllUsers();
		if (optionalUsers.isPresent()) {
			List<User> myUsers = optionalUsers.get();
			for (User u: myUsers) {
				System.out.println(u.getUser_id());
				System.out.println(u.getUser_name());
				System.out.println(u.getUser_password());
				System.out.println(u.getAdmin());
			}
		}
	}
	public static void testSQLConnection() throws SQLException {
		logger.traceEntry();
		Connection con = ConnectionUtil.getConnection();
		String sql = "SELECT * FROM Champions";
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			System.out.println(rs.getString("CHAMPION_NAME"));
		}	
		ChampionService cs = ChampionService.getService();
		Optional<List<Champion>> cd = cs.getChampions();
		if (cd.isPresent()) {
			List<Champion> list = cd.get();
			for (Champion c: list) {
				System.out.println(c.getChampion_name());
			}
		}
		logger.traceExit();
	}
	public static void testStoredProcedures() throws SQLException {
		Connection conn = ConnectionUtil.getConnection();
	    CallableStatement cstmt = null;
	    try {
	       String SQL = "call getUserByName(?, ?, ?, ?)";
	       cstmt = conn.prepareCall(SQL);
	       cstmt.setString(1, "root");
	       cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
	       cstmt.registerOutParameter(3, java.sql.Types.VARCHAR);
	       cstmt.registerOutParameter(4, java.sql.Types.INTEGER);
	       cstmt.execute();
	       String userID = cstmt.getString(2);
	       String userPW = cstmt.getString(3);
	       Integer userIsSuper = cstmt.getInt(4);
	       System.out.println(userID);
	       System.out.println(userPW);
	       System.out.println(userIsSuper);
	    }
	    catch (SQLException e) {
	       logger.catching(e);
	    }
	    finally {
	    	cstmt.close();
	    }
	}
}
