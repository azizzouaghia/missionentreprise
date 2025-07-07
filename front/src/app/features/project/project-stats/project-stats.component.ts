import { Component, OnInit } from '@angular/core'; // Removed AfterViewInit
import { ActivatedRoute } from '@angular/router';
import { ProjectService } from '../../../services/project.service';
import { ProjectStats } from '../../../models/project-stats';
import { Project } from '../../../models/project';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { CommonModule, DecimalPipe } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { forkJoin } from 'rxjs';

declare var Chart: any;

@Component({
  selector: 'app-project-stats',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatListModule, MatIconModule, DecimalPipe],
  templateUrl: './project-stats.component.html',
})
// Removed AfterViewInit from implements list
export class ProjectStatsComponent implements OnInit {
  stats: ProjectStats | null = null;
  project: Project | null = null;
  
  private CHART_COLORS = {
    sky: 'rgb(56, 189, 248)',
    emerald: 'rgb(16, 185, 129)',
    amber: 'rgb(245, 158, 11)',
    rose: 'rgb(244, 63, 94)',
    indigo: 'rgb(99, 102, 241)',
  };

  constructor(private route: ActivatedRoute, private projectService: ProjectService) { }

  ngOnInit() {
    const id = this.route.snapshot.params['id'];
    
    forkJoin({
      stats: this.projectService.getProjectStats(id),
      project: this.projectService.getProjectById(id)
    }).subscribe(({ stats, project }) => {
      this.stats = stats as any;
      this.project = project;
      
      // --- MOVED CHART LOGIC HERE ---
      // By the time the data arrives, the view is ready.
      // Use setTimeout to ensure the browser has rendered the *ngIf containers.
      setTimeout(() => {
        this.createPhaseStatusChart();
        this.createFakeCommitsChart();
      }, 0);
    });
  }

  // ngAfterViewInit is no longer needed

  private createPhaseStatusChart() {
    const canvas = document.getElementById('project-phase-chart') as HTMLCanvasElement;
    if (!canvas || !this.stats) return;

    // Guard against empty or undefined phasesByStatus
    const phaseLabels = this.stats.phasesByStatus ? Object.keys(this.stats.phasesByStatus) : [];
    const phaseData = this.stats.phasesByStatus ? Object.values(this.stats.phasesByStatus) : [];

    new Chart(canvas, {
      type: 'doughnut',
      data: {
        labels: phaseLabels,
        datasets: [{
          data: phaseData,
          backgroundColor: [
            this.CHART_COLORS.sky, 
            this.CHART_COLORS.emerald, 
            this.CHART_COLORS.amber, 
            this.CHART_COLORS.rose, 
            this.CHART_COLORS.indigo
          ],
          borderColor: '#fff',
          borderWidth: 4,
        }],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        cutout: '60%',
        plugins: {
          legend: { position: 'bottom', labels: { padding: 20, usePointStyle: true, pointStyle: 'rectRounded' } },
        },
      },
    });
  }

  private createFakeCommitsChart() {
    const canvas = document.getElementById('project-commit-chart') as HTMLCanvasElement;
    if (!canvas || !this.stats) return;

    const dataPoints = 10;
    const labels = Array.from({ length: dataPoints }, (_, i) => `Day ${i + 1}`);
    // Create more realistic looking random data that trends upwards
    let fakeData = [0];
    for (let i = 1; i < dataPoints; i++) {
        const previous = fakeData[i-1];
        const next = previous + Math.random() * (this.stats.commitCount / 4);
        fakeData.push(next);
    }

    new Chart(canvas, {
      type: 'line',
      data: {
        labels: labels,
        datasets: [{
          label: 'Commits',
          data: fakeData,
          fill: true,
          backgroundColor: 'rgba(244, 63, 94, 0.1)',
          borderColor: this.CHART_COLORS.rose,
          tension: 0.4,
          pointBackgroundColor: this.CHART_COLORS.rose,
          pointRadius: 4
        }],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
            y: { beginAtZero: true, grid: { color: 'rgba(0,0,0,0.05)' } },
            x: { grid: { display: false } }
        },
        plugins: {
            legend: { display: false }
        }
      }
    });
  }
}