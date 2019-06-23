import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { User } from '../models/user';
import { BehaviorSubject, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  
  private baseUrl = 'http://localhost:8081/api/users';

  //This subject is used to refresh the list of users on new user addition
  userListSubject = new BehaviorSubject<User>(new User(0, '', '', 0));
  userListSubjectCast = this.userListSubject.asObservable();

  //This subject is used to refresh the list of users on deletion of an user
  refreshOnDeleteEvent = new BehaviorSubject<void>(null);
  refreshOnDeleteEventCast = this.refreshOnDeleteEvent.asObservable();

  loadOnEditSubject = new BehaviorSubject<User>(new User(0, '', '', 0));
  loadOnEditSubjectCast = this.loadOnEditSubject.asObservable();

  constructor(private http: HttpClient) { }


  createUser(user: User) {
    console.log("UserService.createUser");
    console.log(user);
    
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };
    
    return this.http.post<User>(this.baseUrl, user);

  }

  updateUser(user: User) {
    console.log("UserService.updateUser");
    
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

    return this.http.put<User>(`${this.baseUrl}/${user.employeeId}`, user);

  }

  getUsers() {
    console.log("UserService.getUsers");
    return this.http.get<User[]>(this.baseUrl);
  }

  deleteUser(employeeId: number) {
    console.log("UserService.deleteUser");
    console.log("Employee ID to be deleted" + employeeId);
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
