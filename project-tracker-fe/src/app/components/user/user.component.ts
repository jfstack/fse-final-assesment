import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'pm-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  title = "User";

  constructor() { }

  ngOnInit() {
  }

}
