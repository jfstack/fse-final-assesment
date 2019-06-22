import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { UserService } from '../../../services/user.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { User } from '../../../models/user';

@Component({
  selector: 'user-add',
  templateUrl: './user-add.component.html',
  styleUrls: ['./user-add.component.css']
})
export class UserAddComponent implements OnInit, OnDestroy {

  form = new FormGroup({
    firstName: new FormControl('', Validators.required),
    lastName: new FormControl('', Validators.required),
    employeeId: new FormControl('', Validators.required)
  });

  submitted = false;
  submitMessage: string;
  newUser: User;

  private userSubscription: Subscription;

  constructor(private userService: UserService) { }

  ngOnInit() {
  }

  addUser() {
    console.log(this.form.value);
    
    if(this.form.invalid) {
      return;
    }

    this.submitted = true;

    this.userSubscription = 
        this.userService.createUser(this.form.value)
        .subscribe(
          data => {
            console.log("Data saved successfully:" + data);
            // this.newUser = data; //not required
            this.userService.cast(this.newUser);
            this.form.reset(new User(0, '', '', 0));
          },

          (error: HttpErrorResponse) => {console.log(error.name + ' ' + error.message);}
        );

  }

  ngOnDestroy() {
    if(this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
  }

}
