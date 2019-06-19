package com.jfstack.fse.projtracker.be;

import com.jfstack.fse.projtracker.be.dto.UserDto;
import com.jfstack.fse.projtracker.be.entity.ParentTask;
import com.jfstack.fse.projtracker.be.entity.Project;
import com.jfstack.fse.projtracker.be.entity.Task;
import com.jfstack.fse.projtracker.be.entity.User;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Dummy {

    public static Project createBlankProject() {
        Project project = new Project();
        project.setProject("project 1");
        project.setPriority(1);
        project.setStartDate(LocalDate.now());
        project.setEndDate(LocalDate.now().plusDays(1));

        return project;
    }

    public static List<Project> createProjectList() {
        Project project1 = new Project();
        project1.setProject("project 1");
        project1.setPriority(1);
        project1.setStartDate(LocalDate.now());
        project1.setEndDate(LocalDate.now().plusDays(1));

        Project project2 = new Project();
        project2.setProject("project 1");
        project2.setPriority(1);
        project2.setStartDate(LocalDate.now());
        project2.setEndDate(LocalDate.now().plusDays(1));

        return Arrays.asList(project2, project2);
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
    
    public static List<Task> createTaskList() {
        Task task1 = new Task();
        task1.setTask("task 1");
        task1.setStartDate(LocalDate.now());
        task1.setEndDate(LocalDate.now().plusDays(1));
        task1.setStatus("open");
        task1.setPriority(1);
        
        Task task2 = new Task();
        task2.setTask("task 2");
        task2.setStartDate(LocalDate.now());
        task2.setEndDate(LocalDate.now().plusDays(1));
        task2.setStatus("open");
        task2.setPriority(1);
        
        return Arrays.asList(task1, task2);
    }

    public static ParentTask createBlankParentTask() {

        ParentTask parentTask = new ParentTask();
        parentTask.setParentTask("parent task 1");
        return parentTask;
    }
    public static List<ParentTask> createParentTaskList() {

        ParentTask parentTask1 = new ParentTask();
        parentTask1.setParentTask("parent task1");

        ParentTask parentTask2 = new ParentTask();
        parentTask2.setParentTask("parent task2");
        return Arrays.asList(parentTask1, parentTask2);
    }

    public static User createUser() {
        User user = new User();
        user.setEmployeeId(208066);
        user.setFirstName("Chandan");
        user.setLastName("Ghosh");

        return user;
    }
    
    public static UserDto wrapUserInDto(User entity) {
        UserDto user = new UserDto();
        user.setEmployeeId(entity.getEmployeeId());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());

        return user;
    }
    
    public static List<User> createUserList() {
        User chandan = new User();
        chandan.setEmployeeId(208066);
        chandan.setFirstName("Chandan");
        chandan.setLastName("Ghosh");
        
        User joy = new User();
        joy.setEmployeeId(208067);
        joy.setFirstName("Joy");
        joy.setLastName("Burman");

        return Arrays.asList(chandan, joy);
    }
}
