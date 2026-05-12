package com.parque_industrial.controllers;

import com.parque_industrial.dto.auth.RegisterRequest;
import com.parque_industrial.services.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



    @RestController //esta clase va a recibir requests HTTP y devolver respuestas HTTP
    // esto permite q spring la registre, escuche endopoints, etc

    @RequestMapping("/login") //ruta base
    public class LoginController {

        private final AuthService authService;

        public LoginController(AuthService authService) {
            this.authService = authService;
        }

//        @PostMapping("/iniciarSesion")
//        public  login(@RequestBody LoginRequest request) { //el request
//            return authService.login(request);
//        }

        /*
        Esto le llegaria algo como:
        "email": "german@mail.com",
        "password": "1234"
         deberia devolver algo?
         */

        @PostMapping("/registrarse") //si llega un POST a /register, este metodo se ejecuta
        public void registerRepresenteEmpresa(@RequestBody RegisterRequest request) {
            authService.registerRepresenteEmpresa(request);
        }

//        @PostMapping("/registrarse") //si llega un POST a /register, este metodo se ejecuta
//        public void registerAdministradorParque(@RequestBody RegisterRequest request) {
//            authService.registerAdministradorParque(request);
//        }
//
//        public void registerAdministradorSistema(@RequestBody RegisterRequest request) {
//            authService.registerAdministradorSistema(request);
//        }
    } //500 INTERNAL SERVER ERROR

//LoginResponse
//RegisterResponse
/*
login,
logout,
refresh token,
registro,
reset password.
 */

