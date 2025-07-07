package com.repository;

import com.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByPhaseId(Long phaseId); // Changed from findByProjectId
    List<Feedback> findByProfessorId(Long professorId);

    @Query("SELECT AVG(f.note) FROM Feedback f")
    Double avgNote();

    @Query("SELECT AVG(f.note) FROM Feedback f WHERE f.phase.project.id = :projectId")
    Double avgNoteByProjectId(@Param("projectId") Long projectId);
    
    // Find all feedback for a list of phases, useful for project-level queries
    List<Feedback> findByPhaseIdIn(List<Long> phaseIds);
}