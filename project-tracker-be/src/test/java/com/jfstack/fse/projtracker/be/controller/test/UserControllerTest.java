package com.jfstack.fse.projtracker.be.controller.test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jfstack.fse.projtracker.be.Dummy;
import com.jfstack.fse.projtracker.be.controller.UserController;
import com.jfstack.fse.projtracker.be.dto.ProjectForm;
import com.jfstack.fse.projtracker.be.dto.UserForm;
import com.jfstack.fse.projtracker.be.service.UserService;

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
	  	ObjectMapper om = new ObjectMapper();
    	ObjectWriter ow = om.writer();
    	String jsonTask = ow.writeValueAsString(userForm);
    	
    	when(userService.getUserByEmployeeId(208066)).thenReturn(Optional.of(Dummy.createUser()));
    	
    	
    	mockMvc.perform( post("/api/users").content(jsonTask).contentType(MediaType.APPLICATION_JSON_VALUE) )
    				.andExpect(status().is(409));
    	
    }
	
	@Test
    public void whenAddUserWithNonExistingEmployeeId_thenReturn201Status() throws Exception {
    	
    	UserForm userForm = Dummy.createUserForm();
	  	ObjectMapper om = new ObjectMapper();
    	ObjectWriter ow = om.writer();
    	String jsonTask = ow.writeValueAsString(userForm);
    	
    	when(userService.getUserByEmployeeId(208066)).thenReturn(Optional.empty());
    	
    	
    	mockMvc.perform( post("/api/users").content(jsonTask).contentType(MediaType.APPLICATION_JSON_VALUE) )
    				.andExpect(status().is(201));
    	
    }

}
