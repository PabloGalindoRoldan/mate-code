package com.parque_industrial.persistence.usuario;

import com.parque_industrial.dto.auth.EmpresaResponse;
import com.parque_industrial.dto.auth.LoginResponse;
import com.parque_industrial.dto.auth.UsuarioResponse;
import com.parque_industrial.entities.Empresa;
import com.parque_industrial.entities.Rol;
import com.parque_industrial.entities.Usuario;
// import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        jdbcTemplate.update(sql,
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getNombreUsuario(),
                usuario.getContraseña(),
                usuario.getCuit(),
                usuario.getRol().name(),
                usuario.getEmpresa() != null ? usuario.getEmpresa().getIdentificacion() : null);

    }

    @Override
    public Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario) {
        // CORRECCIÓN: Agregamos LEFT JOIN con empresas para traer la razón social y el
        // estado real
        String sql = """
                SELECT u.nombre, u.apellido, u.email, u.nombre_usuario, u.cuit, u.rol, u.contrasena, u.cuit_empresa,
                        e.razon_social, e.es_radicada
                FROM usuarios u
                LEFT JOIN empresas e ON u.cuit_empresa = e.cuit
                WHERE u.nombre_usuario = ?
                """;
        try {
            Usuario usuario = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String email = rs.getString("email");
                String username = rs.getString("nombre_usuario");
                String cuit = rs.getString("cuit");
                String contrasena = rs.getString("contrasena");

                Rol rol = Rol.REPRESENTANTE_EMPRESA;
                String rolString = rs.getString("rol");
                if (rolString != null) {
                    try {
                        rol = Rol.valueOf(rolString);
                    } catch (IllegalArgumentException e) {
                        // Mantiene el rol por defecto si no matchea
                    }
                }

                String cuitEmpresa = rs.getString("cuit_empresa");
                Empresa empresa = null;
                if (cuitEmpresa != null) {
                    String razonSocial = rs.getString("razon_social");
                    boolean esRadicada = rs.getBoolean("es_radicada");

                    // Salvavidas por si la columna en la base de datos está vacía para registros
                    // viejos
                    if (razonSocial == null || razonSocial.isBlank()) {
                        razonSocial = "Empresa Registrada";
                    }

                    empresa = new Empresa(cuitEmpresa, razonSocial, esRadicada);
                }

                return new Usuario(
                        nombre,
                        apellido,
                        email,
                        username,
                        cuit,
                        rol,
                        contrasena,
                        empresa);
            }, nombreUsuario);

            return Optional.ofNullable(usuario);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<UsuarioResponse> obtenerTodasLasEmpresas() {
        String sql = "SELECT nombre_usuario, nombre, apellido, email, cuit FROM usuarios WHERE rol = 'REPRESENTANTE_EMPRESA'";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new UsuarioResponse(
                rs.getString("nombre_usuario"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("email"),
                rs.getString("cuit")));
    }

    @Override
    public List<UsuarioResponse> obtenerTodosLosUsuariosMenos(String usernameActual) {
        String sql = "SELECT nombre_usuario, nombre, apellido, email, cuit, rol FROM usuarios WHERE nombre_usuario != ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new UsuarioResponse(
                rs.getString("nombre_usuario"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("email"),
                rs.getString("cuit")), usernameActual);
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

        Map<String, Object> first = rows.get(0);
        EmpresaResponse empresaResponse = null;

        if (first.get("empresa_cuit") != null) {
            List<UsuarioResponse> representantes = rows.stream()
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
                empresaResponse,
                null);
    }
}