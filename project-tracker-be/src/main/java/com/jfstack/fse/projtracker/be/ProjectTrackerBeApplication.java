package com.jfstack.fse.projtracker.be;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jfstack.fse.projtracker.be.entity.Project;
import com.jfstack.fse.projtracker.be.entity.User;
import com.jfstack.fse.projtracker.be.repository.ProjectRepository;
import com.jfstack.fse.projtracker.be.repository.TaskRepository;
import com.jfstack.fse.projtracker.be.repository.UserRepository;

@SpringBootApplication
public class ProjectTrackerBeApplication /*implements CommandLineRunner*/ {

	public static void main(String[] args) {
		SpringApplication.run(ProjectTrackerBeApplication.class, args);
	}

	//@Autowired
	UserRepository userRepository;
	
	//@Autowired
	TaskRepository taskRepository;
	
	//@Autowired
	ProjectRepository projectRepository;
	
	//@Override
	public void run(String... args) throws Exception {
		this.userRepository.deleteAll();
		
		User chandan = new User();
		chandan.setEmployeeId(208066);
		chandan.setFirstName("Chandan");
		chandan.setLastName("Ghosh");
		User raja = new User();
		raja.setEmployeeId(312131);
		raja.setFirstName("Raja");
		raja.setLastName("Das");
		
		Arrays.asList(chandan, raja).stream().forEach(entity -> userRepository.save(entity));
		
		chandan = userRepository.findByEmployeeId(208066).get();
		raja = userRepository.findByEmployeeId(312131).get();
		
		System.out.println("chandan id:" + chandan.getUserId());
		System.out.println("raja id:" + raja.getUserId());
		
		Project project1 = new Project();
        project1.setProject("project 1");
        project1.setPriority(1);
        project1.setStartDate(LocalDate.now());
        project1.setEndDate(LocalDate.now().plusDays(1));
        //project1.setManager(chandan);
        //chandan.setProject(project1);
        
        Project project2 = new Project();
        project2.setProject("project ");
        project2.setPriority(1);
        project2.setStartDate(LocalDate.now());
        project2.setEndDate(LocalDate.now().plusDays(1));
        //project2.setManager(raja);
        //raja.setProject(project2);
        
        Arrays.asList(project1, project2).stream().forEach(entity -> projectRepository.save(entity));
		
	}

}
