import { Component, OnInit } from '@angular/core';
import { ModalService } from 'src/app/services/modal.service';
import { TaskService } from 'src/app/services/task.service';
import { FormGroup, FormControl } from '@angular/forms';
import { TaskDetails } from 'src/app/models/task-details';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-view-task',
  templateUrl: './view-task.component.html',
  styleUrls: ['./view-task.component.css']
})
export class ViewTaskComponent implements OnInit {

  form = new FormGroup({
    projectId: new FormControl(-1),
    projectName: new FormControl('')
  });

  tasks: TaskDetails[];

  constructor(private modalService: ModalService, private taskService: TaskService) { }

  ngOnInit() {
    this.tasks = [];
  }

  openModal(modalId: string) {
    this.modalService.open(modalId);
  }

  selectProjectFromModal(event) {
    console.log("Event data");
    console.log(event);
    // this.form.controls['projectName'].setValue(event.name);
    this.form.patchValue({ projectId: event.id, projectName: event.name });
    
    this.taskService.getTasks(event.id).subscribe(
      (data: TaskDetails[]) => {
        this.tasks = data.map(
          (td: TaskDetails) => {
            td.projectId = event.id;
            td.projectName = event.name;
            return td;
          }
        );
      },

      (error: HttpErrorResponse) => {
        console.log(error.name + ' ' + error.message);
      }
    );

  }

}
