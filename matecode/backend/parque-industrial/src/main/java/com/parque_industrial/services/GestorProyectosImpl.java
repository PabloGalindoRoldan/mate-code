package com.parque_industrial.services;

import com.parque_industrial.dto.proyecto.CrearRequestDTO;
import com.parque_industrial.dto.proyecto.CrearRequestDefinitivoDTO;
import com.parque_industrial.dto.proyecto.ProyectosPorCuitDTO;
import com.parque_industrial.entities.ProyectoPreliminar;
import com.parque_industrial.persistence.proyecto.ProyectoDefinitivoDAO;
import com.parque_industrial.persistence.proyecto.ProyectoPreliminarDAO;
import org.springframework.stereotype.Service;
import com.parque_industrial.entities.ProyectoDefinitivo;

import java.util.List;

@Service
public class GestorProyectosImpl implements GestorProyectos {

    private final ProyectoPreliminarDAO proyectoPreliminarDAO;
    private final ProyectoDefinitivoDAO  proyectoDefinitivoDAO;

    public GestorProyectosImpl(ProyectoPreliminarDAO proyectoPreliminarDAO,  ProyectoDefinitivoDAO proyectoDefinitivoDAO ) {
        this.proyectoPreliminarDAO = proyectoPreliminarDAO;
        this.proyectoDefinitivoDAO = proyectoDefinitivoDAO;
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
        proyectoPreliminarDAO.guardar(entidad);
    }

    @Override
    public void crearProyectoDefinitivo(
            CrearRequestDefinitivoDTO dto) {

        ProyectoDefinitivo entidad =
                new ProyectoDefinitivo();

        entidad.setUsuarioNombre(
                dto.usuarioNombre());

        entidad.setCuitEmpresa(
                dto.cuitEmpresa());

        entidad.setNombre(
                dto.nombre());

        entidad.setDescripcion(
                dto.descripcion());

        entidad.setActividadPrincipal(
                dto.actividadPrincipal());

        entidad.setActividadSecundaria(
                dto.actividadSecundaria());

        entidad.setTelefono(
                dto.telefono());

        entidad.setRubro(
                dto.rubro());

        entidad.setDescripcionServicio(
                dto.descripcionServicio());

        entidad.setPersonaReferente(
                dto.personaReferente());

        entidad.setMateriasPrimas(
                dto.materiasPrimas());

        entidad.setDestinoProduccion(
                dto.destinoProduccion());

        entidad.setSuperficieRequerida(
                dto.superficieRequerida());

        entidad.setSuperficieTrabajo(
                dto.superficieTrabajo());

        entidad.setSuperficieDeposito(
                dto.superficieDeposito());

        entidad.setSuperficieCubierta(
                dto.superficieCubierta());

        entidad.setSuperficieEstacionamiento(
                dto.superficieEstacionamiento());

        entidad.setTienePlanos(
                dto.tienePlanos());

        entidad.setLinkPlanos(
                dto.linkPlanos());

        entidad.setEnergiaRequerida(
                dto.energiaRequerida());

        entidad.setPersonalAOcupar(
                dto.personalAOcupar());

        entidad.setTensionAlimentacion(
                dto.tensionAlimentacion());

        entidad.setPotenciaInstalada(
                dto.potenciaInstalada());

        entidad.setAguaMensual(
                dto.aguaMensual());

        entidad.setGasMensual(
                dto.gasMensual());

        entidad.setResiduosTipo(
                dto.residuosTipo());

        entidad.setResiduosCantidad(
                dto.residuosCantidad());

        entidad.setTratamientoEfluentes(
                dto.tratamientoEfluentes());

        entidad.setTipoEmpresa(
                dto.tipoEmpresa());

        entidad.setDireccion(
                dto.direccion());

        entidad.setPretensionTraslado(
                dto.pretensionTraslado());

        entidad.setEmplazamientoActual(
                dto.emplazamientoActual());

        entidad.setTiempoRadicacion(
                dto.tiempoRadicacion());

        entidad.setBalanzaPublica(
                dto.balanzaPublica());

        entidad.setComedor(
                dto.comedor());

        entidad.setSumCoworking(
                dto.sumCoworking());

        entidad.setLinkViabilidadFinanciera(
                dto.linkViabilidadFinanciera());

        entidad.setLinkEstudioMercado(
                dto.linkEstudioMercado());

        entidad.setLinkImpactoAmbiental(
                dto.linkImpactoAmbiental());

        entidad.setLinkHabilitacionMunicipal(
                dto.linkHabilitacionMunicipal());

        entidad.setLinkCertificadoInhibiciones(
                dto.linkCertificadoInhibiciones());

        entidad.setEstado("en_revision");

        proyectoDefinitivoDAO
                .guardarProyectoDefinitivo(
                        entidad);
    }

    @Override
    public List<CrearRequestDefinitivoDTO> listarProyectos() {
        return List.of();
    }

    @Override
    public ProyectosPorCuitDTO listarProyectosPorCuit(String cuit) {
        List<CrearRequestDefinitivoDTO> definitivos = proyectoDefinitivoDAO.listarDefinitivosPorCuit(cuit);
        List<CrearRequestDTO> preliminares = proyectoPreliminarDAO.listarPreliminarePorCuit(cuit);
        return new ProyectosPorCuitDTO(definitivos, preliminares);
    }



}