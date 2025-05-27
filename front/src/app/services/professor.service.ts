import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user';
import { Project } from '../models/project';

@Injectable({
  providedIn: 'root'
})
export class ProfessorService {
  private apiUrl = 'http://localhost:8089/api/professors';

  constructor(private http: HttpClient) { }

  getAllProfessors(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }

  getProfessorById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  getProfessorProjects(professorId: number): Observable<Project[]> {
    return this.http.get<Project[]>(`${this.apiUrl}/${professorId}/projects`);
  }
}