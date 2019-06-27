import { Component, OnInit, Input } from '@angular/core';
import { TaskDetails } from 'src/app/models/task-details';
import { Router } from '@angular/router';

@Component({
  selector: 'task-card',
  templateUrl: './task-card.component.html',
  styleUrls: ['./task-card.component.css']
})
export class TaskCardComponent implements OnInit {

  @Input() task: TaskDetails;

  constructor(private router: Router) { }

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

  }

}
