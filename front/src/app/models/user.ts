export interface User {
  id: number;
  username: string;
  email: string;
  role: 'ADMIN' | 'PROF' | 'ETUDIANT';
  matricule?: string;
  niveau?: string;
  specialite?: string;
  bureau?: string;
}