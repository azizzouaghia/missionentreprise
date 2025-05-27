import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PhaseService } from '../../../services/phase.service';
import { ProjectService } from '../../../services/project.service';
import { Phase } from '../../../models/phase';
import { Project } from '../../../models/project';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { CommonModule } from '@angular/common';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-phase-form',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatButtonModule],
  templateUrl: './phase-form.component.html',
})
export class PhaseFormComponent implements OnInit {
  phaseForm: FormGroup;
  phaseId?: number;
  projects: Project[] = [];

  constructor(
    private fb: FormBuilder,
    private phaseService: PhaseService,
    private projectService: ProjectService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.phaseForm = this.fb.group({
      name: ['', Validators.required],
      description: [''],
      dateDebut: [''],
      dateFin: [''],
      status: ['', Validators.required],
      projectId: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.projectService.getAllProjects().subscribe(projects => this.projects = projects);
    this.phaseId = this.route.snapshot.params['id'];
    if (this.phaseId) {
      this.phaseService.getPhaseById(this.phaseId).subscribe(phase => {
        this.phaseForm.patchValue(phase);
      });
    }
  }

  onSubmit() {
    if (this.phaseForm.valid) {
      const phase: Phase = this.phaseForm.value;
      if (this.phaseId) {
        this.phaseService.updatePhase(this.phaseId, phase).subscribe(() => {
          this.router.navigate(['/phases']);
        });
      } else {
        this.phaseService.createPhase(phase).subscribe(() => {
          this.router.navigate(['/phases']);
        });
      }
    }
  }
}