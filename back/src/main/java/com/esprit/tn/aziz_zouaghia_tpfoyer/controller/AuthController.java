package com.esprit.tn.aziz_zouaghia_tpfoyer.controller;
import com.esprit.tn.aziz_zouaghia_tpfoyer.dto.AuthRequest;
import com.esprit.tn.aziz_zouaghia_tpfoyer.dto.AuthResponse;
import com.esprit.tn.aziz_zouaghia_tpfoyer.dto.RegisterRequest;
import com.esprit.tn.aziz_zouaghia_tpfoyer.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}