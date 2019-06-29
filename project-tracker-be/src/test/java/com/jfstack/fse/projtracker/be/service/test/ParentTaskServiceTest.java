package com.jfstack.fse.projtracker.be.service.test;

import com.jfstack.fse.projtracker.be.Dummy;
import com.jfstack.fse.projtracker.be.entity.ParentTask;
import com.jfstack.fse.projtracker.be.entity.Task;
import com.jfstack.fse.projtracker.be.repository.ParentTaskRepository;
import com.jfstack.fse.projtracker.be.repository.TaskRepository;
import com.jfstack.fse.projtracker.be.service.ParentTaskService;
import com.jfstack.fse.projtracker.be.service.ParentTaskServiceImpl;
import com.jfstack.fse.projtracker.be.service.TaskService;
import com.jfstack.fse.projtracker.be.service.TaskServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ParentTaskServiceTest {

    @TestConfiguration
    static class ParentTaskServiceTestConfig {

        @Autowired
        ParentTaskRepository repository;

        @Bean
        public ParentTaskService taskService() {
            return new ParentTaskServiceImpl(repository);
        }

    }


    @Autowired ParentTaskService parentTaskService;

    @MockBean
    ParentTaskRepository parentTaskRepository;


    @Before
    public void setup() {
        ParentTask task1 = Dummy.createBlankParentTask();
        task1.setParentTask("parent task1");
        when(parentTaskRepository.findByParentTask("parent task1")).thenReturn(Optional.of(task1));

        List<ParentTask> taskList = Dummy.createParentTaskList();
        when(parentTaskRepository.findAll()).thenReturn(taskList);

        when(parentTaskRepository.findById(100L)).thenReturn(Optional.of(task1));

        Long projectId = 202L;
        when(parentTaskRepository.findAllByProject_ProjectId(projectId))
                .thenReturn(taskList);

    }

    @Test
    public void whenValidParentTask_thenParentTaskShouldBeFound() {

        Optional<ParentTask> actual = parentTaskService.getParentTaskByName("parent task1");

        assertThat(actual).isNotEmpty();
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isInstanceOf(ParentTask.class);
        assertThat(actual.get().getParentTask()).isEqualTo("parent task1");
    }

    @Test
    public void whenParentTaskNull_thenEmptyResult() {

        Optional<ParentTask> actual = parentTaskService.getParentTaskByName(null);

        assertThat(actual).isEmpty();
        assertThat(actual.isPresent()).isFalse();

    }


    @Test
    public void whenValidParentTaskId_thenParentTaskShouldBeFound() {

        Optional<ParentTask> actual = parentTaskService.getParentTaskById(100L);

        assertThat(actual).isNotEmpty();
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isInstanceOf(ParentTask.class);
        assertThat(actual.get().getParentTask()).isEqualTo("parent task1");
    }

    @Test
    public void whenParentTaskIdNull_thenEmptyResult() {

        Optional<ParentTask> actual = parentTaskService.getParentTaskById(null);

        assertThat(actual).isEmpty();
        assertThat(actual.isPresent()).isFalse();

    }

    @Test
    public void whenGetAllParentTasks_thenAllParentTasksShouldBeFound() {

        Optional<List<ParentTask>> actual = parentTaskService.getAllParentTasks();

        assertThat(actual).isNotEmpty();
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isInstanceOf(List.class);
        assertThat(actual.get().size()).isEqualTo(2);

    }

    @Test
    public void whenAddNewParentTask_thenParentTaskShouldBeSaved() {

        ParentTask entity = Dummy.createBlankParentTask();

        parentTaskService.addParentTask(entity);

        ArgumentCaptor<ParentTask> argument = ArgumentCaptor.forClass(ParentTask.class);
        verify( parentTaskRepository ).save( argument.capture() );
        assertThat( entity.getParentTask() ).isEqualTo( argument.getValue().getParentTask() );

    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAddNewParentTaskWithNull_thenException() {

        parentTaskService.addParentTask(null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void whenDeleteParentTaskWithNull_thenException() {

        parentTaskService.deleteParentTask(null);

    }

    @Test
    public void whenDeleteParentTaskWithValidId_thenParentTaskShouldBeDeleted() {

        parentTaskService.deleteParentTask(101L);

        ArgumentCaptor<Long> argument = ArgumentCaptor.forClass(Long.class);
        verify( parentTaskRepository ).deleteById( argument.capture() );
        assertThat( argument.getValue().longValue() ).isEqualTo( 101 );

    }


    @Test(expected = IllegalArgumentException.class)
    public void whenUpdateParentTaskWithNull_thenException() {

        parentTaskService.updateParentTask(null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void whenUpdateParentTaskWithNullId_thenException() {

        ParentTask existingTask = Dummy.createBlankParentTask();

        parentTaskService.updateParentTask(existingTask);

    }

    @Test
    public void whenUpdateExistingParentTask_thenParentTaskShouldBeSaved() {

        ParentTask existingTask = Dummy.createBlankParentTask();
        existingTask.setParentId(100L);

        parentTaskService.updateParentTask(existingTask);

        ArgumentCaptor<ParentTask> argument = ArgumentCaptor.forClass(ParentTask.class);
        verify( parentTaskRepository ).save( argument.capture() );
        assertThat( argument.getValue().getParentTask() ).isEqualTo( existingTask.getParentTask() );
        assertThat( argument.getValue().getParentId() ).isEqualTo( existingTask.getParentId() );

    }


    @Test
    public void givenProject_whenGetAllParentTaskByProject_thenReturnList() {
        List<ParentTask> actual = parentTaskService.getAllParentTasksByProject(202L);

        assertThat(actual).isNotEmpty();
        assertThat(actual.size()).isEqualTo(2);
    }
}
