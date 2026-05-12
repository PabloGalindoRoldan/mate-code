package com.parque_industrial.entities;

public class ProyectoPreliminar extends Proyecto {
    public ProyectoPreliminar(String identificacion, String actividadPrincipal, String referente,
                              int superficieRequerida, double energiaRequerida, int personalAOcupar) throws Exception {
        super(identificacion, actividadPrincipal, referente, superficieRequerida, energiaRequerida, personalAOcupar);
    }

    @Override
    public void listoParaRevision() throws Exception {
        this.validar();
        this.estado = PENDIENTE;
    }

}
