package com.parque_industrial.persistence.publicaciones;

import com.parque_industrial.entities.Publicacion;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PublicacionDAOJDBC implements PublicacionDAO {

    private final DataSource dataSource;

    public PublicacionDAOJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Publicacion> listarTodas() {
        List<Publicacion> lista = new ArrayList<>();

        String sql = "SELECT id, titulo, imagen, alt, contenido, fecha_creacion FROM publicaciones ORDER BY fecha_creacion DESC, id DESC";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Timestamp timestamp = rs.getTimestamp("fecha_creacion");
                LocalDateTime fecha = (timestamp != null) ? timestamp.toLocalDateTime() : LocalDateTime.now();

                Publicacion pub = new Publicacion(
                        rs.getLong("id"),
                        rs.getString("titulo"),
                        rs.getString("imagen"),
                        rs.getString("alt"),
                        rs.getString("contenido"),
                        fecha);
                lista.add(pub);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar publicaciones nativas", e);
        }
        return lista;
    }

    @Override
    public Publicacion guardar(Publicacion pub) {
        String sql = "INSERT INTO publicaciones (titulo, imagen, alt, contenido, fecha_creacion) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, pub.getTitulo());
            stmt.setString(2, pub.getImagen());
            stmt.setString(3, pub.getAlt());
            stmt.setString(4, pub.getContenido());

            LocalDateTime now = LocalDateTime.now();
            stmt.setTimestamp(5, Timestamp.valueOf(now));
            pub.setFechaCreacion(now);

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pub.setId(generatedKeys.getLong(1));
                }
            }
            return pub;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar publicación", e);
        }
    }

    @Override
    public boolean eliminar(Long id) {
        String sql = "DELETE FROM publicaciones WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar publicación id: " + id, e);
        }
    }
}