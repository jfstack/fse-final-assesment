package com.jfstack.fse.projtracker.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jfstack.fse.projtracker.be.entity.ParentTask;

public interface ParentTaskRepository extends JpaRepository<ParentTask, Long> {

}
