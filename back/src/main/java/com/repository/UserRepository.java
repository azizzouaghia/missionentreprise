package com.repository;

import com.entity.User;
import com.entity.Role;  // Use your custom Role enum
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    
    // Single countByRole method with correct parameter type
    long countByRole(Role role);
    
    boolean existsByEmail(String email);
    List<User> findByRole(Role role);

    // Simplified query methods without type casting
    @Query("SELECT u FROM User u WHERE u.role = 'ETUDIANT'")
    List<User> findAllStudents();

    @Query("SELECT u FROM User u WHERE u.role = 'PROF'")
    List<User> findAllProfessors();

    // Fixed search query without type casting
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.username) LIKE LOWER(concat('%', :query, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(concat('%', :query, '%'))")
    List<User> searchUsers(@Param("query") String query);

    // Alternative search implementation for students if needed
    @Query("SELECT u FROM User u WHERE TYPE(u) = Student AND " +
           "(LOWER(u.username) LIKE LOWER(concat('%', :query, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(concat('%', :query, '%')) OR " +
           "LOWER(u.matricule) LIKE LOWER(concat('%', :query, '%')))")
    List<User> searchStudents(@Param("query") String query);
}