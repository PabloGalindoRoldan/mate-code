 package com.parque_industrial.persistence.proyecto;

import com.parque_industrial.dto.proyecto.CrearRequestDefinitivoDTO;
import com.parque_industrial.entities.ProyectoDefinitivo;
import com.parque_industrial.exceptions.DatabaseException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ProyectoDefinitivoJDBC implements ProyectoDefinitivoDAO {

    private final JdbcTemplate jdbcTemplate;

    public ProyectoDefinitivoJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<CrearRequestDefinitivoDTO> listarDefinitivosPorCuit(String cuit) {

        String sql = """
                SELECT *
                FROM proyecto_definitivo
                WHERE cuit_empresa = ?
                """;

        try {

            return jdbcTemplate.query(
                    sql,
                    this::mapearDefinitivoDTO,
                    cuit
            );

        } catch (Exception e) {

            throw new DatabaseException(
                    "Error al listar proyectos definitivos",
                    e
            );
        }
    }

    @Override
    public List<CrearRequestDefinitivoDTO> listarProyectos() {

        String sql = """
                SELECT *
                FROM proyecto_definitivo
                """;

        try {

            return jdbcTemplate.query(
                    sql,
                    this::mapearDefinitivoDTO
            );

        } catch (Exception e) {

            throw new DatabaseException(
                    "Error al listar proyectos definitivos",
                    e
            );
        }
    }

    @Override
    public void guardarProyectoDefinitivo(
            ProyectoDefinitivo proyecto
    ) {

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
                    certificado_inhibiciones

                )
                VALUES (
                    ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,
                    ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,
                    ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,
                    ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
                )
                """;

        try {

            jdbcTemplate.update(
                    sql,

                    proyecto.getUsuarioNombre(),
                    proyecto.getNombre(),
                    proyecto.getDescripcion(),
                    proyecto.getActividadPrincipal(),
                    proyecto.getActividadSecundaria(),
                    proyecto.getTelefono(),
                    proyecto.getRubro(),
                    proyecto.getDescripcionServicio(),
                    proyecto.getPersonaReferente(),
                    proyecto.getMateriasPrimas(),
                    proyecto.getDestinoProduccion(),

                    proyecto.getSuperficieRequerida(),
                    proyecto.getSuperficieTrabajo(),
                    proyecto.getSuperficieDeposito(),
                    proyecto.getSuperficieCubierta(),
                    proyecto.getSuperficieEstacionamiento(),

                    proyecto.getTienePlanos(),
                    proyecto.getLinkPlanos(),

                    proyecto.getEnergiaRequerida(),
                    proyecto.getPersonalAOcupar(),
                    proyecto.getTensionAlimentacion(),
                    proyecto.getPotenciaInstalada(),
                    proyecto.getAguaMensual(),
                    proyecto.getGasMensual(),

                    proyecto.getResiduosTipo(),
                    proyecto.getResiduosCantidad(),
                    proyecto.getTratamientoEfluentes(),
                    proyecto.getTipoEmpresa(),
                    proyecto.getDireccion(),
                    proyecto.getPretensionTraslado(),
                    proyecto.getEmplazamientoActual(),
                    proyecto.getTiempoRadicacion(),

                    proyecto.getBalanzaPublica(),
                    proyecto.getComedor(),
                    proyecto.getSumCoworking(),

                    proyecto.getEstado(),
                    proyecto.getCuitEmpresa(),

                    Timestamp.valueOf(proyecto.getFechaCreacion()),
                    Timestamp.valueOf(proyecto.getFechaActualizacion()),

                    proyecto.getLinkViabilidadFinanciera(),
                    proyecto.getLinkEstudioMercado(),
                    proyecto.getLinkImpactoAmbiental(),
                    proyecto.getLinkHabilitacionMunicipal(),
                    proyecto.getLinkCertificadoInhibiciones()
            );

        } catch (Exception e) {

            throw new DatabaseException(
                    "Error al guardar proyecto definitivo",
                    e
            );
        }
    }

    @Override
    public void actualizar(ProyectoDefinitivo proyecto) {

        String sql = """
                UPDATE proyecto_definitivo
                SET

                    usuario_nombre = ?,
                    nombre = ?,
                    descripcion = ?,
                    actividad_principal = ?,
                    actividad_secundaria = ?,
                    telefono = ?,
                    rubro = ?,
                    descripcion_servicio = ?,
                    persona_referente = ?,
                    materias_primas = ?,
                    destino_produccion = ?,

                    superficie_requerida = ?,
                    superficie_trabajo = ?,
                    superficie_deposito = ?,
                    superficie_cubierta = ?,
                    superficie_estacionamiento = ?,

                    tiene_planos = ?,
                    link_planos = ?,

                    energia_requerida = ?,
                    personal_a_ocupar = ?,
                    tension_alimentacion = ?,
                    potencia_instalada = ?,
                    agua_mensual = ?,
                    gas_mensual = ?,

                    residuos_tipo = ?,
                    residuos_cantidad = ?,
                    tratamiento_efluentes = ?,
                    tipo_empresa = ?,
                    direccion = ?,
                    pretension_traslado = ?,
                    emplazamiento_actual = ?,
                    tiempo_radicacion = ?,

                    balanza_publica = ?,
                    comedor = ?,
                    sum_coworking = ?,

                    estado = ?,
                    cuit_empresa = ?,
                    fecha_actualizacion = ?,

                    link_viabilidad_financiera = ?,
                    link_estudio_mercado = ?,
                    link_impacto_ambiental = ?,
                    link_habilitacion_municipal = ?,
                    certificado_inhibiciones = ?

                WHERE id = ?
                """;

        try {

            jdbcTemplate.update(
                    sql,

                    proyecto.getUsuarioNombre(),
                    proyecto.getNombre(),
                    proyecto.getDescripcion(),
                    proyecto.getActividadPrincipal(),
                    proyecto.getActividadSecundaria(),
                    proyecto.getTelefono(),
                    proyecto.getRubro(),
                    proyecto.getDescripcionServicio(),
                    proyecto.getPersonaReferente(),
                    proyecto.getMateriasPrimas(),
                    proyecto.getDestinoProduccion(),

                    proyecto.getSuperficieRequerida(),
                    proyecto.getSuperficieTrabajo(),
                    proyecto.getSuperficieDeposito(),
                    proyecto.getSuperficieCubierta(),
                    proyecto.getSuperficieEstacionamiento(),

                    proyecto.getTienePlanos(),
                    proyecto.getLinkPlanos(),

                    proyecto.getEnergiaRequerida(),
                    proyecto.getPersonalAOcupar(),
                    proyecto.getTensionAlimentacion(),
                    proyecto.getPotenciaInstalada(),
                    proyecto.getAguaMensual(),
                    proyecto.getGasMensual(),

                    proyecto.getResiduosTipo(),
                    proyecto.getResiduosCantidad(),
                    proyecto.getTratamientoEfluentes(),
                    proyecto.getTipoEmpresa(),
                    proyecto.getDireccion(),
                    proyecto.getPretensionTraslado(),
                    proyecto.getEmplazamientoActual(),
                    proyecto.getTiempoRadicacion(),

                    proyecto.getBalanzaPublica(),
                    proyecto.getComedor(),
                    proyecto.getSumCoworking(),

                    proyecto.getEstado(),
                    proyecto.getCuitEmpresa(),

                    Timestamp.valueOf(proyecto.getFechaActualizacion()),

                    proyecto.getLinkViabilidadFinanciera(),
                    proyecto.getLinkEstudioMercado(),
                    proyecto.getLinkImpactoAmbiental(),
                    proyecto.getLinkHabilitacionMunicipal(),
                    proyecto.getLinkCertificadoInhibiciones(),

                    proyecto.getId()
            );

        } catch (Exception e) {

            throw new DatabaseException(
                    "Error al actualizar proyecto definitivo",
                    e
            );
        }
    }



    @Override
    public Optional<ProyectoDefinitivo> buscarPorId(Long id) {

        String sql = """
                SELECT *
                FROM proyecto_definitivo
                WHERE id = ?
                """;

        try {

            List<ProyectoDefinitivo> resultados = jdbcTemplate.query(
                    sql,
                    this::mapearEntidad,
                    id
            );

            return resultados.stream().findFirst();

        } catch (Exception e) {

            throw new DatabaseException(
                    "Error al buscar proyecto definitivo",
                    e
            );
        }
    }

    private CrearRequestDefinitivoDTO mapearDefinitivoDTO(
            ResultSet rs,
            int rowNum
    ) throws SQLException {

        Timestamp fechaCreacionTs =
                rs.getTimestamp("fecha_creacion");

        Timestamp fechaActualizacionTs =
                rs.getTimestamp("fecha_actualizacion");

        LocalDateTime fechaCreacion =
                fechaCreacionTs != null
                        ? fechaCreacionTs.toLocalDateTime()
                        : null;

        LocalDateTime fechaActualizacion =
                fechaActualizacionTs != null
                        ? fechaActualizacionTs.toLocalDateTime()
                        : null;

        return new CrearRequestDefinitivoDTO(
                rs.getLong("id"),
                rs.getString("usuario_nombre"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getString("actividad_principal"),
                rs.getString("actividad_secundaria"),
                rs.getString("telefono"),
                rs.getString("rubro"),
                rs.getString("descripcion_servicio"),
                rs.getString("persona_referente"),
                rs.getString("materias_primas"),
                rs.getString("destino_produccion"),
                (Double) rs.getObject("superficie_requerida"),
                (Double) rs.getObject("superficie_trabajo"),
                (Double) rs.getObject("superficie_deposito"),
                (Double) rs.getObject("superficie_cubierta"),
                (Double) rs.getObject("superficie_estacionamiento"),
                rs.getString("tiene_planos"),
                rs.getString("link_planos"),
                (Double) rs.getObject("energia_requerida"),
                (Integer) rs.getObject("personal_a_ocupar"),
                rs.getString("tension_alimentacion"),
                (Double) rs.getObject("potencia_instalada"),
                (Double) rs.getObject("agua_mensual"),
                (Double) rs.getObject("gas_mensual"),
                rs.getString("residuos_tipo"),
                (Double) rs.getObject("residuos_cantidad"),
                rs.getString("tratamiento_efluentes"),
                rs.getString("tipo_empresa"),
                rs.getString("direccion"),
                rs.getString("pretension_traslado"),
                rs.getString("emplazamiento_actual"),
                rs.getString("tiempo_radicacion"),
                rs.getString("balanza_publica"),
                rs.getString("comedor"),
                rs.getString("sum_coworking"),
                rs.getString("estado"),
                rs.getString("cuit_empresa"),
                fechaCreacion,
                fechaActualizacion,
                rs.getString("link_viabilidad_financiera"),
                rs.getString("link_estudio_mercado"),
                rs.getString("link_impacto_ambiental"),
                rs.getString("link_habilitacion_municipal"),
                rs.getString("certificado_inhibiciones")
        );
    }

    private ProyectoDefinitivo mapearEntidad(
            ResultSet rs,
            int rowNum
    ) throws SQLException {

        ProyectoDefinitivo proyecto =
                new ProyectoDefinitivo();

        proyecto.setId(rs.getLong("id"));
        proyecto.setUsuarioNombre(rs.getString("usuario_nombre"));
        proyecto.setNombre(rs.getString("nombre"));
        proyecto.setDescripcion(rs.getString("descripcion"));
        proyecto.setActividadPrincipal(rs.getString("actividad_principal"));
        proyecto.setActividadSecundaria(rs.getString("actividad_secundaria"));
        proyecto.setTelefono(rs.getString("telefono"));
        proyecto.setRubro(rs.getString("rubro"));
        proyecto.setDescripcionServicio(rs.getString("descripcion_servicio"));
        proyecto.setPersonaReferente(rs.getString("persona_referente"));
        proyecto.setMateriasPrimas(rs.getString("materias_primas"));
        proyecto.setDestinoProduccion(rs.getString("destino_produccion"));

        proyecto.setSuperficieRequerida(
                (Double) rs.getObject("superficie_requerida"));

        proyecto.setSuperficieTrabajo(
                (Double) rs.getObject("superficie_trabajo"));

        proyecto.setSuperficieDeposito(
                (Double) rs.getObject("superficie_deposito"));

        proyecto.setSuperficieCubierta(
                (Double) rs.getObject("superficie_cubierta"));

        proyecto.setSuperficieEstacionamiento(
                (Double) rs.getObject("superficie_estacionamiento"));

        proyecto.setTienePlanos(rs.getString("tiene_planos"));
        proyecto.setLinkPlanos(rs.getString("link_planos"));

        proyecto.setEnergiaRequerida(
                (Double) rs.getObject("energia_requerida"));

        proyecto.setPersonalAOcupar(
                (Integer) rs.getObject("personal_a_ocupar"));

        proyecto.setTensionAlimentacion(
                rs.getString("tension_alimentacion"));

        proyecto.setPotenciaInstalada(
                (Double) rs.getObject("potencia_instalada"));

        proyecto.setAguaMensual(
                (Double) rs.getObject("agua_mensual"));

        proyecto.setGasMensual(
                (Double) rs.getObject("gas_mensual"));

        proyecto.setResiduosTipo(rs.getString("residuos_tipo"));

        proyecto.setResiduosCantidad(
                (Double) rs.getObject("residuos_cantidad"));

        proyecto.setTratamientoEfluentes(
                rs.getString("tratamiento_efluentes"));

        proyecto.setTipoEmpresa(rs.getString("tipo_empresa"));
        proyecto.setDireccion(rs.getString("direccion"));
        proyecto.setPretensionTraslado(
                rs.getString("pretension_traslado"));

        proyecto.setEmplazamientoActual(
                rs.getString("emplazamiento_actual"));

        proyecto.setTiempoRadicacion(
                rs.getString("tiempo_radicacion"));

        proyecto.setBalanzaPublica(
                rs.getString("balanza_publica"));

        proyecto.setComedor(rs.getString("comedor"));
        proyecto.setSumCoworking(rs.getString("sum_coworking"));

        proyecto.setEstado(rs.getString("estado"));
        proyecto.setCuitEmpresa(rs.getString("cuit_empresa"));

        Timestamp fechaCreacionTs =
                rs.getTimestamp("fecha_creacion");

        if (fechaCreacionTs != null) {
            proyecto.setFechaCreacion(
                    fechaCreacionTs.toLocalDateTime());
        }

        Timestamp fechaActualizacionTs =
                rs.getTimestamp("fecha_actualizacion");

        if (fechaActualizacionTs != null) {
            proyecto.setFechaActualizacion(
                    fechaActualizacionTs.toLocalDateTime());
        }

        proyecto.setLinkViabilidadFinanciera(
                rs.getString("link_viabilidad_financiera"));

        proyecto.setLinkEstudioMercado(
                rs.getString("link_estudio_mercado"));

        proyecto.setLinkImpactoAmbiental(
                rs.getString("link_impacto_ambiental"));

        proyecto.setLinkHabilitacionMunicipal(
                rs.getString("link_habilitacion_municipal"));

        proyecto.setLinkCertificadoInhibiciones(
                rs.getString("certificado_inhibiciones"));

        return proyecto;
    }
}

