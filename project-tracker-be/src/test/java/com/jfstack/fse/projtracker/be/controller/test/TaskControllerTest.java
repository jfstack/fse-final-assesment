package com.jfstack.fse.projtracker.be.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jfstack.fse.projtracker.be.Dummy;
import com.jfstack.fse.projtracker.be.controller.ProjectTaskController;
import com.jfstack.fse.projtracker.be.entity.Task;
import com.jfstack.fse.projtracker.be.service.TaskService;

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
public class TaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TaskService taskService;

    @Test
    public void givenTaskList_whenGetAllTasks_thenReturnJsonArray() throws Exception {

        List<Task> taskList = Dummy.createTaskList();
        when(taskService.getAllTasks()).thenReturn(Optional.ofNullable(taskList));

        mockMvc.perform( get("/api/tasks") )
        		.andExpect(status().isOk())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        		.andExpect(jsonPath("$", Matchers.hasSize(2)))
        		.andExpect(jsonPath("$[0].task", Matchers.is("task 1")));
        		
    }
    
    @Test
    public void givenEmptyTasks_whenGetAllTasks_thenReturnNoContent() throws Exception {

//        List<Task> taskList = Dummy.createTaskList();
        when(taskService.getAllTasks()).thenReturn(Optional.empty());

        mockMvc.perform( get("/api/tasks") )
        		.andExpect(status().is(204))
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
//        		.andExpect(jsonPath("$", Matchers.hasSize(0)));
//        		.andExpect(jsonPath("$[0].task", Matchers.is("task 1")));
        		
    }
    
    
    @Test
    public void whenAddTask_thenSaveTask() throws Exception {
    	
    	Task task = Dummy.createBlankTask();
    	ObjectMapper om = new ObjectMapper();
    	ObjectWriter ow = om.writer();
    	String jsonTask = ow.writeValueAsString(task);
    	
    	
    	mockMvc.perform( post("/api/tasks").content(jsonTask).contentType(MediaType.APPLICATION_JSON_VALUE) )
    				.andExpect(status().is(201))
    				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    	
    }
    
    @Test
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
