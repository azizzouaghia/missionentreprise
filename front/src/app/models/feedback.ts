export interface Feedback {
  id: number;
  commentaire: string;
  note: number;
  date: string;
  phaseId: number; // Changed from projectId
  professorId: number;
  professorName: string;
}