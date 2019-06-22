import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { User } from '../models/user';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  
  private baseUrl = 'http://localhost:8081/api/users';

  userListSubject = new BehaviorSubject<User>(new User(0, '', '', 0));
  userListSubjectCast = this.userListSubject.asObservable();

  constructor(private http: HttpClient) { }

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

  cast(newUser: User) {
    this.userListSubject.next(newUser);
  }

}
