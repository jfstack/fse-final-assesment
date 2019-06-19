package com.jfstack.fse.projtracker.be.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jfstack.fse.projtracker.be.dto.UserDto;
import com.jfstack.fse.projtracker.be.entity.User;
import com.jfstack.fse.projtracker.be.repository.UserRepository;

@Service("userService")
public class UserServiceImpl implements UserService {
	
	private UserRepository repository;

	@Autowired
	public UserServiceImpl(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<User> getUserByEmployeeId(Integer empId) {
		
		if(empId == null) {
			return Optional.empty();
		}
		
		Optional<User> user = repository.findByEmployeeId(empId);
		
		return user;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Optional<List<User>> getAllUsers() {
		List<User> allusers = repository.findAll();
//		List<UserDto> list = allusers.stream().map(this::toDto).collect(Collectors.toList());
		return Optional.ofNullable(allusers);
	}
	
	@Override
	@Transactional
	public void addUser(User user) {
		if(user == null)
			throw new IllegalArgumentException("user data is null");
		
//		User user = fromDto(userDto);
		
		repository.save(user);
	}
	
	@Override
	@Transactional
	public void deleteUser(Long userId) {
		if(userId == null)
			throw new IllegalArgumentException("user id is null");
		
		repository.deleteById(userId);
	}
	
	@Override
	@Transactional
	public void updateUser(User user) {
		if(user == null)
			throw new IllegalArgumentException("user data is null");
		
		if(user.getUserId() == null)
			throw new IllegalArgumentException("user id is null");
		
		Optional<User> found = repository.findById(user.getUserId());
		
		if(found.isPresent()) {
			User userToUpdate = found.get();
			userToUpdate.setFirstName(user.getFirstName());
			userToUpdate.setLastName(user.getLastName());
//			user.setEmployeeId(dto.getEmployeeId());
			userToUpdate.setProjectId(user.getProjectId());
			userToUpdate.setTaskId(user.getTaskId());
			
			repository.save(userToUpdate);
		}
		
	}
	
	/*
	private Optional<UserDto> toDtoOptional(Optional<User> user) {
		if(user.isPresent()) {
			return Optional.of(toDto(user.get()));
		} else {
			return Optional.empty();
		}
	}
	
	private UserDto toDto(User user) {
		UserDto dto = new UserDto();
		dto.setUserId(user.getUserId());
		dto.setFirstName(user.getFirstName());
		dto.setLastName(user.getLastName());
		dto.setEmployeeId(user.getEmployeeId());
		dto.setProjectId(user.getProjectId());
		dto.setTaskId(user.getTaskId());
		return dto;
	}
	
	private User fromDto(UserDto dto) {
		User user = new User();
		user.setUserId(dto.getUserId());
		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());
		user.setEmployeeId(dto.getEmployeeId());
		user.setProjectId(dto.getProjectId());
		user.setTaskId(dto.getTaskId());
		return user;
	}
	*/

}
