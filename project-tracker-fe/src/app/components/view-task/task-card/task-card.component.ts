import { Component, OnInit, Input } from '@angular/core';
import { TaskDetails } from 'src/app/models/task-details';
import { Router } from '@angular/router';
import { TaskForm } from 'src/app/models/task-form';
import { TaskService } from 'src/app/services/task.service';
import { HttpErrorResponse } from '@angular/common/http';
import { LogService } from '../../../services/log.service';

@Component({
  selector: 'task-card',
  templateUrl: './task-card.component.html',
  styleUrls: ['./task-card.component.css']
})
export class TaskCardComponent implements OnInit {

  @Input() task: TaskDetails;

  constructor(private router: Router, 
    private taskService: TaskService, 
    private logger: LogService) { }

  ngOnInit() {
  }

  editTask(task: TaskDetails) {
    let extras = { 
      state: {
        projectId: task.projectId, 
        projectName: task.projectName,
        taskId: task.taskId,
        name: task.task,
        parentType: false,
        priority: task.priority,
        parentTaskId: task.parentTask ? task.parentTask.parentId : -1,
        parentTaskName: task.parentTask ? task.parentTask.parentTask : '',
        startDate: task.startDate,
        endDate: task.endDate,
        userId: task.owner.employeeId,
        userName: task.owner.firstName +', '+ task.owner.lastName
      } 
    };
    this.router.navigate(['task'], extras);
  }

  endTask(task: TaskDetails) {
    
    let taskForm = {
      projectId: task.projectId,
      taskId: task.taskId,
      name: task.task,
      parentType: 'false',
      priority: task.priority,
      parentTaskId: task.parentTask ? task.parentTask.parentId : null,
      startDate: task.startDate,
      endDate: task.endDate,
      userId: task.owner.employeeId,
      status: 'COMPLETED'
    };

    this.taskService.updateTask(taskForm).subscribe(
      () => {
        this.logger.debug("Task update successfully");
        task.status = 'COMPLETED';
      },

      (error: HttpErrorResponse) => {
        this.logger.error(error.name + ' ' + error.message);
      }
    );

  }

}
