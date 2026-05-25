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
        String sql = "INSERT INTO proyecto_preliminar (usuario_nombre, cuit_empresa, nombre, descripcion, actividad_principal, "
                +
                "actividad_secundaria, telefono, rubro, descripcion_servicio, persona_referente, materias_primas, " +
                "destino_produccion, superficie_requerida, superficie_trabajo, superficie_deposito, " +
                "superficie_cubierta, superficie_estacionamiento, tiene_planos, link_planos, energia_requerida, " +
                "personal_a_ocupar, tension_alimentacion, potencia_instalada, agua_mensual, gas_mensual, " +
                "residuos_tipo, residuos_cantidad, tratamiento_efluentes, tipo_empresa, direccion, " +
                "pretension_traslado, emplazamiento_actual, tiempo_radicacion, balanza_publica, comedor, sum_coworking, estado) "
                +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getUsuarioNombre());
            ps.setString(2, p.getCuitEmpresa());
            ps.setString(3, p.getNombre());
            ps.setString(4, p.getDescripcion());
            ps.setString(5, p.getActividadPrincipal());
            ps.setString(6, p.getActividadSecundaria());
            ps.setString(7, p.getTelefono());
            ps.setString(8, p.getRubro());
            ps.setString(9, p.getDescripcionServicio());
            ps.setString(10, p.getPersonaReferente());
            ps.setString(11, p.getMateriasPrimas());
            ps.setString(12, p.getDestinoProduccion());

            // Manejo de valores numéricos (desplazados por el nuevo campo)
            if (p.getSuperficieRequerida() != null)
                ps.setDouble(13, p.getSuperficieRequerida());
            else
                ps.setNull(13, Types.DOUBLE);
            if (p.getSuperficieTrabajo() != null)
                ps.setDouble(14, p.getSuperficieTrabajo());
            else
                ps.setNull(14, Types.DOUBLE);
            if (p.getSuperficieDeposito() != null)
                ps.setDouble(15, p.getSuperficieDeposito());
            else
                ps.setNull(15, Types.DOUBLE);
            if (p.getSuperficieCubierta() != null)
                ps.setDouble(16, p.getSuperficieCubierta());
            else
                ps.setNull(16, Types.DOUBLE);
            if (p.getSuperficieEstacionamiento() != null)
                ps.setDouble(17, p.getSuperficieEstacionamiento());
            else
                ps.setNull(17, Types.DOUBLE);

            ps.setString(18, p.getTienePlanos());
            ps.setString(19, p.getLinkPlanos());

            if (p.getEnergiaRequerida() != null)
                ps.setDouble(20, p.getEnergiaRequerida());
            else
                ps.setNull(20, Types.DOUBLE);
            if (p.getPersonalAOcupar() != null)
                ps.setInt(21, p.getPersonalAOcupar());
            else
                ps.setNull(21, Types.INTEGER);

            ps.setString(22, p.getTensionAlimentacion());

            if (p.getPotenciaInstalada() != null)
                ps.setDouble(23, p.getPotenciaInstalada());
            else
                ps.setNull(23, Types.DOUBLE);
            if (p.getAguaMensual() != null)
                ps.setDouble(24, p.getAguaMensual());
            else
                ps.setNull(24, Types.DOUBLE);
            if (p.getGasMensual() != null)
                ps.setDouble(25, p.getGasMensual());
            else
                ps.setNull(25, Types.DOUBLE);

            ps.setString(26, p.getResiduosTipo());

            if (p.getResiduosCantidad() != null)
                ps.setDouble(27, p.getResiduosCantidad());
            else
                ps.setNull(27, Types.DOUBLE);

            ps.setString(28, p.getTratamientoEfluentes());
            ps.setString(29, p.getTipoEmpresa());
            ps.setString(30, p.getDireccion());
            ps.setString(31, p.getPretensionTraslado());
            ps.setString(32, p.getEmplazamientoActual());
            ps.setString(33, p.getTiempoRadicacion());
            ps.setString(34, p.getBalanzaPublica());
            ps.setString(35, p.getComedor());
            ps.setString(36, p.getSumCoworking());
            ps.setString(37, p.getEstado() != null ? p.getEstado() : "en_revision");

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