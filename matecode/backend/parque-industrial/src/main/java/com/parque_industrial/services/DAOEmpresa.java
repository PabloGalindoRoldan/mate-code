package com.parque_industrial.services;

import com.parque_industrial.entities.Empresa;
import com.parque_industrial.entities.Lote;
import com.parque_industrial.entities.RepresentanteEmpresa;

import java.util.List;

public interface DAOEmpresa {
    public void cargarEmpresasRadicadas(List<Empresa> empresas); // este es el metodo para cargar las empresas ya existentes, cargar su respectivo lote
    public void crearEmpresa(Empresa empresa);
    public List<Empresa> empresasRadicadas();
    public List<Empresa> empresasNoRedicadas();
    public List<Empresa> empresas();
    public void asignarRepresentante(RepresentanteEmpresa representanteEmpresa);
    public void asignarLote(Empresa empresa, Lote lote);

}
// la interfaz es para invertir la dependencia, despues en jdbc tendriamos que implementarla
