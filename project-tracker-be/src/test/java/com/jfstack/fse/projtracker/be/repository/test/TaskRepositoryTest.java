package com.jfstack.fse.projtracker.be.repository.test;

import com.jfstack.fse.projtracker.be.Dummy;
import com.jfstack.fse.projtracker.be.entity.Project;
import com.jfstack.fse.projtracker.be.entity.Task;
import com.jfstack.fse.projtracker.be.repository.TaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    TaskRepository repository;

    @Test
    public void testSaveTask() {

        Task task = Dummy.createBlankTask();

        repository.save(task);

        Optional<Task> actual = repository.findByTask("task 1 for project 1");

        assertThat(actual).isNotEmpty();
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isInstanceOf(Task.class);
        assertThat(actual.get().getTask()).isEqualTo(task.getTask());

    }

}
