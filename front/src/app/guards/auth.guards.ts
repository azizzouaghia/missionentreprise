import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { map, take } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/auth/login']);
      this.snackBar.open('Please log in to access this page.', 'Close', { duration: 3000 });
      return false;
    }

    const expectedRoles = route.data['roles'] as string[];
    return this.authService.getCurrentUserRole().pipe(
      take(1),
      map(role => {
        if (role && expectedRoles.includes(role)) {
          return true;
        } else {
          this.router.navigate(['/auth/login']);
          this.snackBar.open('You do not have permission to access this page.', 'Close', { duration: 3000 });
          return false;
        }
      })
    );
  }
}