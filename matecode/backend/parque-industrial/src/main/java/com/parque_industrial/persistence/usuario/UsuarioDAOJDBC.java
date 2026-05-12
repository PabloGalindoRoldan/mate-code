package com.parque_industrial.persistence.usuario;
import com.parque_industrial.entities.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
                usuario.getEmpresa().getIdentificacion()
        );
    }
}