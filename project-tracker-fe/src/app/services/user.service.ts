import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { User } from '../models/user';
import { BehaviorSubject, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  
  private baseUrl = 'http://localhost:8081/api/users';

  userListSubject = new BehaviorSubject<User>(new User(0, '', '', 0));
  userListSubjectCast = this.userListSubject.asObservable();

  private _refreshEvent = new Subject<void>();

  constructor(private http: HttpClient) { }

  get refreshEvent() {
    return this._refreshEvent;
  }

  createUser(user: User) {
    console.log(user);
    
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };
    
    return this.http.post<User>(this.baseUrl, user);

  }

  getUsers() {
    return this.http.get<User[]>(this.baseUrl);
  }

  deleteUser(employeeId: number) {
    console.log("Employee ID to be deleted" + employeeId);
    return this.http.delete<void>(`${this.baseUrl}/${employeeId}`);
  }

  cast(newUser: User) {
    this.userListSubject.next(newUser);
  }

}
