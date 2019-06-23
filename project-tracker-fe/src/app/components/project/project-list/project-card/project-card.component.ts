import { Component, OnInit, Input } from '@angular/core';
import { ProjectDetails } from '../../../../models/project-details';

@Component({
  selector: 'project-card',
  templateUrl: './project-card.component.html',
  styleUrls: ['./project-card.component.css']
})
export class ProjectCardComponent implements OnInit {

  @Input() project: ProjectDetails;

  constructor() { }

  ngOnInit() {
  }

}
