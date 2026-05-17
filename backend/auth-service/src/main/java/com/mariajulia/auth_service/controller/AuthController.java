package com.mariajulia.auth_service.controller;

import com.mariajulia.auth_service.dto.request.LoginRequest;
import com.mariajulia.auth_service.dto.request.RegisterRequest;
import com.mariajulia.auth_service.dto.response.LoginResponse;
import com.mariajulia.auth_service.dto.response.UserResponse;
import com.mariajulia.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register/patient")
    public ResponseEntity<UserResponse> registerPatient(
            @RequestHeader(value = "X-User-Role", required = false) String callerRole,
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.registerPatient(request, callerRole));
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<UserResponse> registerDoctor(
            @RequestHeader(value = "X-User-Role", required = false) String callerRole,
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.registerDoctor(request, callerRole));
    }

    @PostMapping("/register/secretary")
    public ResponseEntity<UserResponse> registerSecretary(
            @RequestHeader(value = "X-User-Role", required = false) String callerRole,
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.registerSecretary(request, callerRole));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }
}
