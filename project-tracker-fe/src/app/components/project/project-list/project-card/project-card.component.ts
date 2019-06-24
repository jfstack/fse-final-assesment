import { Component, OnInit, Input } from '@angular/core';
import { ProjectDetails } from '../../../../models/project-details';
import { ProjectService } from 'src/app/services/project.service';

@Component({
  selector: 'project-card',
  templateUrl: './project-card.component.html',
  styleUrls: ['./project-card.component.css']
})
export class ProjectCardComponent implements OnInit {

  @Input() project: ProjectDetails;

  constructor(private projectService: ProjectService) { }

  ngOnInit() {
  }

  editProject(project: ProjectDetails) {
    console.log("onEditProject:" + project);
    this.projectService.castLoadProjectOnEditSubject(project);
  }

  suspendProject(project: ProjectDetails) {
    console.log("onSuspendProject:" + project);
  }

}
