import { Component, OnInit, Input } from '@angular/core';
import { User } from '../../../../models/user';
import { UserService } from '../../../../services/user.service';
import { tap } from 'rxjs/operators';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'user-card',
  templateUrl: './user-card.component.html',
  styleUrls: ['./user-card.component.css']
})
export class UserCardComponent implements OnInit {

  @Input() user: User = new User(1,'chandan','ghosh',208066);

  constructor(private userService: UserService) { }

  ngOnInit() {
  }

  editUser(user: User) {
    console.log('OnEdit:' + user);
  }

  
  deleteUser(user: User) {
    console.log('OnDelete:' + user);
    this.userService.deleteUser(user.employeeId)
      .subscribe(data => {console.log(data)},
      (error: HttpErrorResponse) => {
        console.log(error.name + ' ' + error.message);
      } );
      /*.pipe(
        tap( () => {
          this.userService.refreshEvent.next();
        }

        )
      );*/
  }
}
