package com.parque_industrial.persistence.proyecto;

import com.parque_industrial.dto.proyecto.CrearRequestDefinitivoDTO;
import com.parque_industrial.exceptions.DatabaseException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
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
}
