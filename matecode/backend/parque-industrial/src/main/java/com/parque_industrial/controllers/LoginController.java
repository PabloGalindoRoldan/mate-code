package com.parque_industrial.controllers;

import com.parque_industrial.dto.auth.ChangePasswordRequest;
import com.parque_industrial.dto.auth.LoginRequest;
import com.parque_industrial.dto.auth.LoginResponse;
import com.parque_industrial.dto.auth.RegisterRequest;
import com.parque_industrial.services.AuthService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

@RestController

@RequestMapping("/auth")
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    // @PostMapping("/login")
    // public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request)
    // {
    // LoginResponse response = authService.login(request);
    // return ResponseEntity.ok(response);
    // }

    @PostMapping("/register")
    public ResponseEntity<String> registerRepresentanteEmpresa(@Valid @RequestBody RegisterRequest request) {
        authService.registerRepresenteEmpresa(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado con éxito");
    }

    @PostMapping("/registerAdminParque")
    public ResponseEntity<String> registerAdministradorParque(@Valid @RequestBody RegisterRequest request) {
        authService.registerAdministradorParque(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Administrador registrado con éxito");
    }

    @PostMapping("/registerExtraRepresentanteEmpresa")
    public ResponseEntity<String> registerUsuarioEmpresaExistente(@Valid @RequestBody RegisterRequest request) { // se le puede
                                                                                                          // mandar los
                                                                                                          // daots del
                                                                                                          // registro
                                                                                                          // comun menos
                                                                                                          // la razon
                                                                                                          // social q se
                                                                                                          // puede no
                                                                                                          // mandar
        authService.registerUsuarioEmpresaExistente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado con éxito");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword( @Valid
                                                      @RequestBody ChangePasswordRequest request,
            Authentication authentication) {

        authService.changePassword(
                authentication.getName(),
                request);

        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }

}
