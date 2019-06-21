import { NgModule } from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { UserComponent } from './components/user/user.component';
import { ProjectComponent } from './components/project/project.component';
import { TaskComponent } from './components/task/task.component';
import { ViewTaskComponent } from './components/view-task/view-task.component';
import { PageNotFoundComponent } from './page-not-found.component';


const routes: Routes = [
  { path: 'welcome', component: WelcomeComponent },
  { path: 'user', component: UserComponent },
  { path: 'project', component: ProjectComponent },
  { path: 'task', component: TaskComponent },
  { path: 'viewtask', component: ViewTaskComponent },
  { path: '', redirectTo: 'user', pathMatch: 'full'},
  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule { }