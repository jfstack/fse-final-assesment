package com.jfstack.fse.projtracker.be.service;

import java.util.List;
import java.util.Optional;

import com.jfstack.fse.projtracker.be.dto.UserDto;

public interface UserService {

	Optional<UserDto> getUserByEmployeeId(Integer empId);
	
	Optional<List<UserDto>> getAllUsers();
	
	void addUser(UserDto userDto);
	
	void deleteUser(Long userId);
	
	void updateUser(UserDto userDto);

}
