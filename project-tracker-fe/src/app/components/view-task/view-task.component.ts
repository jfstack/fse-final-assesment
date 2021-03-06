import { Component, OnInit } from '@angular/core';
import { ModalService } from 'src/app/services/modal.service';
import { TaskService } from 'src/app/services/task.service';
import { FormGroup, FormControl } from '@angular/forms';
import { TaskDetails } from 'src/app/models/task-details';
import { HttpErrorResponse } from '@angular/common/http';
import { LogService } from '../../services/log.service';

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

  constructor(private modalService: ModalService, 
    private taskService: TaskService,
    private logger: LogService) { }

  ngOnInit() {
    this.tasks = [];
  }

  openModal(modalId: string) {
    this.modalService.open(modalId);
  }

  selectProjectFromModal(event) {
    this.logger.debug("Event data: ", event);
    
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
        this.logger.error(error.name + ' ' + error.message);
      }
    );

  }


  sortTasksBy(sortKey: string) {
    if(sortKey) {

      if(sortKey === 'startdate') {
        if(this.tasks) {
          this.tasks.sort(
            (t1, t2) => {
              return t1.startDate < t2.startDate ? -1 : t1.startDate > t2.startDate ? 1 : 0;
            }
          );
        }
      }

      if(sortKey === 'enddate') {
        if(this.tasks) {
          this.tasks.sort(
            (t1, t2) => {
              return t1.endDate < t2.endDate ? -1 : t1.endDate > t2.endDate ? 1 : 0;
            }
          );
        }
        
      }

      if(sortKey === 'priority') {
        if(this.tasks) {
          this.tasks.sort(
            (t1, t2) => {
              return t1.priority < t2.priority ? -1 : t1.priority > t2.priority ? 1 : 0;
            }
          );
        }
        
      }

      if(sortKey === 'status') {
        if(this.tasks) {
          this.tasks.sort(
            (t1, t2) => {
              return t1.status < t2.status ? -1 : t1.status > t2.status ? 1 : 0;
            }
          );
        }
        
      }

    }
  }


}
