import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router'; // --- IMPORT RouterLink ---
import { AuthService } from '../../../services/auth.service';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatIconModule } from '@angular/material/icon'; // --- IMPORT MatIconModule ---

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink, // --- ADD RouterLink ---
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule // --- ADD MatIconModule ---
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'] // Add styleUrls for page-specific styles
})
export class LoginComponent {
  loginForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (!this.loginForm.valid) {
      return;
    }

    this.authService.login(this.loginForm.value).subscribe({
      next: ({ token }) => {
        localStorage.setItem('token', token);
        const role = this.authService.getUserRoleFromToken(token);
        this.authService.setCurrentUserRole(role);
        
        // A more robust redirect
        this.router.navigate(['/stats']);
      },
      error: err => {
        console.error('Login failed', err);
        this.snackBar.open(
          'Login failed. Please check your credentials.',
          'Close',
          { duration: 3000 }
        );
      }
    });
  }
}