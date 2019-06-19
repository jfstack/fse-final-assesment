package com.jfstack.fse.projtracker.be.repository.test;

import com.jfstack.fse.projtracker.be.Dummy;
import com.jfstack.fse.projtracker.be.entity.Project;
import com.jfstack.fse.projtracker.be.entity.Task;
import com.jfstack.fse.projtracker.be.repository.ProjectRepository;
import com.jfstack.fse.projtracker.be.repository.TaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    TaskRepository taskRepository;
    
    @Autowired
    ProjectRepository projectRepository;

    @Test
    public void testSaveTask() {

        Task task = Dummy.createBlankTask();

        taskRepository.save(task);

        Optional<Task> actual = taskRepository.findByTask("task 1 for project 1");

        assertThat(actual).isNotEmpty();
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isInstanceOf(Task.class);
        assertThat(actual.get().getTask()).isEqualTo(task.getTask());

    }
    
    @Test
    public void testFindTaskByProject() {

        Task task = Dummy.createBlankTask();
        Project project = Dummy.createBlankProject();
        project.addTask(task);
        task.setProject(project);

        project = projectRepository.save(project);

        Optional<List<Task>> actual = taskRepository.findAllByProject_ProjectId(project.getProjectId());

        assertThat(actual).isNotEmpty();
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isInstanceOf(List.class);
        assertThat(actual.get().size()).isEqualTo(1);

    }

}
