package com.esprit.tn.aziz_zouaghia_tpfoyer.repository;
import com.esprit.tn.aziz_zouaghia_tpfoyer.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByProjectId(Long projectId);
}
