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


    public boolean existeEmpresa(String cuit) {
        String sql = "SELECT COUNT(*) FROM empresas WHERE cuit = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cuit);
        return count != null && count == 1;
    }

//    public Empresa buscarEmpresaPorCuit(String cuit) {
//        String sql = "SELECT * from empresas WHERE cuit = ?";
//        Empresa empresa = null;
//        try {
//            empresa = jdbcTemplate.queryForObject(sql, new EmpresaRowMapper(), cuit);
//        } catch (DataAccessException e) {
//            throw new IllegalArgumentException("Empresa no encontrada");
//        }
//        return empresa;
//    }

}