package com.serfinsa.backend.auth.service;

import com.serfinsa.backend.auth.dto.AuthResponse;
import com.serfinsa.backend.auth.dto.LoginRequest;
import com.serfinsa.backend.auth.dto.RegisterRequest;
import com.serfinsa.backend.auth.entity.User;
import com.serfinsa.backend.auth.enums.Role;
import com.serfinsa.backend.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthResponse register(RegisterRequest request) {
        
        Role role = Role.valueOf(request.getRole().toUpperCase());

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build(); //Se genera el objeto User

        userRepository.save(user); //Se manda guardar el Usuario a la base

        String token = jwtService.generateToken(user.getEmail()); //Mandamos a generar el JWT
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token);
    }
}
