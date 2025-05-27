import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { StudentService } from '../../../services/student.service';
import { User } from '../../../models/user';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-student-form',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './student-form.component.html',
})
export class StudentFormComponent implements OnInit {
  studentForm: FormGroup;
  studentId?: number;

  constructor(
    private fb: FormBuilder,
    private studentService: StudentService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.studentForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      matricule: ['', Validators.required],
      niveau: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.studentId = this.route.snapshot.params['id'];
    if (this.studentId) {
      this.studentService.getStudentById(this.studentId).subscribe(student => {
        this.studentForm.patchValue(student);
      });
    }
  }

  onSubmit() {
    if (this.studentForm.valid) {
      const student: User = { ...this.studentForm.value, role: 'ETUDIANT' };
      if (this.studentId) {
        this.studentService.updateStudent(this.studentId, student).subscribe(() => {
          this.router.navigate(['/students']);
        });
      } else {
        this.studentService.createStudent(student).subscribe(() => {
          this.router.navigate(['/students']);
        });
      }
    }
  }
}