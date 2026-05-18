package com.parque_industrial.persistence.presupuesto;

import com.parque_industrial.entities.PartidaPresupuestaria;

public interface DAOPresupuesto {
    public void crearPartidaPresupuestaria(PartidaPresupuestaria partidaPresupuestaria);
    public double montoGastado(); // puede servir para un reporte?
    }


// la interfaz es para invertir la dependencia, despues en jdbc tendriamos que implementarla

