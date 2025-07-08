import { Component, OnInit } from '@angular/core';
import { ProjectService } from '../../../services/project.service';
import { Project } from '../../../models/project';
import { Router, RouterModule } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-project-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    MatTableModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    DatePipe
  ],
  templateUrl: './project-list.component.html',
})
export class ProjectListComponent implements OnInit {
  allProjects: Project[] = [];
  filteredProjects: Project[] = [];
  
  searchQuery: string = '';
  statusFilter: string = 'All';

  constructor(private projectService: ProjectService, private router: Router) { }

  ngOnInit() {
    this.projectService.getAllProjects().subscribe(projects => {
      this.allProjects = projects;
      this.applyFilters();
    });
  }

  applyFilters(): void {
    let projects = this.allProjects;

    // Filter by status
    if (this.statusFilter !== 'All') {
      projects = projects.filter(p => this.getProjectStatus(p) === this.statusFilter);
    }

    // Filter by search query
    if (this.searchQuery) {
      projects = projects.filter(p => 
        p.titre.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    }

    this.filteredProjects = projects;
  }

  filterByStatus(status: string): void {
    this.statusFilter = status;
    this.applyFilters();
  }

  getProjectStatus(project: Project): 'Active' | 'Completed' | 'Upcoming' {
    const now = new Date();
    const startDate = new Date(project.dateDebut);
    const endDate = project.dateFin ? new Date(project.dateFin) : null;

    if (endDate && endDate < now) {
      return 'Completed';
    }
    if (startDate <= now) {
      return 'Active';
    }
    return 'Upcoming';
  }

  viewDetails(id: number) {
    this.router.navigate(['/projects/detail', id]);
  }

  editProject(id: number) {
    this.router.navigate(['/projects/edit', id]);
  }

  deleteProject(id: number) {
    if (confirm('Are you sure you want to delete this project?')) {
      this.projectService.deleteProject(id).subscribe(() => {
        // Refetch the list after deletion
        this.ngOnInit();
      });
    }
  }

  viewStats(id: number) {
    this.router.navigate(['/projects/stats', id]);
  }
}