package com.esprit.tn.aziz_zouaghia_tpfoyer.service;

import com.esprit.tn.aziz_zouaghia_tpfoyer.dto.AuthRequest;
import com.esprit.tn.aziz_zouaghia_tpfoyer.dto.AuthResponse;
import com.esprit.tn.aziz_zouaghia_tpfoyer.dto.RegisterRequest;
import com.esprit.tn.aziz_zouaghia_tpfoyer.entity.Professor;
import com.esprit.tn.aziz_zouaghia_tpfoyer.entity.Role;
import com.esprit.tn.aziz_zouaghia_tpfoyer.entity.Student;
import com.esprit.tn.aziz_zouaghia_tpfoyer.entity.User;
import com.esprit.tn.aziz_zouaghia_tpfoyer.repository.UserRepository;
import com.esprit.tn.aziz_zouaghia_tpfoyer.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        logger.info("Registering user with email: {}", request.getEmail());

        // Check for existing email
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            logger.error("Email already exists: {}", request.getEmail());
            throw new IllegalArgumentException("Email already exists");
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
            logger.error("Invalid role: {}", role);
            throw new IllegalArgumentException("Invalid role: " + role);
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        User savedUser = userRepository.save(user);
        logger.info("User saved with email: {}", savedUser.getEmail());

        String jwtToken = jwtService.generateToken((UserDetails) savedUser);
        return new AuthResponse(jwtToken);
    }

    public AuthResponse authenticate(AuthRequest request) {
        logger.info("Authenticating user with email: {}", request.getEmail());
        
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );
        
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        String jwtToken = jwtService.generateToken((UserDetails) user);
        logger.info("Authentication successful for email: {}", user.getEmail());
        return new AuthResponse(jwtToken);
    }
}