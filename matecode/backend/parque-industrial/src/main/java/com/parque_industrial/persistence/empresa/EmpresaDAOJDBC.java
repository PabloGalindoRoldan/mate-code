package com.parque_industrial.persistence.empresa;

import com.parque_industrial.dto.empresa.EmpresaDTO;
import com.parque_industrial.entities.Empresa;
import com.parque_industrial.entities.Usuario;
import com.parque_industrial.persistence.lote.LoteDAOJDBC;
import com.parque_industrial.services.GestorInmobiliario;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class EmpresaDAOJDBC implements EmpresaDAO {

    private final JdbcTemplate jdbcTemplate;

    public EmpresaDAOJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void guardar(Empresa empresa) {
        String sql = "   INSERT INTO empresas (cuit, razon_social, es_radicada) VALUES (?, ?, ?) ";

            jdbcTemplate.update(sql,
                    empresa.getIdentificacion(),
                    empresa.getRazonSocial(),
                    empresa.isEsRadicada());

    }


    public boolean existeEmpresa(String cuit) {
        String sql = "SELECT COUNT(*) FROM empresas WHERE cuit = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cuit);
        return count != null && count == 1;
    }

    @Override
    public List<EmpresaDTO> empresasRadicadas() {
        String sql = "SELECT * FROM empresas WHERE es_radicada = 1";
        List<EmpresaDTO> empresas = jdbcTemplate.query(sql, (rs, rowNum) -> {
            EmpresaDTO empresa = new EmpresaDTO(
                    rs.getString("identificacion"),
                    rs.getString("razon_social"),
                    rs.getBoolean("es_radicada"),
                    rs.getInt("idlote")
            );
            return empresa;
        });
         return empresas;
   }

    @Override
    public List<EmpresaDTO> empresasNoRedicadas() {
        String sql = "SELECT * FROM empresas WHERE es_radicada = 0";
        List<EmpresaDTO> empresas = jdbcTemplate.query(sql, (rs, rowNum) -> {
            EmpresaDTO empresa = new EmpresaDTO(
                    rs.getString("identificacion"),
                    rs.getString("razon_social"),
                    rs.getBoolean("es_radicada"),
                    rs.getInt("idlote")
            );
            return empresa;
        });
        return empresas;
    }

    @Override
    public List<EmpresaDTO> empresas() {
        String sql = "SELECT * FROM empresas";
        List<EmpresaDTO> empresas = jdbcTemplate.query(sql, (rs, rowNum) -> {
            EmpresaDTO empresa = new EmpresaDTO(
                    rs.getString("identificacion"),
                    rs.getString("razon_social"),
                    rs.getBoolean("es_radicada"),
                    rs.getInt("idlote")
            );
            return empresa;
        });
        return empresas;
    }

    @Override
    public EmpresaDTO buscarEmpresaPorCuit(String cuit) {
        String sql = "SELECT * FROM empresas WHERE cuit = ?";
        try {
            EmpresaDTO empresa = jdbcTemplate.queryForObject(sql,
                    new Object[]{cuit},
                    (rs, rowNum) -> new EmpresaDTO(
                            rs.getString("cuit"),
                            rs.getString("razon_social"),
                            rs.getBoolean("es_radicada"),
                            rs.getInt("idlote")
                    )
            );
            return empresa;
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Empresa no encontrada");
        }
    }

    @Override
    @Transactional
    public void asignarLote(String cuit, Integer idLote) {
        String sql = "UPDATE empresas SET idlote = ? WHERE cuit = ?";
        jdbcTemplate.update(sql, idLote, cuit);
      String sql2= "UPDATE Lote SET estado= reservado WHERE id = ?";
      jdbcTemplate.update(sql2, idLote);
    }

}