package com.jfstack.fse.projtracker.be.service;

import com.jfstack.fse.projtracker.be.entity.Project;
import com.jfstack.fse.projtracker.be.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("projectService")
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository repository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Project> getProjectByName(String projectName) {
        if (projectName == null) {
            return Optional.empty();
        }

        Optional<Project> project = repository.findByProject(projectName);

        return project;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Project> getProjectById(Long projectId) {
        if (projectId == null) {
            return Optional.empty();
        }

        Optional<Project> project = repository.findById(projectId);
        return project;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<List<Project>> getAllProjects() {
        List<Project> all = repository.findAll();
        return Optional.ofNullable(all);
    }

    @Override
    @Transactional
    public void addProject(Project project) {
        if(project == null) {
            throw new IllegalArgumentException("project data is null");
        }

        repository.save(project);
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        if (projectId == null)
            throw new IllegalArgumentException("project id is null");

        repository.deleteById(projectId);
    }

    @Override
    @Transactional
    public void updateProject(Project project) {
        if(project == null)
            throw new IllegalArgumentException("project data is null");

        if(project.getProjectId() == null)
            throw new IllegalArgumentException("project id is null");

        Optional<Project> found = repository.findById(project.getProjectId());

        if(found.isPresent()) {
            repository.save(project);
        }
    }
}
