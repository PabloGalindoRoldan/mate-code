package com.parque_industrial.persistence.mensajes;

import com.parque_industrial.dto.mensajes.ConversacionDTO;
import com.parque_industrial.entities.Mensaje;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MensajeDAOJDBC implements MensajeDAO {

    private final DataSource dataSource;

    public MensajeDAOJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Mensaje guardar(Mensaje mensaje) {
        String sql = "INSERT INTO mensajes (emisor_username, receptor_username, contenido, fecha_envio, leido) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, mensaje.getEmisorUsername());
            stmt.setString(2, mensaje.getReceptorUsername());
            stmt.setString(3, mensaje.getContenido());

            LocalDateTime fecha = (mensaje.getFechaEnvio() != null) ? mensaje.getFechaEnvio() : LocalDateTime.now();
            stmt.setTimestamp(4, Timestamp.valueOf(fecha));
            mensaje.setFechaEnvio(fecha);
            stmt.setBoolean(5, mensaje.isLeido());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    mensaje.setId(generatedKeys.getLong(1));
                }
            }
            return mensaje;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el mensaje", e);
        }
    }

    @Override
    public List<Mensaje> obtenerConversacion(String username1, String username2) {
        List<Mensaje> historial = new ArrayList<>();
        String sql = "SELECT id, emisor_username, receptor_username, contenido, fecha_envio, leido FROM mensajes " +
                "WHERE (emisor_username = ? AND receptor_username = ?) OR (emisor_username = ? AND receptor_username = ?) "
                +
                "ORDER BY fecha_envio ASC, id ASC";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, username1);
            stmt.setString(2, username2);
            stmt.setString(3, username2);
            stmt.setString(4, username1);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Timestamp timestamp = rs.getTimestamp("fecha_envio");
                    LocalDateTime fecha = (timestamp != null) ? timestamp.toLocalDateTime() : LocalDateTime.now();

                    Mensaje msg = new Mensaje(
                            rs.getLong("id"),
                            rs.getString("emisor_username"),
                            rs.getString("receptor_username"),
                            rs.getString("contenido"),
                            fecha,
                            rs.getBoolean("leido"));
                    historial.add(msg);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al recuperar la conversación", e);
        }
        return historial;
    }

    @Override
    public void marcarComoLeidos(String receptorUsername, String emisorUsername) {
        String sql = "UPDATE mensajes SET leido = 1 WHERE receptor_username = ? AND emisor_username = ? AND leido = 0";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, receptorUsername);
            stmt.setString(2, emisorUsername);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar estados de lectura", e);
        }
    }

    @Override
    public int contarMensajesSinLeer(String username) {
        String sql = "SELECT COUNT(*) FROM mensajes WHERE receptor_username = ? AND leido = 0";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar mensajes sin leer", e);
        }
        return 0;
    }

    @Override
    public List<ConversacionDTO> obtenerConversacionesActivas(String username) {
        List<ConversacionDTO> lista = new ArrayList<>();
        String sql = """
                SELECT
                    CASE
                        WHEN receptor_username = 'TODOS' THEN 'TODOS'
                        WHEN emisor_username = ? THEN receptor_username
                        ELSE emisor_username
                    END AS contacto,
                    contenido AS ultimo_mensaje,
                    fecha_envio AS fecha,
                    (SELECT COUNT(*) FROM mensajes WHERE emisor_username = contacto AND receptor_username = ? AND leido = 0) AS sin_leer
                FROM mensajes
                WHERE id IN (
                    SELECT MAX(id)
                    FROM mensajes
                    WHERE emisor_username = ? OR receptor_username = ? OR receptor_username = 'TODOS'
                    GROUP BY CASE
                        WHEN receptor_username = 'TODOS' THEN 'TODOS'
                        WHEN emisor_username = ? THEN receptor_username
                        ELSE emisor_username
                    END
                )
                ORDER BY fecha_envio DESC
                """;

        try (Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, username);
            stmt.setString(3, username);
            stmt.setString(4, username);
            stmt.setString(5, username);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Timestamp ts = rs.getTimestamp("fecha");
                    LocalDateTime fecha = (ts != null) ? ts.toLocalDateTime() : LocalDateTime.now();

                    ConversacionDTO dto = new ConversacionDTO(
                            rs.getString("contacto"),
                            rs.getString("ultimo_mensaje"),
                            fecha,
                            rs.getInt("sin_leer"));
                    lista.add(dto);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al recuperar los hilos de conversación", e);
        }
        return lista;
    }

    @Override
    public List<Mensaje> obtenerMensajesDeDifusion() {
        List<Mensaje> mensajesDifusion = new ArrayList<>();
        String sql = "SELECT id, emisor_username, receptor_username, contenido, fecha_envio, leido " +
                "FROM mensajes WHERE receptor_username = 'TODOS' ORDER BY fecha_envio ASC";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("fecha_envio");
                LocalDateTime fecha = (timestamp != null) ? timestamp.toLocalDateTime() : LocalDateTime.now();

                Mensaje msg = new Mensaje(
                        rs.getLong("id"),
                        rs.getString("emisor_username"),
                        rs.getString("receptor_username"),
                        rs.getString("contenido"),
                        fecha,
                        rs.getBoolean("leido"));
                mensajesDifusion.add(msg);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al recuperar los mensajes de difusión", e);
        }
        return mensajesDifusion;
    }
}
