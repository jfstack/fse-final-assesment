package com.jfstack.fse.projtracker.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jfstack.fse.projtracker.be.entity.Task;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByTask(String name);
}
