package com.controller;

import com.dto.PhaseDTO;
import com.service.PhaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phases")
public class PhaseController {
    private final PhaseService phaseService;

    public PhaseController(PhaseService phaseService) {
        this.phaseService = phaseService;
    }

    @PostMapping
    public ResponseEntity<PhaseDTO> createPhase(@RequestBody PhaseDTO phaseDTO) {
        PhaseDTO createdPhase = phaseService.createPhase(phaseDTO);
        return ResponseEntity.ok(createdPhase);
    }

    @GetMapping
    public ResponseEntity<List<PhaseDTO>> getAllPhases() {
        List<PhaseDTO> phases = phaseService.getAllPhases();
        return ResponseEntity.ok(phases);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhaseDTO> getPhaseById(@PathVariable Long id) {
        PhaseDTO phase = phaseService.getPhaseById(id);
        return ResponseEntity.ok(phase);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<PhaseDTO>> getPhasesByProjectId(@PathVariable Long projectId) {
        List<PhaseDTO> phases = phaseService.getPhasesByProjectId(projectId);
        return ResponseEntity.ok(phases);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhaseDTO> updatePhase(@PathVariable Long id, @RequestBody PhaseDTO phaseDTO) {
        PhaseDTO updatedPhase = phaseService.updatePhase(id, phaseDTO);
        return ResponseEntity.ok(updatedPhase);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhase(@PathVariable Long id) {
        phaseService.deletePhase(id);
        return ResponseEntity.noContent().build();
    }
}