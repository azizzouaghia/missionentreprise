import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { CommonModule } from '@angular/common';

// --- Import necessary Angular Material modules ---
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatStepperModule } from '@angular/material/stepper';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatStepperModule,
    MatIconModule
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.registerForm = this.fb.group({
      credentials: this.fb.group({
        username: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]],
        password: ['', Validators.required],
      }),
      roleInfo: this.fb.group({
        role: ['', Validators.required],
      }),
      details: this.fb.group({
        matricule: [''],
        niveau: [''],
        specialite: [''],
        bureau: ['']
      })
    });
  }

  // --- NEW: Add getters for each form group ---
  get credentialsForm(): FormGroup {
    return this.registerForm.get('credentials') as FormGroup;
  }

  get roleInfoForm(): FormGroup {
    return this.registerForm.get('roleInfo') as FormGroup;
  }

  get detailsForm(): FormGroup {
    return this.registerForm.get('details') as FormGroup;
  }

  // Helper getter for easy access in the template
  get role(): string {
    return this.roleInfoForm.get('role')?.value;
  }

  onSubmit() {
    if (this.registerForm.valid) {
      // Combine the values from all steps into a single object
      const formValue = {
        ...this.credentialsForm.value,
        ...this.roleInfoForm.value,
        ...this.detailsForm.value
      };

      this.authService.register(formValue).subscribe({
        next: (response) => {
          localStorage.setItem('token', response.token);
          this.router.navigate(['/profile']);
        },
        error: (err) => console.error('Registration failed', err)
      });
    }
  }
}