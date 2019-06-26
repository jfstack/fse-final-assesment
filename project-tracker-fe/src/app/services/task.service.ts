import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TaskForm } from '../models/task-form';
import { ParentTask } from '../models/parent-task';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private baseUrl = 'http://localhost:8081/api/projects';

  constructor(private http: HttpClient) { }

  createTask(taskForm: TaskForm) {
    console.log("TaskService.createTask");
    console.log(taskForm);
    
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };
    
    return this.http.post<void>(`${this.baseUrl}/${taskForm.projectId}/tasks`, taskForm);

  }

  getTasks() {

  }

  getParentTasks(projectId) {
    return this.http.get<ParentTask[]>(`${this.baseUrl}/${projectId}/tasks/parents`);
  }

}
