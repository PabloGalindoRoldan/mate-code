package com.parque_industrial.services;

import com.parque_industrial.dto.empresa.EmpresaDTO;
// import com.parque_industrial.entities.Empresa;
// import com.parque_industrial.entities.Usuario;
import com.parque_industrial.persistence.empresa.EmpresaDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GestorEmpresa {
    private EmpresaDAO dao;

    public GestorEmpresa(EmpresaDAO dao) {
        this.dao = dao;
    }

    public void CrearEmpresa(EmpresaDTO e) {
        dao.guardar(e.entidad());
    }

    public boolean existeEmpresa(String cuit) {
        return dao.existeEmpresa(cuit);
    }

    public EmpresaDTO buscarEmpresaPorCuit(String cuit) {
        return dao.buscarEmpresaPorCuit(cuit);
    }

    public List<EmpresaDTO> empresasRadicadas() {
        return dao.empresasRadicadas();
    }

    public List<EmpresaDTO> empresasNoRedicadas() {
        return dao.empresasNoRedicadas();
    }

    public List<EmpresaDTO> empresas() {
        return dao.empresas();
    }

    public void asignarLoteAEmpresa(String cuit, Integer idLote) {
        dao.asignarLote(cuit, idLote);
    }

    public void ocuparLote(String cuit, Integer idLote) {
        dao.ocuparLote(cuit, idLote);
    }

    public void actualizarEstadoRadicacion(String cuit, boolean radicada) {
        dao.actualizarEstadoRadicacion(cuit, radicada);
    }

    public void desocuparLote(String cuit) {
        dao.desocuparLote(cuit);
    }
}
