import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { ReactiveFormsModule } from '@angular/forms';

import { ProjectListComponent } from './project-list/project-list.component';
import { ProjectFormComponent } from './project-form/project-form.component';
import { ProjectDetailComponent } from './project-detail/project-detail.component';
import { ProjectStatsComponent } from './project-stats/project-stats.component';

const routes: Routes = [
  { path: '', component: ProjectListComponent },
  { path: 'create', component: ProjectFormComponent },
  { path: 'edit/:id', component: ProjectFormComponent },
  { path: 'detail/:id', component: ProjectDetailComponent },
  { path: 'stats/:id', component: ProjectStatsComponent }
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
    MatSelectModule,
    MatCardModule,
    MatListModule,
    ReactiveFormsModule,
    ProjectListComponent,
    ProjectFormComponent,
    ProjectDetailComponent,
    ProjectStatsComponent
  ]
})
export class ProjectModule { }