import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router'; // --- IMPORT RouterLink ---
import { ProjectService } from '../../../services/project.service';
import { StudentService } from '../../../services/student.service';
import { PhaseService } from '../../../services/phase.service';
import { FeedbackService } from '../../../services/feedback.service'; 
import { Project } from '../../../models/project';
import { User } from '../../../models/user';
import { Phase } from '../../../models/phase';
import { Feedback } from '../../../models/feedback';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
<<<<<<< HEAD
=======
import { GitlabService } from '../../../services/gitlab.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { forkJoin } from 'rxjs'; 
>>>>>>> 9647786 (footer)

@Component({
  selector: 'app-project-detail',
  standalone: true,
  // --- ADD RouterLink to the imports array ---
  imports: [CommonModule, FormsModule, MatCardModule, MatListModule, MatButtonModule, MatSelectModule, ReactiveFormsModule, RouterLink],
  templateUrl: './project-detail.component.html',
})
export class ProjectDetailComponent implements OnInit {
  project: Project | null = null;
  students: User[] = [];
  availableStudents: User[] = [];
  phases: Phase[] = [];
  selectedStudentId: number | null = null;
<<<<<<< HEAD
=======
  commits: any[] = [];
  selectedCommits: { [phaseId: number]: string } = {};

  allFeedback: Feedback[] = [];
  totalProjectScore: number = 0;
>>>>>>> 9647786 (footer)

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectService,
    private studentService: StudentService,
<<<<<<< HEAD
    private phaseService: PhaseService
=======
    private phaseService: PhaseService,
    private gitlabService: GitlabService,
    private snackBar: MatSnackBar,
    private feedbackService: FeedbackService 
>>>>>>> 9647786 (footer)
  ) { }

  ngOnInit() {
    const id = this.route.snapshot.params['id'];
    
    this.projectService.getProjectById(id).subscribe(project => {
      this.project = project;
<<<<<<< HEAD
      this.studentService.getAllStudents().subscribe(students => {
        this.students = students.filter(s => project.studentIds.includes(s.id));
        this.availableStudents = students.filter(s => !project.studentIds.includes(s.id));
      });
      this.phaseService.getPhasesByProject(id).subscribe(phases => this.phases = phases);
    });
=======
      this.loadInitialData();

      if (project.gitlabProjectId) {
        this.gitlabService.getProjectCommits(project.gitlabProjectId).subscribe({
          next: commits => this.commits = commits,
          error: err => console.error('Failed to fetch GitLab commits', err)
        });
      }
    });
  }

  loadInitialData(): void {
    if (!this.project) return;
    const projectId = this.project.id;

    forkJoin({
      allStudents: this.studentService.getAllStudents(),
      phases: this.phaseService.getPhasesByProject(projectId),
      feedback: this.feedbackService.getFeedbackByProject(projectId)
    }).subscribe(({ allStudents, phases, feedback }) => {
      this.students = allStudents.filter(s => this.project!.studentIds.includes(s.id));
      this.availableStudents = allStudents.filter(s => !this.project!.studentIds.includes(s.id));
      
      this.phases = phases;
      
      this.allFeedback = feedback;
      this.calculateTotalScore();
    });
  }

  calculateTotalScore(): void {
    if (this.allFeedback.length === 0) {
      this.totalProjectScore = 0;
      return;
    }
    this.totalProjectScore = this.allFeedback.reduce((sum, current) => sum + current.note, 0);
  }
  
  getFeedbackForPhase(phaseId: number): Feedback[] {
    return this.allFeedback.filter(f => f.phaseId === phaseId);
>>>>>>> 9647786 (footer)
  }

  assignStudent() {
    if (this.selectedStudentId && this.project) {
      this.projectService.assignStudents(this.project.id, [this.selectedStudentId]).subscribe(project => {
        this.project = project;
        this.loadInitialData();
        this.selectedStudentId = null;
      });
    }
  }

  removeStudent(studentId: number) {
    if (this.project) {
      this.projectService.removeStudent(this.project.id, studentId).subscribe(project => {
        this.project = project;
        this.loadInitialData();
      });
    }
  }
<<<<<<< HEAD
=======

  linkCommit(phaseId: number) {
    const commitId = this.selectedCommits[phaseId];
    if (!commitId) {
      this.snackBar.open('Please select a commit first', 'Close', { duration: 3000 });
      return;
    }

    this.phaseService.linkCommitToPhase(phaseId, commitId).subscribe({
      next: updatedPhase => {
        const index = this.phases.findIndex(p => p.id === phaseId);
        if (index !== -1) {
          this.phases[index] = updatedPhase;
        }
        this.snackBar.open('Commit linked successfully!', 'Close', { duration: 3000 });
      },
      error: err => {
        console.error('Failed to link commit', err);
        this.snackBar.open('Error linking commit.', 'Close', { duration: 3000 });
      }
    });
  }
>>>>>>> 9647786 (footer)
}