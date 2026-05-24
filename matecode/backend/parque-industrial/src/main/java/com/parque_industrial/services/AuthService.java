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
        // 1. Fetch user data
        LoginResponse userDetails;
        try {
            userDetails = usuarioDAO.buscarLoginPorNombreUsuario(request.nombreUsuario());
        } catch (Exception e) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos");
        }

        // 2. Validate Password
        if (!userDetails.contrasena().equals(request.password())) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos");
        }

        // 3. El identificador del token vuelve a ser SIEMPRE el nombre de usuario único
        String subjectIdentifier = userDetails.nombreUsuario();

        // 4. Generate the token
        String token = jwtUtil.generateToken(subjectIdentifier, userDetails.rol());

        // 5. Return the complete package to the controller
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

    public void registerUsuarioEmpresaExistente(RegisterRequest request) {

        if (!empresaDAO.existeEmpresa(request.cuitEmpresa())) {
            throw new IllegalArgumentException("La empresa con CUIT " + request.cuitEmpresa() + " no existe");
        }
        Empresa empresa = new Empresa(request.cuitEmpresa(), "no es importante", false); // Solo necesitamos el CUIT
                                                                                         // para asociar al usuario
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

    @Transactional
    public void changePassword(
            String username,
            ChangePasswordRequest request) {

        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new IllegalArgumentException(
                    "Las nuevas contraseñas no coinciden");
        }

        Usuario usuario = usuarioDAO
                .buscarPorNombreUsuario(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!usuario.getContraseña().equals(request.currentPassword())) {
            throw new IllegalArgumentException(
                    "La contraseña actual es incorrecta");
        }

        usuarioDAO.actualizarPassword(
                username,
                request.newPassword());
    }

}