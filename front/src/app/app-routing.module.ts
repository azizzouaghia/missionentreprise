import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './guards/auth.guards';

export const routes: Routes = [
  { path: '', redirectTo: 'stats', pathMatch: 'full' },
  { path: 'auth', loadChildren: () => import('./features/auth/auth.module').then(m => m.AuthModule) },
  { path: 'users', loadChildren: () => import('./features/user/user.module').then(m => m.UserModule), canActivate: [AuthGuard], data: { roles: ['ADMIN'] } },
  { path: 'projects', loadChildren: () => import('./features/project/project.module').then(m => m.ProjectModule), canActivate: [AuthGuard], data: { roles: ['ADMIN', 'PROF', 'ETUDIANT'] } },
  { path: 'feedback', loadChildren: () => import('./features/feedback/feedback.module').then(m => m.FeedbackModule), canActivate: [AuthGuard], data: { roles: ['ADMIN', 'PROF', 'ETUDIANT'] } },
  { path: 'phases', loadChildren: () => import('./features/phase/phase.module').then(m => m.PhaseModule), canActivate: [AuthGuard], data: { roles: ['ADMIN', 'PROF'] } },
  { path: 'professors', loadChildren: () => import('./features/professor/professor.module').then(m => m.ProfessorModule), canActivate: [AuthGuard], data: { roles: ['ADMIN', 'PROF'] } },
  { path: 'students', loadChildren: () => import('./features/student/student.module').then(m => m.StudentModule), canActivate: [AuthGuard], data: { roles: ['ADMIN', 'ETUDIANT'] } },
  { path: 'gitlab', loadChildren: () => import('./features/gitlab/gitlab.module').then(m => m.GitlabModule), canActivate: [AuthGuard], data: { roles: ['ADMIN', 'PROF', 'ETUDIANT'] } },
  { path: 'stats', loadChildren: () => import('./features/stats/stats.module').then(m => m.StatsModule), canActivate: [AuthGuard], data: { roles: ['ADMIN', 'PROF', 'ETUDIANT'] } },
  { path: '**', redirectTo: 'stats' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }