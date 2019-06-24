import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ProjectForm } from '../models/project-form';
import { ProjectDetails } from '../models/project-details';
import { BehaviorSubject, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  private baseUrl = 'http://localhost:8081/api/projects';

  blankProjectDetail = {
    projectId: -1,
    project: '',
    startDate: '2019-06-24',
    endDate: '2019-06-25',
    priority: 1,
    manager: null,
    tasks: []
  };

  //This subject is used to refresh the list of projects on new project addition
  projectListSubject = new BehaviorSubject<ProjectDetails>(this.blankProjectDetail);
  projectListSubjectCast = this.projectListSubject.asObservable();

  constructor(private http: HttpClient) { }

  createProject(projectForm: ProjectForm) {
    console.log("ProjectService.createProject");
    console.log(projectForm);
    
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };
    
    return this.http.post<ProjectDetails>(this.baseUrl, projectForm);

  }

  getProjects() {
    console.log("ProjectService.getProjects");
    return this.http.get<ProjectDetails[]>(this.baseUrl);
  }

  castProjectOnCreate(projectDetails: ProjectDetails) {
    this.projectListSubject.next(projectDetails);
  }

  getDefaultStartAndEndDate(): Array<string> {
    let d = new Date();
    let day = d.getDate();
    let month = d.getMonth()+1;
    month = month < 10 ? '0'+month : month;
    let year = d.getFullYear();
    let today = year + '-' + month + '-' + day;

    d.setDate(d.getDate() + 1);
    day = d.getDate();
    month = d.getMonth() +1;
    month = month < 10 ? '0'+month : month;
    year = d.getFullYear();

    let tomorrow = year + '-' + month + '-' + day;

    return [today, tomorrow];

  }


}
