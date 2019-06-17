package com.jfstack.fse.projtracker.be.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "PARENT_TABLE")
public class ParentTask {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PARENT_ID")
	private Long parentId;
	
	@Column(name = "PARENT_TASK")
	private String parentTask;
	
	@OneToMany(mappedBy = "parentTask",
	cascade = CascadeType.ALL,
	orphanRemoval = true)
	private List<Task> tasks = new ArrayList<>();

	public ParentTask() {
	}

	public void addTask(Task task) {
		tasks.add(task);
		task.setParentTask(this);
	}

	public void removeTask(Task task) {
		tasks.remove(task);
		task.setParentTask(null);
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentTask() {
		return parentTask;
	}

	public void setParentTask(String parentTask) {
		this.parentTask = parentTask;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
}
