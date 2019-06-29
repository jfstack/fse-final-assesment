import { Component, OnInit, Input } from '@angular/core';
import { User } from '../../../../models/user';
import { UserService } from '../../../../services/user.service';
import { tap } from 'rxjs/operators';
import { HttpErrorResponse } from '@angular/common/http';
import { LogService } from '../../../../services/log.service';

@Component({
  selector: 'user-card',
  templateUrl: './user-card.component.html',
  styleUrls: ['./user-card.component.css']
})
export class UserCardComponent implements OnInit {

  @Input() user: User = new User(1,'chandan','ghosh',208066);

  constructor(private userService: UserService,
    private logger: LogService) { }

  ngOnInit() {
  }

  editUser(user: User) {
    this.logger.debug('OnEdit:', user);
    this.userService.castLoadOnEditSubject(user);
  }

  
  deleteUser(user: User) {
    this.logger.debug('OnDelete:', user);
    this.userService.deleteUser(user.employeeId)
      .subscribe(() => {
        this.logger.debug(`User with Id ${user.employeeId} is deleted...`)
          this.userService.castRefreshEvent();
          },
          (error: HttpErrorResponse) => {
            this.logger.error(error.name + ' ' + error.message);
          } 
        );
  }
}
