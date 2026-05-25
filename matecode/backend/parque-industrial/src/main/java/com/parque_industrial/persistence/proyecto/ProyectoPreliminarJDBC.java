package com.parque_industrial.persistence.proyecto;

import com.parque_industrial.entities.ProyectoPreliminar;
import com.parque_industrial.exceptions.DatabaseException;
import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProyectoPreliminarJDBC implements ProyectoDAO {

    private final DataSource dataSource;

    public ProyectoPreliminarJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void guardar(ProyectoPreliminar p) {
        // 1. Añadido 'estado' al SQL
        String sql = "INSERT INTO proyecto_preliminar (usuario_nombre, nombre, descripcion, actividad_principal, " +
                "actividad_secundaria, telefono, rubro, descripcion_servicio, persona_referente, materias_primas, " +
                "destino_produccion, superficie_requerida, superficie_trabajo, superficie_deposito, " +
                "superficie_cubierta, superficie_estacionamiento, tiene_planos, link_planos, energia_requerida, " +
                "personal_a_ocupar, tension_alimentacion, potencia_instalada, agua_mensual, gas_mensual, " +
                "residuos_tipo, residuos_cantidad, tratamiento_efluentes, tipo_empresa, direccion, " +
                "pretension_traslado, emplazamiento_actual, tiempo_radicacion, balanza_publica, comedor, sum_coworking, estado) "
                +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getUsuarioNombre());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setString(4, p.getActividadPrincipal());
            ps.setString(5, p.getActividadSecundaria());
            ps.setString(6, p.getTelefono());
            ps.setString(7, p.getRubro());
            ps.setString(8, p.getDescripcionServicio());
            ps.setString(9, p.getPersonaReferente());
            ps.setString(10, p.getMateriasPrimas());
            ps.setString(11, p.getDestinoProduccion());

            // Manejo de valores numéricos que pueden ser NULL
            if (p.getSuperficieRequerida() != null)
                ps.setDouble(12, p.getSuperficieRequerida());
            else
                ps.setNull(12, Types.DOUBLE);
            if (p.getSuperficieTrabajo() != null)
                ps.setDouble(13, p.getSuperficieTrabajo());
            else
                ps.setNull(13, Types.DOUBLE);
            if (p.getSuperficieDeposito() != null)
                ps.setDouble(14, p.getSuperficieDeposito());
            else
                ps.setNull(14, Types.DOUBLE);
            if (p.getSuperficieCubierta() != null)
                ps.setDouble(15, p.getSuperficieCubierta());
            else
                ps.setNull(15, Types.DOUBLE);
            if (p.getSuperficieEstacionamiento() != null)
                ps.setDouble(16, p.getSuperficieEstacionamiento());
            else
                ps.setNull(16, Types.DOUBLE);

            ps.setString(17, p.getTienePlanos());
            ps.setString(18, p.getLinkPlanos());

            if (p.getEnergiaRequerida() != null)
                ps.setDouble(19, p.getEnergiaRequerida());
            else
                ps.setNull(19, Types.DOUBLE);
            if (p.getPersonalAOcupar() != null)
                ps.setInt(20, p.getPersonalAOcupar());
            else
                ps.setNull(20, Types.INTEGER);

            ps.setString(21, p.getTensionAlimentacion());

            if (p.getPotenciaInstalada() != null)
                ps.setDouble(22, p.getPotenciaInstalada());
            else
                ps.setNull(22, Types.DOUBLE);
            if (p.getAguaMensual() != null)
                ps.setDouble(23, p.getAguaMensual());
            else
                ps.setNull(23, Types.DOUBLE);
            if (p.getGasMensual() != null)
                ps.setDouble(24, p.getGasMensual());
            else
                ps.setNull(24, Types.DOUBLE);

            ps.setString(25, p.getResiduosTipo());

            if (p.getResiduosCantidad() != null)
                ps.setDouble(26, p.getResiduosCantidad());
            else
                ps.setNull(26, Types.DOUBLE);

            ps.setString(27, p.getTratamientoEfluentes());
            ps.setString(28, p.getTipoEmpresa());
            ps.setString(29, p.getDireccion());
            ps.setString(30, p.getPretensionTraslado());
            ps.setString(31, p.getEmplazamientoActual());
            ps.setString(32, p.getTiempoRadicacion());
            ps.setString(33, p.getBalanzaPublica());
            ps.setString(34, p.getComedor());
            ps.setString(35, p.getSumCoworking());
            ps.setString(36, p.getEstado() != null ? p.getEstado() : "en_revision"); // 36: El nuevo campo estado

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next())
                    p.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error al insertar proyecto preliminar", e);
        }
    }

    // Nota: Implementar el resto de los métodos (buscarPorId, listar, etc.)
    // siguiendo la misma lógica de ResultSet a Objeto Java.

    @Override
    public Optional<ProyectoPreliminar> buscarPorId(Long id) {
        /* TODO */ return Optional.empty();
    }

    @Override
    public List<ProyectoPreliminar> listarPorUsuario(String usuarioNombre) {
        /* TODO */ return new ArrayList<>();
    }

    @Override
    public void actualizar(ProyectoPreliminar proyecto) {
        /* TODO */ }

    @Override
    public void eliminar(Long id) {
        /* TODO */ }
}