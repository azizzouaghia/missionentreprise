export interface ProjectStats {
  commitCount: number;
  averageFeedbackScore: number;
  feedbackCount: number;
  teamSize: number;
  phaseCount: number;
  phasesByStatus: { [key: string]: number };
}