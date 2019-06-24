import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ProjectService } from '../../../services/project.service';
import { Subscription } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'project-add',
  templateUrl: './project-add.component.html',
  styleUrls: ['./project-add.component.css']
})
export class ProjectAddComponent implements OnInit {

  form = new FormGroup({
    projectId : new FormControl(-1),
    name : new FormControl('', Validators.required),
    // startDate : new FormControl({value: '2019-06-23', disabled: true}, Validators.required),
    startDate : new FormControl(this.projectService.getDefaultStartAndEndDate()[0], Validators.required),
    // endDate : new FormControl({value: '2019-06-23', disabled: true}, Validators.required),
    endDate : new FormControl(this.projectService.getDefaultStartAndEndDate()[1], Validators.required),
    priority : new FormControl(1, Validators.required),
    managerId : new FormControl('', Validators.required)
  });

  marked = false;

  createProjectSubscription: Subscription;

  constructor(private projectService: ProjectService) { 
  }

  ngOnInit() {
  }

  addProject() {
    console.log("ProjectAddComponent.addProject");
    console.log(this.form.getRawValue());
    
    if(this.form.invalid) {
      return;
    }

    this.createProjectSubscription = 
        this.projectService.createProject(this.form.getRawValue()).subscribe(

          (data) => {
            console.log("Data saved successfully:");
            this.projectService.castProjectOnCreate(data);
            this.resetForm();
          },

          (error: HttpErrorResponse) => {console.log(error.name + ' ' + error.message);}

        );

  }

  resetForm() {
    this.form.reset({
      projectId: -1,
      name: '',
      startDate: '2019-06-24',
      endDate: '2019-06-25',
      priority: 1,
      managerId: ''
    });
  }

}
