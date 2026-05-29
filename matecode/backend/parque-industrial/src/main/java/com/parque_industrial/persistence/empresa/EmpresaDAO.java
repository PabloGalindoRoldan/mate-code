package com.parque_industrial.persistence.empresa;

import com.parque_industrial.dto.empresa.EmpresaDTO;
import com.parque_industrial.entities.Empresa;
// import com.parque_industrial.entities.Usuario;

import java.util.List;

public interface EmpresaDAO {
    void guardar(Empresa empresa);

    boolean existeEmpresa(String cuit);

    public List<EmpresaDTO> empresasRadicadas();

    public List<EmpresaDTO> empresasNoRedicadas();

    public List<EmpresaDTO> empresas();

    public EmpresaDTO buscarEmpresaPorCuit(String cuit);

    public void asignarLote(String cuit, Integer idLote);

    public void ocuparLote(String cuit, Integer idLote);

    void actualizarEstadoRadicacion(String cuit, boolean radicada);

    void desocuparLote(String cuit);
}
