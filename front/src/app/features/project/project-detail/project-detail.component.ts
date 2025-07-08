import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
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
import { GitlabService } from '../../../services/gitlab.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { forkJoin } from 'rxjs'; 

@Component({
  selector: 'app-project-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, MatCardModule, MatListModule, MatButtonModule, MatSelectModule, ReactiveFormsModule, RouterLink],
  templateUrl: './project-detail.component.html',
})
export class ProjectDetailComponent implements OnInit {
  project: Project | null = null;
  students: User[] = [];
  availableStudents: User[] = [];
  phases: Phase[] = [];
  selectedStudentId: number | null = null;
  commits: any[] = [];
  selectedCommits: { [phaseId: number]: string } = {};

  allFeedback: Feedback[] = [];
  totalProjectScore: number = 0;

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectService,
    private studentService: StudentService,
    private phaseService: PhaseService,
    private gitlabService: GitlabService,
    private snackBar: MatSnackBar,
    private feedbackService: FeedbackService 
  ) { }

  ngOnInit() {
    const id = this.route.snapshot.params['id'];
    
    this.projectService.getProjectById(id).subscribe(project => {
      this.project = project;
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

  linkCommit(phaseId: number) {
    const commitId = this.selectedCommits[phaseId];
    if (!commitId) {
      this.snackBar.open('Please select a commit first', 'Close', { duration: 3000 });
      return;
    }

    this.phaseService.linkCommitToPhase(phaseId, commitId).subscribe({
      next: updatedPhase => {
        // --- FIX: Use this more robust method to update the array ---
        // This ensures Angular's change detection is triggered correctly.
        
        // Add this line for debugging. Check your browser's console (F12).
        console.log('Received updated phase from backend:', updatedPhase);

        const index = this.phases.findIndex(p => p.id === phaseId);
        if (index !== -1) {
          // Create a new array to guarantee the view updates
          const newPhases = [...this.phases];
          newPhases[index] = updatedPhase;
          this.phases = newPhases;
        }
        this.snackBar.open('Commit linked successfully!', 'Close', { duration: 3000 });
      },
      error: err => {
        console.error('Failed to link commit', err);
        // Also log the actual error for more insight
        this.snackBar.open(`Error linking commit: ${err.message}`, 'Close', { duration: 3000 });
      }
    });
  }
}