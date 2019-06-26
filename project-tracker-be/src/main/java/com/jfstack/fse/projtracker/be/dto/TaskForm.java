package com.jfstack.fse.projtracker.be.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jfstack.fse.projtracker.be.controller.LocalDateDeSerializer;
import com.jfstack.fse.projtracker.be.controller.LocalDateSerializer;

public class TaskForm {
	
	private Long projectId;
	
	private String name;
	
	private String parentType;
	
	private Integer priority;
	
	private Long parentTaskId;
	

	@JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeSerializer.class)
	private LocalDate startDate;

	@JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeSerializer.class)
	private LocalDate endDate;
	
	private Integer userId;

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Long getParentTaskId() {
		return parentTaskId;
	}

	public void setParentTaskId(Long parentTaskId) {
		this.parentTaskId = parentTaskId;
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Boolean isParent() {
		return ("true".equalsIgnoreCase(parentType)) ? true : false;
	}

	public void setParentType(String parentType) {
		this.parentType = parentType;
	}
	
	public String getParentType() {
		return this.parentType;
	}

	@Override
	public String toString() {
		return "TaskForm{" +
				"projectId=" + projectId +
				", name='" + name + '\'' +
				", parentType='" + parentType + '\'' +
				", priority=" + priority +
				", parentTaskId=" + parentTaskId +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", userId=" + userId +
				'}';
	}
}
