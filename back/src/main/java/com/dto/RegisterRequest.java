package com.dto;
import com.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private Role role;
    private String specialite; // For professors
    private String bureau; // For professors
    private String matricule; // For students
    private String niveau; // For students
}
