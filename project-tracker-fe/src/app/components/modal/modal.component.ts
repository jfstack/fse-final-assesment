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
import { LogService } from '../../services/log.service';

@Component({
  selector: 'jw-modal',
  template:
    `<div class="jw-modal">
      <div class="jw-modal-body">
        <h3 class="text-center">{{title}}</h3>
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
        <div class="row text-center">
          <button type="button" class="btn btn-sm btn-primary" style="margin-right: 5px;" (click)="close()">Close</button>
          <button type="button" class="btn btn-sm btn-success" (click)="choose(); close()">Choose</button>
        </div>
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
    private taskService: TaskService, private logger: LogService) {

      this.element = el.nativeElement;
      this.records = [];
  }

  ngOnInit() {
    this.logger.debug(`modal id: ${this.id}`);
    this.logger.debug(`modal type: ${this.type}`);
    

    let modal = this;

    // ensure id attribute exists
    if (!this.id) {
      this.logger.error('modal must have an id');
      return;
    }

    // ensure title attribute exists
    if (!this.type) {
      this.logger.error('modal must have a type');
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
      this.logger.debug("Loading projects....");

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
          this.logger.error(error.name + ' ' + error.message);
        }

      );

    }

    if(this.type === 'ParentTasks') {
      this.logger.debug("Loading parent tasks....");

      let selectedProjectId = localStorage.getItem('selectedProjectId');
      this.logger.debug("Selected project id:" + selectedProjectId);

      if(selectedProjectId) {
        this.taskService.getParentTasks(selectedProjectId).subscribe(
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
            this.logger.error(error.name + ' ' + error.message);
          }
        );

      }

    }

    if(this.type === 'Users') {
      this.logger.debug("Loading users....");

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
          this.logger.error(error.name + ' ' + error.message);
        }
      );
    }
  }

  select(record: ModalRecord) {
    this.logger.debug("Selected record: ", record);
    this.selectedRecord = record;
  }

  choose() {
    this.logger.debug("Emitting the selected record: ", this.selectedRecord);
    
    this.onSelect.emit(this.selectedRecord);
    this.records = [];
  }

}
