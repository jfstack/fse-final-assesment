package com.jfstack.fse.projtracker.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jfstack.fse.projtracker.be.entity.ParentTask;

import java.util.List;
import java.util.Optional;

public interface ParentTaskRepository extends JpaRepository<ParentTask, Long> {

    Optional<ParentTask> findByParentTask(String name);

    List<ParentTask> findAllByProject_ProjectId(Long projectId);
}
