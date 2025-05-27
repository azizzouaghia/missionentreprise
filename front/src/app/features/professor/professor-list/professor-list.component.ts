import { Component, OnInit } from '@angular/core';
import { ProfessorService } from '../../../services/professor.service';
import { User } from '../../../models/user';
import { Project } from '../../../models/project';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatListModule } from '@angular/material/list';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-professor-list',
  standalone: true,
  imports: [CommonModule,MatTableModule, MatButtonModule, MatListModule],
  templateUrl: './professor-list.component.html',
})
export class ProfessorListComponent implements OnInit {
  professors: User[] = [];
  displayedColumns: string[] = ['id', 'username', 'email', 'specialite', 'bureau', 'actions'];
  selectedProfessorId: number | null = null;
  projects: Project[] = [];

  constructor(private professorService: ProfessorService) { }

  ngOnInit() {
    this.professorService.getAllProfessors().subscribe(professors => this.professors = professors);
  }

  viewProjects(professorId: number) {
    this.selectedProfessorId = professorId;
    this.professorService.getProfessorProjects(professorId).subscribe(projects => this.projects = projects);
  }
}