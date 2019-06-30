package com.jfstack.fse.projtracker.be.controller.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jfstack.fse.projtracker.be.Dummy;
import com.jfstack.fse.projtracker.be.controller.UserController;
import com.jfstack.fse.projtracker.be.dto.UserForm;
import com.jfstack.fse.projtracker.be.entity.User;
import com.jfstack.fse.projtracker.be.service.UserService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	UserService userService;
	
	@Test
    public void whenAddUserWithExistingEmployeeId_thenReturn409Status() throws Exception {
    	
    	UserForm userForm = Dummy.createUserForm();
    	String jsonTask = toJson(userForm);
    	
    	when(userService.getUserByEmployeeId(userForm.getEmployeeId()))
				.thenReturn(Optional.of(Dummy.createUser()));
    	
    	
    	mockMvc.perform( post("/api/users")
							.content(jsonTask)
							.contentType(MediaType.APPLICATION_JSON_VALUE) )
    				.andExpect(status().is(409));
    	
    }
	
	@Test
    public void whenAddUserWithNonExistingEmployeeId_thenReturn201Status() throws Exception {
    	
    	UserForm userForm = Dummy.createUserForm();
    	String jsonTask = toJson(userForm);
    	
    	when(userService.getUserByEmployeeId(userForm.getEmployeeId()))
				.thenReturn(Optional.empty());
    	
    	
    	mockMvc.perform( post("/api/users")
							.content(jsonTask)
							.contentType(MediaType.APPLICATION_JSON_VALUE) )
    				.andExpect(status().is(201));
    	
    }

    @Test
    public void givenListOfUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
		List<User> userList = Dummy.createUserList();

		when(userService.getAllUsers()).thenReturn(Optional.of(userList));

		mockMvc.perform( get("/api/users") )
				.andExpect( status().isOk() )
				.andExpect( content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect( jsonPath("$", Matchers.hasSize(2)))
				.andExpect( jsonPath("$[0].employeeId", Matchers.is(userList.get(0).getEmployeeId())));
	}

	@Test
	public void givenNoUsers_whenGetUsers_thenReturnNoContent() throws Exception {

		when(userService.getAllUsers()).thenReturn(Optional.empty());

		mockMvc.perform( get("/api/users") )
				.andExpect( status().is(HttpStatus.NO_CONTENT.value()) );
	}

	@Test
	public void givenExistingUser_whenUpdateUser_thenUpdateShouldSuccessful() throws Exception {
		UserForm userForm = Dummy.createUserForm();
		userForm.setLastName("Roy");
		String jsonTask = toJson(userForm);

		Long userId = 322L;
		when(userService.getUserById(userId))
				.thenReturn(Optional.of(Dummy.createUser()));


		mockMvc.perform( put("/api/users/"+userId)
							.content(jsonTask)
							.contentType(MediaType.APPLICATION_JSON_VALUE) )
				.andExpect(status().isOk())
				.andExpect( content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect( jsonPath("$.employeeId", Matchers.is(userForm.getEmployeeId())))
				.andExpect( jsonPath("$.lastName", Matchers.is(userForm.getLastName())));
	}

	@Test
	public void givenNonExistingUser_whenUpdateUser_thenReturnNotFound() throws Exception {
		UserForm userForm = Dummy.createUserForm();
		String jsonTask = toJson(userForm);

		when(userService.getUserByEmployeeId(userForm.getEmployeeId()))
				.thenReturn(Optional.empty());


		mockMvc.perform( put("/api/users/"+userForm.getEmployeeId())
				.content(jsonTask)
				.contentType(MediaType.APPLICATION_JSON_VALUE) )
				.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
	}


	@Test
	public void givenExistingUser_whenDeleteUser_thenDeleteShouldSuccessful() throws Exception {

		when(userService.getUserByEmployeeId(208066))
				.thenReturn(Optional.of(Dummy.createUser()));


		mockMvc.perform( delete("/api/users/"+208066) )
				.andExpect(status().is(HttpStatus.NO_CONTENT.value()));
	}

	@Test
	public void givenNonExistingUser_whenDeleteUser_thenReturnNotFound() throws Exception {

		when(userService.getUserByEmployeeId(208066)).thenReturn(Optional.empty());


		mockMvc.perform( delete("/api/users/"+208066))
				.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
	}


    private String toJson(Object value) throws JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		ObjectWriter ow = om.writer();
		return ow.writeValueAsString(value);
	}

}
