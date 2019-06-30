import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { User } from '../models/user';
import { BehaviorSubject, Subject } from 'rxjs';
import { LogService } from './log.service';
import { AppConfig } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  
  private baseUrl = AppConfig.apiBaseUrl_Users;

  blankUser = {
    userId: -1,
    firstName: '',
    lastName: '',
    employeeId: 0
  };

  //This subject is used to refresh the list of users on new user addition
  userListSubject = new BehaviorSubject<User>(this.blankUser);
  userListSubjectCast = this.userListSubject.asObservable();

  //This subject is used to refresh the list of users on deletion of an user
  refreshOnDeleteEvent = new BehaviorSubject<void>(null);
  refreshOnDeleteEventCast = this.refreshOnDeleteEvent.asObservable();

  loadOnEditSubject = new BehaviorSubject<User>(this.blankUser);
  loadOnEditSubjectCast = this.loadOnEditSubject.asObservable();

  constructor(private http: HttpClient, private logger: LogService) { }


  createUser(user: User) {
    this.logger.debug("UserService.createUser", user);
      
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };
    
    return this.http.post<User>(this.baseUrl, user);

  }

  updateUser(user: User) {
    this.logger.info("UserService.updateUser");
    
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

    return this.http.put<User>(`${this.baseUrl}/${user.userId}`, user);

  }

  getUsers() {
    this.logger.info("UserService.getUsers");
    return this.http.get<User[]>(this.baseUrl);
  }

  deleteUser(employeeId: number) {
    this.logger.info("UserService.deleteUser");
    this.logger.debug("Employee ID to be deleted" + employeeId);
    return this.http.delete<void>(`${this.baseUrl}/${employeeId}`);
  }

  cast(newUser: User) {
    this.userListSubject.next(newUser);
  }

  castRefreshEvent() {
    this.refreshOnDeleteEvent.next(null);
  }

  castLoadOnEditSubject(editUser: User) {
    this.loadOnEditSubject.next(editUser);
  }

}
