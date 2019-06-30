import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ProjectForm } from '../models/project-form';
import { ProjectDetails } from '../models/project-details';
import { BehaviorSubject, Subject } from 'rxjs';
import { LogService } from './log.service';
import { AppConfig } from '../../environments/environment';


@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  private baseUrl = AppConfig.apiBaseUrl_Projects;

  blankProjectDetail = {
    projectId: -1,
    project: '',
    startDate: this.getDefaultStartAndEndDate()[0],
    endDate: this.getDefaultStartAndEndDate()[1],
    priority: 0,
    manager: null,
    tasks: [],
    status: 'OPEN'
  };

  //This subject is used to refresh the list of projects on new project addition
  projectListSubject = new BehaviorSubject<ProjectDetails>(this.blankProjectDetail);
  projectListSubjectCast = this.projectListSubject.asObservable();

  loadProjectOnEditSubject = new BehaviorSubject<ProjectDetails>(this.blankProjectDetail);
  loadProjectOnEditSubjectCast = this.loadProjectOnEditSubject.asObservable();

  //This subject is used to refresh the list of users on deletion of an user
  refreshProjectListEvent = new BehaviorSubject<void>(null);
  refreshProjectListEventCast = this.refreshProjectListEvent.asObservable();

  constructor(private http: HttpClient, private logger: LogService) { }

  createProject(projectForm: ProjectForm) {
    this.logger.debug("ProjectService.createProject", projectForm);
    
    
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };
    
    return this.http.post<ProjectDetails>(this.baseUrl, projectForm);

  }

  updateProject(projectForm: ProjectForm) {
    this.logger.debug("ProjectService.updateProject", projectForm);
      
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

    return this.http.put<ProjectDetails>(`${this.baseUrl}/${projectForm.projectId}`, projectForm);
  }

  getProjects() {
    this.logger.info("ProjectService.getProjects");
    return this.http.get<ProjectDetails[]>(this.baseUrl);
  }

  castProjectOnCreate(projectDetails: ProjectDetails) {
    this.projectListSubject.next(projectDetails);
  }

  castLoadProjectOnEditSubject(project: ProjectDetails) {
    this.loadProjectOnEditSubject.next(project);
  }

  castRefreshProjectListEvent() {
    this.refreshProjectListEvent.next(null);
  }

  getDefaultStartAndEndDate(): Array<string> {
    let d = new Date();
    let day = d.getDate();
    let dayStr = day < 10 ? '0'+day : ''+day;
    let month = d.getMonth()+1;
    let monthStr = month < 10 ? '0'+month : ''+month;
    let year = d.getFullYear();
    let today = year + '-' + monthStr + '-' + dayStr;

    d.setDate(d.getDate() + 1);
    day = d.getDate();
    dayStr = day < 10 ? '0'+day : ''+day;
    month = d.getMonth() +1;
    monthStr = month < 10 ? '0'+month : ''+month;
    year = d.getFullYear();

    let tomorrow = year + '-' + monthStr + '-' + dayStr;

    return [today, tomorrow];

  }


}
