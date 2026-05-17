package com.parque_industrial.persistence.consumos;

import com.parque_industrial.entities.Consumo;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ConsumoDAOJDBC implements ConsumoDAO {

    private final DataSource dataSource;

    // Spring va a inyectar automáticamente tu DataSource configurado
    public ConsumoDAOJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void registrarConsumo(Consumo consumo) {
        String sql = """
                INSERT INTO consumos (cuit_empresa, mes, ano, gas, luz, agua, empleados, vehiculos)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, consumo.getCuitEmpresa());
            stmt.setInt(2, consumo.getMes());
            stmt.setInt(3, consumo.getAno());
            stmt.setBigDecimal(4, consumo.getGas());
            stmt.setBigDecimal(5, consumo.getLuz());
            stmt.setBigDecimal(6, consumo.getAgua());
            stmt.setInt(7, consumo.getEmpleados());
            stmt.setInt(8, consumo.getVehiculos());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar el registro de consumo en la base de datos", e);
        }
    }

    @Override
    public boolean existePeriodo(String cuitEmpresa, int mes, int ano) {
        String sql = "SELECT COUNT(*) FROM consumos WHERE cuit_empresa = ? AND mes = ? AND ano = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cuitEmpresa);
            stmt.setInt(2, mes);
            stmt.setInt(3, ano);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al validar la existencia del período", e);
        }
        return false;
    }

    @Override
    public List<Consumo> obtenerHistorialPorEmpresa(String cuitEmpresa) {
        List<Consumo> lista = new ArrayList<>();
        String sql = "SELECT * FROM consumos WHERE cuit_empresa = ? ORDER BY ano DESC, mes DESC";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cuitEmpresa);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearConsumo(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener historial de consumos de la empresa", e);
        }
        return lista;
    }

    @Override
    public Optional<Consumo> obtenerUltimoConsumo(String cuitEmpresa) {
        String sql = "SELECT * FROM consumos WHERE cuit_empresa = ? ORDER BY ano DESC, mes DESC LIMIT 1";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cuitEmpresa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearConsumo(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al recuperar el último consumo de la empresa", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Consumo> obtenerConsumosGlobalesPorAno(int ano) {
        List<Consumo> lista = new ArrayList<>();
        String sql = "SELECT * FROM consumos WHERE ano = ? ORDER BY mes ASC";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ano);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearConsumo(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los consumos globales del año", e);
        }
        return lista;
    }

    // Helper metodito privado para no repetir código de mapeo ResultSet -> Objeto
    private Consumo mapearConsumo(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("fecha_carga");
        return new Consumo(
                rs.getLong("id"),
                rs.getString("cuit_empresa"),
                rs.getInt("mes"),
                rs.getInt("ano"),
                rs.getBigDecimal("gas"),
                rs.getBigDecimal("luz"),
                rs.getBigDecimal("agua"),
                rs.getInt("empleados"),
                rs.getInt("vehiculos"),
                (ts != null) ? ts.toLocalDateTime() : null);
    }
}