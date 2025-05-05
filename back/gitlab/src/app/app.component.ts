import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CreateRepoComponent } from './create-repo/create-repo.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, CreateRepoComponent],
  template: `<app-create-repo></app-create-repo>`
})
export class AppComponent {}