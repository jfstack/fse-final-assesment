import { Component, OnInit, OnDestroy, Input, ElementRef, Output, EventEmitter } from '@angular/core';
import { ModalService } from '../../services/modal.service';
import { ModalRecord } from '../../models/modal-record';
import { ProjectService } from 'src/app/services/project.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ProjectDetails } from 'src/app/models/project-details';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user';
import { TaskService } from '../../services/task.service';
import { ParentTask } from '../../models/parent-task';

@Component({
  selector: 'jw-modal',
  template:
    `<div class="jw-modal">
      <div class="jw-modal-body">
        <h3>{{title}}</h3>
        <div class="container">
          <button type="button" class="btn btn-sm btn-primary" (click)="load()">Load {{type}}</button>
          <table class="table">
            <thead>
              <tr>
                <th>Select</th>
                <th>Id</th>
                <th>Name</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let record of records">
                <td><input type="radio" name="group" (click)="select(record)"/></td>
                <td>{{record.id}}</td>
                <td>{{record.name}}</td>
              </tr>
            </tbody>
          </table>
        </div>
        <button type="button" class="btn btn-sm btn-primary" (click)="close()">Close</button>
        <button type="button" class="btn btn-sm btn-success" (click)="choose(); close()">Choose</button>
      </div>
    </div>
    <div class="jw-modal-background"></div>`,
  styleUrls: ['./modal.component.css']
})
export class ModalComponent implements OnInit, OnDestroy {

  @Input() id: string;
  @Input() type: string;
  @Input() title: string;

  @Output() onSelect = new EventEmitter<ModalRecord>();

  records: ModalRecord[];
  selectedRecord: ModalRecord;


  private element: any;

  constructor(private modalService: ModalService, 
    private el: ElementRef,
    private projectService: ProjectService,
    private userService: UserService,
    private taskService: TaskService) {

      this.element = el.nativeElement;
      this.records = [{id: 1, name: 'record1'}, {id: 2, name: 'record2'}]
  }

  ngOnInit() {
    console.log(`modal id: ${this.id}`);
    console.log(`modal type: ${this.type}`);
    

    let modal = this;

    // ensure id attribute exists
    if (!this.id) {
      console.error('modal must have an id');
      return;
    }

    // ensure title attribute exists
    if (!this.type) {
      console.error('modal must have a type');
      return;
    }

    // move element to bottom of page (just before </body>) so it can be displayed above everything else
    document.body.appendChild(this.element);

    // add self (this modal instance) to the modal service so it's accessible from controllers
    this.modalService.add(this);

    this.close();

  }

  ngOnDestroy() {
    this.modalService.remove(this.id);
    this.element.remove();
  }

  // open modal
  open(): void {
    this.element.style.display = 'block';
    document.body.classList.add('jw-modal-open');
  }

  // close modal
  close(): void {
    this.element.style.display = 'none';
    document.body.classList.remove('jw-modal-open');
  }

  load() {
    if(this.type === 'Projects') {
      console.log("Loading projects....");

      this.projectService.getProjects().subscribe(
        (data: ProjectDetails[]) => {
          if(data) {
            this.records = data.map(
              (pd: ProjectDetails) => {
                return new ModalRecord(pd.projectId, pd.project);
              }
            );
          }
        },

        (error: HttpErrorResponse) => {
          console.log(error.name + ' ' + error.message);
        }

      );

    }

    if(this.type === 'ParentTasks') {
      console.log("Loading parent tasks....");
      console.log("Selected project id:" + localStorage.getItem('selectedProjectId'));

      this.taskService.getParentTasks(localStorage.getItem('selectedProjectId')).subscribe(
        (data: ParentTask[]) => {
          if(data) {
            this.records = data.map(
              (pt: ParentTask) => {
                return new ModalRecord(pt.parentId, pt.parentTask);
              }
            );
          }
        },

        (error: HttpErrorResponse) => {
          console.log(error.name + ' ' + error.message);
        }
      );

    }

    if(this.type === 'Users') {
      console.log("Loading users....");
      this.userService.getUsers().subscribe(
        (data: User[]) => {
          if(data) {
            this.records = data.map(
              (u: User) => {
                return new ModalRecord(u.employeeId, u.firstName+', '+u.lastName);
              }
            );
          }
        },

        (error: HttpErrorResponse) => {
          console.log(error.name + ' ' + error.message);
        }
      );
    }
  }

  select(record: ModalRecord) {
    console.log(record);
    this.selectedRecord = record;
  }

  choose() {
    console.log("selected record:");
    console.log(this.selectedRecord);
    this.onSelect.emit(this.selectedRecord);
  }

}
