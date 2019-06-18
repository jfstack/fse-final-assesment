package com.jfstack.fse.projtracker.be.service;

import java.util.List;
import java.util.Optional;

import com.jfstack.fse.projtracker.be.entity.Task;

public interface TaskService {
	
	Optional<Task> getTaskByName(String taskName);
	
	Optional<Task> getTaskById(Long taskId);

	Optional<List<Task>> getAllTasks();
	
	void addTask(Task task);
	
	void deleteTask(Long taskId);
	
	void updateTask(Task task);
	
}
