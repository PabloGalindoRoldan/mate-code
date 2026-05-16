package com.parque_industrial.persistence.empresa;

import com.parque_industrial.entities.Empresa;

public interface EmpresaDAO {
    void guardar(Empresa empresa);

    boolean existeEmpresa(String cuit);

}



