package com.parque_industrial.services;

import java.util.List;
import com.parque_industrial.entities.Empresa;
import com.parque_industrial.entities.Usuario;
import com.parque_industrial.persistence.dtos.ConsumosDTO;

public interface DAOEmpresa {
    public void cargarEmpresasRadicadas(List<Empresa> empresas); // este es el metodo para cargar las empresas ya
                                                                 // existentes, cargar su respectivo lote

    public void crearEmpresa(Empresa empresa);

    public List<Empresa> empresasRadicadas();

    public List<Empresa> empresasNoRedicadas();

    public List<Empresa> empresas();

    public void asignarRepresentante(Usuario representanteEmpresa);

}
