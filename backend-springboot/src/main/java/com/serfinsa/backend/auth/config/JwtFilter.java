package com.serfinsa.backend.auth.config;

import com.serfinsa.backend.auth.entity.User;
import com.serfinsa.backend.auth.repository.UserRepository;
import com.serfinsa.backend.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro personalizado que se ejecuta antes de cada petición.
 * Extrae el token JWT del encabezado, lo valida y autentica al usuario.
 */
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Leer el header "Authorization"
        final String authHeader = request.getHeader("Authorization");

        // Si no viene token o no empieza con "Bearer ", no se hace nada
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraer solo el token (sin la palabra Bearer)
        String jwt = authHeader.substring(7);

        // Obtener el email (subject) del token
        String userEmail = jwtService.extractUsername(jwt);

        // Verificar que el token no haya sido procesado aún
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Buscar el usuario por email
            User user = userRepository.findByEmail(userEmail).orElse(null);

            // Validar el token y el usuario
            if (user != null && jwtService.isTokenValid(jwt, user.getEmail())) {

                // Crear el objeto de autenticación con el usuario
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user, null, null);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Establecer el usuario autenticado en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
