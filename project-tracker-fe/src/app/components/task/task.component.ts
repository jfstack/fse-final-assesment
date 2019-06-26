import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ProjectService } from 'src/app/services/project.service';
import { TaskService } from 'src/app/services/task.service';
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
    projectName: new FormControl(),
    name : new FormControl('', Validators.required),
    parentType : new FormControl(false),
    startDate : new FormControl(this.projectService.getDefaultStartAndEndDate()[0], Validators.required),
    endDate : new FormControl(this.projectService.getDefaultStartAndEndDate()[1], Validators.required),
    priority : new FormControl(1, Validators.required),
    parentTaskId: new FormControl(-1),
    parentTaskName: new FormControl('', Validators.required),
    userId : new FormControl(-1),
    userName: new FormControl('')
  }, {validators: startDateEndDateValidator});

  // selectedProject: string;
  // selectedParentTask: string;
  // selectedUser: string;

  constructor(
    private projectService: ProjectService,
    private modalService: ModalService,
    private taskService: TaskService) { }

  ngOnInit() {
  }

  openModal(modalId: string) {
    this.modalService.open(modalId);
  }

  // this method is not used
  closeModal(modalId: string) {
    this.modalService.close(modalId);
  }

  selectProjectFromModal(event) {
    console.log("Event data");
    console.log(event);
    // this.form.controls['projectName'].setValue(event.name);
    this.form.patchValue({ projectId: event.id, projectName: event.name });
    localStorage.setItem("selectedProjectId", event.id);
  }

  selectParentTaskFromModal(event) {
    console.log("Event data");
    console.log(event);
    this.form.patchValue({ parentTaskId: event.id, parentTaskName: event.name });
  }

  selectUserFromModal(event) {
    console.log("Event data");
    console.log(event);
    this.form.patchValue({ userId: event.id, userName: event.name });
  }

  addTask() {

    if(this.form.invalid) {
      return;
    }

    console.log(this.form.value);

    this.taskService.createTask(this.form.value).subscribe(
      () => {
        console.log("Task created successfully");
      }
    );

  }

}
