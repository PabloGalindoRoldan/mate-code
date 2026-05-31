package com.parque_industrial.persistence.usuario;

import com.parque_industrial.dto.auth.LoginResponse;
import com.parque_industrial.dto.auth.UsuarioResponse;
import com.parque_industrial.entities.Usuario;
import java.util.Optional;
import java.util.List;

public interface UsuarioDAO {

    public void guardar(Usuario usuario);

    public LoginResponse buscarLoginPorNombreUsuario(String nombreUsuario);

    public Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario);

    List<UsuarioResponse> obtenerTodasLasEmpresas();

    List<UsuarioResponse> obtenerTodosLosUsuariosMenos(String usernameActual);

    void actualizarPassword(String username, String nuevaPassword);


}