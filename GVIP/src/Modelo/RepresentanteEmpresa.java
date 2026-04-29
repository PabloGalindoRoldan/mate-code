package Modelo;

public class RepresentanteEmpresa extends Usuario {
    private String cuit;
    public RepresentanteEmpresa(String nombre, String apellido, String email, String nombreUsuario, String contraseña, String cuit) {
        super(nombre, apellido, email, nombreUsuario, contraseña);
        validarCuit(cuit);
        this.cuit = cuit;
    }
    private void validarCuit(String cuit) {
        if (cuit == null || cuit.isBlank()) {
            throw new IllegalArgumentException("El CUIT no puede estar vacío.");
        }
        //  formato  debe ser XX-XXXXXXXX-X
        // Esto valida que sean 2 dígitos, un guion, 8 dígitos, un guion y 1 dígito.
        String regexCuit = "^\\d{2}-\\d{8}-\\d{1}$";
        if (!cuit.matches(regexCuit)) {
            throw new IllegalArgumentException("El formato del CUIT es inválido. Debe ser XX-XXXXXXXX-X.");
        }
    }
  /*  un representante deberia ser quien cree a otros representantes para que acceda a la empresa
     un representante deberia tener una empresa
     no que una empresa tenga varios representantes, porque seria como lo representariamos en nuestra BD
     en el representante vamos a tener asociado la fk de la empresa */
}
