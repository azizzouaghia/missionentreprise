package com.esprit.tn.aziz_zouaghia_tpfoyer.controller;
import com.esprit.tn.aziz_zouaghia_tpfoyer.entity.Feedback;
import com.esprit.tn.aziz_zouaghia_tpfoyer.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackRepository feedbackRepository;

    @PostMapping
    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedback) {
        return ResponseEntity.ok(feedbackRepository.save(feedback));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Feedback>> getProjectFeedback(@PathVariable Long projectId) {
        return ResponseEntity.ok(feedbackRepository.findByProjectId(projectId));
    }
}
