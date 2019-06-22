import { Component, OnInit, Input } from '@angular/core';
import { User } from '../../../../models/user';
import { UserService } from '../../../../services/user.service';

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
  }
}
