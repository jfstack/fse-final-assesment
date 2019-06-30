package com.jfstack.fse.projtracker.be.repository.test;

import com.jfstack.fse.projtracker.be.Dummy;
import com.jfstack.fse.projtracker.be.entity.Project;
import com.jfstack.fse.projtracker.be.entity.Task;
import com.jfstack.fse.projtracker.be.entity.User;
import com.jfstack.fse.projtracker.be.repository.ProjectRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProjectRepositoryTest {

    @Autowired
    ProjectRepository repository;

    @Test
    public void testSaveProject() {
        Project project = Dummy.createBlankProject();
        project.setProject("projectA");
        project.setStatus("OPEN");

        repository.save(project);
        Optional<Project> actual = repository.findByProject("projectA");

        assertThat(actual).isNotEmpty();
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isInstanceOf(Project.class);
        assertThat(actual.get().getProject()).isEqualTo(project.getProject());
        assertThat(actual.get().getStatus()).isEqualTo(project.getStatus());

    }

    @Test
    public void testSaveProjectWithTask() {
        Project project = Dummy.createBlankProject();
        project.setProject("projectB");
        Task task = Dummy.createBlankTask();
        User user = Dummy.createUser();
        user.setProject(project);
        project.setManager(user);
        project.addTask(task);
        task.setProject(project);

        repository.save(project);
        Optional<Project> actual = repository.findByProject("projectB");

        assertThat(actual).isNotEmpty();
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isInstanceOf(Project.class);
        assertThat(actual.get().getProject()).isEqualTo(project.getProject());
        assertThat(actual.get().getTasks()).isNotEmpty();
        assertThat(actual.get().getTasks().size()).isOne();


    }
}
