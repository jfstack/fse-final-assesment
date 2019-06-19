package com.jfstack.fse.projtracker.be.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jfstack.fse.projtracker.be.dto.TaskDto;
import com.jfstack.fse.projtracker.be.entity.Task;
import com.jfstack.fse.projtracker.be.repository.TaskRepository;

@Service("taskService")
public class TaskServiceImpl implements TaskService {
	
	private TaskRepository repository;
	
	@Autowired
	public TaskServiceImpl(TaskRepository repository) {
		this.repository = repository;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Optional<Task> getTaskByName(String taskName) {
		if(taskName == null) {
			return Optional.empty();
		}
		
		Optional<Task> task = repository.findByTask(taskName);
		return task;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Task> getTaskById(Long taskId) {
		
		if(taskId == null) {
			return Optional.empty();
		}
		
		Optional<Task> task = repository.findById(taskId);
		return task;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<List<Task>> getAllTasks() {
		List<Task> allTasks = repository.findAll();
		return Optional.ofNullable(allTasks);
	}

	@Override
	@Transactional
	public void addTask(Task task) {
		
		if(task == null)
			throw new IllegalArgumentException("task data is null");
		
		repository.save(task);
	}

	@Override
	@Transactional
	public void deleteTask(Long taskId) {
		
		if(taskId == null) {
			throw new IllegalArgumentException("task id is null");
		}
		
		repository.deleteById(taskId);

	}

	@Override
	@Transactional
	public void updateTask(Task task) {

		if(task == null)
			throw new IllegalArgumentException("task date is null");
		
		if(task.getTaskId() == null)
			throw new IllegalArgumentException("task id is null");
		
		Optional<Task> found = repository.findById(task.getTaskId());
		
		if(found.isPresent()) {
			repository.save(task);
		}
	}

	@Override
	public Optional<List<Task>> getAllTasksByProject(Long projectId) {
		return repository.findAllByProject_ProjectId(projectId);
	}

}
