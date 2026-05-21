package com.parque_industrial.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 1. Check if the Authorization header exists and has a Bearer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extract token (skip "Bearer " prefix which is 7 characters long)
        jwt = authHeader.substring(7);
        try {
            username = jwtUtil.extractUsername(jwt);

            // 3. If there is a valid user context and they aren't authenticated yet
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                if (jwtUtil.isTokenValid(jwt, username)) {
                    String rol = jwtUtil.extractRol(jwt);

                    // VALIDACIÓN COMPATIBLE: Asegura el prefijo "ROLE_" exigido por .hasRole()
                    // Si por algún motivo el string del token ya dice "ROLE_ADMIN", lo usa directo.
                    // Si dice "REPRESENTANTE_EMPRESA", lo transforma en
                    // "ROLE_REPRESENTANTE_EMPRESA".
                    String formattedRole = rol.startsWith("ROLE_") ? rol : "ROLE_" + rol;

                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(formattedRole);

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            List.of(authority));

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 4. Authenticate the user globally for this specific request thread
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // If the token is tampered with, expired, or invalid, do not authenticate
            logger.error("Could not set user authentication in security context", e);
        }

        filterChain.doFilter(request, response);
    }
}