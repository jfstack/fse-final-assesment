package com.jfstack.fse.projtracker.be.service;

import com.jfstack.fse.projtracker.be.entity.ParentTask;
import com.jfstack.fse.projtracker.be.entity.Project;
import com.jfstack.fse.projtracker.be.repository.ParentTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("parentService")
public class ParentTaskServiceImpl implements ParentTaskService {

    private ParentTaskRepository repository;

    @Autowired
    public ParentTaskServiceImpl(ParentTaskRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ParentTask> getParentTaskByName(String taskName) {
        if (taskName == null) {
            return Optional.empty();
        }

        Optional<ParentTask> parentTask = repository.findByParentTask(taskName);

        return parentTask;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ParentTask> getParentTaskById(Long taskId) {
        if (taskId == null) {
            return Optional.empty();
        }

        Optional<ParentTask> parentTask = repository.findById(taskId);

        return parentTask;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<List<ParentTask>> getAllParentTasks() {
        List<ParentTask> all = repository.findAll();

        return Optional.ofNullable(all);
    }

    @Override
    @Transactional
    public void addParentTask(ParentTask task) {
        if(task == null) {
            throw new IllegalArgumentException("parent data is null");
        }

        repository.save(task);
    }

    @Override
    @Transactional
    public void deleteParentTask(Long taskId) {
        if (taskId == null)
            throw new IllegalArgumentException("parent task id is null");

        repository.deleteById(taskId);
    }

    @Override
    @Transactional
    public void updateParentTask(ParentTask task) {
        if(task == null)
            throw new IllegalArgumentException("parent task data is null");

        if(task.getParentId() == null)
            throw new IllegalArgumentException("project task id is null");

        Optional<ParentTask> found = repository.findById(task.getParentId());


        if(found.isPresent()) {
            repository.save(task);
        }
    }
}
