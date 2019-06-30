package com.jfstack.fse.projtracker.be.service;

import com.jfstack.fse.projtracker.be.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

	Optional<User> getUserByEmployeeId(Integer empId);

	Optional<User> getUserById(Long userId);

	Optional<List<User>> getAllUsers();
	
	User addUser(User user);
	
	void deleteUser(Long userId);
	
	void updateUser(User user);

}
