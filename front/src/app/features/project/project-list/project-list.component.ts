import { Component, OnInit } from '@angular/core';
import { ProjectService } from '../../../services/project.service';
import { AuthService } from '../../../services/auth.service';
import { Project } from '../../../models/project';
import { Router, RouterModule } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-project-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    RouterModule
  ],
  templateUrl: './project-list.component.html',
})
export class ProjectListComponent implements OnInit {
  projects: Project[] = [];
  displayedColumns: string[] = ['id', 'titre', 'description', 'dateDebut', 'actions'];

  role: 'ADMIN'|'PROF'|'ETUDIANT'|null = null;
  userId: number | null = null;

  constructor(
    private projectService: ProjectService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.authService.getCurrentUser().subscribe(user => {
      this.userId = user.id;
      this.role = user.role;
      switch (this.role) {
        case 'ADMIN':
          this.projectService.getAllProjects().subscribe(data => this.projects = data);
          break;
        case 'PROF':
          this.projectService.getProjectsByProfessor(this.userId!)
            .subscribe(data => this.projects = data);
          break;
        case 'ETUDIANT':
        default:
          this.projectService.getProjectsByStudent(this.userId!)
            .subscribe(data => this.projects = data);
          break;
      }
    });
  }

  navigateToCreate(): void {
    this.router.navigate(['/projects/create']);
  }

  viewDetails(id: number): void {
    this.router.navigate(['/projects/detail', id]);
  }

  editProject(id: number): void {
    if (this.role === 'ADMIN' || (this.role === 'PROF' && id === this.userId)) {
      this.router.navigate(['/projects/edit', id]);
    }
  }

  deleteProject(id: number): void {
    if (this.role === 'ADMIN' || (this.role === 'PROF' && id === this.userId)) {
      if (confirm('Are you sure you want to delete this project?')) {
        this.projectService.deleteProject(id).subscribe(() => {
          this.projects = this.projects.filter(p => p.id !== id);
        });
      }
    }
  }

  viewStats(id: number): void {
    this.router.navigate(['/projects/stats', id]);
  }
}