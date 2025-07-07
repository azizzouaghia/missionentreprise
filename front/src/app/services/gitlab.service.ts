import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GitlabService {
  private apiUrl = 'http://localhost:8089/api/gitlab';

  constructor(private http: HttpClient) { }

  // --- NEW: Get repository tree ---
  getRepositoryTree(projectId: string, ref: string, path: string = ''): Observable<any[]> {
    const params = new HttpParams().set('ref', ref).set('path', path);
    return this.http.get<any[]>(`${this.apiUrl}/projects/${projectId}/repository/tree`, { params });
  }

  // --- NEW: Get file content ---
  getFileContent(projectId: string, ref: string, filePath: string): Observable<any> {
    const params = new HttpParams().set('ref', ref).set('filePath', filePath);
    return this.http.get<any>(`${this.apiUrl}/projects/${projectId}/repository/files`, { params });
  }

  getAllProjects(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/projects`);
  }

  createProject(name: string, description: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/projects?name=${name}&description=${description}`, {});
  }

  getProjectCommits(projectId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/projects/${projectId}/commits`);
  }

  getSingleCommit(projectId: string, commitId: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/projects/${projectId}/commits/${commitId}`);
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