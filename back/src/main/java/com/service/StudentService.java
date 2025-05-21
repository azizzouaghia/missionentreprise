package com.service;

import com.dto.StudentDTO;
import com.entity.Role;
import com.entity.Student;
import com.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public StudentDTO createStudent(StudentDTO studentDTO) {
        if (userRepository.findByEmail(studentDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + studentDTO.getEmail());
        }

        Student student = studentDTO.toEntity();
        student.setRole(Role.ETUDIANT);
        student.setPassword(passwordEncoder.encode("defaultPassword")); // Default password, adjust as needed
        Student savedStudent = userRepository.save(student);
        return StudentDTO.fromEntity(savedStudent);
    }

    public List<StudentDTO> getAllStudents() {
        return userRepository.findByRole(Role.ETUDIANT).stream()
                .map(student -> StudentDTO.fromEntity((Student) student))
                .collect(Collectors.toList());
    }

    public StudentDTO getStudentById(Long id) {
        Student student = (Student) userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        if (student.getRole() != Role.ETUDIANT) {
            throw new RuntimeException("User is not a student: " + id);
        }
        return StudentDTO.fromEntity(student);
    }

    public List<StudentDTO> searchStudents(String query) {
        return userRepository.searchStudents(query).stream()
                .map(student -> StudentDTO.fromEntity((Student) student))
                .collect(Collectors.toList());
    }

    @Transactional
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        Student existingStudent = (Student) userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        if (existingStudent.getRole() != Role.ETUDIANT) {
            throw new RuntimeException("User is not a student: " + id);
        }

        existingStudent.setUsername(studentDTO.getUsername());
        existingStudent.setEmail(studentDTO.getEmail());
        existingStudent.setMatricule(studentDTO.getMatricule());
        existingStudent.setNiveau(studentDTO.getNiveau());

        Student updatedStudent = userRepository.save(existingStudent);
        return StudentDTO.fromEntity(updatedStudent);
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = (Student) userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        if (student.getRole() != Role.ETUDIANT) {
            throw new RuntimeException("User is not a student: " + id);
        }
        userRepository.delete(student);
    }
}