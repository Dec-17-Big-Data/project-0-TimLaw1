package com.revature.services;

import java.util.List;
import java.util.Optional;

import com.revature.dao.UserDao;
import com.revature.dao.UserOracle;
import com.revature.exceptions.IncorrectPasswordException;
import com.revature.exceptions.UsernameAlreadyExists;
import com.revature.exceptions.UserIDDoesNotExistException;
import com.revature.models.User;

public class UserService {

	private static UserService userService;
	final static UserDao userDao = UserOracle.getDao();
	
	private UserService() {
		
	}
	
	public static UserService getService() {
		if (userService == null) {
			userService = new UserService();
		}
		return userService;
	}
	
	public Optional<List<User>> getAllUsers() {
		return userDao.getAllUsers();
	}
	
	public Optional<User> loginUser(String username, String password) throws IncorrectPasswordException {
		return userDao.loginUser(username,password);
	}
	
	public Optional<User> registerUser(String username, String password) throws UsernameAlreadyExists {
		return userDao.registerUser(username,password);
	}
	
	public Optional<Boolean> deleteUser(Integer userID) {
		return userDao.deleteUser(userID);
	}
	
	public Optional<Boolean> updateUser(Integer userID, String username, String password) throws UsernameAlreadyExists {
		return userDao.updateUser(userID, username, password);
	}
}
