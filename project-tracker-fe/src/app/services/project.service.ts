import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ProjectForm } from '../models/project-form';
import { ProjectDetails } from '../models/project-details';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  private baseUrl = 'http://localhost:8081/api/projects';

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




}
