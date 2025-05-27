import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ReactiveFormsModule } from '@angular/forms';

import { GitlabProjectListComponent } from './gitlab-project-list/gitlab-project-list.component';
import { GitlabBranchFormComponent } from './gitlab-branch-form/gitlab-branch-form.component';

const routes: Routes = [
  { path: 'projects', component: GitlabProjectListComponent },
  { path: 'branches/:projectId', component: GitlabBranchFormComponent }
];

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    MatTableModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    GitlabProjectListComponent,
    GitlabBranchFormComponent
  ]
})
export class GitlabModule { }