package com.jfstack.fse.projtracker.be.service.test;

import com.jfstack.fse.projtracker.be.Dummy;
import com.jfstack.fse.projtracker.be.entity.Task;
import com.jfstack.fse.projtracker.be.repository.TaskRepository;
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
public class TaskServiceTest {
	
	@TestConfiguration
	static class TaskServiceTestConfig {
		
		@Autowired TaskRepository repository;
		
		@Bean
		public TaskService taskService() {
			return new TaskServiceImpl(repository);
		}
		
	}
	
	
	@Autowired TaskService taskService;
	
	@MockBean TaskRepository taskRepository;
	
	
	@Before
	public void setup() {
		Task task1 = Dummy.createBlankTask();
		task1.setTask("task1");
		when(taskRepository.findByTask("task1")).thenReturn(Optional.of(task1));
		
		List<Task> taskList = Dummy.createTaskList();
		when(taskRepository.findAll()).thenReturn(taskList);
		
		when(taskRepository.findById(100L)).thenReturn(Optional.of(task1));
		
	}
	
	@Test
	public void whenValidTask_thenTaskShouldBeFound() {
		
		Optional<Task> actual = taskService.getTaskByName("task1");
		
		assertThat(actual).isNotEmpty();
		assertThat(actual.isPresent()).isTrue();
		assertThat(actual.get()).isInstanceOf(Task.class);
		assertThat(actual.get().getTask()).isEqualTo("task1");
	}
	
	@Test
	public void whenTaskNull_thenEmptyResult() {
		
		Optional<Task> actual = taskService.getTaskByName(null);
		
		assertThat(actual).isEmpty();
		assertThat(actual.isPresent()).isFalse();
		
	}
	
	@Test
	public void whenValidTaskId_thenTaskShouldBeFound() {
		
		Optional<Task> actual = taskService.getTaskById(100L);
		
		assertThat(actual).isNotEmpty();
		assertThat(actual.isPresent()).isTrue();
		assertThat(actual.get()).isInstanceOf(Task.class);
		assertThat(actual.get().getTask()).isEqualTo("task1");
	}
	
	@Test
	public void whenTaskIdNull_thenEmptyResult() {
		
		Optional<Task> actual = taskService.getTaskById(null);
		
		assertThat(actual).isEmpty();
		assertThat(actual.isPresent()).isFalse();
		
	}
	
	@Test
	public void whenGetAllTasks_thenAllTasksShouldBeFound() {
		
		Optional<List<Task>> actual = taskService.getAllTasks();
		
		assertThat(actual).isNotEmpty();
		assertThat(actual.isPresent()).isTrue();
		assertThat(actual.get()).isInstanceOf(List.class);
		assertThat(actual.get().size()).isEqualTo(2);
		
	}
	
	@Test
	public void whenAddNewTask_thenTaskShouldBeSaved() {
		
		Task entity = Dummy.createBlankTask();
		
		taskService.addTask(entity);
		
		ArgumentCaptor<Task> argument = ArgumentCaptor.forClass(Task.class);
	    verify( taskRepository ).save( argument.capture() );
	    assertThat( entity.getTask() ).isEqualTo( argument.getValue().getTask() );
	    assertThat( entity.getStatus() ).isEqualTo( argument.getValue().getStatus() );
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void whenAddNewTaskWithNull_thenException() {
		
		taskService.addTask(null);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void whenDeleteTaskWithNull_thenException() {
		
		taskService.deleteTask(null);
		
	}
	
	@Test
	public void whenDeleteTaskWithValidId_thenTaskShouldBeDeleted() {
		
		taskService.deleteTask(101L);
		
		ArgumentCaptor<Long> argument = ArgumentCaptor.forClass(Long.class);
	    verify( taskRepository ).deleteById( argument.capture() );
	    assertThat( argument.getValue().longValue() ).isEqualTo( 101 );
		
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void whenUpdateTaskWithNull_thenException() {
		
		taskService.updateTask(null);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void whenUpdateTaskWithNullId_thenException() {
		
		Task existingTask = Dummy.createBlankTask();
		
		taskService.updateTask(existingTask);
		
	}
	
	@Test
	public void whenUpdateExistingTask_thenTaskShouldBeSaved() {
		
		Task existingTask = Dummy.createBlankTask();
		existingTask.setTaskId(100L);
		
		taskService.updateTask(existingTask);
		
		ArgumentCaptor<Task> argument = ArgumentCaptor.forClass(Task.class);
	    verify( taskRepository ).save( argument.capture() );
	    assertThat( argument.getValue().getTask() ).isEqualTo( existingTask.getTask() );
	    assertThat( argument.getValue().getTaskId() ).isEqualTo( existingTask.getTaskId() );
	    assertThat( argument.getValue().getStatus() ).isEqualTo( existingTask.getStatus() );
		
	}
	
	
	
	
	
	
	
}
