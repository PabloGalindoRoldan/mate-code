package com.parque_industrial.services;

import com.parque_industrial.dto.proyecto.ProyectoDefinitivoRequest;
import com.parque_industrial.dto.proyecto.ProyectoPreliminarRequest;
import com.parque_industrial.entities.Proyecto;
import com.parque_industrial.entities.ProyectoDefinitivo;
import com.parque_industrial.entities.ProyectoPreliminar;
import com.parque_industrial.persistence.jdbc.ProyectoDefinitivoJDBC;
import com.parque_industrial.persistence.jdbc.ProyectoPreliminarJDBC;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GestorProyectos {

    private final ProyectoPreliminarJDBC preliminarDAO;
    private final ProyectoDefinitivoJDBC definitivoDAO;

    public GestorProyectos(
            ProyectoPreliminarJDBC preliminarDAO,
            ProyectoDefinitivoJDBC definitivoDAO) {

        this.preliminarDAO = preliminarDAO;
        this.definitivoDAO = definitivoDAO;
    }

    // -------------------- PRELIMINAR --------------------

    public void crearProyectoPreliminar(
            ProyectoPreliminarRequest request)
            throws Exception {

        ProyectoPreliminar proyecto =
                new ProyectoPreliminar(
                        request.getIdentificacion(),
                        request.getActividadPrincipal(),
                        request.getReferente(),
                        request.getSuperficieRequerida(),
                        request.getEnergiaRequerida(),
                        request.getPersonalAOcupar()
                );

        preliminarDAO.guardar(proyecto);
    }

    public void enviarPreliminarARevision(String id)
            throws Exception {

        ProyectoPreliminar proyecto =
                obtenerPreliminar(id);

        proyecto.listoParaRevision();

        preliminarDAO.actualizar(proyecto);
    }

    public void aprobarPreliminar(String id)
            throws Exception {

        ProyectoPreliminar proyecto =
                obtenerPreliminar(id);

        proyecto.aprobar();

        preliminarDAO.actualizar(proyecto);
    }

    public void rechazarPreliminar(String id)
            throws Exception {

        ProyectoPreliminar proyecto =
                obtenerPreliminar(id);

        proyecto.rechazar();

        preliminarDAO.actualizar(proyecto);
    }

    public void rectificarPreliminar(String id)
            throws Exception {

        ProyectoPreliminar proyecto =
                obtenerPreliminar(id);

        proyecto.rectificar();

        preliminarDAO.actualizar(proyecto);
    }

    // -------------------- DEFINITIVO --------------------

    public void crearDefinitivoDesdePreliminar(
            String idPreliminar,
            ProyectoDefinitivoRequest request)
            throws Exception {

        ProyectoPreliminar preliminar =
                obtenerPreliminar(idPreliminar);

        if (!preliminar.getEstado()
                .equals(Proyecto.APROBADO)) {

            throw new IllegalStateException(
                    "El preliminar debe estar aprobado");
        }

        ProyectoDefinitivo proyecto =
                new ProyectoDefinitivo(
                        request.getIdentificacion(),
                        request.getActividadPrincipal(),
                        request.getReferente(),
                        request.getSuperficieRequerida(),
                        request.getEnergiaRequerida(),
                        request.getPersonalAOcupar(),
                        request.getFechaInicioObra(),
                        request.getFechaFinObra(),
                        request.isViabilidadFinanciera(),
                        request.getInformeAmbiental()
                );

        definitivoDAO.guardar(proyecto);
    }

    public void enviarDefinitivoARevision(String id)
            throws Exception {

        ProyectoDefinitivo proyecto =
                obtenerDefinitivo(id);

        proyecto.listoParaRevision();

        definitivoDAO.actualizar(proyecto);
    }

    public void aprobarDefinitivo(String id)
            throws Exception {

        ProyectoDefinitivo proyecto =
                obtenerDefinitivo(id);

        proyecto.aprobar();

        definitivoDAO.actualizar(proyecto);
    }

    public void rechazarDefinitivo(String id)
            throws Exception {

        ProyectoDefinitivo proyecto =
                obtenerDefinitivo(id);

        proyecto.rechazar();

        definitivoDAO.actualizar(proyecto);
    }

    public void rectificarDefinitivo(String id)
            throws Exception {

        ProyectoDefinitivo proyecto =
                obtenerDefinitivo(id);

        proyecto.rectificar();

        definitivoDAO.actualizar(proyecto);
    }

    // -------------------- CONSULTAS --------------------

    public List<ProyectoPreliminar>
    listarPreliminares(String estado)
            throws Exception {

        return preliminarDAO.buscarPorEstado(estado);
    }

    public List<ProyectoDefinitivo>
    listarDefinitivos(String estado)
            throws Exception {

        return definitivoDAO.buscarPorEstado(estado);
    }

    public String estadoPreliminar(String id)
            throws Exception {

        return obtenerPreliminar(id).getEstado();
    }

    public String estadoDefinitivo(String id)
            throws Exception {

        return obtenerDefinitivo(id).getEstado();
    }

    // -------------------- HELPERS --------------------

    private ProyectoPreliminar obtenerPreliminar(String id)
            throws Exception {

        ProyectoPreliminar p =
                preliminarDAO.buscarPorId(id);

        if (p == null) {
            throw new IllegalArgumentException(
                    "Proyecto preliminar no encontrado");
        }

        return p;
    }

    private ProyectoDefinitivo obtenerDefinitivo(String id)
            throws Exception {

        ProyectoDefinitivo p =
                definitivoDAO.buscarPorId(id);

        if (p == null) {
            throw new IllegalArgumentException(
                    "Proyecto definitivo no encontrado");
        }

        return p;
    }
}