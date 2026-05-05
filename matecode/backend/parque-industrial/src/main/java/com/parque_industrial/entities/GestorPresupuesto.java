package Modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface GestorPresupuesto {
    public void crearPartidaPresupuestaria(PartidaPresupuestaria partidaPresupuestaria);
    public double montoGastado(); // puede servir para un reporte?
    }



