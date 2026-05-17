package com.parque_industrial.persistence.empresa;

import com.parque_industrial.entities.Empresa;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EmpresaDAOJDBC implements EmpresaDAO {

    private final JdbcTemplate jdbcTemplate;

    public EmpresaDAOJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void guardar(Empresa empresa) {
        String sql = """
                INSERT INTO empresas (cuit, razon_social, es_radicada)
                VALUES (?, ?, ?)
                """;
        try {
            jdbcTemplate.update(sql,
                    empresa.getIdentificacion(),
                    empresa.getRazonSocial(),
                    empresa.isEsRadicada());
        } catch (DataAccessException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}