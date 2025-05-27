import { Component, OnInit } from '@angular/core';
import { GitlabService } from '../../../services/gitlab.service';
import { Router } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-gitlab-project-list',
  standalone: true,
  imports: [MatTableModule, MatButtonModule],
  templateUrl: './gitlab-project-list.component.html',
})
export class GitlabProjectListComponent implements OnInit {
  projects: any[] = [];
  displayedColumns: string[] = ['id', 'name', 'description', 'actions'];

  constructor(private gitlabService: GitlabService, private router: Router) { }

  ngOnInit() {
    this.gitlabService.getAllProjects().subscribe(projects => this.projects = projects);
  }

  createBranch(projectId: string) {
    this.router.navigate(['/gitlab/branches', projectId]);
  }

  viewMergeRequests(projectId: string) {
    // Placeholder for future implementation
    alert('Merge Requests viewing not implemented yet.');
  }
}