import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ProjectService } from '../../../services/project.service';
import { Subscription } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { startDateEndDateValidator } from '../../../validators/date.validator';
import { ModalService } from 'src/app/services/modal.service';
import { LogService } from '../../../services/log.service';

@Component({
  selector: 'project-add',
  templateUrl: './project-add.component.html',
  styleUrls: ['./project-add.component.css']
})
export class ProjectAddComponent implements OnInit, OnDestroy {

  form = new FormGroup({
    projectId : new FormControl(-1),
    name : new FormControl('', Validators.required),
    startDate : new FormControl(this.projectService.getDefaultStartAndEndDate()[0], Validators.required),
    endDate : new FormControl(this.projectService.getDefaultStartAndEndDate()[1], Validators.required),
    priority : new FormControl(1, Validators.required),
    managerId : new FormControl(-1),
    managerName : new FormControl('')
  }, {validators: startDateEndDateValidator});

  marked = false;
  enableUpdateButton = false;

  createProjectSubscription: Subscription;
  updateProjectSubscription: Subscription;
  loadProjectOnEditSubscription: Subscription;

  constructor(private projectService: ProjectService, 
    private modalService: ModalService,
    private logger: LogService) { 
  }

  ngOnInit() {
    this.loadProjectOnEditSubscription = 
      this.projectService.loadProjectOnEditSubjectCast.subscribe(
        data => {
          this.logger.debug(`start date: ${data.startDate}`);
          this.logger.debug(`end date: ${data.endDate}`);
          this.form.setValue({
            projectId: data.projectId,
            name: data.project,
            startDate: data.startDate,
            endDate: data.endDate,
            priority: data.priority,
            managerId: (data.manager) ? data.manager.employeeId : -1,
            managerName: (data.manager) ? data.manager.firstName+', '+data.manager.lastName : ''
          });
          this.form.patchValue({
            startDate: data.startDate,
            endDate: data.endDate
          });

          this.enableUpdateButton = true;
        }
      );

    //on initial component load
    this.enableUpdateButton = false;
  }

  addProject() {
    this.logger.info("ProjectAddComponent.addProject");
    this.logger.debug(this.form.getRawValue());
    
    if(this.form.invalid) {
      return;
    }

    if(this.enableUpdateButton) {
      this.enableUpdateButton = false;

      this.updateProjectSubscription = 
          this.projectService.updateProject(this.form.getRawValue())
              .subscribe(
                  data => {
                    this.logger.debug("Data updated successfully:", data);
                    this.projectService.castRefreshProjectListEvent();
                    this.resetForm();
                  },

                  (error: HttpErrorResponse) => {
                    this.logger.error(error.name + ' ' + error.message);
                  }

              );
    } else {

      this.createProjectSubscription = 
          this.projectService.createProject(this.form.getRawValue()).subscribe(

            (data) => {
              this.logger.debug("Data saved successfully:");
              this.projectService.castProjectOnCreate(data);
              this.resetForm();
            },

            (error: HttpErrorResponse) => {this.logger.error(error.name + ' ' + error.message);}

          );
      }
  }

  resetForm() {
    this.form.reset({
      projectId: -1,
      name: '',
      startDate: this.projectService.getDefaultStartAndEndDate()[0],
      endDate: this.projectService.getDefaultStartAndEndDate()[1],
      priority: 1,
      managerId: -1,
      managerName: ''
    });
  }

  openModal(modalId: string) {
    this.modalService.open(modalId);
  }

  selectUserFromModal(event) {
    this.logger.debug("Event data", event);
    
    this.form.patchValue({ managerId: event.id, managerName: event.name });
  }

  ngOnDestroy() {
    if(this.createProjectSubscription) {
      this.createProjectSubscription.unsubscribe();
    }

    if(this.updateProjectSubscription) {
      this.updateProjectSubscription.unsubscribe();
    }

    if(this.loadProjectOnEditSubscription) {
      this.loadProjectOnEditSubscription.unsubscribe();
    }
  }

}
