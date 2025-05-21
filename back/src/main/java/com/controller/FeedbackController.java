package com.controller;

import com.dto.FeedbackRequest;
import com.dto.FeedbackResponse;
import com.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackResponse> createFeedback(@RequestBody FeedbackRequest request) {
        return ResponseEntity.ok(feedbackService.createFeedback(request));
    }

    @GetMapping
    public ResponseEntity<List<FeedbackResponse>> getAllFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAllFeedbacks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedbackResponse> getFeedbackById(@PathVariable Long id) {
        return ResponseEntity.ok(feedbackService.getFeedbackById(id));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<FeedbackResponse>> getFeedbackByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(feedbackService.getByProject(projectId));
    }

    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<FeedbackResponse>> getFeedbackByProfessor(@PathVariable Long professorId) {
        return ResponseEntity.ok(feedbackService.getByProfessor(professorId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponse> updateFeedback(@PathVariable Long id, @RequestBody FeedbackRequest request) {
        return ResponseEntity.ok(feedbackService.updateFeedback(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }
}