package Modelo;

import java.util.List;

public interface GestorEmpresa {
    public void cargarEmpresasRadicadas(List<Empresa> empresas); // este es el metodo para cargar las empresas ya existentes, cargar su respectivo lote
    public void crearEmpresa(Empresa empresa);
    public List<Empresa> empresasRadicadas();
    public List<Empresa> empresasNoRedicadas();
    public List<Empresa> empresas();
    public void asignarRepresentante(RepresentanteEmpresa representanteEmpresa);
}
