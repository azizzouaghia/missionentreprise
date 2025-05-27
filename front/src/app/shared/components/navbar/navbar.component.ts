import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, MatToolbarModule, MatButtonModule],
  templateUrl: './navbar.component.html',
})
export class NavbarComponent implements OnInit {
  isAuthenticated: boolean = false;
  userRole: string | null = null;

  constructor(private authService: AuthService, public router: Router) {}

  ngOnInit() {
    this.authService.getCurrentUserRole().subscribe(role => {
      this.userRole = role;
      this.isAuthenticated = this.authService.isAuthenticated();
    });
  }

  logout() {
    this.authService.logout();
    this.isAuthenticated = false;
    this.userRole = null;
    this.router.navigate(['/auth/login']);
  }
}