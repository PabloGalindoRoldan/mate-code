package com.parque_industrial.entities;

import java.time.LocalDate;

public class ProyectoDefinitivo extends Proyecto {
    private LocalDate fechaInicioObra;
    private LocalDate fechaFinObra;
    private boolean viabilidadFinanciera;
    private String informeAmbiental; // podria ser un archivo no?

    public ProyectoDefinitivo(String identificacion, String actividadPrincipal, String referente,
                               int superficieRequerida, double energiaRequerida, int personalAOcupar,
                               LocalDate fechaInicioObra, LocalDate fechaFinObra,
                               boolean viabilidadFinanciera, String informeAmbiental) throws Exception {
        super(identificacion, actividadPrincipal, referente, superficieRequerida, energiaRequerida, personalAOcupar);
        validar();
        validarFechas(fechaInicioObra, fechaFinObra);

        if(informeAmbiental == null || informeAmbiental.isBlank()){
            throw new Exception("El informe ambiental no puede estar vacío");
        }

        this.fechaInicioObra = fechaInicioObra;
        this.fechaFinObra = fechaFinObra;
        this.viabilidadFinanciera = viabilidadFinanciera;
        this.informeAmbiental = informeAmbiental;
    }

    private void validarFechas(LocalDate inicio, LocalDate fin) throws Exception {
        if (inicio == null) {
            throw new Exception("La fecha de inicio de obra no puede ser nula.");
        }
        if (fin == null) {
            throw new Exception("La fecha de fin de obra no puede ser nula.");
        }
        if (inicio.isBefore(LocalDate.now())) {
            throw new Exception("La fecha de inicio de obra no puede ser anterior a la actualidad.");
        }
        if (fin.isBefore(inicio)) {
            throw new Exception("La fecha de fin de obra no puede ser anterior a la de inicio.");
        }
    }

    @Override
    public void listoParaRevision() throws Exception {
        validar();
        validarFechas(fechaInicioObra, fechaFinObra);
        this.estado = PENDIENTE;
    }

    public LocalDate getFechaInicioObra() {
        return fechaInicioObra;
    }

    public LocalDate getFechaFinObra() {
        return fechaFinObra;
    }

    public String getInformeAmbiental() {
        return informeAmbiental;
    }

    public boolean isViabilidadFinanciera(){
        return viabilidadFinanciera;
    }
}
