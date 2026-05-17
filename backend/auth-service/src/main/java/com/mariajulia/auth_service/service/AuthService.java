package com.mariajulia.auth_service.service;

import com.mariajulia.auth_service.config.JwtProperties;
import com.mariajulia.auth_service.dto.request.LoginRequest;
import com.mariajulia.auth_service.dto.request.RegisterRequest;
import com.mariajulia.auth_service.dto.response.LoginResponse;
import com.mariajulia.auth_service.dto.response.UserResponse;
import com.mariajulia.auth_service.exception.EmailAlreadyExistsException;
import com.mariajulia.auth_service.exception.InsufficientRoleException;
import com.mariajulia.auth_service.enums.Role;
import com.mariajulia.auth_service.entity.User;
import com.mariajulia.auth_service.exception.InvalidCredentialsException;
import com.mariajulia.auth_service.mapper.UserMapper;
import com.mariajulia.auth_service.repository.UserRepository;
import com.mariajulia.auth_service.security.JwtProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties jwtProperties;
    private final PasswordService passwordService;

    public AuthService(UserRepository userRepository,
                       JwtProvider jwtProvider,
                       PasswordEncoder passwordEncoder,
                       JwtProperties jwtProperties, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.jwtProperties = jwtProperties;
        this.passwordService = passwordService;

    }

    public UserResponse registerPatient(RegisterRequest request, String callerRole) {
        requireAnyRole(callerRole, Role.SECRETARY, Role.ADMIN);
        return registerUser(request, Role.PATIENT);
    }

    public UserResponse registerDoctor(RegisterRequest request, String callerRole) {
        requireAnyRole(callerRole, Role.ADMIN);
        return registerUser(request, Role.DOCTOR);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> {
                    log.warn("Email ou senha incorretos.");
                    return new InvalidCredentialsException();
                });

        if (!user.isActive()) {
            log.warn("Usuário inativo.");
            throw new InvalidCredentialsException();
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            log.warn("Email ou senha incorretos.");
            throw new InvalidCredentialsException();
        }

        String token = jwtProvider.generateToken(user.getId(), user.getEmail(), user.getRole().name());

        log.info("Login bem sucedido.");

        return LoginResponse.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .type("Bearer")
                .expiresIn(jwtProperties.getExpiration())
                .build();
    }

    private UserResponse registerUser(RegisterRequest request, Role role) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        String hashedPassword = passwordService.encodePassword(request.password());

        User user = userRepository.save(UserMapper.toUser(request, hashedPassword, role));

        log.info("User registered: userId={}, role={}", user.getId(), role);
        return UserMapper.toUserResponse(user);
    }

    private void requireAnyRole(String callerRole, Role... allowedRoles) {
        if (callerRole == null || callerRole.isBlank()) {
            throw new InsufficientRoleException("Access denied: no role provided");
        }
        try {
            Role caller = Role.valueOf(callerRole.toUpperCase());
            for (Role allowed : allowedRoles) {
                if (caller == allowed) return;
            }
        } catch (IllegalArgumentException e) {
            throw new InsufficientRoleException("Access denied: invalid role");
        }
        throw new InsufficientRoleException("Access denied: insufficient role");
    }

}
