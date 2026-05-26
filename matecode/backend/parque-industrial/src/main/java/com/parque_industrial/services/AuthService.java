package com.parque_industrial.services;

import com.parque_industrial.dto.auth.ChangePasswordRequest;
import com.parque_industrial.dto.auth.LoginRequest;
import com.parque_industrial.config.JwtUtil;
import com.parque_industrial.dto.auth.LoginResponse;
import com.parque_industrial.dto.auth.RegisterRequest;
import com.parque_industrial.entities.Empresa;
import com.parque_industrial.entities.Rol;
import com.parque_industrial.entities.Usuario;
import com.parque_industrial.persistence.empresa.EmpresaDAO;
import com.parque_industrial.persistence.usuario.UsuarioDAO;
import org.springframework.security.crypto.password.PasswordEncoder; // <-- Importante
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final EmpresaDAO empresaDAO;
    private final UsuarioDAO usuarioDAO;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioDAO usuarioDAO, EmpresaDAO empresaDAO, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.usuarioDAO = usuarioDAO;
        this.empresaDAO = empresaDAO;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request) {
        LoginResponse userDetails;
        try {
            userDetails = usuarioDAO.buscarLoginPorNombreUsuario(request.nombreUsuario());
        } catch (Exception e) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos");
        }
        if (!passwordEncoder.matches(request.password(), userDetails.contrasena())) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos");
        }
        String subjectIdentifier = userDetails.nombreUsuario();
        String token = jwtUtil.generateToken(subjectIdentifier, userDetails.rol());
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
        if (!request.password().equals(request.confirmarPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        Empresa empresa = new Empresa(request.cuitEmpresa(), request.razonSocialEmpresa(), true);
        empresaDAO.guardar(empresa);
        String passwordHasheada = passwordEncoder.encode(request.password());
        Usuario usuario = new Usuario(
                request.nombre(),
                request.apellido(),
                request.email(),
                request.nombreUsuario(),
                request.cuitUsuario(),
                Rol.REPRESENTANTE_EMPRESA,
                passwordHasheada,
                empresa);
        usuarioDAO.guardar(usuario);
    }

    public void registerAdministradorParque(RegisterRequest request) {
        String passwordHasheada = passwordEncoder.encode(request.password());
        Usuario usuario = new Usuario(
                request.nombre(),
                request.apellido(),
                request.email(),
                request.nombreUsuario(),
                request.cuitUsuario(),
                Rol.ADMINISTRADOR_PARQUE,
                passwordHasheada);
        usuarioDAO.guardar(usuario);
    }

    public void registerUsuarioEmpresaExistente(RegisterRequest request) {
        if (!empresaDAO.existeEmpresa(request.cuitEmpresa())) {
            throw new IllegalArgumentException("La empresa con CUIT " + request.cuitEmpresa() + " no existe");
        }

        Empresa empresa = new Empresa(request.cuitEmpresa(), "no es importante", false);
        String passwordHasheada = passwordEncoder.encode(request.password());
        Usuario usuario = new Usuario(
                request.nombre(),
                request.apellido(),
                request.email(),
                request.nombreUsuario(),
                request.cuitUsuario(),
                Rol.REPRESENTANTE_EMPRESA,
                passwordHasheada,
                empresa);
        usuarioDAO.guardar(usuario);
    }
    @Transactional
    public void changePassword(String username, ChangePasswordRequest request) {
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("Las nuevas contraseñas no coinciden");
        }
        Usuario usuario = usuarioDAO
                .buscarPorNombreUsuario(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        if (!passwordEncoder.matches(request.currentPassword(), usuario.getContraseña())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }
        String nuevaPasswordHasheada = passwordEncoder.encode(request.newPassword());

        usuarioDAO.actualizarPassword(
                username,
                nuevaPasswordHasheada);
    }
}