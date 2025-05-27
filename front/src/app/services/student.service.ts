import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  private apiUrl = 'http://localhost:8089/api/students';

  constructor(private http: HttpClient) { }

  getAllStudents(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }

  getStudentById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  searchStudents(query: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/search?query=${query}`);
  }

  createStudent(student: User): Observable<User> {
    return this.http.post<User>(this.apiUrl, student);
  }

  updateStudent(id: number, student: User): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/${id}`, student);
  }

  deleteStudent(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}