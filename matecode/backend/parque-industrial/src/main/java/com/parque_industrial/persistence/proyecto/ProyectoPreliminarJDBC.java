package com.parque_industrial.persistence.proyecto;

import com.parque_industrial.entities.ProyectoPreliminar;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProyectoPreliminarJDBC implements ProyectoDAO<ProyectoPreliminar> {

    private final Connection connection;

    public ProyectoPreliminarJDBC(Connection connection) {
        this.connection = connection;
    }

    // GUARDAR
    public void guardar(ProyectoPreliminar p) throws SQLException {
        String sql = "INSERT INTO proyecto_preliminar " +
                "(identificacion, actividad_principal, referente, superficie, energia, personal, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, p.getCuitEmpresaAsociada());
        stmt.setString(2, p.getActividadPrincipal());
        stmt.setString(3, p.getReferente());
        stmt.setInt(4, p.getSuperficieRequerida());
        stmt.setDouble(5, p.getEnergiaRequerida());
        stmt.setInt(6, p.getPersonalAOcupar());
        stmt.setString(7, p.getEstado());

        stmt.executeUpdate();
    }

    // BUSCAR POR ID
    public ProyectoPreliminar buscarPorId(String identificacion) throws SQLException {
        String sql = "SELECT * FROM proyecto_preliminar WHERE identificacion = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, identificacion);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return mapear(rs);
        }

        return null;
    }

    // BUSCAR POR ESTADO
    public List<ProyectoPreliminar> buscarPorEstado(String estado) throws SQLException {
        String sql = "SELECT * FROM proyecto_preliminar WHERE estado = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, estado);

        ResultSet rs = stmt.executeQuery();

        List<ProyectoPreliminar> lista = new ArrayList<>();

        while (rs.next()) {
            lista.add(mapear(rs));
        }

        return lista;
    }

    // ACTUALIZAR
    public void actualizar(ProyectoPreliminar p) throws SQLException {
        String sql = """
            UPDATE proyecto_preliminar
            SET actividad_principal = ?, referente = ?, superficie = ?, energia = ?, personal = ?, estado = ?
            WHERE identificacion = ?
        """;

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, p.getActividadPrincipal());
        stmt.setString(2, p.getReferente());
        stmt.setInt(3, p.getSuperficieRequerida());
        stmt.setDouble(4, p.getEnergiaRequerida());
        stmt.setInt(5, p.getPersonalAOcupar());
        stmt.setString(6, p.getEstado());
        stmt.setString(7, p.getCuitEmpresaAsociada());

        stmt.executeUpdate();
    }

    // ELIMINAR
    public void eliminar(String identificacion) throws SQLException {
        String sql = "DELETE FROM proyecto_preliminar WHERE identificacion = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, identificacion);

        stmt.executeUpdate();
    }

    // MAPPER
    private ProyectoPreliminar mapear(ResultSet rs) {
        try {
            ProyectoPreliminar p = new ProyectoPreliminar(
                    rs.getString("identificacion"),
                    rs.getString("actividad_principal"),
                    rs.getString("referente"),
                    rs.getInt("superficie"),
                    rs.getDouble("energia"),
                    rs.getInt("personal")
            );

            p.setEstado(rs.getString("estado"));

            return p;

        } catch (Exception e) {
            throw new RuntimeException("Error mapeando ProyectoPreliminar", e);
        }
    }
}