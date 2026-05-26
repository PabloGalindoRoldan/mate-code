package com.parque_industrial.persistence.proyecto;

import com.parque_industrial.dto.proyecto.CrearRequestDefinitivoDTO;
import com.parque_industrial.entities.ProyectoDefinitivo;
import com.parque_industrial.exceptions.DatabaseException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Repository
public class ProyectoDefinitivoJDBC implements ProyectoDefinitivoDAO {
    private final DataSource dataSource;

    public ProyectoDefinitivoJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public List<CrearRequestDefinitivoDTO> listarDefinitivosPorCuit(String cuit) {

        String sql = "SELECT * FROM proyecto_definitivo WHERE cuit_empresa = ?";

        List<CrearRequestDefinitivoDTO> proyectos = new ArrayList<>();

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cuit);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    proyectos.add(mapearDefinitivoDTO(rs));
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error al listar proyectos definitivos", e);
        }

        return proyectos;
    }

    @Override
    public List<CrearRequestDefinitivoDTO> listarProyectos() {

        String sql = "SELECT * FROM proyecto_definitivo";

        List<CrearRequestDefinitivoDTO> proyectos = new ArrayList<>();

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                proyectos.add(mapearDefinitivoDTO(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error al listar proyectos definitivos", e);
        }

        return proyectos;
    }

    private CrearRequestDefinitivoDTO mapearDefinitivoDTO(ResultSet rs) throws SQLException {
        return new CrearRequestDefinitivoDTO(rs.getLong("id"), rs.getString("usuario_nombre"), rs.getString("nombre"), rs.getString("descripcion"), rs.getString("actividad_principal"), rs.getString("actividad_secundaria"), rs.getString("telefono"), rs.getString("rubro"), rs.getString("descripcion_servicio"), rs.getString("persona_referente"), rs.getString("materias_primas"), rs.getString("destino_produccion"), (Double) rs.getObject("superficie_requerida"), (Double) rs.getObject("superficie_trabajo"), (Double) rs.getObject("superficie_deposito"), (Double) rs.getObject("superficie_cubierta"), (Double) rs.getObject("superficie_estacionamiento"), rs.getString("tiene_planos"), rs.getString("link_planos"), (Double) rs.getObject("energia_requerida"), (Integer) rs.getObject("personal_a_ocupar"), rs.getString("tension_alimentacion"), (Double) rs.getObject("potencia_instalada"), (Double) rs.getObject("agua_mensual"), (Double) rs.getObject("gas_mensual"), rs.getString("residuos_tipo"), (Double) rs.getObject("residuos_cantidad"), rs.getString("tratamiento_efluentes"), rs.getString("tipo_empresa"), rs.getString("direccion"), rs.getString("pretension_traslado"), rs.getString("emplazamiento_actual"), rs.getString("tiempo_radicacion"), rs.getString("balanza_publica"), rs.getString("comedor"), rs.getString("sum_coworking"), rs.getString("estado"), rs.getString("cuit_empresa"),

                rs.getTimestamp("fecha_creacion") != null ? rs.getTimestamp("fecha_creacion").toLocalDateTime() : null,

                rs.getTimestamp("fecha_actualizacion") != null ? rs.getTimestamp("fecha_actualizacion").toLocalDateTime() : null,

                rs.getString("link_viabilidad_financiera"), rs.getString("link_estudio_mercado"), rs.getString("link_impacto_ambiental"), rs.getString("link_habilitacion_municipal"), rs.getString("certificado_inhibiciones"));
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