import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { GitlabService } from '../gitlab.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-create-repo',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './create-repo.component.html',
  styleUrls: ['./create-repo.component.css']
})
export class CreateRepoComponent {
  repoForm: FormGroup;
  successMessage: string = '';
  errorMessage: string = '';
  isLoading: boolean = false;

  constructor(private fb: FormBuilder, private gitlabService: GitlabService) {
    this.repoForm = this.fb.group({
      name: ['', Validators.required],
      description: [''],
      visibility: ['private']
    });
  }

  onSubmit() {
    if (this.repoForm.valid) {
      this.isLoading = true;
      const { name, description } = this.repoForm.value;
      
      this.gitlabService.createRepository(name, description)
        .subscribe({
          next: () => {
            this.successMessage = 'Repository created successfully!';
            this.errorMessage = '';
            this.isLoading = false;
          },
          error: (err) => {
            this.errorMessage = `Error: ${err.message}`;
            this.successMessage = '';
            this.isLoading = false;
          }
        });
    }
  }
}