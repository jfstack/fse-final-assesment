package com.jfstack.fse.projtracker.be.controller.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jfstack.fse.projtracker.be.Dummy;
import com.jfstack.fse.projtracker.be.controller.ProjectTaskController;
import com.jfstack.fse.projtracker.be.dto.ProjectForm;
import com.jfstack.fse.projtracker.be.dto.TaskForm;
import com.jfstack.fse.projtracker.be.entity.ParentTask;
import com.jfstack.fse.projtracker.be.entity.Project;
import com.jfstack.fse.projtracker.be.entity.Task;
import com.jfstack.fse.projtracker.be.entity.User;
import com.jfstack.fse.projtracker.be.service.ParentTaskService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
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
    
    @MockBean
    ParentTaskService parentTaskService;
    
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
    public void givenValidUser_whenAddProject_thenSaveProjectSuccessful() throws Exception {
    	
    	ProjectForm projectForm = Dummy.createProjectForm();
    	String jsonTask = toJson(projectForm);
    	
    	when(userService.getUserByEmployeeId(projectForm.getManagerId()))
                .thenReturn(Optional.of(Dummy.createUser()));
    	
    	
    	mockMvc.perform( post("/api/projects").content(jsonTask).contentType(MediaType.APPLICATION_JSON_VALUE) )
    				.andExpect(status().is(HttpStatus.CREATED.value()));
    	
    }

    @Test
    public void givenInvalidUser_whenAddProject_thenReturn404() throws Exception {

        ProjectForm projectForm = Dummy.createProjectForm();
        String jsonTask = toJson(projectForm);

        when(userService.getUserByEmployeeId(projectForm.getManagerId()))
                .thenReturn(Optional.empty());


        mockMvc.perform( post("/api/projects")
                            .content(jsonTask)
                            .contentType(MediaType.APPLICATION_JSON_VALUE) )
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));

    }

    @Test
    public void givenValidProject_whenUpdateProject_thenUpdateSuccessful() throws Exception {
        ProjectForm projectForm = Dummy.createProjectForm();
        String jsonTask = toJson(projectForm);
        Long projectId = 500L;
        Project project = Dummy.createBlankProject();
        project.setManager(Dummy.createUser());


        when(projectService.getProjectById(projectId))
                .thenReturn(Optional.of(project));


        mockMvc.perform( put("/api/projects/"+projectId)
                .content(jsonTask)
                .contentType( MediaType.APPLICATION_JSON_VALUE) )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.project", Matchers.is(project.getProject())));

    }


    @Test
    public void givenInValidProject_whenUpdateProject_thenReturn404() throws Exception {
        ProjectForm projectForm = Dummy.createProjectForm();
        String jsonTask = toJson(projectForm);
        Long projectId = 500L;


        when(projectService.getProjectById(projectId))
                .thenReturn(Optional.empty());


        mockMvc.perform( put("/api/projects/"+projectId)
                .content(jsonTask)
                .contentType( MediaType.APPLICATION_JSON_VALUE) )
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));

    }

    @Test
    public void givenValidProject_whenUpdateProjectWithManager_thenUpdateSuccessful() throws Exception {
        User user = Dummy.createUser();
        user.setEmployeeId(1111);
        ProjectForm projectForm = Dummy.createProjectForm();
        projectForm.setManagerId(user.getEmployeeId());
        String jsonTask = toJson(projectForm);
        Long projectId = 500L;
        Project project = Dummy.createBlankProject();
        project.setManager(Dummy.createUser());


        when(projectService.getProjectById(projectId))
                .thenReturn(Optional.of(project));
        when(userService.getUserByEmployeeId(projectForm.getManagerId()))
                .thenReturn(Optional.of(user));


        mockMvc.perform( put("/api/projects/"+projectId)
                .content(jsonTask)
                .contentType( MediaType.APPLICATION_JSON_VALUE) )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.manager", Matchers.notNullValue()))
                .andExpect(jsonPath("$.manager.employeeId", Matchers.is(user.getEmployeeId())))
                .andExpect(jsonPath("$.project", Matchers.is(projectForm.getName())));

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
    public void givenEmptyTaskList_whenGetAllTasks_thenReturn204() throws Exception {

        when(taskService.getAllTasksByProject(1L))
                .thenReturn(Optional.empty());

        mockMvc.perform( get("/api/projects/1/tasks") )
        		.andExpect(status().is(HttpStatus.NO_CONTENT.value()))
        		.andExpect(jsonPath("$", Matchers.hasSize(0)));

    }

    @Test
    public void givenValidTaskId_whenGetTaskById_thenReturnTask() throws Exception {

    	Task task = Dummy.createBlankTask();
    	task.setTaskId(1L);
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));

        mockMvc.perform( get("/api/projects/1/tasks/1") )
        		.andExpect(status().isOk())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
        		
    }

    @Test
    public void givenInvalidTaskId_whenGetTaskById_thenReturn404() throws Exception {

        when(taskService.getTaskById(1L)).thenReturn(Optional.empty());

        mockMvc.perform( get("/api/projects/1/tasks/1") )
        		.andExpect(status().is(HttpStatus.NOT_FOUND.value()));

    }

    @Test
    public void givenValidProject_whenGetAllParentTasks_thenReturnJsonArray() throws Exception {

        Long projectId = 200L;
        List<ParentTask> parentTaskList = Dummy.createParentTaskList();
        when(parentTaskService.getAllParentTasksByProject(projectId))
                .thenReturn(parentTaskList);

        mockMvc.perform( get("/api/projects/"+projectId+"/tasks/parents"))
                .andExpect( status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].parentTask", Matchers.is("parent task1")));
    }

    @Test
    public void givenInvalidProject_whenGetAllParentTasks_thenReturn204() throws Exception {

        Long projectId = 200L;
        when(parentTaskService.getAllParentTasksByProject(projectId))
                .thenReturn(new ArrayList<>());

        mockMvc.perform( get("/api/projects/"+projectId+"/tasks/parents"))
                .andExpect( status().is(HttpStatus.NO_CONTENT.value()))
                .andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    
    @Test
    public void givenValidProject_whenAddTaskForParent_thenSaveParentTaskSuccessfully() throws Exception {
        TaskForm taskForm = Dummy.createParentTaskForm();
        String jsonTask = toJson(taskForm);

        Long projectId = 300L;
        Project project = Dummy.createBlankProject();

        when(projectService.getProjectById(projectId))
                .thenReturn(Optional.of(project));
    	
    	mockMvc.perform( post("/api/projects/"+projectId+"/tasks")
                            .content(jsonTask)
                            .contentType(MediaType.APPLICATION_JSON_VALUE) )
    				.andExpect(status().is(HttpStatus.CREATED.value()));

    }

    @Test
    public void givenValidProject_whenAddTaskWithParent_thenSaveTaskSuccessfully() throws Exception {
        TaskForm taskForm = Dummy.createTaskForm();
        String jsonTask = toJson(taskForm);

        Long projectId = 300L;
        Project project = Dummy.createBlankProject();

        when(projectService.getProjectById(projectId))
                .thenReturn(Optional.of(project));
        when(userService.getUserByEmployeeId(taskForm.getUserId()))
                .thenReturn(Optional.of(Dummy.createUser()));
        when(parentTaskService.getParentTaskById(taskForm.getParentTaskId()))
                .thenReturn(Optional.of(Dummy.createBlankParentTask()));

    	mockMvc.perform( post("/api/projects/"+projectId+"/tasks")
                            .content(jsonTask)
                            .contentType(MediaType.APPLICATION_JSON_VALUE) )
    				.andExpect(status().is(HttpStatus.CREATED.value()));

    }

    @Test
    public void givenInvalidProject_whenAddTask_thenReturn404() throws Exception {

        Long projectId = 333L;
        when(projectService.getProjectById(projectId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/projects/"+projectId+"/tasks")
                            .content(toJson(Dummy.createTaskForm()))
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenValidProject_whenAddTaskWithoutOwner_thenSaveTaskSuccessfully() throws Exception {
        TaskForm taskForm = Dummy.createTaskForm();
        String jsonTask = toJson(taskForm);

        Long projectId = 300L;
        Project project = Dummy.createBlankProject();

        when(projectService.getProjectById(projectId))
                .thenReturn(Optional.of(project));
        when(userService.getUserByEmployeeId(taskForm.getUserId()))
                .thenReturn(Optional.empty());
        when(parentTaskService.getParentTaskById(taskForm.getParentTaskId()))
                .thenReturn(Optional.of(Dummy.createBlankParentTask()));

    	mockMvc.perform( post("/api/projects/"+projectId+"/tasks")
                            .content(jsonTask)
                            .contentType(MediaType.APPLICATION_JSON_VALUE) )
    				.andExpect(status().is(HttpStatus.NOT_FOUND.value()));

    }

    @Test
    public void givenValidTaskId_whenUpdateTask_thenUpdateTaskSuccessful() throws Exception {

        TaskForm taskForm = Dummy.createTaskForm();
        User user = Dummy.createUser();
        Task task = Dummy.createBlankTask();
        task.setOwner(user);
        ParentTask parentTask = Dummy.createBlankParentTask();
        parentTask.setParentId(taskForm.getParentTaskId());

        Long taskId = 123L;
        when(taskService.getTaskById(taskId)).thenReturn(Optional.of(task));
        when(userService.getUserByEmployeeId(taskForm.getUserId())).thenReturn(Optional.of(user));
        when(parentTaskService.getParentTaskById(taskForm.getParentTaskId())).thenReturn(Optional.of(parentTask));
    	
    	mockMvc.perform( put("/api/projects/1/tasks/"+taskId)
                            .content(toJson(taskForm)).contentType(MediaType.APPLICATION_JSON_VALUE) )
    				.andExpect(status().isOk());

    }

    @Test
    public void givenValidTaskIdAndInvalidUser_whenUpdateTask_thenUpdateTaskSuccessful() throws Exception {

        TaskForm taskForm = Dummy.createTaskForm();
        User user = Dummy.createUser();
        Task task = Dummy.createBlankTask();
        task.setOwner(user);
        ParentTask parentTask = Dummy.createBlankParentTask();
        parentTask.setParentId(taskForm.getParentTaskId());

        Long taskId = 123L;
        when(taskService.getTaskById(taskId)).thenReturn(Optional.of(task));
        when(userService.getUserByEmployeeId(taskForm.getUserId())).thenReturn(Optional.empty());
        when(parentTaskService.getParentTaskById(taskForm.getParentTaskId())).thenReturn(Optional.of(parentTask));

    	mockMvc.perform( put("/api/projects/1/tasks/"+taskId)
                            .content(toJson(taskForm)).contentType(MediaType.APPLICATION_JSON_VALUE) )
    				.andExpect(status().is(HttpStatus.NOT_FOUND.value()));

    }

    @Test
    public void givenInvalidTaskId_whenUpdateTask_thenReturn404() throws Exception {

        TaskForm taskForm = Dummy.createTaskForm();

        Long taskId = 123L;
        when(taskService.getTaskById(taskId)).thenReturn(Optional.empty());

    	mockMvc.perform( put("/api/projects/1/tasks/"+taskId)
                            .content(toJson(taskForm)).contentType(MediaType.APPLICATION_JSON_VALUE) )
    				.andExpect(status().is(HttpStatus.NOT_FOUND.value()));

    }


    @Test
    public void givenValidTaskId_whenDeleteTask_thenDeleteShouldSuccessful() throws Exception {

        Long taskId = 111L;
        when(taskService.getTaskById(taskId))
                .thenReturn(Optional.of(Dummy.createBlankTask()));


        mockMvc.perform( delete("/api/projects/1/tasks/"+taskId) )
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));
    }

    @Test
    public void givenInValidTaskId_whenDeleteTask_thenReturn404() throws Exception {

        Long taskId = 111L;
        when(taskService.getTaskById(taskId))
                .thenReturn(Optional.empty());


        mockMvc.perform( delete("/api/projects/1/tasks/"+taskId) )
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }


    private String toJson(Object value) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        ObjectWriter ow = om.writer();
        return ow.writeValueAsString(value);
    }


}
