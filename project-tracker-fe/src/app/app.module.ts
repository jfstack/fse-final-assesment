import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { UserComponent } from './components/user/user.component';
import { UserAddComponent } from './components/user/user-add/user-add.component';
import { UserEditComponent } from './components/user/user-edit/user-edit.component';
import { UserListComponent } from './components/user/user-list/user-list.component';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { ProjectComponent } from './components/project/project.component';
import { TaskComponent } from './components/task/task.component';
import { ViewTaskComponent } from './components/view-task/view-task.component';
import { PageNotFoundComponent } from './page-not-found.component';
import { UserCardComponent } from './components/user/user-list/user-card/user-card.component';
import { ProjectAddComponent } from './components/project/project-add/project-add.component';
import { ProjectListComponent } from './components/project/project-list/project-list.component';
import { ProjectCardComponent } from './components/project/project-list/project-card/project-card.component';
import { TaskCardComponent } from './components/view-task/task-card/task-card.component';
import { ModalComponent } from './components/modal/modal.component';
import { FilterUserPipe } from './pipes/filter-user.pipe';
import { DisableControlDirective } from './directives/disable-control.directive';
import { CountTaskPipe } from './pipes/count-task.pipe';

@NgModule({
  declarations: [
    AppComponent,
    UserComponent,
    UserAddComponent,
    UserEditComponent,
    UserListComponent,
    WelcomeComponent,
    ProjectComponent,
    TaskComponent,
    ViewTaskComponent,
    PageNotFoundComponent,
    UserCardComponent,
    ProjectAddComponent,
    ProjectListComponent,
    ProjectCardComponent,
    TaskCardComponent,
    ModalComponent,
    FilterUserPipe,
    DisableControlDirective,
    CountTaskPipe
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
