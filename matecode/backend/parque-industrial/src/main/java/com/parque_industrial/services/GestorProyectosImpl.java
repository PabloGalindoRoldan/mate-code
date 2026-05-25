package com.parque_industrial.services;

import com.parque_industrial.dto.proyecto.CrearRequestDTO;
import com.parque_industrial.entities.ProyectoPreliminar;
import com.parque_industrial.persistence.proyecto.ProyectoDAO;
import org.springframework.stereotype.Service;

@Service
public class GestorProyectosImpl implements GestorProyectos {

    private final ProyectoDAO proyectoDAO;

    public GestorProyectosImpl(ProyectoDAO proyectoDAO) {
        this.proyectoDAO = proyectoDAO;
    }

    @Override
    public void crearProyectoPreliminar(CrearRequestDTO dto) {
        ProyectoPreliminar entidad = new ProyectoPreliminar();

        entidad.setUsuarioNombre(dto.getUsuarioNombre());
        entidad.setCuitEmpresa(dto.getCuitEmpresa());
        entidad.setNombre(dto.getNombre());
        entidad.setDescripcion(dto.getDescripcion());
        entidad.setActividadPrincipal(dto.getActividadPrincipal());
        entidad.setActividadSecundaria(dto.getActividadSecundaria());
        entidad.setTelefono(dto.getTelefono());
        entidad.setRubro(dto.getRubro());
        entidad.setDescripcionServicio(dto.getDescripcionServicio());
        entidad.setPersonaReferente(dto.getPersonaReferente());
        entidad.setMateriasPrimas(dto.getMateriasPrimas());
        entidad.setDestinoProduccion(dto.getDestinoProduccion());
        entidad.setSuperficieRequerida(dto.getSuperficieRequerida());
        entidad.setSuperficieTrabajo(dto.getSuperficieTrabajo());
        entidad.setSuperficieDeposito(dto.getSuperficieDeposito());
        entidad.setSuperficieCubierta(dto.getSuperficieCubierta());
        entidad.setSuperficieEstacionamiento(dto.getSuperficieEstacionamiento());
        entidad.setTienePlanos(dto.getTienePlanos());
        entidad.setLinkPlanos(dto.getLinkPlanos());
        entidad.setEnergiaRequerida(dto.getEnergiaRequerida());
        entidad.setPersonalAOcupar(dto.getPersonalAOcupar());
        entidad.setTensionAlimentacion(dto.getTensionAlimentacion());
        entidad.setPotenciaInstalada(dto.getPotenciaInstalada());
        entidad.setAguaMensual(dto.getAguaMensual());
        entidad.setGasMensual(dto.getGasMensual());
        entidad.setResiduosTipo(dto.getResiduosTipo());
        entidad.setResiduosCantidad(dto.getResiduosCantidad());
        entidad.setTratamientoEfluentes(dto.getTratamientoEfluentes());
        entidad.setTipoEmpresa(dto.getTipoEmpresa());
        entidad.setDireccion(dto.getDireccion());
        entidad.setPretensionTraslado(dto.getPretensionTraslado());
        entidad.setEmplazamientoActual(dto.getEmplazamientoActual());
        entidad.setTiempoRadicacion(dto.getTiempoRadicacion());
        entidad.setBalanzaPublica(dto.getBalanzaPublica());
        entidad.setComedor(dto.getComedor());
        entidad.setSumCoworking(dto.getSumCoworking());

        String estado = (dto.getEstado() != null && !dto.getEstado().isEmpty()) ? dto.getEstado() : "en_revision";
        entidad.setEstado(estado);
        proyectoDAO.guardar(entidad);
    }
}