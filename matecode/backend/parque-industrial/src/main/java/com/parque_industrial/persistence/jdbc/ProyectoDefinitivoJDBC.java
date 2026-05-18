package com.parque_industrial.persistence.jdbc;

import com.parque_industrial.entities.ProyectoDefinitivo;
import com.parque_industrial.services.DAOProyectos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProyectoDefinitivoJDBC implements DAOProyectos<ProyectoDefinitivo> {

    private final Connection connection;

    public ProyectoDefinitivoJDBC(Connection connection) {
        this.connection = connection;
    }

    // GUARDAR
    public void guardar(ProyectoDefinitivo p) throws SQLException {
        String sql = "INSERT INTO proyecto_definitivo " +
                "(identificacion, actividad_principal, referente, superficie, energia, personal, estado, " +
                "fecha_inicio, fecha_fin, viabilidad_financiera, informe_ambiental) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = connection.prepareStatement(sql);

        stmt.setString(1, p.getIdentificacion());
        stmt.setString(2, p.getActividadPrincipal());
        stmt.setString(3, p.getReferente());
        stmt.setInt(4, p.getSuperficieRequerida());
        stmt.setDouble(5, p.getEnergiaRequerida());
        stmt.setInt(6, p.getPersonalAOcupar());
        stmt.setString(7, p.getEstado());

        stmt.setDate(8, Date.valueOf(p.getFechaInicioObra()));
        stmt.setDate(9, Date.valueOf(p.getFechaFinObra()));
        stmt.setBoolean(10, p.isViabilidadFinanciera());
        stmt.setString(11, p.getInformeAmbiental());

        stmt.executeUpdate();
    }

    // BUSCAR POR ID
    public ProyectoDefinitivo buscarPorId(String identificacion) throws SQLException {
        String sql = "SELECT * FROM proyecto_definitivo WHERE identificacion = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, identificacion);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return mapear(rs);
        }

        return null;
    }

    // BUSCAR POR ESTADO
    public List<ProyectoDefinitivo> buscarPorEstado(String estado) throws SQLException {
        String sql = "SELECT * FROM proyecto_definitivo WHERE estado = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, estado);

        ResultSet rs = stmt.executeQuery();

        List<ProyectoDefinitivo> lista = new ArrayList<>();

        while (rs.next()) {
            lista.add(mapear(rs));
        }

        return lista;
    }

    // ACTUALIZAR
    public void actualizar(ProyectoDefinitivo p) throws SQLException {
        String sql = """
            UPDATE proyecto_definitivo
            SET actividad_principal = ?, referente = ?, superficie = ?, energia = ?, personal = ?, estado = ?,
                fecha_inicio = ?, fecha_fin = ?, viabilidad_financiera = ?, informe_ambiental = ?
            WHERE identificacion = ?
        """;

        PreparedStatement stmt = connection.prepareStatement(sql);

        stmt.setString(1, p.getActividadPrincipal());
        stmt.setString(2, p.getReferente());
        stmt.setInt(3, p.getSuperficieRequerida());
        stmt.setDouble(4, p.getEnergiaRequerida());
        stmt.setInt(5, p.getPersonalAOcupar());
        stmt.setString(6, p.getEstado());

        stmt.setDate(7, Date.valueOf(p.getFechaInicioObra()));
        stmt.setDate(8, Date.valueOf(p.getFechaFinObra()));
        stmt.setBoolean(9, p.isViabilidadFinanciera());
        stmt.setString(10, p.getInformeAmbiental());

        stmt.setString(11, p.getIdentificacion());

        stmt.executeUpdate();
    }

    // ELIMINAR
    public void eliminar(String identificacion) throws SQLException {
        String sql = "DELETE FROM proyecto_definitivo WHERE identificacion = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, identificacion);

        stmt.executeUpdate();
    }

    // MAPPER
    private ProyectoDefinitivo mapear(ResultSet rs) {
        try {
            ProyectoDefinitivo p = new ProyectoDefinitivo(
                    rs.getString("identificacion"),
                    rs.getString("actividad_principal"),
                    rs.getString("referente"),
                    rs.getInt("superficie"),
                    rs.getDouble("energia"),
                    rs.getInt("personal"),
                    rs.getDate("fecha_inicio").toLocalDate(),
                    rs.getDate("fecha_fin").toLocalDate(),
                    rs.getBoolean("viabilidad_financiera"),
                    rs.getString("informe_ambiental")
            );

            p.setEstado(rs.getString("estado"));

            return p;

        } catch (Exception e) {
            throw new RuntimeException("Error mapeando ProyectoDefinitivo", e);
        }
    }
}