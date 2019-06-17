package com.jfstack.fse.projtracker.be.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
	private LocalDate startDate;
	
	@Column(name = "END_DATE")
	private LocalDate endDate;
	
	@Column(name = "PRIORITY")
	private Integer priority;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "PARENT_ID")
	private Long parentId;
	
	@Column(name = "PROJECT_ID")
	private Long projectId;

}
