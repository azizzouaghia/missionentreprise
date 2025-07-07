import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ProjectService } from '../../../services/project.service';
import { ProfessorService } from '../../../services/professor.service';
import { User } from '../../../models/user';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon'; // --- IMPORT MatIconModule ---
import { MatDividerModule } from '@angular/material/divider';

@Component({
  selector: 'app-project-form',
  standalone: true,
  // --- ADD MatIconModule to imports ---
  imports: [MatDividerModule,CommonModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatButtonModule, MatIconModule],
  templateUrl: './project-form.component.html',
})
export class ProjectFormComponent implements OnInit {
  projectForm: FormGroup;
  projectId?: number;
  professors: User[] = [];

  constructor(
    private fb: FormBuilder,
    private projectService: ProjectService,
    private professorService: ProfessorService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.projectForm = this.fb.group({
      titre: ['', Validators.required],
      description: [''],
      dateDebut: ['', Validators.required],
      dateFin: [''],
      professorId: ['']
    });
  }

  ngOnInit() {
    this.professorService.getAllProfessors().subscribe(professors => this.professors = professors);
    this.projectId = this.route.snapshot.params['id'];
    if (this.projectId) {
      this.projectService.getProjectById(this.projectId).subscribe(project => {
        this.projectForm.patchValue(project);
      });
    }
  }

  onSubmit() {
    if (this.projectForm.valid) {
      const project = this.projectForm.value;
      if (this.projectId) {
        this.projectService.updateProject(this.projectId, project).subscribe(() => {
          this.router.navigate(['/projects']);
        });
      } else {
        this.projectService.createProject(project, project.professorId).subscribe(() => {
          this.router.navigate(['/projects']);
        });
      }
    }
  }
}