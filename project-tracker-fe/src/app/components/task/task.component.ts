import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ProjectService } from 'src/app/services/project.service';
import { TaskService } from 'src/app/services/task.service';
import { startDateEndDateValidator } from '../../validators/date.validator';
import { ModalService } from '../../services/modal.service';
import { IStatus } from '../../models/status';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { LogService } from '../../services/log.service';


@Component({
  selector: 'app-task',
  templateUrl: './task.component.html',
  styleUrls: ['./task.component.css']
})
export class TaskComponent implements OnInit {

  form = new FormGroup({
    taskId : new FormControl(-1),
    projectId : new FormControl(-1),
    projectName: new FormControl('', Validators.required),
    name : new FormControl('', Validators.required),
    parentType : new FormControl(false),
    priority : new FormControl(0),
    startDate : new FormControl(this.projectService.getDefaultStartAndEndDate()[0], Validators.required),
    endDate : new FormControl(this.projectService.getDefaultStartAndEndDate()[1], Validators.required),
    parentTaskId: new FormControl(-1),
    parentTaskName: new FormControl(''),
    userId : new FormControl(-1),
    userName: new FormControl('', Validators.required)
  }, {validators: startDateEndDateValidator});

  // selectedProject: string;
  // selectedParentTask: string;
  // selectedUser: string;

  status: IStatus;
  title = "Task";

  navigationExtrasState;

  constructor(
    private projectService: ProjectService,
    private modalService: ModalService,
    private taskService: TaskService,
    private router: Router,
    private logger: LogService) { 

      this.logger.debug("Navigation extras:", router.getCurrentNavigation().extras.state);
      this.navigationExtrasState = router.getCurrentNavigation().extras.state;
    }

  ngOnInit() {
    this.form.get('parentType').valueChanges.subscribe(
      value => {
        this.logger.debug("Value = " + value);
        if(value === true) {
          this.form.get('priority').disable();
          this.form.get('startDate').disable();
          this.form.get('endDate').disable();
          this.form.get('parentTaskName').disable();
          this.form.get('userName').disable();
        }

        else {
          this.form.get('priority').enable();
          this.form.get('startDate').enable();
          this.form.get('endDate').enable();
          this.form.get('parentTaskName').enable();
          this.form.get('userName').enable();
        }
      }
    );

    //on edit, fill up form using navigation extras state
    if(this.navigationExtrasState) {

      this.form.setValue({
        projectId: this.navigationExtrasState.projectId, 
        projectName: this.navigationExtrasState.projectName,
        taskId: this.navigationExtrasState.taskId,
        name: this.navigationExtrasState.name,
        parentType: this.navigationExtrasState.parentType,
        priority: this.navigationExtrasState.priority,
        parentTaskId: this.navigationExtrasState.parentTaskId,
        parentTaskName: this.navigationExtrasState.parentTaskName,
        startDate: this.navigationExtrasState.startDate,
        endDate: this.navigationExtrasState.endDate,
        userId: this.navigationExtrasState.userId,
        userName: this.navigationExtrasState.userName
      });
      //patch is required for fields which may be disabled
      this.form.patchValue({
        startDate: this.navigationExtrasState.startDate,
        endDate: this.navigationExtrasState.endDate
      });
    }


  }

  openModal(modalId: string) {
    this.modalService.open(modalId);
  }

  // this method is not used
  closeModal(modalId: string) {
    this.modalService.close(modalId);
  }

  selectProjectFromModal(event) {
    this.logger.debug("Event data: ", event);
    
    this.form.patchValue({ projectId: event.id, projectName: event.name });
    localStorage.setItem("selectedProjectId", event.id);
  }

  selectParentTaskFromModal(event) {
    this.logger.debug("Event data: ", event);
    
    this.form.patchValue({ parentTaskId: event.id, parentTaskName: event.name });
  }

  selectUserFromModal(event) {
    this.logger.debug("Event data: ", event);
    
    this.form.patchValue({ userId: event.id, userName: event.name });
  }

  addTask() {

    if(this.form.invalid) {
      return;
    }

    this.logger.debug(this.form.value);

    if(this.form.controls.taskId.value > 0) { //update

      this.taskService.updateTask(this.form.value).subscribe(
        
        () => {
          this.logger.debug("Task update successfully");
          this.resetForm();
          this.status = { success: true, msg: "Task updated successfully"};
        },

        (error: HttpErrorResponse) => {
          this.logger.error(error.name + ' ' + error.message);
          this.status = { success: false, msg: "Something went wrong..."};
        }

      );

    }

    else {  //create

      this.taskService.createTask(this.form.value).subscribe(
        () => {
          this.logger.debug("Task created successfully");
          this.resetForm();
          this.status = { success: true, msg: "Task created successfully"};
        },

        (error: HttpErrorResponse) => {
          this.logger.error(error.name + ' ' + error.message);
          this.status = { success: false, msg: "Something went wrong..."};
        }

      );

  }

  }

  resetForm() {
    this.form.reset({
      taskId: -1,
      projectId: -1,
      projectName: '',
      name: '',
      parentType: false,
      priority: 0,
      startDate: this.projectService.getDefaultStartAndEndDate()[0],
      endDate: this.projectService.getDefaultStartAndEndDate()[1],
      parentTaskId: -1,
      parentTaskName: '',
      userId: -1,
      userName: ''
    });

    localStorage.removeItem("selectedProjectId");
  }

}
