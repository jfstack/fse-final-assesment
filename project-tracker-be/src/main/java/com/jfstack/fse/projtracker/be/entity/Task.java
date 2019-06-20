package com.jfstack.fse.projtracker.be.entity;

import java.time.LocalDate;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jfstack.fse.projtracker.be.controller.LocalDateDeSerializer;
import com.jfstack.fse.projtracker.be.controller.LocalDateSerializer;

@Entity
@Table(name = "TASK_TABLE")
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "TASK_ID")
	private Long taskId;
	
	@Column(name = "TASK")
	private String task;
	
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
	
	@Column(name = "STATUS")
	private String status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PARENT_ID")
	private ParentTask parentTask;


	@ManyToOne//(fetch = FetchType.EAGER)
	@JoinColumn(name = "PROJECT_ID")
	@JsonIgnore
	private Project project;
	
	@OneToOne(mappedBy = "task", cascade = CascadeType.ALL)
	private User owner;

	public Task() {
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ParentTask getParentTask() {
		return parentTask;
	}

	public void setParentTask(ParentTask parentTask) {
		this.parentTask = parentTask;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	
}
