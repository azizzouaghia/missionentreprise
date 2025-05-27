import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProjectService } from '../../../services/project.service';
import { Stats } from '../../../models/stats';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-project-stats',
  standalone: true,
  imports: [CommonModule,MatCardModule, MatListModule],
  templateUrl: './project-stats.component.html',
})
export class ProjectStatsComponent implements OnInit {
  stats: Stats | null = null;

  constructor(private route: ActivatedRoute, private projectService: ProjectService) { }

  ngOnInit() {
    const id = this.route.snapshot.params['id'];
    this.projectService.getProjectStats(id).subscribe(stats => this.stats = stats);
  }
}