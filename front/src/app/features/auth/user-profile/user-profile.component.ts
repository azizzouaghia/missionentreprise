import { Component, OnInit } from '@angular/core';
import { TitleCasePipe } from '@angular/common';
import { AuthService } from '../../../services/auth.service';
import { ProjectService } from '../../../services/project.service';
import { ProfessorService } from '../../../services/professor.service';
import { User } from '../../../models/user';
import { Project } from '../../../models/project';
import { Observable, of } from 'rxjs';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    MatCardModule, 
    MatListModule, 
    MatIconModule, 
    MatDividerModule,
    TitleCasePipe
  ],
  templateUrl: './user-profile.component.html',
})
export class UserProfileComponent implements OnInit {
  user: User | null = null;
  projects$: Observable<Project[]> = of([]);

  constructor(
    private authService: AuthService,
    private projectService: ProjectService,
    private professorService: ProfessorService
  ) { }

  ngOnInit() {
    this.authService.getCurrentUser().subscribe({
      next: (user) => {
        this.user = user;
        // After getting the user, fetch their projects based on their role
        if (user.role === 'PROF') {
          this.projects$ = this.professorService.getProfessorProjects(user.id);
        } else if (user.role === 'ETUDIANT') {
          this.projects$ = this.projectService.getProjectsByStudent(user.id);
        }
      },
      error: (err) => console.error('Failed to load user', err)
    });
  }
}