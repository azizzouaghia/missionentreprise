export interface Stats {
  totalProfessors: number;
  totalStudents: number;
  totalAdmins: number;
  totalProjects: number;
  activeProjects: number;
  completedProjects: number;
  projectCompletionRate: number;
  totalPhases: number;
  phasesByStatus: { [key: string]: number };
  averageFeedbackScore: number;
  averageCommitsPerProject: number;
  studentsAssigned: number;
  professorsWithProjects: number;
}