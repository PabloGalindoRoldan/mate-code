package com.parque_industrial.persistence.usuario;

import com.parque_industrial.dto.auth.EmpresaResponse;
import com.parque_industrial.dto.auth.LoginResponse;
import com.parque_industrial.dto.auth.UsuarioResponse;
import com.parque_industrial.entities.Rol;
import com.parque_industrial.entities.Usuario;
import org.springframework.dao.DataAccessException;
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
        try {
            jdbcTemplate.update(sql,
                    usuario.getNombre(),
                    usuario.getApellido(),
                    usuario.getEmail(),
                    usuario.getNombreUsuario(),
                    usuario.getContraseña(),
                    usuario.getCuit(),
                    usuario.getRol().name(),
                    usuario.getEmpresa() != null ? usuario.getEmpresa().getIdentificacion() : null);
        } catch (DataAccessException e) {
            throw new IllegalArgumentException(e.getRootCause().getMessage());
        }
    }

    @Override
    public Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario) {
        String sql = "SELECT nombre, apellido, email, nombre_usuario, cuit, rol, contrasena FROM usuarios WHERE nombre_usuario = ?";

        try {
            Usuario usuario = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {

                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String email = rs.getString("email");
                String username = rs.getString("nombre_usuario");
                String cuit = rs.getString("cuit");
                String contrasena = rs.getString("contrasena");
                Rol rol = Rol.REPRESENTANTE_EMPRESA; // Valor por defecto en caso de que el rol sea nulo o no válido -
                                                     // No debería pasar si la base de datos está bien, pero es una
                                                     // medida de seguridad adicional para evitar excepciones. Si el rol
                                                     // es nulo o no coincide con ningún valor del enum, se asignará
                                                     // REPRESENTANTE_EMPRESA por defecto.
                String rolString = rs.getString("rol");
                if (rolString != null) {
                    try {
                        rol = Rol.valueOf(rolString);
                    } catch (IllegalArgumentException e) {

                    }
                }

                return new Usuario(
                        nombre,
                        apellido,
                        email,
                        username,
                        cuit,
                        rol,
                        contrasena);
            }, nombreUsuario);

            return Optional.ofNullable(usuario);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al buscar usuario por nombre de usuario", e);
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