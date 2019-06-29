import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TaskForm } from '../models/task-form';
import { ParentTask } from '../models/parent-task';
import { TaskDetails } from 'src/app/models/task-details';
import { LogService } from './log.service';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private baseUrl = 'http://localhost:8081/api/projects';

  constructor(private http: HttpClient, private logger: LogService) { }

  createTask(taskForm: TaskForm) {
    this.logger.debug("TaskService.createTask", taskForm);
      
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };
    
    return this.http.post<void>(`${this.baseUrl}/${taskForm.projectId}/tasks`, taskForm);

  }

  getTasks(projectId) {
    return this.http.get<TaskDetails[]>(`${this.baseUrl}/${projectId}/tasks`);
  }

  getParentTasks(projectId) {
    return this.http.get<ParentTask[]>(`${this.baseUrl}/${projectId}/tasks/parents`);
  }
  
  updateTask(taskForm: TaskForm) {
    this.logger.debug("TaskService.updateTask", taskForm);
      
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };
    
    return this.http.put<void>(`${this.baseUrl}/${taskForm.projectId}/tasks/${taskForm.taskId}`, taskForm);

  }

}
