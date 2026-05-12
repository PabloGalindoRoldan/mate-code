package com.parque_industrial.services;

import com.parque_industrial.dto.auth.LoginRequest;
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

    public AuthService(UsuarioDAO usuarioDAO, EmpresaDAO empresaDAO) {
        this.usuarioDAO = usuarioDAO;
        this.empresaDAO = empresaDAO;
    }

    public void login(LoginRequest request) {
        // TODO: implementar autenticación
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
                empresa
        );
        usuarioDAO.guardar(usuario);
    }
}