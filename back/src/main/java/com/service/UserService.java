package com.service;

import com.dto.UpdateUserRequest;
import com.dto.UserResponse;
import com.entity.Professor;
import com.entity.Role;
import com.entity.Student;
import com.entity.User;
import com.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(UpdateUserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }

        User user;
        Role role = request.getRole();
        if (role == Role.ETUDIANT) {
            Student student = new Student();
            student.setMatricule(request.getMatricule());
            student.setNiveau(request.getNiveau());
            user = student;
        } else if (role == Role.PROF) {
            Professor professor = new Professor();
            professor.setSpecialite(request.getSpecialite());
            professor.setBureau(request.getBureau());
            user = professor;
        } else if (role == Role.ADMIN) {
            user = new User();
        } else {
            throw new RuntimeException("Invalid role: " + role);
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode("defaultPassword"));
        user.setRole(role);

        User savedUser = userRepository.save(user);
        return convertToResponse(savedUser);
    }

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

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return convertToResponse(user);
    }

    public List<UserResponse> getUsersByRole(Role role) {
        return userRepository.findByRole(role).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<UserResponse> searchUsers(String query) {
        List<User> users = userRepository.searchUsers(query);
        return users.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Transactional
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

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    private UserResponse convertToResponse(User user) {
        UserResponse.UserResponseBuilder builder = UserResponse.builder()
                .id(user.getId())
                .username(user.getActualUsername())
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