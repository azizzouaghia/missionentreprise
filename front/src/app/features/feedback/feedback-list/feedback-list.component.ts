import { Component, OnInit } from '@angular/core';
import { FeedbackService } from '../../../services/feedback.service';
import { Feedback } from '../../../models/feedback';
import { Router, RouterModule } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-feedback-list',
  standalone: true,
  imports: [
    MatTableModule,
    MatButtonModule,
    RouterModule
  ],
  templateUrl: './feedback-list.component.html',
})
export class FeedbackListComponent implements OnInit {
  feedbacks: Feedback[] = [];
  displayedColumns: string[] = ['id', 'commentaire', 'note', 'projectId', 'professorName', 'actions'];
  snackBar: any;

  constructor(private feedbackService: FeedbackService, private router: Router) { }

  ngOnInit() {
    this.feedbackService.getAllFeedbacks().subscribe(feedbacks => this.feedbacks = feedbacks);
  }

  editFeedback(id: number) {
    this.router.navigate(['/feedback/edit', id]);
  }
  navigateToCreate() {
    console.log('Navigating to /feedback/create');
    this.router.navigate(['/feedback/create']).catch(err => {
      console.error('Navigation failed:', err);
      this.snackBar.open('Navigation to create user failed', 'Close', { duration: 3000 });
    });
  }
  deleteFeedback(id: number) {
    if (confirm('Are you sure you want to delete this feedback?')) {
      this.feedbackService.deleteFeedback(id).subscribe(() => {
        this.feedbacks = this.feedbacks.filter(feedback => feedback.id !== id);
      });
    }
  }
}