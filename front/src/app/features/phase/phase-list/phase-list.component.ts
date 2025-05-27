import { Component, OnInit } from '@angular/core';
import { PhaseService } from '../../../services/phase.service';
import { Phase } from '../../../models/phase';
import { Router } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-phase-list',
  standalone: true,
  imports: [MatTableModule, MatButtonModule],
  templateUrl: './phase-list.component.html',
})
export class PhaseListComponent implements OnInit {
  phases: Phase[] = [];
  displayedColumns: string[] = ['id', 'name', 'status', 'projectId', 'actions'];
  snackBar: any;

  constructor(private phaseService: PhaseService, private router: Router) { }

  ngOnInit() {
    this.phaseService.getAllPhases().subscribe(phases => this.phases = phases);
  }

  editPhase(id: number) {
    this.router.navigate(['/phases/edit', id]);
  }
  navigateToCreate() {
    console.log('Navigating to /phases/create');
    this.router.navigate(['/phases/create']).catch(err => {
      console.error('Navigation failed:', err);
      this.snackBar.open('Navigation to create user failed', 'Close', { duration: 3000 });
    });
  }
  deletePhase(id: number) {
    if (confirm('Are you sure you want to delete this phase?')) {
      this.phaseService.deletePhase(id).subscribe(() => {
        this.phases = this.phases.filter(phase => phase.id !== id);
      });
    }
  }
}