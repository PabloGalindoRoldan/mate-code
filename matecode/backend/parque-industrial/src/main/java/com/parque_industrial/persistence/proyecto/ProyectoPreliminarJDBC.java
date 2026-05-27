package com.parque_industrial.persistence.proyecto;

import com.parque_industrial.dto.proyecto.CrearRequestDTO;
import com.parque_industrial.entities.ProyectoPreliminar;
import com.parque_industrial.exceptions.DatabaseException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class ProyectoPreliminarJDBC implements ProyectoPreliminarDAO {

        private final JdbcTemplate jdbcTemplate;

        public ProyectoPreliminarJDBC(JdbcTemplate jdbcTemplate) {
                this.jdbcTemplate = jdbcTemplate;
        }

        @Override
        public void guardar(ProyectoPreliminar p) {

                String sql = """
                                INSERT INTO proyecto_preliminar (

                                    usuario_nombre,
                                    cuit_empresa,
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

                                    estado

                                )
                                VALUES (
                                    ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,
                                    ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,
                                    ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
                                )
                                """;

                try {

                        jdbcTemplate.update(
                                        sql,

                                        p.getUsuarioNombre(),
                                        p.getCuitEmpresa(),
                                        p.getNombre(),
                                        p.getDescripcion(),
                                        p.getActividadPrincipal(),
                                        p.getActividadSecundaria(),
                                        p.getTelefono(),
                                        p.getRubro(),
                                        p.getDescripcionServicio(),
                                        p.getPersonaReferente(),
                                        p.getMateriasPrimas(),
                                        p.getDestinoProduccion(),

                                        p.getSuperficieRequerida(),
                                        p.getSuperficieTrabajo(),
                                        p.getSuperficieDeposito(),
                                        p.getSuperficieCubierta(),
                                        p.getSuperficieEstacionamiento(),

                                        p.getTienePlanos(),
                                        p.getLinkPlanos(),

                                        p.getEnergiaRequerida(),
                                        p.getPersonalAOcupar(),
                                        p.getTensionAlimentacion(),
                                        p.getPotenciaInstalada(),
                                        p.getAguaMensual(),
                                        p.getGasMensual(),

                                        p.getResiduosTipo(),
                                        p.getResiduosCantidad(),
                                        p.getTratamientoEfluentes(),
                                        p.getTipoEmpresa(),
                                        p.getDireccion(),
                                        p.getPretensionTraslado(),
                                        p.getEmplazamientoActual(),
                                        p.getTiempoRadicacion(),

                                        p.getBalanzaPublica(),
                                        p.getComedor(),
                                        p.getSumCoworking(),

                                        p.getEstado() != null
                                                        ? p.getEstado()
                                                        : "en_revision");

                } catch (Exception e) {

                        throw new DatabaseException(
                                        "Error al insertar proyecto preliminar: "
                                                        + e.getMessage(),
                                        e);
                }
        }

        @Override
        public Optional<ProyectoPreliminar> buscarPorId(Long id) {

                String sql = """
                                SELECT *
                                FROM proyecto_preliminar
                                WHERE id = ?
                                """;

                try {

                        List<ProyectoPreliminar> resultados = jdbcTemplate.query(
                                        sql,
                                        this::mapearEntidad,
                                        id);

                        return resultados.stream().findFirst();

                } catch (Exception e) {

                        throw new DatabaseException(
                                        "Error al buscar proyecto preliminar: "
                                                        + e.getMessage(),
                                        e);
                }
        }

        @Override
        public void actualizar(ProyectoPreliminar proyecto) {

                String sql = """
                                UPDATE proyecto_preliminar
                                SET

                                    usuario_nombre = ?,
                                    cuit_empresa = ?,
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
                                    fecha_actualizacion = ?

                                WHERE id = ?
                                """;

                try {

                        jdbcTemplate.update(
                                        sql,

                                        proyecto.getUsuarioNombre(),
                                        proyecto.getCuitEmpresa(),
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

                                        Timestamp.valueOf(
                                                        proyecto.getFechaActualizacion()),

                                        proyecto.getId());

                } catch (Exception e) {

                        throw new DatabaseException(
                                        "Error al actualizar proyecto preliminar: "
                                                        + e.getMessage(),
                                        e);
                }
        }

        @Override
        public List<CrearRequestDTO> listarProyectos() {

                String sql = """
                                SELECT *
                                FROM proyecto_preliminar
                                """;

                try {

                        return jdbcTemplate.query(
                                        sql,
                                        this::mapearDTO);

                } catch (Exception e) {

                        throw new DatabaseException(
                                        "Error al listar proyectos preliminares",
                                        e);
                }
        }

        @Override
        public List<CrearRequestDTO> listarPreliminarPorCuit(
                        String cuit) {

                String sql = """
                                SELECT *
                                FROM proyecto_preliminar
                                WHERE cuit_empresa = ?
                                """;

                try {

                        return jdbcTemplate.query(
                                        sql,
                                        this::mapearDTO,
                                        cuit);

                } catch (Exception e) {

                        throw new DatabaseException(
                                        "Error al listar proyectos preliminares",
                                        e);
                }
        }

        private CrearRequestDTO mapearDTO(
                        ResultSet rs,
                        int rowNum) throws SQLException {

                CrearRequestDTO dto = new CrearRequestDTO();

                dto.setId(rs.getLong("id"));
                dto.setUsuarioNombre(rs.getString("usuario_nombre"));
                dto.setCuitEmpresa(rs.getString("cuit_empresa"));
                dto.setNombre(rs.getString("nombre"));
                dto.setDescripcion(rs.getString("descripcion"));
                dto.setActividadPrincipal(rs.getString("actividad_principal"));
                dto.setActividadSecundaria(rs.getString("actividad_secundaria"));
                dto.setTelefono(rs.getString("telefono"));
                dto.setRubro(rs.getString("rubro"));
                dto.setDescripcionServicio(rs.getString("descripcion_servicio"));
                dto.setPersonaReferente(rs.getString("persona_referente"));
                dto.setMateriasPrimas(rs.getString("materias_primas"));
                dto.setDestinoProduccion(rs.getString("destino_produccion"));

                dto.setSuperficieRequerida(
                                (Double) rs.getObject("superficie_requerida"));

                dto.setSuperficieTrabajo(
                                (Double) rs.getObject("superficie_trabajo"));

                dto.setSuperficieDeposito(
                                (Double) rs.getObject("superficie_deposito"));

                dto.setSuperficieCubierta(
                                (Double) rs.getObject("superficie_cubierta"));

                dto.setSuperficieEstacionamiento(
                                (Double) rs.getObject("superficie_estacionamiento"));

                dto.setTienePlanos(rs.getString("tiene_planos"));
                dto.setLinkPlanos(rs.getString("link_planos"));

                dto.setEnergiaRequerida(
                                (Double) rs.getObject("energia_requerida"));

                dto.setPersonalAOcupar(
                                (Integer) rs.getObject("personal_a_ocupar"));

                dto.setTensionAlimentacion(
                                rs.getString("tension_alimentacion"));

                dto.setPotenciaInstalada(
                                (Double) rs.getObject("potencia_instalada"));

                dto.setAguaMensual(
                                (Double) rs.getObject("agua_mensual"));

                dto.setGasMensual(
                                (Double) rs.getObject("gas_mensual"));

                dto.setResiduosTipo(rs.getString("residuos_tipo"));

                dto.setResiduosCantidad(
                                (Double) rs.getObject("residuos_cantidad"));

                dto.setTratamientoEfluentes(
                                rs.getString("tratamiento_efluentes"));

                dto.setTipoEmpresa(rs.getString("tipo_empresa"));
                dto.setDireccion(rs.getString("direccion"));
                dto.setPretensionTraslado(
                                rs.getString("pretension_traslado"));

                dto.setEmplazamientoActual(
                                rs.getString("emplazamiento_actual"));

                dto.setTiempoRadicacion(
                                rs.getString("tiempo_radicacion"));

                dto.setBalanzaPublica(
                                rs.getString("balanza_publica"));

                dto.setComedor(rs.getString("comedor"));
                dto.setSumCoworking(rs.getString("sum_coworking"));

                dto.setEstado(rs.getString("estado"));

                return dto;
        }

        private ProyectoPreliminar mapearEntidad(
                        ResultSet rs,
                        int rowNum) throws SQLException {

                ProyectoPreliminar proyecto = new ProyectoPreliminar();

                proyecto.setId(rs.getLong("id"));

                proyecto.setUsuarioNombre(
                                rs.getString("usuario_nombre"));

                proyecto.setCuitEmpresa(
                                rs.getString("cuit_empresa"));

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

                Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion");

                if (fechaCreacion != null) {

                        proyecto.setFechaCreacion(
                                        fechaCreacion.toLocalDateTime());
                }

                Timestamp fechaActualizacion = rs.getTimestamp("fecha_actualizacion");

                if (fechaActualizacion != null) {

                        proyecto.setFechaActualizacion(
                                        fechaActualizacion.toLocalDateTime());
                }

                return proyecto;
        }
}