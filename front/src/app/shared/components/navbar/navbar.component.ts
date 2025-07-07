import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider'; // --- IMPORT Divider Module ---
import { AuthService } from '../../../services/auth.service';
import { User } from '../../../models/user';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    RouterLinkActive,
    MatToolbarModule,
    MatButtonModule,
    MatMenuModule,
    MatIconModule,
    MatDividerModule // --- ADD Divider Module HERE ---
  ],
  templateUrl: './navbar.component.html',
})
export class NavbarComponent implements OnInit {
  isAuthenticated: boolean = false;
  userRole: string | null = null;
  user: User | null = null;

  constructor(private authService: AuthService, public router: Router) {}

  ngOnInit() {
    this.authService.getCurrentUserRole().subscribe(role => {
      this.userRole = role;
      this.isAuthenticated = this.authService.isAuthenticated();

      if (this.isAuthenticated) {
        this.authService.getCurrentUser().subscribe(user => {
          this.user = user;
        });
      } else {
        this.user = null;
      }
    });
  }

  logout() {
    this.authService.logout();
    this.isAuthenticated = false;
    this.userRole = null;
    this.user = null;
    this.router.navigate(['/auth/login']);
  }
}