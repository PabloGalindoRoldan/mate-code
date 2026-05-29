package com.parque_industrial.persistence.presupuesto;

import com.parque_industrial.dto.presupuesto.BalancePartidaDTO;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Repository
public class DAOPresupuestoJDBC implements DAOPresupuesto {

    private final DataSource dataSource;

    // Spring inyecta automáticamente el DataSource configurado en tus
    // application.properties
    public DAOPresupuestoJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Connection getConnection() throws SQLException {
        // Obtenemos la conexión directamente del pool administrado por Spring
        return dataSource.getConnection();
    }

    @Override
    public List<BalancePartidaDTO> obtenerBalancePresupuestario(int ejercicioFiscal) {
        List<BalancePartidaDTO> balances = new ArrayList<>();
        String sql = "SELECT pa.id as presupuesto_id, p.codigo, p.nombre, pa.fuente_financiamiento, pa.credito_original, pa.credito_vigente, "
                + "COALESCE(SUM(CASE WHEN reg.fase = 'COMPROMISO' THEN reg.monto ELSE 0 END), 0) as comprometido, "
                + "COALESCE(SUM(CASE WHEN reg.fase = 'DEVENGADO' THEN reg.monto ELSE 0 END), 0) as devengado, "
                + "COALESCE(SUM(CASE WHEN reg.fase = 'PAGADO' THEN reg.monto ELSE 0 END), 0) as pagado "
                + "FROM presupuesto_anual pa "
                + "JOIN partidas_presupuestarias p ON pa.partida_id = p.id "
                + "LEFT JOIN registro_ejecucion_gasto reg ON pa.id = reg.presupuesto_id "
                + "WHERE pa.ejercicio_fiscal = ? "
                + "GROUP BY pa.id, p.codigo, p.nombre, pa.fuente_financiamiento, pa.credito_original, pa.credito_vigente";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ejercicioFiscal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BigDecimal credVigente = rs.getBigDecimal("credito_vigente");
                    BigDecimal comprometido = rs.getBigDecimal("comprometido");
                    BigDecimal saldoDisp = credVigente.subtract(comprometido);

                    balances.add(new BalancePartidaDTO(
                            rs.getInt("presupuesto_id"),
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getString("fuente_financiamiento"),
                            rs.getBigDecimal("credito_original"),
                            credVigente,
                            comprometido,
                            rs.getBigDecimal("devengado"),
                            rs.getBigDecimal("pagado"),
                            saldoDisp));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al generar balance del libro contable", e);
        }
        return balances;
    }

    @Override
    public BigDecimal obtenerSaldoDisponiblePartida(int presupuestoId) {
        String sql = "SELECT (pa.credito_vigente - COALESCE(SUM(CASE WHEN reg.fase = 'COMPROMISO' THEN reg.monto ELSE 0 END), 0)) as disponible "
                + "FROM presupuesto_anual pa "
                + "LEFT JOIN registro_ejecucion_gasto reg ON pa.id = reg.presupuesto_id "
                + "WHERE pa.id = ? GROUP BY pa.id";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, presupuestoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getBigDecimal("disponible");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar saldo disponible", e);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public void registrarModificacionPresupuestaria(int presupuestoId, String tipo, BigDecimal monto,
            String justificacion) {
        String insertSql = "INSERT INTO modificaciones_presupuestarias (presupuesto_id, tipo, monto, justificacion) VALUES (?, ?, ?, ?)";
        String updateSql = tipo.equalsIgnoreCase("INCREMENTO")
                ? "UPDATE presupuesto_anual SET credito_vigente = credito_vigente + ? WHERE id = ?"
                : "UPDATE presupuesto_anual SET credito_vigente = credito_vigente - ? WHERE id = ?";

        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false); // Transaccionalidad estricta

            try (PreparedStatement psInsert = conn.prepareStatement(insertSql);
                    PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {

                psInsert.setInt(1, presupuestoId);
                psInsert.setString(2, tipo.toUpperCase());
                psInsert.setBigDecimal(3, monto);
                psInsert.setString(4, justificacion);
                psInsert.executeUpdate();

                psUpdate.setBigDecimal(1, monto);
                psUpdate.setInt(2, presupuestoId);
                psUpdate.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                if (conn != null)
                    conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en reestructuración de partida contable", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public void registrarFaseGasto(int presupuestoId, java.sql.Date fecha, String tipoComp, String nroComp, String desc,
            String fase, BigDecimal monto) {
        String sql = "INSERT INTO registro_ejecucion_gasto (presupuesto_id, fecha, comprobante_tipo, comprobante_nro, descripcion, fase, monto) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, presupuestoId);
            ps.setDate(2, fecha);
            ps.setString(3, tipoComp);
            ps.setString(4, nroComp);
            ps.setString(5, desc);
            ps.setString(6, fase.toUpperCase());
            ps.setBigDecimal(7, monto);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar fase del gasto", e);
        }
    }

    @Override
    public String obtenerCodigoPartidaPorPresupuestoId(int presupuestoId) {
        String sql = "SELECT p.codigo FROM presupuesto_anual pa JOIN partidas_presupuestarias p ON pa.partida_id = p.id WHERE pa.id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, presupuestoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getString("codigo");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    @Override
    public boolean existePresupuesto(int partidaId, int ejercicioFiscal) {
        String sql = "SELECT COUNT(*) FROM presupuesto_anual WHERE partida_id = ? AND ejercicio_fiscal = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, partidaId);
            ps.setInt(2, ejercicioFiscal);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia de presupuesto", e);
        }
        return false;
    }

    @Override
    public void insertarPresupuestoInicial(int partidaId, int ejercicio, String fuente, BigDecimal monto) {
        String sql = "INSERT INTO presupuesto_anual (partida_id, ejercicio_fiscal, fuente_financiamiento, credito_original, credito_vigente) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, partidaId);
            ps.setInt(2, ejercicio);
            ps.setString(3, fuente);
            ps.setBigDecimal(4, monto);
            ps.setBigDecimal(5, monto); // El vigente inicial es igual al original
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar la carga inicial del presupuesto", e);
        }
    }

    @Override
    public List<Map<String, Object>> obtenerTodasLasPartidas() {
        List<Map<String, Object>> catalogo = new ArrayList<>();
        String sql = "SELECT id, codigo, nombre FROM partidas_presupuestarias ORDER BY codigo ASC";

        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> partida = new HashMap<>();
                partida.put("id", rs.getInt("id"));
                partida.put("codigo", rs.getString("codigo"));
                partida.put("nombre", rs.getString("nombre"));
                catalogo.add(partida);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener el catálogo de partidas", e);
        }
        return catalogo;
    }
}