import { Component, OnInit, OnDestroy } from '@angular/core';
import { ProjectDetails } from '../../../models/project-details';
import { ProjectService } from '../../../services/project.service';
import { Subscription } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css']
})
export class ProjectListComponent implements OnInit, OnDestroy {

  projects: Array<ProjectDetails>;

  projectListSubscription: Subscription;

  constructor(private projectService: ProjectService) { 
    this.projects = [];
  }

  ngOnInit() {

    this.projectService.projectListSubjectCast.subscribe(
      project => {
        this.projects.push(project);
      }
    );
    this.refreshProjectList();

  }

  refreshProjectList() {
    this.projectListSubscription = 
        this.projectService.getProjects().subscribe(
          data => {
            console.log("Project data: " + data);
            this.projects = data;
          },

          (error: HttpErrorResponse) => {
            console.log(error.name + ' ' + error.message);
          }
        );
  }


  ngOnDestroy() {
    this.projectListSubscription.unsubscribe();
  }
}
