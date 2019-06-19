package com.jfstack.fse.projtracker.be.service.test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.jfstack.fse.projtracker.be.Dummy;
import com.jfstack.fse.projtracker.be.dto.UserDto;
import com.jfstack.fse.projtracker.be.entity.User;
import com.jfstack.fse.projtracker.be.repository.UserRepository;
import com.jfstack.fse.projtracker.be.service.UserService;
import com.jfstack.fse.projtracker.be.service.UserServiceImpl;

@RunWith(SpringRunner.class)public class UserServiceTest {
	
	@TestConfiguration
	static class UserServiceTestConfig {
		@Autowired UserRepository repository;
		
		@Bean
		public UserService userService() {
			return new UserServiceImpl(repository);
		}
	}
	
	@Autowired UserService userService;
	
	@MockBean UserRepository userRepository;
	
	@Before
	public void setup() {
		User chandan = Dummy.createUser();
		when(userRepository.findByEmployeeId(208066)).thenReturn(Optional.of(chandan));
		
		List<User> userList = Dummy.createUserList();
		when(userRepository.findAll()).thenReturn(userList);
		
		when(userRepository.findById(100L)).thenReturn(Optional.of(chandan));
	}
	
	@Test
	public void whenValidEmpId_thenUserShouldBeFound() {
		
		Optional<UserDto> actual = userService.getUserByEmployeeId(208066);
		
		assertThat(actual).isNotEmpty();
		assertThat(actual.isPresent()).isTrue();
		assertThat(actual.get()).isInstanceOf(UserDto.class);
		assertThat(actual.get().getFirstName()).isEqualTo("Chandan");
		
	}
	
	@Test
	public void whenGetAllUsers_thenAllUsersShouldBeFound() {
		
		Optional<List<UserDto>> actual = userService.getAllUsers();
		
		assertThat(actual).isNotEmpty();
		assertThat(actual.isPresent()).isTrue();
		assertThat(actual.get()).isInstanceOf(List.class);
		assertThat(actual.get().size()).isEqualTo(2);
		
	}
	
	@Test
	public void whenAddNewUser_thenUserShouldBeSaved() {
		
		User entity = Dummy.createUser();
		UserDto dto = Dummy.wrapUserInDto(entity);
		
		userService.addUser(dto);
		
		ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
	    verify( userRepository ).save( argument.capture() );
	    assertThat( entity.getEmployeeId() ).isEqualTo( argument.getValue().getEmployeeId() );
	    assertThat( entity.getFirstName() ).isEqualTo( argument.getValue().getFirstName() );
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void whenAddNewUserWithNull_thenException() {
		
		userService.addUser(null);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void whenUpdateUserWithNull_thenException() {
		
		userService.updateUser(null);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void whenUpdateUserWithNullId_thenException() {
		
		UserDto dto = Dummy.wrapUserInDto(Dummy.createUser());
		userService.updateUser(dto);
		
	}
	
	@Test
	public void whenUpdateExistingUser_thenUserShouldBeSaved() {
		
		User entity = Dummy.createUser();
		UserDto dto = Dummy.wrapUserInDto(entity);
		dto.setUserId(100L);
		dto.setFirstName("Saheb");
		dto.setLastName("Dhole");
		
		userService.updateUser(dto);
		
		ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
	    verify( userRepository ).save( argument.capture() );
	    assertThat( argument.getValue().getEmployeeId() ).isEqualTo( dto.getEmployeeId() );
	    assertThat( argument.getValue().getFirstName() ).isEqualTo( dto.getFirstName() );
	    assertThat( argument.getValue().getLastName() ).isEqualTo( dto.getLastName() );
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void whenDeleteUserWithNull_thenException() {
		
		userService.deleteUser(null);
		
	}
	
	@Test
	public void whenDeleteUserWithValidId_thenUserShouldBeDeleted() {
		
		userService.deleteUser(101L);
		
		ArgumentCaptor<Long> argument = ArgumentCaptor.forClass(Long.class);
	    verify( userRepository ).deleteById( argument.capture() );
	    assertThat( argument.getValue().longValue() ).isEqualTo( 101 );
		
	}
}
