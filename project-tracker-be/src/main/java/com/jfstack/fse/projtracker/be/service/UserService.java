package com.jfstack.fse.projtracker.be.service;

import java.util.List;
import java.util.Optional;

import com.jfstack.fse.projtracker.be.dto.UserDto;
import com.jfstack.fse.projtracker.be.entity.User;

public interface UserService {

	Optional<User> getUserByEmployeeId(Integer empId);
	
	Optional<List<User>> getAllUsers();
	
	User addUser(User user);
	
	void deleteUser(Long userId);
	
	void updateUser(User user);

}
