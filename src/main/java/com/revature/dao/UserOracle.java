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

import com.revature.exceptions.IncorrectPasswordException;
import com.revature.exceptions.UsernameAlreadyExists;
import com.revature.exceptions.UserIDDoesNotExistException;
import com.revature.models.User;
import com.revature.utils.ConnectionUtil;

import oracle.jdbc.OracleTypes;
public class UserOracle implements UserDao {
	private static final Logger logger = LogManager.getLogger(UserOracle.class);
	private static UserOracle userOracle;
	final static UserDao userDao = UserOracle.getDao();
	
	private UserOracle() {
		
	}
	
	public static UserOracle getDao() {
		if (userOracle == null) {
			userOracle = new UserOracle();
		}
		return userOracle;
	}
	
	@Override
	public Optional<List<User>> getAllUsers() {
		Connection con = ConnectionUtil.getConnection();
		
		CallableStatement cstmt = null;
	    if (con == null) {
			return Optional.empty();
		}
	    Boolean getAllSuccess = false;
	    List<User> listOfUsers = new ArrayList<>();
		try {
			String SQL = "call getAllUsers(?)";
			cstmt = con.prepareCall(SQL);
			cstmt.registerOutParameter(1, OracleTypes.CURSOR);
			cstmt.execute();
			ResultSet rs = (ResultSet) cstmt.getObject(1);
			
			while (rs.next()) {
				listOfUsers.add(new User(rs.getInt("user_id"),rs.getString("user_name"),rs.getString("user_password"),rs.getInt("super_user")));
			}
			Collections.sort(listOfUsers);
			getAllSuccess = true;
		} catch (SQLException e) {
			logger.catching(e);
			getAllSuccess = false;
		} finally {
	    	try {
				cstmt.close();
			} catch (SQLException e) {
				logger.catching(e);
			}
	    }
		if (getAllSuccess) {
			return Optional.of(listOfUsers);		
		} else {
			return Optional.empty();	
		}
	}

	@Override
	public Optional<User> loginUser(String username,String password) throws IncorrectPasswordException {
		Connection con = ConnectionUtil.getConnection();
		if (con == null) {
			return Optional.empty();
		}
		CallableStatement cstmt = null;
		User myUser = null;
	    try {
	       String SQL = "call loginUser(?, ?, ?, ?, ?)";
	       cstmt = con.prepareCall(SQL);
	       cstmt.setString(1, username);
	       cstmt.setString(2, password);
	       cstmt.registerOutParameter(3, java.sql.Types.INTEGER);
	       cstmt.registerOutParameter(4, java.sql.Types.VARCHAR);
	       cstmt.registerOutParameter(5, java.sql.Types.INTEGER);
	       cstmt.execute();
	       Integer successfulLogin = cstmt.getInt(5);
	       if (successfulLogin==1) {
	    	   myUser = new User(cstmt.getInt(3),username,password,cstmt.getInt(4));
	       } else {
	    	   throw new IncorrectPasswordException("You entered the wrong password.");
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
	    return Optional.of(myUser);
	}
	
	@Override
	public Optional<User> registerUser(String username, String password) throws UsernameAlreadyExists {
		Connection con = ConnectionUtil.getConnection();
		if (con == null) {
			return Optional.empty();
		}
		CallableStatement cstmt = null;
		User myUser = null;
	    try {
	       String SQL = "call registerUser(?, ?, ?, ?)";
	       cstmt = con.prepareCall(SQL);
	       cstmt.setString(1, username);
	       cstmt.setString(2, password);
	       cstmt.registerOutParameter(3, java.sql.Types.INTEGER);
	       cstmt.registerOutParameter(4, java.sql.Types.INTEGER);
	       cstmt.execute();
	       Integer registerSuccess = cstmt.getInt(4);
	       if (registerSuccess==1) {
	    	   Integer userID = cstmt.getInt(3);
	    	   myUser = new User(userID,username,password,0);
	       } else {
	    	   throw new UsernameAlreadyExists("That username is already in the database.");
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
	    return Optional.of(myUser);
	}

	@Override
	public Optional<Boolean> deleteUser(Integer userID) {
		Connection con = ConnectionUtil.getConnection();
		if (con == null) {
			return Optional.empty();
		}
		CallableStatement cstmt = null;
		Boolean deleteSuccess = false;
	    try {
	       String SQL = "call deleteUser(?, ?)";
	       cstmt = con.prepareCall(SQL);
	       cstmt.setInt(1, userID);
	       cstmt.registerOutParameter(2, java.sql.Types.INTEGER);
	       cstmt.execute();
	       Integer requestSuccess = cstmt.getInt(2);
	       if (requestSuccess==1) {
	    	   deleteSuccess = true;
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
		return Optional.of(deleteSuccess);
	}

	@Override
	public Optional<Boolean> updateUser(Integer userID, String username, String password) throws UsernameAlreadyExists {
		Connection con = ConnectionUtil.getConnection();
		if (con == null) {
			return Optional.empty();
		}
		CallableStatement cstmt = null;
		Boolean updateSuccess = false;
	    try {
	       String SQL = "call updateUser(?, ?, ?, ?)";
	       cstmt = con.prepareCall(SQL);
	       cstmt.setInt(1, userID);
	       cstmt.setString(2, username);
	       cstmt.setString(3, password);
	       cstmt.registerOutParameter(4, java.sql.Types.INTEGER);
	       cstmt.execute();
	       Integer requestSuccess = cstmt.getInt(4);
	       if (requestSuccess==1) {
	    	   updateSuccess = true;
	       } else {
	    	   throw new UsernameAlreadyExists("You can't update a username to something that already exists.");
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
		return Optional.of(updateSuccess);
	}
	
	
}
