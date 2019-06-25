import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ProjectService } from 'src/app/services/project.service';
import { startDateEndDateValidator } from '../../validators/date.validator';
import { ModalService } from '../../services/modal.service';


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

  selectedProject: string;
  selectedParentTask: string;
  selectedUser: string;

  constructor(
    private projectService: ProjectService,
    private modalService: ModalService) { }

  ngOnInit() {
  }

  openModal(modalId: string) {
    this.modalService.open(modalId);
  }

  closeModal(modalId: string) {
    this.modalService.close(modalId);
  }

  selectProjectFromModal(event) {
    console.log("Event data");
    console.log(event);
    
  }

  selectParentTaskFromModal(event) {
    console.log("Event data");
    console.log(event);
  }

  selectUserFromModal(event) {
    console.log("Event data");
    console.log(event);
  }

}
