package com.jfstack.fse.projtracker.be.controller;

import com.jfstack.fse.projtracker.be.dto.ProjectForm;
import com.jfstack.fse.projtracker.be.dto.TaskForm;
import com.jfstack.fse.projtracker.be.entity.Project;
import com.jfstack.fse.projtracker.be.entity.Task;
import com.jfstack.fse.projtracker.be.entity.User;
import com.jfstack.fse.projtracker.be.service.ProjectService;
import com.jfstack.fse.projtracker.be.service.TaskService;
import com.jfstack.fse.projtracker.be.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/projects")
public class ProjectTaskController {

    private TaskService taskService;
    
    private ProjectService projectService;
    
    private UserService userService;

    @Autowired
    public ProjectTaskController(TaskService taskService, ProjectService projectService, UserService userService) {
		super();
		this.taskService = taskService;
		this.projectService = projectService;
		this.userService = userService;
	}
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Project>> getAllProjects() {
    	
    	Optional<List<Project>> allProjects = this.projectService.getAllProjects();
    	
    	if(!allProjects.isPresent()) {
    		return new ResponseEntity<>(new ArrayList(), HttpStatus.OK);
    	}
    	
    	return new ResponseEntity<>(allProjects.get(), HttpStatus.OK);
    }
    
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addProject(
    		@RequestBody ProjectForm projectForm) {
    	
    	Project project = new Project();
    	project.setProject(projectForm.getName());
    	project.setStartDate(projectForm.getStartDate());
    	project.setEndDate(projectForm.getEndDate());
    	project.setPriority(projectForm.getPriority());
    	
    	Optional<User> found = this.userService.getUserByEmployeeId(projectForm.getManagerId());
    	
    	if(!found.isPresent()) {
    		throw new RuntimeException();
    	}
    	
    	User user = found.get();
    	user.setProject(project);
    	
    	project.setManager(user);
    	project = this.projectService.addProject(project);
    	
    	
    	HttpHeaders headers = new HttpHeaders();
    	
    	return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
    

	@GetMapping(value = "/{projectId}/tasks",
			produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Task>> getAllTasks(@PathVariable("projectId") Long projectId) {
    	
    	Optional<List<Task>> allTasks = taskService.getAllTasksByProject(projectId);
    	
    	HttpHeaders headers = new HttpHeaders();
    	headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
    	
    	if(!allTasks.isPresent()) {
    		return new ResponseEntity<>(new ArrayList<>(), headers, HttpStatus.NO_CONTENT);
    	}
    	
    	return new ResponseEntity<>(allTasks.get(), headers, HttpStatus.OK);
    	
    }
	

	@GetMapping(value = "/{projectId}/tasks/{taskId}",
			produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> getTaskById(
    		@PathVariable("projectId") Long projectId,
    		@PathVariable("taskId") Long taskId ) {
    	
    	Optional<Task> found = taskService.getTaskById(taskId);
    	
    	HttpHeaders headers = new HttpHeaders();
    	headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
    	
    	if(!found.isPresent()) {
    		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    	}
    	
    	return new ResponseEntity<>(found.get(), headers, HttpStatus.OK);
    	
    }
    
    @PostMapping(value = "/{projectId}/tasks",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addTask(
    		@PathVariable("projectId") Long projectId,
            @RequestBody TaskForm taskForm/*,
            UriComponentsBuilder ucBuilder*/) {
    	
    	Optional<Project> found = this.projectService.getProjectById(projectId);
    	
    	if(!found.isPresent())
    		throw new RuntimeException();
    	
    	Project project = found.get();
    	
    	User owner = null;
    	if(taskForm.getUserId() != null) {
	    	Optional<User> userFound = this.userService.getUserByEmployeeId(taskForm.getUserId());
	    	if(!userFound.isPresent()) {
	    		throw new RuntimeException();
	    	}
	    	owner = userFound.get();
    	}
    	
    	Task task = new Task();
    	task.setTask(taskForm.getName());
    	task.setStartDate(taskForm.getStartDate());
    	task.setEndDate(taskForm.getEndDate());
    	task.setPriority(taskForm.getPriority());
    	task.setStatus("OPEN");
    	task.setOwner(owner);
    	
    	owner.setTask(task);
    	project.addTask(task);
    	task.setProject(project);
    	
    	this.projectService.updateProject(project);
    	
    	HttpHeaders headers = new HttpHeaders();
    	headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
    	/*headers.setLocation(
                ucBuilder.path("/api/tasks/{id}")
                        .buildAndExpand(task.getTaskId())
                        .toUri());*/
        
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    	
    }
    

    @PutMapping(
    		value = "/{projectId}/tasks/{taskId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> updateTask(
            @PathVariable("projectId") Long projectId,
            @PathVariable("taskId") Long taskId,
            @RequestBody TaskForm taskForm) {
    	
    	
    	Optional<Task> found = taskService.getTaskById(taskId);
    	
    	if (!found.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    	
    	Task task = found.get();
    	task.setTask(taskForm.getName());
    	task.setStartDate(taskForm.getStartDate());
    	task.setEndDate(taskForm.getEndDate());
    	task.setPriority(taskForm.getPriority());
    	
    	taskService.updateTask(task);
    	
    	return new ResponseEntity<>(HttpStatus.OK); 
    	
    }

    @DeleteMapping(value = "/{projectId}/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable("taskId") Long taskId) {
    	
    	Optional<Task> found = taskService.getTaskById(taskId);
    	
    	if (!found.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    	
    	taskService.deleteTask(taskId);
    	
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    	
    	
    }

}
