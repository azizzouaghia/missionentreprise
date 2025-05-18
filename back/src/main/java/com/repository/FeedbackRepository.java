package com.repository;

import com.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByProjectId(Long projectId);
    List<Feedback> findByProfessorId(Long professorId);
    @Query("SELECT AVG(f.note) FROM Feedback f")
    Double avgNote();
    @Query("SELECT AVG(f.note) FROM Feedback f WHERE f.project.id = :projectId")
    Double avgNoteByProjectId(@Param("projectId") Long projectId);
}