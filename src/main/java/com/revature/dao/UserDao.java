package com.revature.dao;

import java.util.List;
import java.util.Optional;

import com.revature.exceptions.IncorrectPasswordException;
import com.revature.exceptions.UsernameAlreadyExists;
import com.revature.exceptions.UserIDDoesNotExistException;
import com.revature.models.User;

public interface UserDao {
	Optional<List<User>> getAllUsers();
	Optional<User> loginUser(String name,String password) throws IncorrectPasswordException;
	Optional<User> registerUser(String username, String password) throws UsernameAlreadyExists;
	Optional<Boolean> deleteUser(Integer userID);
	Optional<Boolean> updateUser(Integer userID, String username, String password) throws UsernameAlreadyExists;
}