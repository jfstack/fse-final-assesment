package com.jfstack.fse.projtracker.be.service;

import com.jfstack.fse.projtracker.be.entity.Project;
import com.jfstack.fse.projtracker.be.entity.Task;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

    Optional<Project> getProjectByName(String projectName);

    Optional<Project> getProjectById(Long projectId);

    Optional<List<Project>> getAllProjects();

    Project addProject(Project project);

    void deleteProject(Long projectId);

    void updateProject(Project project);
}
