package com.serfinsa.backend.auth.controller;

import com.serfinsa.backend.auth.dto.AuthResponse;
import com.serfinsa.backend.auth.dto.LoginRequest;
import com.serfinsa.backend.auth.dto.RegisterRequest;
import com.serfinsa.backend.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST que maneja las solicitudes de autenticación: login y registro.
 */
@RestController
@RequestMapping("/auth") // Ruta base
@RequiredArgsConstructor
public class AuthController {

    // Inyección de dependencia del servicio que contiene la lógica de autenticación
    private final AuthService authService;

    /**
     * Endpoint para registrar usuarios.
     * @param request contiene email, contraseña y rol ("USER" o "ADMIN")
     * @return token JWT generado para el nuevo usuario
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        // Llama al servicio para registrar el usuario y generar un token
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response); // Devuelve HTTP 200 con el token
    }

    /**
     * Endpoint para login de usuarios existentes.
     * @param request contiene email y contraseña
     * @return token JWT si las credenciales son válidas
     */
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // Llama al servicio para validar las credenciales y generar el token
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response); // Devuelve HTTP 200 con el token
    }
}
