import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Project } from '../models/project';
import { Stats } from '../models/stats';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private apiUrl = 'http://localhost:8089/api/projects';

  constructor(private http: HttpClient) { }

  getAllProjects(): Observable<Project[]> {
    return this.http.get<Project[]>(this.apiUrl);
  }

  getProjectById(id: number): Observable<Project> {
    return this.http.get<Project>(`${this.apiUrl}/${id}`);
  }

  getProjectsByStudent(studentId: number): Observable<Project[]> {
    return this.http.get<Project[]>(`${this.apiUrl}/student/${studentId}`);
  }

  createProject(project: Project, professorId: number): Observable<Project> {
    return this.http.post<Project>(`${this.apiUrl}?professorId=${professorId}`, project);
  }

  updateProject(id: number, project: Project): Observable<Project> {
    return this.http.put<Project>(`${this.apiUrl}/${id}`, project);
  }

  deleteProject(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  assignStudents(projectId: number, studentIds: number[]): Observable<Project> {
    return this.http.post<Project>(`${this.apiUrl}/${projectId}/assign`, studentIds);
  }

  bulkAssign(projectIds: number[], studentIds: number[]): Observable<Project[]> {
    return this.http.post<Project[]>(`${this.apiUrl}/bulk-assign`, { projectIds, studentIds });
  }

  removeStudent(projectId: number, studentId: number): Observable<Project> {
    return this.http.delete<Project>(`${this.apiUrl}/${projectId}/students/${studentId}`);
  }

  getProjectStats(id: number): Observable<Stats> {
    return this.http.get<Stats>(`${this.apiUrl}/${id}/stats`);
  }

  // FIX: The incorrect method at the end has been removed.
}