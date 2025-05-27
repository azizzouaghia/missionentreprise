import { Component, AfterViewInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Stats } from '../../../models/stats';
import { MatCardModule } from '@angular/material/card';
import { CommonModule } from '@angular/common';

// Declare Chart as a global variable from the CDN
declare var Chart: any;

@Component({
  selector: 'app-stats-dashboard',
  standalone: true,
  imports: [CommonModule, MatCardModule],
  templateUrl: './stats-dashboard.component.html',
})
export class StatsDashboardComponent implements AfterViewInit {
  stats: Stats | null = null;
  chartError: string | null = null;

  constructor(private http: HttpClient) {}

ngAfterViewInit() {
  this.http.get<Stats>('http://localhost:8089/api/stats').subscribe({
    next: (data) => {
      this.stats = data;
      // Use setTimeout to ensure the view is updated first
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
      // User Types Bar Chart
      const userCanvas = document.getElementById('user-timeline') as HTMLCanvasElement;
      if (!userCanvas) throw new Error('User chart canvas not found');
      const userChart = new Chart(userCanvas, {
        type: 'bar',
        data: {
          labels: ['Professors', 'Students', 'Admins'],
          datasets: [{
            label: 'Users',
            data: [this.stats.totalProfessors, this.stats.totalStudents, this.stats.totalAdmins],
            backgroundColor: ['#dc2626', '#2563eb', '#16a34a'],
            borderColor: ['#b91c1c', '#1e40af', '#15803d'],
            borderWidth: 1,
          }],
        },
        options: {
          responsive: true,
          plugins: {
            legend: { display: false },
            tooltip: { enabled: true },
          },
          scales: {
            y: { beginAtZero: true, title: { display: true, text: 'Count' } },
            x: { title: { display: true, text: 'User Type' } },
          },
        },
      });

      // Phase Status Pie Chart
      const phaseCanvas = document.getElementById('phase-timeline') as HTMLCanvasElement;
      if (!phaseCanvas) throw new Error('Phase chart canvas not found');
      const phaseChart = new Chart(phaseCanvas, {
        type: 'pie',
        data: {
          labels: Object.keys(this.stats.phasesByStatus),
          datasets: [{
            data: Object.values(this.stats.phasesByStatus),
            backgroundColor: ['#dc2626', '#2563eb', '#16a34a', '#eab308', '#f3f4f6'],
            borderColor: ['#b91c1c', '#1e40af', '#15803d', '#ca8a04', '#d1d5db'],
            borderWidth: 1,
          }],
        },
        options: {
          responsive: true,
          plugins: {
            legend: { position: 'bottom' },
            tooltip: { enabled: true },
          },
        },
      });

      // Project Status Doughnut Chart
      const projectCanvas = document.getElementById('project-timeline') as HTMLCanvasElement;
      if (!projectCanvas) throw new Error('Project chart canvas not found');
      const remainingProjects = this.stats.totalProjects - this.stats.activeProjects - this.stats.completedProjects;
      const projectChart = new Chart(projectCanvas, {
        type: 'doughnut',
        data: {
          labels: ['Active', 'Completed', 'Other'],
          datasets: [{
            data: [this.stats.activeProjects, this.stats.completedProjects, remainingProjects],
            backgroundColor: ['#2563eb', '#16a34a', '#6b7280'],
            borderColor: ['#1e40af', '#15803d', '#4b5563'],
            borderWidth: 1,
          }],
        },
        options: {
          responsive: true,
          plugins: {
            legend: { position: 'bottom' },
            tooltip: { enabled: true },
          },
        },
      });

      // Performance Metrics Line Chart
      const metricsCanvas = document.getElementById('metrics-timeline') as HTMLCanvasElement;
      if (!metricsCanvas) throw new Error('Metrics chart canvas not found');
      const metricsChart = new Chart(metricsCanvas, {
        type: 'line',
        data: {
          labels: ['Metric'], // Placeholder, as no time-series data
          datasets: [
            {
              label: 'Avg Feedback Score',
              data: [this.stats.averageFeedbackScore],
              borderColor: '#dc2626',
              backgroundColor: '#dc2626',
              fill: false,
              tension: 0.4,
            },
            {
              label: 'Avg Commits per Project',
              data: [this.stats.averageCommitsPerProject],
              borderColor: '#2563eb',
              backgroundColor: '#2563eb',
              fill: false,
              tension: 0.4,
            },
          ],
        },
        options: {
          responsive: true,
          plugins: {
            legend: { position: 'top' },
            tooltip: { enabled: true },
          },
          scales: {
            y: { beginAtZero: true, title: { display: true, text: 'Value' } },
            x: { title: { display: true, text: 'Metric' } },
          },
        },
      });
    } catch (error) {
      console.error('Error initializing charts:', error);
      this.chartError = 'Failed to initialize charts. Displaying data as text.';
    }
  }
}