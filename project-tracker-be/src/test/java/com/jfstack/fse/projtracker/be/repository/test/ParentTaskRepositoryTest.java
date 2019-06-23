package com.jfstack.fse.projtracker.be.repository.test;

import com.jfstack.fse.projtracker.be.Dummy;
import com.jfstack.fse.projtracker.be.entity.ParentTask;
import com.jfstack.fse.projtracker.be.entity.Project;
import com.jfstack.fse.projtracker.be.entity.Task;
import com.jfstack.fse.projtracker.be.entity.User;
import com.jfstack.fse.projtracker.be.repository.ParentTaskRepository;
import com.jfstack.fse.projtracker.be.repository.ProjectRepository;
import com.jfstack.fse.projtracker.be.repository.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ParentTaskRepositoryTest {

    @Autowired
    ParentTaskRepository repository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProjectRepository projectRepository;
    
    @Before
    public void setup() {
    }

    @Test
    public void testSaveParentTask() {

    	User user = userRepository.save(Dummy.createUser());
    	
    	Project project = Dummy.createBlankProject();
    	project.setManager(user);
    	project = projectRepository.save(project);
    	
        ParentTask parentTask = Dummy.createBlankParentTask();
        parentTask.setProject(project);
        

        repository.save(parentTask);

        Optional<ParentTask> actual = repository.findByParentTask("parent task 1");

        assertThat(actual).isNotEmpty();
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isInstanceOf(ParentTask.class);
        assertThat(actual.get().getParentTask()).isEqualTo(parentTask.getParentTask());
        assertThat(actual.get().getProject()).isNotNull();
        assertThat(actual.get().getProject().getProject()).isEqualTo("project 1");
        assertThat(actual.get().getProject().getManager()).isNotNull();
        assertThat(actual.get().getProject().getManager().getEmployeeId()).isEqualTo(208066);

    }
}
