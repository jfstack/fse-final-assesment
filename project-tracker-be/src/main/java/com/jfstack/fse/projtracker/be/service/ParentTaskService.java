package com.jfstack.fse.projtracker.be.service;

import com.jfstack.fse.projtracker.be.entity.ParentTask;
import com.jfstack.fse.projtracker.be.entity.Task;

import java.util.List;
import java.util.Optional;

public interface ParentTaskService {
    Optional<ParentTask> getParentTaskByName(String taskName);

    Optional<ParentTask> getParentTaskById(Long taskId);

    Optional<List<ParentTask>> getAllParentTasks();

    void addParentTask(ParentTask task);

    void deleteParentTask(Long taskId);

    void updateParentTask(ParentTask task);
}
