import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatCardModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './login.component.html',
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

  onSubmit() {
    if (this.loginForm.valid) {
      this.authService.login(this.loginForm.value).subscribe({
        next: (response) => {
          console.log('Login successful', response);
          localStorage.setItem('token', response.token);
          const role = this.authService.getUserRoleFromToken(response.token);
          console.log('User role from token:', role);
          this.authService.setCurrentUserRole(role);
          switch (role) {
            case 'ADMIN':
              this.router.navigate(['/users']);
              break;
            case 'PROF':
              this.router.navigate(['/professors']);
              break;
            case 'ETUDIANT':
              this.router.navigate(['/students']);
              break;
            default:
              this.snackBar.open('Invalid role', 'Close', { duration: 3000 });
              localStorage.removeItem('token');
          }
        },
        error: (err) => {
          console.error('Login failed', err);
          this.snackBar.open('Login failed. Please check your credentials.', 'Close', { duration: 3000 });
        }
      });
    }
  }
}