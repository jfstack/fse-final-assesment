package com.jfstack.fse.projtracker.be.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jfstack.fse.projtracker.be.Dummy;
import com.jfstack.fse.projtracker.be.controller.ProjectTaskController;
import com.jfstack.fse.projtracker.be.dto.ProjectForm;
import com.jfstack.fse.projtracker.be.entity.Project;
import com.jfstack.fse.projtracker.be.entity.Task;
import com.jfstack.fse.projtracker.be.service.ProjectService;
import com.jfstack.fse.projtracker.be.service.TaskService;
import com.jfstack.fse.projtracker.be.service.UserService;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ProjectTaskController.class)
public class ProjectTaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TaskService taskService;
    
    @MockBean
    ProjectService projectService;
    
    @MockBean
    UserService userService;
    
    @Test
    public void givenProjectList_whenGetAllProjects_thenReturnJsonArray() throws Exception {

        List<Project> projectList = Dummy.createProjectList();
        when(projectService.getAllProjects()).thenReturn(Optional.ofNullable(projectList));

        mockMvc.perform( get("/api/projects") )
        		.andExpect(status().isOk())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        		.andExpect(jsonPath("$", Matchers.hasSize(2)))
        		.andExpect(jsonPath("$[0].project", Matchers.is("project 1")));
        		
    }
    
    @Test
    public void givenEmptyProjectList_whenGetAllProjects_thenReturnEmptyJsonArray() throws Exception {

        when(projectService.getAllProjects()).thenReturn(Optional.empty());

        mockMvc.perform( get("/api/projects") )
        		.andExpect(status().isOk())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        		.andExpect(jsonPath("$", Matchers.hasSize(0)));
        		
    }
    
  @Test
    public void whenAddProject_thenSaveProject() throws Exception {
    	
    	ProjectForm projectForm = Dummy.createProjectForm();
	  	ObjectMapper om = new ObjectMapper();
    	ObjectWriter ow = om.writer();
    	String jsonTask = ow.writeValueAsString(projectForm);
    	
    	when(userService.getUserByEmployeeId(208066)).thenReturn(Optional.of(Dummy.createUser()));
    	
    	
    	mockMvc.perform( post("/api/projects").content(jsonTask).contentType(MediaType.APPLICATION_JSON_VALUE) )
    				.andExpect(status().is(201));
    	
    }

    @Test
    public void givenTaskList_whenGetAllTasks_thenReturnJsonArray() throws Exception {

        List<Task> taskList = Dummy.createTaskList();
        when(taskService.getAllTasksByProject(1L)).thenReturn(Optional.ofNullable(taskList));

        mockMvc.perform( get("/api/projects/1/tasks") )
        		.andExpect(status().isOk())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        		.andExpect(jsonPath("$", Matchers.hasSize(2)))
        		.andExpect(jsonPath("$[0].task", Matchers.is("task 1")));
        		
    }
    
    @Test
    public void givenValidTaskId_whenGetTaskById_thenReturnTask() throws Exception {

    	Task task = Dummy.createBlankTask();
    	task.setTaskId(1L);
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));

        mockMvc.perform( get("/api/projects/1/tasks/1") )
        		.andExpect(status().is(200))
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
        		
    }
    
    
    //@Test
    public void whenAddTask_thenSaveTask() throws Exception {
    	
    	Task task = Dummy.createBlankTask();
    	ObjectMapper om = new ObjectMapper();
    	ObjectWriter ow = om.writer();
    	String jsonTask = ow.writeValueAsString(task);
    	
    	
    	mockMvc.perform( post("/api/tasks").content(jsonTask).contentType(MediaType.APPLICATION_JSON_VALUE) )
    				.andExpect(status().is(201))
    				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    	
    }
    
    //@Test
    public void givenValidTaskIdAndContent_whenUpdateTask_thenUpdateTask() throws Exception {
    	
    	Task task = Dummy.createBlankTask();
    	ObjectMapper om = new ObjectMapper();
    	ObjectWriter ow = om.writer();
    	String jsonTask = ow.writeValueAsString(task);
    	
    	
    	/*mockMvc.perform( put("/api/tasks").content(jsonTask).contentType(MediaType.APPLICATION_JSON_VALUE) )
    				.andExpect(status().is(201))
    				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));*/
    	
    }
    
}
