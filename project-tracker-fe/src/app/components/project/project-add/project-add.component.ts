import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ProjectService } from '../../../services/project.service';

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
    startDate : new FormControl('2019-06-23', Validators.required),
    // endDate : new FormControl({value: '2019-06-23', disabled: true}, Validators.required),
    endDate : new FormControl('2019-06-24', Validators.required),
    priority : new FormControl(1, Validators.required),
    managerId : new FormControl('', Validators.required)
  });

  marked = false;

  constructor(private projectService: ProjectService) { }

  ngOnInit() {
  }

  addProject() {
    console.log("ProjectAddComponent.addProject");

  }

}
