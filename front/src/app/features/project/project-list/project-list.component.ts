import { Component, OnInit } from '@angular/core';
import { ProjectService } from '../../../services/project.service';
import { AuthService } from '../../../services/auth.service';
import { Project } from '../../../models/project';
import { Router, RouterModule } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
<<<<<<< HEAD
import { CommonModule } from '@angular/common';
=======
import { CommonModule, DatePipe } from '@angular/common'; // Import CommonModule and DatePipe
import { FormsModule } from '@angular/forms'; // Import FormsModule for ngModel
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
>>>>>>> 9647786 (footer)

@Component({
  selector: 'app-project-list',
  standalone: true,
  // Update the imports array
  imports: [
    CommonModule,
<<<<<<< HEAD
=======
    RouterModule,
    FormsModule,
>>>>>>> 9647786 (footer)
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
<<<<<<< HEAD
  projects: Project[] = [];
  displayedColumns: string[] = ['id', 'titre', 'description', 'dateDebut', 'actions'];
=======
  allProjects: Project[] = [];
  filteredProjects: Project[] = [];
  
  searchQuery: string = '';
  statusFilter: string = 'All';
>>>>>>> 9647786 (footer)

  role: 'ADMIN'|'PROF'|'ETUDIANT'|null = null;
  userId: number | null = null;

<<<<<<< HEAD
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
=======
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
>>>>>>> 9647786 (footer)
  }

  navigateToCreate(): void {
    this.router.navigate(['/projects/create']);
  }

  viewDetails(id: number): void {
    this.router.navigate(['/projects/detail', id]);
  }

<<<<<<< HEAD
  editProject(id: number): void {
    if (this.role === 'ADMIN' || (this.role === 'PROF' && id === this.userId)) {
      this.router.navigate(['/projects/edit', id]);
=======
  editProject(id: number) {
    this.router.navigate(['/projects/edit', id]);
  }

  deleteProject(id: number) {
    if (confirm('Are you sure you want to delete this project?')) {
      this.projectService.deleteProject(id).subscribe(() => {
        // Refetch the list after deletion
        this.ngOnInit();
      });
>>>>>>> 9647786 (footer)
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