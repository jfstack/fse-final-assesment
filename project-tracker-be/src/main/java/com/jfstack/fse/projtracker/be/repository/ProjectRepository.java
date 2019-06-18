package com.jfstack.fse.projtracker.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jfstack.fse.projtracker.be.entity.Project;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByProject(String name);
}
