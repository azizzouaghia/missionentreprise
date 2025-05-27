import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { FeedbackService } from '../../../services/feedback.service';
import { ProjectService } from '../../../services/project.service';
import { ProfessorService } from '../../../services/professor.service';
import { Feedback } from '../../../models/feedback';
import { Project } from '../../../models/project';
import { User } from '../../../models/user';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-feedback-form',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatButtonModule],
  templateUrl: './feedback-form.component.html',
})
export class FeedbackFormComponent implements OnInit {
  feedbackForm: FormGroup;
  feedbackId?: number;
  projects: Project[] = [];
  professors: User[] = [];

  constructor(
    private fb: FormBuilder,
    private feedbackService: FeedbackService,
    private projectService: ProjectService,
    private professorService: ProfessorService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.feedbackForm = this.fb.group({
      commentaire: ['', Validators.required],
      note: ['', [Validators.required, Validators.min(1), Validators.max(5)]],
      projectId: ['', Validators.required],
      professorId: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.projectService.getAllProjects().subscribe(projects => this.projects = projects);
    this.professorService.getAllProfessors().subscribe(professors => this.professors = professors);
    this.feedbackId = this.route.snapshot.params['id'];
    if (this.feedbackId) {
      this.feedbackService.getFeedbackById(this.feedbackId).subscribe(feedback => {
        this.feedbackForm.patchValue(feedback);
      });
    }
  }

  onSubmit() {
    if (this.feedbackForm.valid) {
      const feedback: Feedback = this.feedbackForm.value;
      if (this.feedbackId) {
        this.feedbackService.updateFeedback(this.feedbackId, feedback).subscribe(() => {
          this.router.navigate(['/feedback']);
        });
      } else {
        this.feedbackService.createFeedback(feedback).subscribe(() => {
          this.router.navigate(['/feedback']);
        });
      }
    }
  }
}