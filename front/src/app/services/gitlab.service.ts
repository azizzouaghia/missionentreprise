import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GitlabService {
  private apiUrl = 'http://localhost:8089/api/gitlab';

  constructor(private http: HttpClient) { }

  getAllProjects(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/projects`);
  }

  createProject(name: string, description: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/projects?name=${name}&description=${description}`, {});
  }

  getProjectCommits(projectId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/projects/${projectId}/commits`);
  }

  getProjectBranches(projectId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/projects/${projectId}/branches`);
  }

  getProjectMergeRequests(projectId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/projects/${projectId}/merge-requests`);
  }

  createBranch(projectId: string, branchName: string, ref: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/projects/${projectId}/branches?branchName=${branchName}&ref=${ref}`, {});
  }
}