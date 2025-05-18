package com.service;  // Fixed package declaration

import com.dto.UpdateUserRequest;
import com.dto.UserResponse;
import com.entity.Professor;
import com.entity.Role;
import com.entity.Student;
import com.entity.User;import com.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToResponse(user);
    }

    public List<UserResponse> searchUsers(String query) {
        List<User> users = userRepository.searchUsers(query);
        return users.stream()
                .map(user -> convertToResponse(user)) // Changed from method reference to lambda
                .collect(Collectors.toList());
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());

        if (user instanceof Professor prof && request.getRole() == Role.PROF) {
            prof.setSpecialite(request.getSpecialite());
            prof.setBureau(request.getBureau());
        } else if (user instanceof Student student && request.getRole() == Role.ETUDIANT) {
            student.setMatricule(request.getMatricule());
            student.setNiveau(request.getNiveau());
        }

        User savedUser = userRepository.save(user);
        return convertToResponse(savedUser);
    }

private UserResponse convertToResponse(User user) {
    UserResponse.UserResponseBuilder builder = UserResponse.builder()
            .id(user.getId())
            .username(user.getActualUsername()) // Make sure this method exists
            .email(user.getEmail())
            .role(user.getRole());

    if (user instanceof Professor prof) {
        builder.specialite(prof.getSpecialite())
              .bureau(prof.getBureau());
    } else if (user instanceof Student student) {
        builder.matricule(student.getMatricule())
              .niveau(student.getNiveau());
    }

    return builder.build();
}
}