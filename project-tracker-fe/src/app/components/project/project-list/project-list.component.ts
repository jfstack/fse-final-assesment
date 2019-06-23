import { Component, OnInit } from '@angular/core';
import { ProjectDetails } from '../../../models/project-details';
import { ProjectService } from '../../../services/project.service';
import { Subscription } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css']
})
export class ProjectListComponent implements OnInit {

  projects: Array<ProjectDetails>;

  projectListSubscription: Subscription;

  constructor(private projectService: ProjectService) { 
    this.projects = [];
  }

  ngOnInit() {
  }

  refreshProjectList() {
    this.projectListSubscription = 
        this.projectService.getProjects().subscribe(
          data => {
            this.projects = data;
          },

          (error: HttpErrorResponse) => {
            console.log(error.name + ' ' + error.message);
          }
        );
  }

}
