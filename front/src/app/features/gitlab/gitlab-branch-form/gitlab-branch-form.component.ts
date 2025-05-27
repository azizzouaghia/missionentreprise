import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { GitlabService } from '../../../services/gitlab.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-gitlab-branch-form',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './gitlab-branch-form.component.html',
})
export class GitlabBranchFormComponent {
  branchForm: FormGroup;
  projectId: string;

  constructor(
    private fb: FormBuilder,
    private gitlabService: GitlabService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.projectId = this.route.snapshot.params['projectId'];
    this.branchForm = this.fb.group({
      branchName: ['', Validators.required],
      ref: ['main', Validators.required]
    });
  }

  onSubmit() {
    if (this.branchForm.valid) {
      const { branchName, ref } = this.branchForm.value;
      this.gitlabService.createBranch(this.projectId, branchName, ref).subscribe({
        next: () => this.router.navigate(['/gitlab/projects']),
        error: (err) => console.error('Failed to create branch', err)
      });
    }
  }
}