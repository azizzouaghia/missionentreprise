package com.service;

import com.dto.FeedbackResponse;
import com.entity.Feedback;
import com.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

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