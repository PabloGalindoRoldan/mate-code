package com.parque_industrial.controllers;

import com.parque_industrial.dto.auth.LoginRequest;
import com.parque_industrial.dto.auth.LoginResponse;
import com.parque_industrial.dto.auth.RegisterRequest;
import com.parque_industrial.services.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // esta clase va a recibir requests HTTP y devolver respuestas HTTP
// esto permite q spring la registre, escuche endopoints, etc

@RequestMapping("/auth") // ruta base
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Returns 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /*
     * Esto le llegaria algo como:
     * "email": "german@mail.com",
     * "password": "1234"
     * deberia devolver algo?
     */

    @PostMapping("/register")
    public ResponseEntity<String> registerRepresentanteEmpresa(@RequestBody RegisterRequest request) {
        try {
            authService.registerRepresenteEmpresa(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado con éxito"); // 201 Created
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // 400 Bad Request
        }
    }

     @PostMapping("/registerAdminParque")
     public ResponseEntity<String> registerAdministradorParque(@RequestBody RegisterRequest request)
     {
         try {
     authService.registerAdministradorParque(request);
             return ResponseEntity.status(HttpStatus.CREATED).body("Administrador registrado con éxito"); // 201 Created
         } catch (IllegalArgumentException e) {
             return ResponseEntity.badRequest().body(e.getMessage()); // 400 Bad Request
         }
     }

    // public void registerAdministradorSistema(@RequestBody RegisterRequest
    // request) {
    // authService.registerAdministradorSistema(request);
    // }
} // 500 INTERNAL SERVER ERROR

// LoginResponse
// RegisterResponse
/*
 * login,
 * logout,
 * refresh token,
 * registro,
 * reset password.
 */
