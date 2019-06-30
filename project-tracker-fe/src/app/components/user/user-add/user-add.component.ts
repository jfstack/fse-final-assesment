import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { UserService } from '../../../services/user.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { User } from '../../../models/user';
import { LogService } from '../../../services/log.service';

@Component({
  selector: 'user-add',
  templateUrl: './user-add.component.html',
  styleUrls: ['./user-add.component.css']
})
export class UserAddComponent implements OnInit, OnDestroy {

  form = new FormGroup({
    userId: new FormControl(-1),
    firstName: new FormControl('', Validators.required),
    lastName: new FormControl('', Validators.required),
    employeeId: new FormControl('', Validators.required)
  });

  submitted = false;
  enableUpdateButton = false;
  submitMessage: string;
  newUser: User;

  private userSubscription: Subscription;
  private loadOnEditSubjectSubscription: Subscription;
  private updateUserSubscription: Subscription;

  constructor(private userService: UserService,
      private logger: LogService) { }

  ngOnInit() {
    this.loadOnEditSubjectSubscription = 
      this.userService.loadOnEditSubjectCast.subscribe(
        data => {
          this.form.setValue({
            userId: data.userId,
            firstName: data.firstName, 
            lastName: data.lastName, 
            employeeId: data.employeeId
          });

          this.enableUpdateButton = true;
        }
      );
      
      //on initial component load
      this.enableUpdateButton = false;
  }

  addUser() {
    this.logger.debug(this.form.value);
    
    if(this.form.invalid) {
      return;
    }

    this.submitted = true;

    if(this.enableUpdateButton) {

      this.enableUpdateButton = false;

      this.updateUserSubscription = 
          this.userService.updateUser(this.form.value)
              .subscribe(
                data => {
                  this.logger.debug("Data updated successfully:", data);
                  this.userService.castRefreshEvent();
                  this.resetForm();
                },

                (error: HttpErrorResponse) => {
                  this.logger.error(error.name + ' ' + error.message);
                }

              );

    } else {
    
      this.userSubscription = 
          this.userService.createUser(this.form.value)
          .subscribe(
            data => {
              this.logger.debug("Data saved successfully:", data);
              this.newUser = data; 
              this.userService.cast(this.newUser);
              this.resetForm();
            },

            (error: HttpErrorResponse) => {this.logger.error(error.name + ' ' + error.message);}
          );

      }

  }

  resetForm() {
    this.form.reset(new User(0, '', '', 0));
  }

  ngOnDestroy() {
    if(this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
  }

}
