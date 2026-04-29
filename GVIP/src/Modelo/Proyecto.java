package Modelo;

public abstract class Proyecto {
   private final String APROBADO = "aproado";
   private final String PENDIENTE = "pendiente";
   private final String RECHAZADO = "rechazado";
    private final String RECTITIFICADO = "rectificado";
    protected String identificacion;// seria la misma que de la empresa, el cuit
    protected String actividadPrincipal;
    protected String referente;
    protected int superficieRequerida;
    protected double energiaRequerida;
    protected int personalAOcupar;
    protected String estado;
    public Proyecto(String identificacion, String actividadPrincipal, String referente, int superficieRequerida, double energiaRequerida, int personalAOcupar) {
        if (identificacion == null || identificacion.isBlank()) {
            throw new IllegalArgumentException("La identificación del proyecto no puede estar vacía");
        }
        if (superficieRequerida <= 0) {
            throw new IllegalArgumentException("La superficie requerida debe ser mayor que cero");
        }
        if (energiaRequerida <= 0) {
            throw new IllegalArgumentException("La energía requerida debe ser mayor que cero");
        }
        if (personalAOcupar <= 0) {
            throw new IllegalArgumentException("El personal a ocupar debe ser mayor que cero");
        }

        this.identificacion = identificacion;
        this.actividadPrincipal = actividadPrincipal;
        this.referente = referente;
        this.superficieRequerida = superficieRequerida;
        this.energiaRequerida = energiaRequerida;
        this.personalAOcupar = personalAOcupar;
        this.estado = PENDIENTE; // por defecto una vez q se crea queda en pendiente
    }
    // quien deba aprobar rechazar un proyecto debe ser un admin
}
