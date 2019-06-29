import { Component, OnInit } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { User } from '../../../models/user';
import { HttpErrorResponse } from '@angular/common/http';
import { LogService } from '../../../services/log.service';

@Component({
  selector: 'user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  users: Array<User>;
  searchTerm: string;

  constructor(private userService: UserService,
    private logger: LogService) { 
    this.users = [];
  }

  ngOnInit() {
    
    this.userService.refreshOnDeleteEventCast.subscribe(
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
        this.logger.error(error.name + ' ' + error.message);
      }
    );
  }

  sortUsersBy(sortKey: string) {
    if(sortKey) {

      if(sortKey === 'fname') {
        this.logger.debug("Sorting users using first name...");

        if(this.users) {
          this.users.sort(
            (user1, user2) => {
              return (user1.firstName < user2.firstName) ? -1 : (user1.firstName > user2.firstName) ? 1 : 0;
            }
          );
        }

      }

      if(sortKey === 'lname') {
        this.logger.debug("Sorting users using last name...");

        if(this.users) {
          this.users.sort(
            (user1, user2) => {
              return (user1.lastName < user2.lastName) ? -1 : (user1.lastName > user2.lastName) ? 1 : 0;
            }
          );
        }

      }

      if(sortKey === 'empid') {
        this.logger.debug("Sorting users using employee id...");

        if(this.users) {
          this.users.sort(
            (user1, user2) => {
              return (user1.employeeId < user2.employeeId) ? -1 : (user1.employeeId > user2.employeeId) ? 1 : 0;
            }
          );
        }

      }

    }
  }

}
