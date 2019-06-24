import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ProjectService } from 'src/app/services/project.service';
import { startDateEndDateValidator } from '../../validators/date.validator';


@Component({
  selector: 'app-task',
  templateUrl: './task.component.html',
  styleUrls: ['./task.component.css']
})
export class TaskComponent implements OnInit {

  form = new FormGroup({
    taskId : new FormControl(-1),
    projectId : new FormControl(-1),
    name : new FormControl('', Validators.required),
    parentType : new FormControl(false),
    startDate : new FormControl(this.projectService.getDefaultStartAndEndDate()[0], Validators.required),
    endDate : new FormControl(this.projectService.getDefaultStartAndEndDate()[1], Validators.required),
    priority : new FormControl(1, Validators.required),
    parentTaskId: new FormControl(''),
    userId : new FormControl('', Validators.required)
  }, {validators: startDateEndDateValidator});

  constructor(private projectService: ProjectService) { }

  ngOnInit() {
  }

}
