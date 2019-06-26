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
    this.router.navigate(['task']);
  }

  endTask(task: TaskDetails) {

  }

}
