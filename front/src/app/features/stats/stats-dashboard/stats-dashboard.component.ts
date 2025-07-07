import { Component, AfterViewInit } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http'; // Correctly import HttpClientModule
import { Stats } from '../../../models/stats';
import { MatCardModule } from '@angular/material/card';
import { CommonModule, DecimalPipe } from '@angular/common';

// Declare Chart as a global variable from the CDN
declare var Chart: any;

@Component({
  selector: 'app-stats-dashboard',
  standalone: true,
  // Use HttpClientModule in the imports array
  imports: [CommonModule, HttpClientModule, MatCardModule, DecimalPipe],
  templateUrl: './stats-dashboard.component.html',
  // REMOVE the providers array from here
})
export class StatsDashboardComponent implements AfterViewInit {
  stats: Stats | null = null;
  chartError: string | null = null;

  private CHART_COLORS = {
    sky: 'rgb(56, 189, 248)',
    emerald: 'rgb(16, 185, 129)',
    amber: 'rgb(245, 158, 11)',
    rose: 'rgb(244, 63, 94)',
    indigo: 'rgb(99, 102, 241)',
    gray: 'rgb(209, 213, 219)',
    lightGray: 'rgba(229, 231, 235, 0.6)'
  };
  
  constructor(private http: HttpClient) {}

  ngAfterViewInit() {
    // Register a custom plugin to draw text in the middle of doughnut charts
    Chart.register({
      id: 'doughnutText',
      afterDraw: (chart: any) => {
        if (chart.config.options.plugins?.doughnutText?.display) {
          const text = chart.config.options.plugins.doughnutText.text;
          const color = chart.config.options.plugins.doughnutText.color || chart.config.options.color;
          const font = chart.config.options.plugins.doughnutText.font || 'Arial';
          const size = chart.config.options.plugins.doughnutText.size || '40px';
          const ctx = chart.ctx;
          const R = chart.innerRadius;
          const centerX = (chart.chartArea.left + chart.chartArea.right) / 2;
          const centerY = (chart.chartArea.top + chart.chartArea.bottom) / 2;

          ctx.font = `${size} ${font}`;
          ctx.fillStyle = color;
          ctx.textAlign = 'center';
          ctx.textBaseline = 'middle';
          ctx.fillText(text, centerX, centerY);
        }
      },
    });

    this.http.get<Stats>('http://localhost:8089/api/stats').subscribe({
      next: (data) => {
        this.stats = data;
        setTimeout(() => this.initCharts(), 0);
      },
      error: (err) => {
        console.error('Failed to fetch stats:', err);
        this.chartError = 'Failed to load statistics. Please try again later.';
      },
    });
  }

  initCharts() {
    if (!this.stats) {
      this.chartError = 'No statistics data available.';
      return;
    }

    try {
      this.createUserChart();
      this.createProjectChart();
      this.createPhaseChart();
      this.createFeedbackGauge();
      this.createCommitsGauge();
    } catch (error) {
      console.error('Error initializing charts:', error);
      this.chartError = 'Failed to initialize charts.';
    }
  }

  private createUserChart() {
    const userCanvas = document.getElementById('user-chart') as HTMLCanvasElement;
    if (!userCanvas || !this.stats) return;

    new Chart(userCanvas, {
      type: 'bar',
      data: {
        labels: ['Professors', 'Students', 'Admins'],
        datasets: [{
          label: 'User Count',
          data: [this.stats.totalProfessors, this.stats.totalStudents, this.stats.totalAdmins],
          backgroundColor: [this.CHART_COLORS.sky, this.CHART_COLORS.emerald, this.CHART_COLORS.amber],
          borderRadius: 4,
          barPercentage: 0.6,
        }],
      },
      options: this.getdefaultChartOptions('User Roles', 'Count'),
    });
  }

  private createProjectChart() {
    const projectCanvas = document.getElementById('project-chart') as HTMLCanvasElement;
    if (!projectCanvas || !this.stats) return;
    
    const remaining = this.stats.totalProjects - this.stats.activeProjects - this.stats.completedProjects;

    new Chart(projectCanvas, {
      type: 'doughnut',
      data: {
        labels: ['Active', 'Completed', 'Other'],
        datasets: [{
          data: [this.stats.activeProjects, this.stats.completedProjects, remaining > 0 ? remaining : 0],
          backgroundColor: [this.CHART_COLORS.sky, this.CHART_COLORS.emerald, this.CHART_COLORS.lightGray],
          borderColor: '#fff',
          borderWidth: 4,
        }],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        cutout: '70%',
        plugins: {
          legend: { position: 'bottom', labels: { padding: 20, usePointStyle: true, pointStyle: 'rectRounded' } },
          tooltip: { enabled: true },
          doughnutText: { // Custom plugin options
            display: true,
            text: this.stats.totalProjects,
            color: this.CHART_COLORS.emerald,
            size: '40px',
            font: 'bold'
          }
        },
      },
    });
  }

  private createPhaseChart() {
    const phaseCanvas = document.getElementById('phase-chart') as HTMLCanvasElement;
    if (!phaseCanvas || !this.stats) return;

    new Chart(phaseCanvas, {
      type: 'polarArea',
      data: {
        labels: Object.keys(this.stats.phasesByStatus),
        datasets: [{
          data: Object.values(this.stats.phasesByStatus),
          backgroundColor: [
            this.CHART_COLORS.sky, 
            this.CHART_COLORS.emerald, 
            this.CHART_COLORS.amber, 
            this.CHART_COLORS.rose, 
            this.CHART_COLORS.indigo
          ],
          borderColor: '#fff',
          borderWidth: 2,
        }],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: { r: { grid: { circular: true } } },
        plugins: {
          legend: { position: 'bottom', labels: { padding: 20, usePointStyle: true, pointStyle: 'rectRounded' } },
          tooltip: { enabled: true },
        },
      },
    });
  }

  private createFeedbackGauge() {
    const gaugeCanvas = document.getElementById('feedback-gauge') as HTMLCanvasElement;
    if (!gaugeCanvas || !this.stats) return;

    const score = this.stats.averageFeedbackScore || 0;

    new Chart(gaugeCanvas, {
      type: 'doughnut',
      data: {
        labels: ['Score', 'Remaining'],
        datasets: [{
          data: [score, 5 - score],
          backgroundColor: [this.CHART_COLORS.amber, this.CHART_COLORS.lightGray],
          borderColor: 'transparent',
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        circumference: 180,
        rotation: -90,
        cutout: '75%',
        plugins: {
          legend: { display: false },
          tooltip: { enabled: false },
          doughnutText: {
            display: true,
            text: new DecimalPipe('en-US').transform(score, '1.1-2'),
            color: this.CHART_COLORS.amber,
            size: '30px',
            font: 'bold'
          }
        }
      }
    });
  }

  private createCommitsGauge() {
    const gaugeCanvas = document.getElementById('commits-gauge') as HTMLCanvasElement;
    if (!gaugeCanvas || !this.stats) return;

    const avgCommits = this.stats.averageCommitsPerProject || 0;
    const maxCommits = Math.max(10, Math.ceil(avgCommits));

    new Chart(gaugeCanvas, {
      type: 'doughnut',
      data: {
        labels: ['Avg Commits', ''],
        datasets: [{
          data: [avgCommits, maxCommits - avgCommits],
          backgroundColor: [this.CHART_COLORS.rose, this.CHART_COLORS.lightGray],
          borderColor: 'transparent',
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        circumference: 180,
        rotation: -90,
        cutout: '75%',
        plugins: {
          legend: { display: false },
          tooltip: { enabled: false },
           doughnutText: {
            display: true,
            text: new DecimalPipe('en-US').transform(avgCommits, '1.1-1'),
            color: this.CHART_COLORS.rose,
            size: '30px',
            font: 'bold'
          }
        }
      }
    });
  }

  private getdefaultChartOptions(xTitle: string, yTitle: string) {
    return {
      responsive: true,
      maintainAspectRatio: false,
      scales: {
        y: {
          beginAtZero: true,
          title: { display: true, text: yTitle, font: { size: 14 } },
          grid: { drawOnChartArea: false },
          ticks: { precision: 0 }
        },
        x: {
          title: { display: true, text: xTitle, font: { size: 14 } },
          grid: { display: false }
        },
      },
      plugins: {
        legend: { display: false },
        tooltip: {
          enabled: true,
          backgroundColor: '#333',
          titleFont: { size: 14 },
          bodyFont: { size: 12 },
          padding: 10,
          cornerRadius: 4,
        },
      },
    };
  }
}