import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';

import { PhaseListComponent } from './phase-list/phase-list.component';
import { PhaseFormComponent } from './phase-form/phase-form.component';

const routes: Routes = [
  { path: '', component: PhaseListComponent },
  { path: 'create', component: PhaseFormComponent },
  { path: 'edit/:id', component: PhaseFormComponent }
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
    ReactiveFormsModule,
    PhaseListComponent,
    PhaseFormComponent
  ]
})
export class PhaseModule { }