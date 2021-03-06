import { Component, OnInit, OnDestroy } from '@angular/core';
import { ProjectDetails } from '../../../models/project-details';
import { ProjectService } from '../../../services/project.service';
import { Subscription } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { LogService } from '../../../services/log.service';

@Component({
  selector: 'project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css']
})
export class ProjectListComponent implements OnInit, OnDestroy {

  projects: Array<ProjectDetails>;

  projectListSubscription: Subscription;

  searchTerm: string;

  constructor(private projectService: ProjectService,
    private logger: LogService) { 
    this.projects = [];
  }

  ngOnInit() {

    this.projectService.refreshProjectListEventCast.subscribe(
      () => {
        this.refreshProjectList();
      }
    );

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
            this.logger.debug("Project data: ", data);
            this.projects = data;
          },

          (error: HttpErrorResponse) => {
            this.logger.error(error.name + ' ' + error.message);
          }
        );
  }

  sortProjectBy(sortKey: string) {
    if(sortKey) {

      if(sortKey === 'startdate') {
        if(this.projects) {
          this.projects.sort(
            (p1, p2) => {
              return p1.startDate < p2.startDate ? -1 : p1.startDate > p2.startDate ? 1 : 0;
            }
          );
        }
      }

      if(sortKey === 'enddate') {
        if(this.projects) {
          this.projects.sort(
            (p1, p2) => {
              return p1.endDate < p2.endDate ? -1 : p1.endDate > p2.endDate ? 1 : 0;
            }
          );
        }
        
      }

      if(sortKey === 'priority') {
        if(this.projects) {
          this.projects.sort(
            (p1, p2) => {
              return p1.priority < p2.priority ? -1 : p1.priority > p2.priority ? 1 : 0;
            }
          );
        }
        
      }

      if(sortKey === 'status') {
        if(this.projects) {
          this.projects.sort(
            (p1, p2) => {
              return p1.status < p2.status ? -1 : p1.status > p2.status ? 1 : 0;
            }
          );
        }
        
      }

    }
  }

  ngOnDestroy() {
    this.projectListSubscription.unsubscribe();
  }
}
