package com.parque_industrial.persistence.inventario;

import com.parque_industrial.entities.Elemento;
import com.parque_industrial.entities.CategoriaInventario;
import com.parque_industrial.entities.RazonBajaCategoria;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class InventarioDAOJDBC implements InventarioDAO {

    private final JdbcTemplate jdbcTemplate;

    public InventarioDAOJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Mapeador explícito agregando @NonNull en los parámetros heredados para saciar
    // al compilador
    private final RowMapper<Elemento> elementRowMapper = new RowMapper<Elemento>() {
        @Override
        public Elemento mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            Elemento elemento = new Elemento();
            elemento.setId(rs.getInt("id"));
            elemento.setNombre(rs.getString("nombre"));
            elemento.setCategoria(CategoriaInventario.valueOf(rs.getString("categoria")));
            elemento.setDetalle(rs.getString("detalle"));
            elemento.setActivo(rs.getBoolean("activo"));

            String razonStr = rs.getString("baja_razon_categoria");
            if (razonStr != null) {
                elemento.setBajaRazonCategoria(RazonBajaCategoria.valueOf(razonStr));
            }

            elemento.setBajaObservacion(rs.getString("baja_observacion"));
            return elemento;
        }
    };

    @Override
    public Elemento guardar(Elemento elemento) {
        String sql = "INSERT INTO inventario (nombre, categoria, activo, detalle) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, elemento.getNombre());
            ps.setString(2, elemento.getCategoria().name());
            ps.setBoolean(3, elemento.isActivo());
            ps.setString(4, elemento.getDetalle());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            elemento.setId(key.intValue());
        } else {
            throw new IllegalStateException("No se pudo obtener el ID autoincremental generado por la base de datos.");
        }

        return elemento;
    }

    @Override
    public Optional<Elemento> buscarPorId(Integer id) {
        String sql = "SELECT id, nombre, categoria, activo, baja_razon_categoria, baja_observacion, detalle FROM inventario WHERE id = ?";
        List<Elemento> resultados = jdbcTemplate.query(sql, elementRowMapper, id);
        return resultados.stream().findFirst();
    }

    @Override
    public List<Elemento> listarTodos() {
        String sql = "SELECT id, nombre, categoria, activo, baja_razon_categoria, baja_observacion, detalle FROM inventario";
        return jdbcTemplate.query(sql, elementRowMapper);
    }

    @Override
    public List<Elemento> listarActivos() {
        String sql = "SELECT id, nombre, categoria, activo, baja_razon_categoria, baja_observacion, detalle FROM inventario WHERE activo = TRUE";
        return jdbcTemplate.query(sql, elementRowMapper);
    }

    @Override
    public void actualizar(Elemento elemento) {
        String sql = "UPDATE inventario SET nombre = ?, categoria = ?, activo = ?, baja_razon_categoria = ?, baja_observacion = ?, detalle = ? WHERE id = ?";

        String razonBaja = (elemento.getBajaRazonCategoria() != null) ? elemento.getBajaRazonCategoria().name() : null;

        jdbcTemplate.update(sql,
                elemento.getNombre(),
                elemento.getCategoria().name(),
                elemento.isActivo(),
                razonBaja,
                elemento.getBajaObservacion(),
                elemento.getDetalle(),
                elemento.getId());
    }
}