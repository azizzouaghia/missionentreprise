package com.controller;

import com.dto.FeedbackRequest;
import com.dto.FeedbackResponse;
import com.entity.Feedback;
import com.entity.Project;
import com.entity.Role;
import com.entity.User;
import com.repository.FeedbackRepository;
import com.repository.ProjectRepository;
import com.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackRepository feedbackRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<FeedbackResponse> createFeedback(@RequestBody FeedbackRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + request.getProjectId()));

        User professor = userRepository.findById(request.getProfessorId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getProfessorId()));

        if (!professor.getRole().equals(Role.PROF)) {
            throw new RuntimeException("User with id " + request.getProfessorId() + " is not a professor");
        }

        Feedback feedback = new Feedback();
        feedback.setCommentaire(request.getCommentaire());
        feedback.setNote(request.getNote());
        feedback.setDate(LocalDate.now());
        feedback.setProject(project);
        feedback.setProfessor(professor);

        Feedback savedFeedback = feedbackRepository.save(feedback);

        FeedbackResponse response = FeedbackResponse.builder()
                .id(savedFeedback.getId())
                .commentaire(savedFeedback.getCommentaire())
                .note(savedFeedback.getNote())
                .date(savedFeedback.getDate())
                .projectId(savedFeedback.getProject().getId())
                .professorId(savedFeedback.getProfessor().getId())
                .professorName(savedFeedback.getProfessor().getUsername())
                .build();

        return ResponseEntity.ok(response);
    }
}