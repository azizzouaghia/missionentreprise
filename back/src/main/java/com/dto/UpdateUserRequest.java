package com.dto;

import com.entity.Role;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private String username;
    private String email;   
    private Role role;
    private String specialite; // For professors
    private String bureau; // For professors
    private String matricule; // For students
    private String niveau; // For students
}