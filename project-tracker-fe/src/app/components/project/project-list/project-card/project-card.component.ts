import { Component, OnInit, Input } from '@angular/core';
import { ProjectDetails } from '../../../../models/project-details';
import { ProjectService } from 'src/app/services/project.service';
import { LogService } from '../../../../services/log.service';
import { ProjectForm } from '../../../../models/project-form';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'project-card',
  templateUrl: './project-card.component.html',
  styleUrls: ['./project-card.component.css']
})
export class ProjectCardComponent implements OnInit {

  @Input() project: ProjectDetails;

  constructor(private projectService: ProjectService,
    private logger: LogService) { }

  ngOnInit() {
  }

  editProject(project: ProjectDetails) {
    this.logger.debug("onEditProject:", project);
    this.projectService.castLoadProjectOnEditSubject(project);
  }

  suspendProject(project: ProjectDetails) {
    this.logger.debug("onSuspendProject:", project);

    let projectForm = new ProjectForm();
    projectForm.projectId = project.projectId;
    projectForm.name = project.project;
    projectForm.startDate = project.startDate;
    projectForm.endDate = project.endDate;
    projectForm.priority = project.priority;
    projectForm.managerId = (project.manager) ? project.manager.employeeId : -1;
    projectForm.status = 'SUSPEND';

    this.projectService.updateProject(projectForm).subscribe(
      (data) => {
        this.logger.debug("Data updated successfully:", data);
        this.projectService.castRefreshProjectListEvent();
      },
      (error: HttpErrorResponse) => {
        this.logger.error(error.name + ' ' + error.message);
      }
    );
  }

}
