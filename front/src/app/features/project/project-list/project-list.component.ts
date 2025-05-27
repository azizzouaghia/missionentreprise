import { Component, OnInit } from '@angular/core';
import { ProjectService } from '../../../services/project.service';
import { Project } from '../../../models/project';
import { Router } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-project-list',
  standalone: true,
  imports: [MatTableModule, MatButtonModule],
  templateUrl: './project-list.component.html',
})
export class ProjectListComponent implements OnInit {
  projects: Project[] = [];
  displayedColumns: string[] = ['id', 'titre', 'description', 'dateDebut', 'actions'];
  snackBar: any;

  constructor(private projectService: ProjectService, private router: Router) { }

  ngOnInit() {
    this.projectService.getAllProjects().subscribe(projects => this.projects = projects);
  }

  viewDetails(id: number) {
    this.router.navigate(['/projects/detail', id]);
  }

  editProject(id: number) {
    this.router.navigate(['/projects/edit', id]);
  }
  navigateToCreate() {
    console.log('Navigating to /projects/create');
    this.router.navigate(['/projects/create']).catch(err => {
      console.error('Navigation failed:', err);
      this.snackBar.open('Navigation to create user failed', 'Close', { duration: 3000 });
    });
  }
  deleteProject(id: number) {
    if (confirm('Are you sure you want to delete this project?')) {
      this.projectService.deleteProject(id).subscribe(() => {
        this.projects = this.projects.filter(project => project.id !== id);
      });
    }
  }

  viewStats(id: number) {
    this.router.navigate(['/projects/stats', id]);
  }
}