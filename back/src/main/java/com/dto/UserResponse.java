package com.dto;

import com.entity.Role;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private String specialite;
    private String bureau;
    private String matricule;
    private String niveau;
}