package com.jfstack.fse.projtracker.be.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jfstack.fse.projtracker.be.controller.LocalDateDeSerializer;
import com.jfstack.fse.projtracker.be.controller.LocalDateSerializer;

@Entity
@Table(name = "PROJECT_TABLE")
public class Project {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PROJECT_ID")
	private Long projectId;
	
	@Column(name = "PROJECT")
	private String project;
	
	@Column(name = "START_DATE")
	@JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeSerializer.class)
	private LocalDate startDate;
	
	@Column(name = "END_DATE")
	@JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeSerializer.class)
	private LocalDate endDate;
	
	@Column(name = "PRIORITY")
	private Integer priority;
	
	@OneToMany(
			mappedBy = "project",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	private List<Task> tasks = new ArrayList<>();

	public Project() {
	}

	public void addTask(Task task) {
		tasks.add(task);
		task.setProject(this);
	}

	public void removeTask(Task task) {
		tasks.remove(task);
		task.setProject(null);
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
}
