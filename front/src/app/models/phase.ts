export interface Phase {
  id: number;
  name: string;
  description?: string;
  dateDebut?: string;
  dateFin?: string;
  status: 'PLANNED' | 'IN_PROGRESS' | 'COMPLETED' | 'ON_HOLD' | 'CANCELLED';
  projectId: number;
}