package com.parque_industrial.persistence.empresa;

import com.parque_industrial.entities.Empresa;
import com.parque_industrial.entities.Usuario;

import java.util.List;

public interface EmpresaDAO {
    void guardar(Empresa empresa);

    boolean existeEmpresa(String cuit);

//    public List<Empresa> empresasRadicadas();
//
//    public List<Empresa> empresasNoRedicadas();
//
//    public List<Empresa> empresas();
//
//    public void asignarRepresentante(Usuario representanteEmpresa);
//
//    public void cargarEmpresasRadicadas(List<Empresa> empresas); // este es el metodo para cargar las empresas ya
//    // existentes, cargar su respectivo lote
}



