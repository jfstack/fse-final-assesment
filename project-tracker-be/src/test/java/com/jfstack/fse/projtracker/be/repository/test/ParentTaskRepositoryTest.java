package com.jfstack.fse.projtracker.be.repository.test;

import com.jfstack.fse.projtracker.be.Dummy;
import com.jfstack.fse.projtracker.be.entity.ParentTask;
import com.jfstack.fse.projtracker.be.entity.Task;
import com.jfstack.fse.projtracker.be.repository.ParentTaskRepository;
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

    @Test
    public void testSaveParentTask() {
        ParentTask parentTask = Dummy.createBlankParentTask();

        repository.save(parentTask);

        Optional<ParentTask> actual = repository.findByParentTask("parent task 1");

        assertThat(actual).isNotEmpty();
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isInstanceOf(ParentTask.class);
        assertThat(actual.get().getParentTask()).isEqualTo(parentTask.getParentTask());

    }
}
