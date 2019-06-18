package com.jfstack.fse.projtracker.be;

import com.jfstack.fse.projtracker.be.entity.ParentTask;
import com.jfstack.fse.projtracker.be.entity.Project;
import com.jfstack.fse.projtracker.be.entity.Task;
import com.jfstack.fse.projtracker.be.entity.User;

import java.time.LocalDate;

public class Dummy {

    public static Project createBlankProject() {
        Project project = new Project();
        project.setProject("project 1");
        project.setPriority(1);
        project.setStartDate(LocalDate.now());
        project.setEndDate(LocalDate.now().plusDays(1));

        return project;
    }

    public static Task createBlankTask() {
        Task task = new Task();
        task.setTask("task 1 for project 1");
        task.setStartDate(LocalDate.now());
        task.setEndDate(LocalDate.now().plusDays(1));
        task.setStatus("open");
        task.setPriority(1);
        return task;
    }

    public static ParentTask createBlankParentTask() {

        ParentTask parentTask = new ParentTask();
        parentTask.setParentTask("parent task 1");
        return parentTask;
    }

    public static User createUser() {
        User user = new User();
        user.setEmployeeId(208066);
        user.setFirstName("Chandan");
        user.setLastName("Ghosh");

        return user;
    }
}
