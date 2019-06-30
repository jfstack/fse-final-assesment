package com.jfstack.fse.projtracker.be.controller;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/projects")
public class ProjectTaskController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

    private TaskService taskService;
    
    private ProjectService projectService;
    
    private UserService userService;
    
    private ParentTaskService parentTaskService;

    @Autowired
    public ProjectTaskController(TaskService taskService, 
    		ProjectService projectService, 
    		UserService userService,
    		ParentTaskService parentTaskService) {
		super();
		this.taskService = taskService;
		this.projectService = projectService;
		this.userService = userService;
		this.parentTaskService = parentTaskService;
	}
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Project>> getAllProjects() {
    	logger.info("getAllProjects(ENTER)");
    	Optional<List<Project>> allProjects = this.projectService.getAllProjects();

    	if(!allProjects.isPresent()) {
    		return new ResponseEntity<>(new ArrayList(), HttpStatus.OK);
    	}

    	logger.info("getAllProjects(EXIT)");
    	return new ResponseEntity<>(allProjects.get(), HttpStatus.OK);
    }
    
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> addProject(
    		@RequestBody ProjectForm projectForm) {
    	logger.info("addProject(ENTER)");

    	Project project = new Project();
    	project.setProject(projectForm.getName());
    	project.setStartDate(projectForm.getStartDate());
    	project.setEndDate(projectForm.getEndDate());
    	project.setPriority(projectForm.getPriority());
    	project.setStatus("OPEN");

    	Optional<User> found = this.userService.getUserByEmployeeId(projectForm.getManagerId());

    	if(!found.isPresent()) {
    		throw new RuntimeException("User not found");
    	}

    	User user = found.get();
    	user.setProject(project);

    	project.setManager(user);
    	project = this.projectService.addProject(project);


    	HttpHeaders headers = new HttpHeaders();

    	logger.info("addProject(EXIT)");
    	return new ResponseEntity<>(project, HttpStatus.CREATED);
    }
    
    
    @PutMapping(value = "/{projectId}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> updateProject(
    		@PathVariable("projectId") Long projectId, @RequestBody ProjectForm projectForm) {
    	logger.info("updateProject(ENTER)");

    	logger.debug("Project data: ", projectForm);

    	Optional<Project> found = this.projectService.getProjectById(projectId);
    	if(!found.isPresent()) {
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}


    	Project project = found.get();

    	if(project.getManager().getEmployeeId() != projectForm.getManagerId()) {
    		Optional<User> userFound = this.userService.getUserByEmployeeId(projectForm.getManagerId());
    		if(userFound.isPresent()) {
    			project.setManager(userFound.get());
    		}
    	}

    	project.setProject(projectForm.getName());
    	project.setStartDate(projectForm.getStartDate());
    	project.setEndDate(projectForm.getEndDate());
    	project.setPriority(projectForm.getPriority());
    	project.setStatus(projectForm.getStatus());

    	this.projectService.updateProject(project);

    	logger.info("updateProject(EXIT)");
    	return new ResponseEntity<>(project, HttpStatus.OK);

    }

//    @PatchMapping(value = "/{projectId}")
//    public ResponseEntity<Void> suspendProject(@PathVariable("projectId") Long projectId) {
//
//    	Optional<Project> found = this.projectService.getProjectById(projectId);
//		if(!found.isPresent()) {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//
//		//introduce a new field in project called status
//		return new ResponseEntity<>(HttpStatus.OK);
//	}
    

	@GetMapping(value = "/{projectId}/tasks",
			produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Task>> getAllTasks(@PathVariable("projectId") Long projectId) {
    	logger.info("getAllTasks(ENTER)");

    	Optional<List<Task>> allTasks = taskService.getAllTasksByProject(projectId);

    	HttpHeaders headers = new HttpHeaders();
    	headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

    	if(!allTasks.isPresent()) {
    		return new ResponseEntity<>(new ArrayList<>(), headers, HttpStatus.NO_CONTENT);
    	}

    	logger.info("getAllTasks(EXIT)");
    	return new ResponseEntity<>(allTasks.get(), headers, HttpStatus.OK);
    	
    }
	

	@GetMapping(value = "/{projectId}/tasks/parents",
			produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ParentTask>> getAllParentTasks(
    		@PathVariable("projectId") Long projectId) {
		logger.info("getAllParentTasks(ENTER)");

		List<ParentTask> allParentTasks =
				parentTaskService.getAllParentTasksByProject(projectId);

		HttpHeaders headers = new HttpHeaders();
    	headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

    	if(allParentTasks.isEmpty()) {
    		return new ResponseEntity<>(new ArrayList<>(), headers, HttpStatus.NO_CONTENT);
    	}

		logger.info("getAllParentTasks(EXIT)");
    	return new ResponseEntity<>(allParentTasks, headers, HttpStatus.OK);

    }


	@GetMapping(value = "/{projectId}/tasks/{taskId}",
			produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> getTaskById(
    		@PathVariable("projectId") Long projectId,
    		@PathVariable("taskId") Long taskId ) {
    	logger.info("getTaskById(ENTER)");

    	Optional<Task> found = taskService.getTaskById(taskId);

    	HttpHeaders headers = new HttpHeaders();
    	headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

    	if(!found.isPresent()) {
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}

    	logger.info("getTaskById(EXIT)");
    	return new ResponseEntity<>(found.get(), headers, HttpStatus.OK);

    }
    
    @PostMapping(value = "/{projectId}/tasks",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addTask(
    		@PathVariable("projectId") Long projectId,
            @RequestBody TaskForm taskForm/*,
            UriComponentsBuilder ucBuilder*/) {
		logger.info("addTask(ENTER)");

		logger.debug("task form:" + taskForm.toString());
    	logger.debug("projectId:" + taskForm.getProjectId());
    	logger.debug("name:" + taskForm.getName());
    	logger.debug("isParent:" + taskForm.isParent());

    	Optional<Project> found = this.projectService.getProjectById(projectId);

    	if(!found.isPresent())
    		throw new RuntimeException("Project not found");

    	Project project = found.get();

    	if(taskForm.isParent()) {

    		ParentTask parentTask = new ParentTask();
    		parentTask.setParentTask(taskForm.getName());
    		parentTask.setProject(project);
    		this.parentTaskService.addParentTask(parentTask);

    	} else {

	    	User owner = null;
	    	if(taskForm.getUserId() != null) {
		    	Optional<User> userFound = this.userService.getUserByEmployeeId(taskForm.getUserId());
		    	if(!userFound.isPresent()) {
		    		throw new RuntimeException("User not found");
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

	    	if(taskForm.getParentTaskId() != null) {
	    		Optional<ParentTask> parentTaskFound = this.parentTaskService.getParentTaskById(taskForm.getParentTaskId());
	    		task.setParentTask(parentTaskFound.orElse(null));

	    	}

	    	this.projectService.updateProject(project);

    	}

    	HttpHeaders headers = new HttpHeaders();
    	headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
    	/*headers.setLocation(
                ucBuilder.path("/api/tasks/{id}")
                        .buildAndExpand(task.getTaskId())
                        .toUri());*/

		logger.info("addTask(EXIT)");
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
    	
    	logger.info("updateTask(ENTER)");

    	Optional<Task> found = taskService.getTaskById(taskId);

    	if (!found.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    	Task task = found.get();
    	task.setTask(taskForm.getName());
    	task.setStartDate(taskForm.getStartDate());
    	task.setEndDate(taskForm.getEndDate());
    	task.setPriority(taskForm.getPriority());
    	task.setStatus(taskForm.getStatus());

    	if(taskForm.getUserId() != null &&
    			( task.getOwner() == null ||
    	    			task.getOwner().getEmployeeId() != taskForm.getUserId()) ) {

	    	Optional<User> userFound = this.userService.getUserByEmployeeId(taskForm.getUserId());

	    	if(!userFound.isPresent()) {
	    		throw new RuntimeException("User not found");
	    	}

	    	task.setOwner(userFound.get());

    	}

    	if(taskForm.getParentTaskId() != null) {
    		Optional<ParentTask> parentTaskFound = this.parentTaskService.getParentTaskById(taskForm.getParentTaskId());
    		task.setParentTask(parentTaskFound.orElse(null));

    	}

    	taskService.updateTask(task);

    	logger.info("updateTask(EXIT)");
    	return new ResponseEntity<>(HttpStatus.OK);

    }

    @DeleteMapping(value = "/{projectId}/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable("taskId") Long taskId) {
    	logger.info("deleteTask(ENTER)");

    	Optional<Task> found = taskService.getTaskById(taskId);

    	if (!found.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    	taskService.deleteTask(taskId);

    	logger.info("deleteTask(EXIT)");
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    	
    	
    }
//
//    @PatchMapping(value = "/{projectId}/tasks/{taskId}")
//    public ResponseEntity<Void> endTask(@PathVariable("taskId") Long taskId) {
//
//		Optional<Task> found = taskService.getTaskById(taskId);
//
//		if (!found.isPresent()) {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//
//		Task task = found.get();
//		task.setStatus("COMPLETED");
//		taskService.updateTask(task);
//
//		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//
//	}

}
