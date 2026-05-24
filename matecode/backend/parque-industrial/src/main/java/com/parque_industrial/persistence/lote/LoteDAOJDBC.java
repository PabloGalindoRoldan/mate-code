package com.parque_industrial.persistence.lote;

import com.parque_industrial.entities.Lote;
import com.parque_industrial.exceptions.DatabaseException;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class LoteDAOJDBC implements LoteDAO {

    private final @NonNull DataSource connection;

    public LoteDAOJDBC(@NonNull DataSource connection) {
        this.connection = connection;
    }

    // @Override
    // public void crear(Lote lote) {
    //
    // String sql = """
    // INSERT INTO lote(
    // id,
    // nro_lote,
    // superficie,
    // estado,
    // fecha_venta,
    // monto_venta,
    // nc,
    // parque
    // )
    // VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    // """;
    //
    // Connection conn = null;
    //
    // try {
    //
    // conn = DataSourceUtils.getConnection(connection);
    //
    // try (PreparedStatement ps = conn.prepareStatement(sql)) {
    //
    // ps.setInt(1, lote.getIdentificacion());
    //
    // ps.setString(2, lote.getNroLote());
    //
    // ps.setDouble(3, lote.getSuperficie());
    //
    // ps.setString(4, lote.getEstado());
    //
    // ps.setDate(5, lote.getFechaVentaSQL());
    //
    // if (lote.getMontoVenta() == null) {
    //
    // ps.setNull(6, java.sql.Types.DOUBLE);
    //
    // } else {
    //
    // ps.setDouble(6, lote.getMontoVenta());
    // }
    //
    // ps.setString(7, lote.getNc());
    //
    // ps.setString(8, lote.getParque());
    //
    // ps.executeUpdate();
    // }
    //
    // } catch (SQLException ex) {
    //
    // throw new DatabaseException("Error al crear lote", ex);
    //
    // } finally {
    //
    // DataSourceUtils.releaseConnection(conn, connection);
    // }
    // }

    @Override
    public void actualizar(Lote lote) {

        String sql = """
                UPDATE lote
                SET
                    estado = ?,
                    fecha_venta = ?,
                    monto_venta = ?
                WHERE id = ?
                """;

        Connection conn = null;

        try {

            conn = DataSourceUtils.getConnection(connection);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, lote.getEstado());

                ps.setDate(2, lote.getFechaVentaSQL());

                if (lote.getMontoVenta() == null) {

                    ps.setNull(3, java.sql.Types.DOUBLE);

                } else {

                    ps.setBigDecimal(3, lote.getMontoVenta());
                }

                ps.setInt(4, lote.getIdentificacion());

                ps.executeUpdate();
            }

        } catch (SQLException ex) {

            throw new DatabaseException("Error al actualizar lote", ex);

        } finally {

            DataSourceUtils.releaseConnection(conn, connection);
        }
    }

    @Override
    public Lote buscarPorID(int id) {

        String sql = """
                SELECT *
                FROM lote
                WHERE id = ?
                """;

        Connection conn = null;

        try {

            conn = DataSourceUtils.getConnection(connection);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, id);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return mapear(rs);
                }

                return null;
            }

        } catch (SQLException ex) {

            throw new DatabaseException("Error al buscar lote", ex);

        } finally {

            DataSourceUtils.releaseConnection(conn, connection);
        }
    }

    @Override
    public List<Lote> buscarTodos() {

        String sql = """
                SELECT *
                FROM lote
                """;

        List<Lote> lotes = new ArrayList<>();

        Connection conn = null;

        try {

            conn = DataSourceUtils.getConnection(connection);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    lotes.add(mapear(rs));
                }
            }

        } catch (SQLException ex) {

            throw new DatabaseException("Error al listar lotes", ex);

        } finally {

            DataSourceUtils.releaseConnection(conn, connection);
        }

        return lotes;
    }

    private Lote mapear(ResultSet rs) throws SQLException {

        Date fechaSql = rs.getDate("fecha_venta");

        LocalDate fechaVenta = fechaSql != null ? fechaSql.toLocalDate() : null;

        BigDecimal montoVenta = rs.getBigDecimal("monto_venta");
        return new Lote(rs.getInt("id"), rs.getString("nro_lote"), rs.getDouble("superficie"), rs.getString("estado"),
                fechaVenta, montoVenta, rs.getString("nc"), rs.getString("parque"), rs.getString("coordenadas"));
    }
}