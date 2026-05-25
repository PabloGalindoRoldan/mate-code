package com.parque_industrial.persistence.proyecto;

import com.parque_industrial.dto.proyecto.CrearRequestDefinitivoDTO;
import com.parque_industrial.entities.ProyectoDefinitivo;
import com.parque_industrial.exceptions.DatabaseException;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Repository
public class ProyectoDefinitivoJDBC
        implements ProyectoDefinitivoDAO {

    private final DataSource dataSource;

    public ProyectoDefinitivoJDBC(
            DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<CrearRequestDefinitivoDTO>
    listarDefinitivosPorCuit(String cuit) {
        return List.of();
    }

    public void guardarProyectoDefinitivo(
            ProyectoDefinitivo proyecto) {

        String sql = """
        INSERT INTO proyecto_definitivo (

            usuario_nombre,
            nombre,
            descripcion,
            actividad_principal,
            actividad_secundaria,
            telefono,
            rubro,
            descripcion_servicio,
            persona_referente,
            materias_primas,
            destino_produccion,

            superficie_requerida,
            superficie_trabajo,
            superficie_deposito,
            superficie_cubierta,
            superficie_estacionamiento,

            tiene_planos,
            link_planos,

            energia_requerida,
            personal_a_ocupar,
            tension_alimentacion,
            potencia_instalada,
            agua_mensual,
            gas_mensual,

            residuos_tipo,
            residuos_cantidad,
            tratamiento_efluentes,
            tipo_empresa,
            direccion,
            pretension_traslado,
            emplazamiento_actual,
            tiempo_radicacion,

            balanza_publica,
            comedor,
            sum_coworking,

            estado,
            cuit_empresa,
            fecha_creacion,
            fecha_actualizacion,

            link_viabilidad_financiera,
            link_estudio_mercado,
            link_impacto_ambiental,
            link_habilitacion_municipal,
            link_certificado_inhibiciones

        )
        VALUES (
            ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,
            ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,
            ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,
            ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
        )
        """;

        try (Connection conn =
                     dataSource.getConnection();

             PreparedStatement ps =
                     conn.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1,
                    proyecto.getUsuarioNombre());

            ps.setString(2,
                    proyecto.getNombre());

            ps.setString(3,
                    proyecto.getDescripcion());

            ps.setString(4,
                    proyecto.getActividadPrincipal());

            ps.setString(5,
                    proyecto.getActividadSecundaria());

            ps.setString(6,
                    proyecto.getTelefono());

            ps.setString(7,
                    proyecto.getRubro());

            ps.setString(8,
                    proyecto.getDescripcionServicio());

            ps.setString(9,
                    proyecto.getPersonaReferente());

            ps.setString(10,
                    proyecto.getMateriasPrimas());

            ps.setString(11,
                    proyecto.getDestinoProduccion());

            if (proyecto.getSuperficieRequerida() != null)
                ps.setDouble(12,
                        proyecto.getSuperficieRequerida());
            else
                ps.setNull(12, Types.DOUBLE);

            if (proyecto.getSuperficieTrabajo() != null)
                ps.setDouble(13,
                        proyecto.getSuperficieTrabajo());
            else
                ps.setNull(13, Types.DOUBLE);

            if (proyecto.getSuperficieDeposito() != null)
                ps.setDouble(14,
                        proyecto.getSuperficieDeposito());
            else
                ps.setNull(14, Types.DOUBLE);

            if (proyecto.getSuperficieCubierta() != null)
                ps.setDouble(15,
                        proyecto.getSuperficieCubierta());
            else
                ps.setNull(15, Types.DOUBLE);

            if (proyecto.getSuperficieEstacionamiento() != null)
                ps.setDouble(16,
                        proyecto.getSuperficieEstacionamiento());
            else
                ps.setNull(16, Types.DOUBLE);

            ps.setString(17,
                    proyecto.getTienePlanos());

            ps.setString(18,
                    proyecto.getLinkPlanos());

            if (proyecto.getEnergiaRequerida() != null)
                ps.setDouble(19,
                        proyecto.getEnergiaRequerida());
            else
                ps.setNull(19, Types.DOUBLE);

            if (proyecto.getPersonalAOcupar() != null)
                ps.setInt(20,
                        proyecto.getPersonalAOcupar());
            else
                ps.setNull(20, Types.INTEGER);

            ps.setString(21,
                    proyecto.getTensionAlimentacion());

            if (proyecto.getPotenciaInstalada() != null)
                ps.setDouble(22,
                        proyecto.getPotenciaInstalada());
            else
                ps.setNull(22, Types.DOUBLE);

            if (proyecto.getAguaMensual() != null)
                ps.setDouble(23,
                        proyecto.getAguaMensual());
            else
                ps.setNull(23, Types.DOUBLE);

            if (proyecto.getGasMensual() != null)
                ps.setDouble(24,
                        proyecto.getGasMensual());
            else
                ps.setNull(24, Types.DOUBLE);

            ps.setString(25,
                    proyecto.getResiduosTipo());

            if (proyecto.getResiduosCantidad() != null)
                ps.setDouble(26,
                        proyecto.getResiduosCantidad());
            else
                ps.setNull(26, Types.DOUBLE);

            ps.setString(27,
                    proyecto.getTratamientoEfluentes());

            ps.setString(28,
                    proyecto.getTipoEmpresa());

            ps.setString(29,
                    proyecto.getDireccion());

            ps.setString(30,
                    proyecto.getPretensionTraslado());

            ps.setString(31,
                    proyecto.getEmplazamientoActual());

            ps.setString(32,
                    proyecto.getTiempoRadicacion());

            ps.setString(33,
                    proyecto.getBalanzaPublica());

            ps.setString(34,
                    proyecto.getComedor());

            ps.setString(35,
                    proyecto.getSumCoworking());

            ps.setString(36,
                    proyecto.getEstado());

            ps.setString(37,
                    proyecto.getCuitEmpresa());

            ps.setTimestamp(38,
                    Timestamp.valueOf(
                            proyecto.getFechaCreacion()));

            ps.setTimestamp(39,
                    Timestamp.valueOf(
                            proyecto.getFechaActualizacion()));

            ps.setString(40,
                    proyecto.getLinkViabilidadFinanciera());

            ps.setString(41,
                    proyecto.getLinkEstudioMercado());

            ps.setString(42,
                    proyecto.getLinkImpactoAmbiental());

            ps.setString(43,
                    proyecto.getLinkHabilitacionMunicipal());

            ps.setString(44,
                    proyecto.getLinkCertificadoInhibiciones());

            ps.executeUpdate();

            try (ResultSet rs =
                         ps.getGeneratedKeys()) {

                if (rs.next()) {
                    proyecto.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Error al insertar proyecto definitivo",
                    e
            );
        }
    }
}