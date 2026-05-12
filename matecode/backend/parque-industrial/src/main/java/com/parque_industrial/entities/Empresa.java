package com.parque_industrial.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Empresa {
    private String identificacion; // era un cuit no? al igual que el representante empresa
    private String actividadPrincipal;
    private boolean esRadicada;
    private List<Usuario> representantes;//una empresa puede tener varios representantes
    private List<Lote> lote; // una empresa creo q podia tener mas de un lote o ninguno, por eso puse un list, si no es asi cambiarlo
    private Proyecto proyecto;//una emprsa puedo

    public Empresa(String identificacion, String actividadPrincipal, boolean esRadicada) {
        validarCuit(identificacion);
        validarActividad(actividadPrincipal);
        this.identificacion = identificacion;
        this.actividadPrincipal = actividadPrincipal;
        this.esRadicada = esRadicada;
        this.representantes = new ArrayList<>();
    }
    private void validarActividad(String actividadPrincipal) {
        if(actividadPrincipal == null || actividadPrincipal.isBlank()){
            throw new IllegalArgumentException("La actividad principal no puede estar vacía");
        }
    }
    private  void validarCuit(String identificacion ) {
        if (identificacion == null || identificacion.isBlank()) {
            throw new IllegalArgumentException("La identificación no puede estar vacía");
        }
        String regexCuit = "^\\d{2}-\\d{8}-\\d{1}$";
        if (!identificacion.matches(regexCuit)) {
            throw new IllegalArgumentException("El formato del CUIT es inválido. Debe ser XX-XXXXXXXX-X.");
        }
    }
    public void asignarLote(Lote lote){
        this.lote.add(lote);
    }

    public void agregarRepresentante(Usuario representante){
        this.representantes.add(representante);
    }
    public void crearProyecto(Proyecto proyecto){
        this.proyecto = proyecto;
    }

    public Collection<Usuario> getRepresentantes() {
        return representantes;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getActividadPrincipal() {
        return actividadPrincipal;
    }

    public boolean isEsRadicada() {
        return esRadicada;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public List<Lote> getLote() {
        return lote;
    }
}
