import { Component, OnInit } from '@angular/core';
import { StudentService } from '../../../services/student.service';
import { User } from '../../../models/user';
import { Router } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-student-list',
  standalone: true,
  imports: [FormsModule,MatTableModule, MatButtonModule, MatFormFieldModule, MatInputModule],
  templateUrl: './student-list.component.html',
})
export class StudentListComponent implements OnInit {
  students: User[] = [];
  displayedColumns: string[] = ['id', 'username', 'email', 'matricule', 'niveau', 'actions'];
  searchQuery: string = '';

  constructor(private studentService: StudentService, private router: Router) { }

  ngOnInit() {
    this.loadStudents();
  }

  loadStudents() {
    if (this.searchQuery) {
      this.studentService.searchStudents(this.searchQuery).subscribe(students => this.students = students);
    } else {
      this.studentService.getAllStudents().subscribe(students => this.students = students);
    }
  }

  search() {
    this.loadStudents();
  }

  editStudent(id: number) {
    this.router.navigate(['/students/edit', id]);
  }

  deleteStudent(id: number) {
    if (confirm('Are you sure you want to delete this student?')) {
      this.studentService.deleteStudent(id).subscribe(() => {
        this.loadStudents();
      });
    }
  }
}