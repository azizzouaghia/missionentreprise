import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../environments/environment';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class GitlabService {
  private headers = new HttpHeaders({
    'PRIVATE-TOKEN': environment.gitlabToken
  });

  constructor(private http: HttpClient) {}

  createRepository(name: string, description: string) {
    return this.http.post(
      `${environment.gitlabUrl}/projects`,
      {
        name: name,
        description: description,
        visibility: 'private'
      },
      { headers: this.headers }
    );
  }
}