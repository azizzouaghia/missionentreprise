import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Feedback } from '../models/feedback';

@Injectable({
  providedIn: 'root'
})
export class FeedbackService {
  private apiUrl = 'http://localhost:8089/api/feedback';

  constructor(private http: HttpClient) { }

  getAllFeedbacks(): Observable<Feedback[]> {
    return this.http.get<Feedback[]>(this.apiUrl);
  }

  getFeedbackById(id: number): Observable<Feedback> {
    return this.http.get<Feedback>(`${this.apiUrl}/${id}`);
  }

  // This method still exists, but the backend now returns all feedback for all phases of a project
  getFeedbackByProject(projectId: number): Observable<Feedback[]> {
    return this.http.get<Feedback[]>(`${this.apiUrl}/project/${projectId}`);
  }

  getFeedbackByProfessor(professorId: number): Observable<Feedback[]> {
    return this.http.get<Feedback[]>(`${this.apiUrl}/professor/${professorId}`);
  }

  // The request body for create and update now expects a phaseId
  createFeedback(feedback: Partial<Feedback>): Observable<Feedback> {
    return this.http.post<Feedback>(this.apiUrl, feedback);
  }

  updateFeedback(id: number, feedback: Partial<Feedback>): Observable<Feedback> {
    return this.http.put<Feedback>(`${this.apiUrl}/${id}`, feedback);
  }

  deleteFeedback(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}