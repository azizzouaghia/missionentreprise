import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProjectService } from '../../../services/project.service';
import { StudentService } from '../../../services/student.service';
import { PhaseService } from '../../../services/phase.service';
import { Project } from '../../../models/project';
import { User } from '../../../models/user';
import { Phase } from '../../../models/phase';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-project-detail',
  standalone: true,
  imports: [CommonModule,FormsModule,MatCardModule, MatListModule, MatButtonModule, MatSelectModule, ReactiveFormsModule],
  templateUrl: './project-detail.component.html',
})
export class ProjectDetailComponent implements OnInit {
  project: Project | null = null;
  students: User[] = [];
  availableStudents: User[] = [];
  phases: Phase[] = [];
  selectedStudentId: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectService,
    private studentService: StudentService,
    private phaseService: PhaseService
  ) { }

  ngOnInit() {
    const id = this.route.snapshot.params['id'];
    this.projectService.getProjectById(id).subscribe(project => {
      this.project = project;
      this.studentService.getAllStudents().subscribe(students => {
        this.students = students.filter(s => project.studentIds.includes(s.id));
        this.availableStudents = students.filter(s => !project.studentIds.includes(s.id));
      });
      this.phaseService.getPhasesByProject(id).subscribe(phases => this.phases = phases);
    });
  }

  assignStudent() {
    if (this.selectedStudentId && this.project) {
      this.projectService.assignStudents(this.project.id, [this.selectedStudentId]).subscribe(project => {
        this.project = project;
        this.studentService.getAllStudents().subscribe(students => {
          this.students = students.filter(s => project.studentIds.includes(s.id));
          this.availableStudents = students.filter(s => !project.studentIds.includes(s.id));
        });
        this.selectedStudentId = null;
      });
    }
  }

  removeStudent(studentId: number) {
    if (this.project) {
      this.projectService.removeStudent(this.project.id, studentId).subscribe(project => {
        this.project = project;
        this.studentService.getAllStudents().subscribe(students => {
          this.students = students.filter(s => project.studentIds.includes(s.id));
          this.availableStudents = students.filter(s => !project.studentIds.includes(s.id));
        });
      });
    }
  }
}