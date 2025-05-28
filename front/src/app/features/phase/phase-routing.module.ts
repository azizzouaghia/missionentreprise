import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../../guards/auth.guards';
import { ProjectListComponent } from '../project/project-list/project-list.component';
import { ProjectFormComponent } from '../project/project-form/project-form.component';

const routes: Routes = [
  {
    path: '',
    component: ProjectListComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN', 'PROF', 'ETUDIANT'] }
  },
  {
    path: 'create',
    component: ProjectFormComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN', 'PROF'] }
  },
  {
    path: 'edit/:id',
    component: ProjectFormComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN', 'PROF'] }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProjectRoutingModule { }
