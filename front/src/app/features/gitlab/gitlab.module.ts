import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon'; // Import Icon module
import { HighlightModule, HIGHLIGHT_OPTIONS } from 'ngx-highlightjs'; // Import Highlight module

import { GitlabProjectListComponent } from './gitlab-project-list/gitlab-project-list.component';
import { GitlabBranchFormComponent } from './gitlab-branch-form/gitlab-branch-form.component';
import { GitlabCommitDetailComponent } from './gitlab-commit-detail/gitlab-commit-detail.component';

const routes: Routes = [
  { path: 'projects', component: GitlabProjectListComponent },
  { path: 'branches/:projectId', component: GitlabBranchFormComponent },
  { path: 'commit/:projectId/:commitId', component: GitlabCommitDetailComponent }
];

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    MatTableModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    MatIconModule, // Add Icon module
    ReactiveFormsModule,
    HighlightModule, // Add Highlight module
    GitlabProjectListComponent,
    GitlabBranchFormComponent,
    GitlabCommitDetailComponent
  ],
  providers: [
    {
      provide: HIGHLIGHT_OPTIONS,
      useValue: {
        coreLibraryLoader: () => import('highlight.js/lib/core'),
        languages: {
          typescript: () => import('highlight.js/lib/languages/typescript'),
          css: () => import('highlight.js/lib/languages/css'),
          xml: () => import('highlight.js/lib/languages/xml'),
          java: () => import('highlight.js/lib/languages/java')
        }
      }
    }
  ]
})
export class GitlabModule { }