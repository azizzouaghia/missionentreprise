import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { GitlabService } from '../../../services/gitlab.service';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { HighlightModule } from 'ngx-highlightjs';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-gitlab-commit-detail',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatListModule, MatIconModule, HighlightModule],
  templateUrl: './gitlab-commit-detail.component.html',
  styleUrls: ['./gitlab-commit-detail.component.css']
})
export class GitlabCommitDetailComponent implements OnInit {
  projectId!: string;
  commitId!: string;
  
  tree$: Observable<any[]> | undefined;
  pathHistory: string[] = [''];
  
  selectedFile: any | null = null;
  selectedFileContent: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private gitlabService: GitlabService
  ) { }

  ngOnInit(): void {
    this.projectId = this.route.snapshot.paramMap.get('projectId')!;
    this.commitId = this.route.snapshot.paramMap.get('commitId')!;
    this.loadTree();
  }

  loadTree(path: string = ''): void {
    this.tree$ = this.gitlabService.getRepositoryTree(this.projectId, this.commitId, path);
    this.selectedFile = null;
    this.selectedFileContent = null;
  }
  
  handleItemClick(item: any): void {
    if (item.type === 'tree') {
      this.pathHistory.push(item.path);
      this.loadTree(item.path);
    } else if (item.type === 'blob') {
      this.selectedFile = item;
      this.gitlabService.getFileContent(this.projectId, this.commitId, item.path).subscribe(fileData => {
        this.selectedFileContent = fileData.content;
      });
    }
  }

  goBack(): void {
    if (this.pathHistory.length > 1) {
      this.pathHistory.pop();
      const previousPath = this.pathHistory[this.pathHistory.length - 1];
      this.loadTree(previousPath);
    }
  }

  getCurrentPath(): string {
    return this.pathHistory[this.pathHistory.length - 1] || 'Root';
  }
}