import { Component, OnInit } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { ProjectService } from '../../../services/project.service';
import { User } from '../../../models/user';
import { Project } from '../../../models/project';
import { Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { CommonModule } from '@angular/common'; // Import CommonModule
import { MatButtonModule } from '@angular/material/button'; // If you still need buttons

interface HierarchyItem {
  professor: User;
  students: User[];
}

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule, MatButtonModule], // Use CommonModule for *ngIf, *ngFor
  templateUrl: './user-list.component.html',
})
export class UserListComponent implements OnInit {
  isLoading = true;
  hierarchy: HierarchyItem[] = [];
  unassignedStudents: User[] = [];

  constructor(
    private userService: UserService, 
    private projectService: ProjectService, 
    private router: Router
  ) { }

  ngOnInit() {
    // Fetch all required data concurrently
    forkJoin({
      professors: this.userService.getUsersByRole('PROF'),
      students: this.userService.getUsersByRole('ETUDIANT'),
      projects: this.projectService.getAllProjects()
    }).subscribe(({ professors, students, projects }) => {
      this.buildHierarchy(professors, students, projects);
      this.isLoading = false;
    });
  }

  buildHierarchy(professors: User[], students: User[], projects: Project[]) {
    const studentMap = new Map(students.map(s => [s.id, s]));
    const assignedStudentIds = new Set<number>();

    this.hierarchy = professors.map(prof => {
      // Find all student IDs linked to this professor through projects
      const profStudentIds = new Set<number>();
      projects.forEach(proj => {
        if (proj.professorId === prof.id) {
          proj.studentIds.forEach(studentId => {
            profStudentIds.add(studentId);
            assignedStudentIds.add(studentId); // Track all students who have a professor
          });
        }
      });

      // Get the student objects for the collected IDs
      const profStudents = Array.from(profStudentIds)
        .map(id => studentMap.get(id))
        .filter((s): s is User => !!s);

      return {
        professor: prof,
        students: profStudents
      };
    });

    // Find students who are not assigned to any professor via a project
    this.unassignedStudents = students.filter(s => !assignedStudentIds.has(s.id));
  }

  navigateToCreate() {
    this.router.navigate(['/users/create']);
  }
}