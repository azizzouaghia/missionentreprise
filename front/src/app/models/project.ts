export interface Project {
  id: number;
  titre: string;
  description: string;
  dateDebut: string;
  dateFin?: string;
  gitlabProjectId?: string;
  professorId?: number;
  studentIds: number[];
  phaseIds: number[];
}