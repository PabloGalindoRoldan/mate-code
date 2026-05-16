package com.parque_industrial.services;

import com.parque_industrial.dto.auth.LoginRequest;
import com.parque_industrial.config.JwtUtil;
import com.parque_industrial.dto.auth.LoginResponse;
import com.parque_industrial.dto.auth.RegisterRequest;
import com.parque_industrial.entities.Empresa;
import com.parque_industrial.entities.Rol;
import com.parque_industrial.entities.Usuario;
import com.parque_industrial.persistence.empresa.EmpresaDAO;
import com.parque_industrial.persistence.usuario.UsuarioDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final EmpresaDAO empresaDAO;
    private final UsuarioDAO usuarioDAO;
    private final JwtUtil jwtUtil;

    public AuthService(UsuarioDAO usuarioDAO, EmpresaDAO empresaDAO, JwtUtil jwtUtil) {
        this.usuarioDAO = usuarioDAO;
        this.empresaDAO = empresaDAO;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest request) {
        // 1. Fetch user data (Assuming this method returns user details or entity)
        LoginResponse userDetails;
        try {
            userDetails = usuarioDAO.buscarLoginPorNombreUsuario(request.nombreUsuario());
        } catch (Exception e) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos");
        }

        // 2. Validate Password (Reminder: Plan to add BCrypt soon!)
        if (!userDetails.contrasena().equals(request.password())) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos");
        }

        // 3. Generate the token
        String token = jwtUtil.generateToken(userDetails.nombreUsuario(), userDetails.rol());

        // 4. Return the complete package to the controller
        return new LoginResponse(
                userDetails.nombreUsuario(),
                userDetails.nombre(),
                userDetails.apellido(),
                userDetails.email(),
                userDetails.cuit(),
                userDetails.rol(),
                null,
                userDetails.empresa(),
                token);
    }

    @Transactional
    public void registerRepresenteEmpresa(RegisterRequest request) {
        // Validar que las contraseñas coincidan antes de hacer cualquier cosa
        if (!request.password().equals(request.confirmarPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }
        Empresa empresa = new Empresa(request.cuitEmpresa(), request.razonSocialEmpresa(), false);
        empresaDAO.guardar(empresa);

        Usuario usuario = new Usuario(
                request.nombre(),
                request.apellido(),
                request.email(),
                request.nombreUsuario(),
                request.cuitUsuario(),
                Rol.REPRESENTANTE_EMPRESA,
                request.password(),
                empresa);
        usuarioDAO.guardar(usuario);
    }

    public void registerAdministradorParque(RegisterRequest request) {

        Usuario usuario = new Usuario(
                request.nombre(),
                request.apellido(),
                request.email(),
                request.nombreUsuario(),
                request.cuitUsuario(),
                Rol.ADMINISTRADOR_PARQUE,
                request.password());
        usuarioDAO.guardar(usuario);
    }

}