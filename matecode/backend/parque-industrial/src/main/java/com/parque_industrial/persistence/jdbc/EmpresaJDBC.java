package com.parque_industrial.persistence.jdbc;

import com.parque_industrial.entities.Empresa;
import com.parque_industrial.entities.Usuario;
import com.parque_industrial.persistence.dtos.ConsumosDTO;
import com.parque_industrial.services.DAOEmpresa;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpresaJDBC implements DAOEmpresa {
    private final DataSource conecction;

    public EmpresaJDBC(DataSource conecction) {
        this.conecction = conecction;
    }


    @Override
    public void cargarEmpresasRadicadas(List<Empresa> empresas) {

    }

    @Override
    public void crearEmpresa(Empresa empresa) {

    }

    @Override
    public List<Empresa> empresasRadicadas() {
        return List.of();
    }

    @Override
    public List<Empresa> empresasNoRedicadas() {
        return List.of();
    }

    @Override
    public List<Empresa> empresas() {
        return List.of();
    }

    @Override
    public void asignarRepresentante(Usuario representanteEmpresa) {

    }


}
