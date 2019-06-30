package com.jfstack.fse.projtracker.be.service.test;

import com.jfstack.fse.projtracker.be.Dummy;
import com.jfstack.fse.projtracker.be.entity.Project;
import com.jfstack.fse.projtracker.be.repository.ProjectRepository;
import com.jfstack.fse.projtracker.be.service.ProjectService;
import com.jfstack.fse.projtracker.be.service.ProjectServiceImpl;
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
public class ProjectServiceTest {

    @TestConfiguration
    static class ProjectServiceTestConfig {

        @Autowired
        ProjectRepository repository;

        @Bean
        public ProjectService projectService() {
            return new ProjectServiceImpl(repository);
        }

    }


    @Autowired ProjectService projectService;

    @MockBean ProjectRepository projectRepository;

    @Before
    public void setup() {
        Project project = Dummy.createBlankProject();
        when(projectRepository.findByProject("project 1")).thenReturn(Optional.of(project));

        List<Project> projectList = Dummy.createProjectList();
        when(projectRepository.findAll()).thenReturn(projectList);

        when(projectRepository.findById(100L)).thenReturn(Optional.of(project));

    }


    @Test
    public void whenValidProject_thenTaskShouldBeFound() {

        Optional<Project> actual = projectService.getProjectByName("project 1");

        assertThat(actual).isNotEmpty();
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isInstanceOf(Project.class);
        assertThat(actual.get().getProject()).isEqualTo("project 1");
    }

    @Test
    public void whenTaskNull_thenEmptyResult() {

        Optional<Project> actual = projectService.getProjectByName(null);

        assertThat(actual).isEmpty();
        assertThat(actual.isPresent()).isFalse();

    }


    @Test
    public void whenValidProjectId_thenProjectShouldBeFound() {

        Optional<Project> actual = projectService.getProjectById(100L);

        assertThat(actual).isNotEmpty();
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isInstanceOf(Project.class);
        assertThat(actual.get().getProject()).isEqualTo("project 1");
    }

    @Test
    public void whenProjectIdNull_thenEmptyResult() {

        Optional<Project> actual = projectService.getProjectById(null);

        assertThat(actual).isEmpty();
        assertThat(actual.isPresent()).isFalse();

    }


    @Test
    public void whenGetAllProjects_thenAllProjectsShouldBeFound() {

        Optional<List<Project>> actual = projectService.getAllProjects();

        assertThat(actual).isNotEmpty();
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isInstanceOf(List.class);
        assertThat(actual.get().size()).isEqualTo(2);

    }


    @Test
    public void whenAddNewProject_thenProjectShouldBeSaved() {

        Project entity = Dummy.createBlankProject();
        entity.setStatus("OPEN");
        projectService.addProject(entity);

        ArgumentCaptor<Project> argument = ArgumentCaptor.forClass(Project.class);
        verify( projectRepository ).save( argument.capture() );
        assertThat( entity.getProject() ).isEqualTo( argument.getValue().getProject() );
        assertThat( entity.getPriority() ).isEqualTo( argument.getValue().getPriority() );
        assertThat( entity.getStatus() ).isEqualTo( argument.getValue().getStatus() );

    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAddNewProjectWithNull_thenException() {

        projectService.addProject(null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void whenDeleteProjectWithNull_thenException() {

        projectService.deleteProject(null);

    }

    @Test
    public void whenDeleteProjectWithValidId_thenProjectShouldBeDeleted() {

        projectService.deleteProject(101L);

        ArgumentCaptor<Long> argument = ArgumentCaptor.forClass(Long.class);
        verify( projectRepository ).deleteById( argument.capture() );
        assertThat( argument.getValue().longValue() ).isEqualTo( 101 );

    }


    @Test(expected = IllegalArgumentException.class)
    public void whenUpdateProjectWithNull_thenException() {

        projectService.updateProject(null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void whenUpdateProjectWithNullId_thenException() {

        Project existingProject = Dummy.createBlankProject();

        projectService.updateProject(existingProject);

    }


    @Test
    public void whenUpdateExistingProject_thenProjectShouldBeSaved() {

        Project existingProject = Dummy.createBlankProject();
        existingProject.setStatus("SUSPEND");
        existingProject.setProjectId(100L);

        projectService.updateProject(existingProject);

        ArgumentCaptor<Project> argument = ArgumentCaptor.forClass(Project.class);
        verify( projectRepository ).save( argument.capture() );
        assertThat( argument.getValue().getProject() ).isEqualTo( existingProject.getProject() );
        assertThat( argument.getValue().getProjectId() ).isEqualTo( existingProject.getProjectId() );
        assertThat( argument.getValue().getPriority() ).isEqualTo( existingProject.getPriority() );
        assertThat( argument.getValue().getStatus() ).isEqualTo( existingProject.getStatus() );

    }
}
