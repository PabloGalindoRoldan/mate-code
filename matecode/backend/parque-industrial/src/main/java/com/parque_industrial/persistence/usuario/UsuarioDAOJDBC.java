package com.parque_industrial.persistence.usuario;

import com.parque_industrial.dto.auth.EmpresaResponse;
import com.parque_industrial.dto.auth.LoginResponse;
import com.parque_industrial.dto.auth.UsuarioResponse;
import com.parque_industrial.entities.Usuario;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class UsuarioDAOJDBC implements UsuarioDAO {

    private final JdbcTemplate jdbcTemplate;

    public UsuarioDAOJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void guardar(Usuario usuario) {
        String sql = """
                INSERT INTO usuarios (nombre, apellido, email, nombre_usuario, contrasena, cuit, rol, cuit_empresa)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try {
            jdbcTemplate.update(sql,
                    usuario.getNombre(),
                    usuario.getApellido(),
                    usuario.getEmail(),
                    usuario.getNombreUsuario(),
                    usuario.getContraseña(),
                    usuario.getCuit(),
                    usuario.getRol().name(),
                    // Safeguard: check if company exists before trying to extract its
                    // identification CUIT
                    usuario.getEmpresa() != null ? usuario.getEmpresa().getIdentificacion() : null);
        } catch (DataAccessException e) {
            throw new IllegalArgumentException(e.getRootCause().getMessage());
        }
    }

    @Override
    public LoginResponse buscarLoginPorNombreUsuario(String nombreUsuario) {
        String sql = """
                SELECT
                    u.nombre_usuario, u.nombre, u.apellido, u.email, u.cuit, u.rol, u.contrasena,
                    e.cuit AS empresa_cuit, e.razon_social, e.es_radicada,
                    r.nombre_usuario AS rep_usuario, r.nombre AS rep_nombre,
                    r.apellido AS rep_apellido, r.email AS rep_email, r.cuit AS rep_cuit
                FROM usuarios u
                LEFT JOIN empresas e ON u.cuit_empresa = e.cuit
                LEFT JOIN usuarios r ON r.cuit_empresa = e.cuit
                WHERE u.nombre_usuario = ?
                """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, nombreUsuario);

        if (rows.isEmpty()) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos");
        }

        // La primera fila tiene los datos del usuario y empresa (si existe)
        Map<String, Object> first = rows.get(0);

        // Initialize as null. It will remain null for non-corporate administrators
        // (ADMINISTRADOR_PARQUE, ADMINISTRADOR_SISTEMA)
        EmpresaResponse empresaResponse = null;

        // Only build the Empresa response if the user actually has a linked company
        // record
        if (first.get("empresa_cuit") != null) {
            List<UsuarioResponse> representantes = rows.stream()
                    // Extra safety: make sure we drop rows where left-joined fields are null
                    .filter(row -> row.get("rep_usuario") != null)
                    .map(row -> new UsuarioResponse(
                            (String) row.get("rep_usuario"),
                            (String) row.get("rep_nombre"),
                            (String) row.get("rep_apellido"),
                            (String) row.get("rep_email"),
                            (String) row.get("rep_cuit")))
                    .toList();

            empresaResponse = new EmpresaResponse(
                    (String) first.get("empresa_cuit"),
                    (String) first.get("razon_social"),
                    (Boolean) first.get("es_radicada"),
                    representantes);
        }

        return new LoginResponse(
                (String) first.get("nombre_usuario"),
                (String) first.get("nombre"),
                (String) first.get("apellido"),
                (String) first.get("email"),
                (String) first.get("cuit"),
                (String) first.get("rol"),
                (String) first.get("contrasena"),
                empresaResponse, // Safely null for non-corporate users
                null);
    }
}