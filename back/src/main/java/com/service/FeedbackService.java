package com.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional
    public FeedbackResponse createFeedback(FeedbackRequest request) {
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
        return convertToResponse(savedFeedback);
    }

    public List<FeedbackResponse> getAllFeedbacks() {
        return feedbackRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public FeedbackResponse getFeedbackById(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id: " + id));
        return convertToResponse(feedback);
    }

    public List<FeedbackResponse> getByProfessor(Long professorId) {
        return feedbackRepository.findByProfessorId(professorId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<FeedbackResponse> getByProject(Long projectId) {
        return feedbackRepository.findByProjectId(projectId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Double getAverageNoteByProjectId(Long projectId) {
        return feedbackRepository.avgNoteByProjectId(projectId);
    }

    public Double getAverageNote() {
        return feedbackRepository.avgNote();
    }

    @Transactional
    public FeedbackResponse updateFeedback(Long id, FeedbackRequest request) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id: " + id));

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + request.getProjectId()));

        User professor = userRepository.findById(request.getProfessorId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getProfessorId()));

        if (!professor.getRole().equals(Role.PROF)) {
            throw new RuntimeException("User with id " + request.getProfessorId() + " is not a professor");
        }

        feedback.setCommentaire(request.getCommentaire());
        feedback.setNote(request.getNote());
        feedback.setDate(LocalDate.now());
        feedback.setProject(project);
        feedback.setProfessor(professor);

        Feedback updatedFeedback = feedbackRepository.save(feedback);
        return convertToResponse(updatedFeedback);
    }

    @Transactional
    public void deleteFeedback(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id: " + id));
        feedbackRepository.delete(feedback);
    }

    private FeedbackResponse convertToResponse(Feedback feedback) {
        return FeedbackResponse.builder()
                .id(feedback.getId())
                .commentaire(feedback.getCommentaire())
                .note(feedback.getNote())
                .date(feedback.getDate())
                .projectId(feedback.getProject().getId())
                .professorId(feedback.getProfessor().getId())
                .professorName(feedback.getProfessor().getActualUsername())
                .build();
    }
}