import { Component, OnInit } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { User } from '../../../models/user';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  users: Array<User>;

  constructor(private userService: UserService) { 
    this.users = [];
  }

  ngOnInit() {
    
    this.userService.refreshEvent.subscribe(
      () => {
        this.refreshUserList();
      }
    );

    //during initial bootstrap
    this.refreshUserList();

      this.userService.userListSubjectCast.subscribe(
        user => {
          this.users.push(user);
        }
      );
  }

  refreshUserList() {
    this.userService.getUsers()
    .subscribe(
      data =>{
        this.users = data;
      },

      (error: HttpErrorResponse) => {
        console.log(error.name + ' ' + error.message);
      }
    );
  }

}
