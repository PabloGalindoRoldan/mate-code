package com.parque_industrial.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LoteTest {
    @Test
    public void testCrearLote()   {
        Lote lote = new Lote(101, 500.0,"N/A", Lote.PARQUE_NUEVO );
        assertEquals(lote.getIdentificacion(), 101);
        assertEquals(lote.getSuperficie(), 500.0);
        assertEquals(lote.getEstado(), Lote.DISPONIBLE);
        assertEquals(lote.getMontoVenta(), 0.0);
        assertEquals(lote.getNc(), "N/A");
        assertEquals(lote.getParque(),"nuevo");
        assertEquals(lote.getTipo(), "lote");
    }
    @Test
    @DisplayName("debe cambiar el estado a 'disponible'")
    void testVenderLoteDisponible()  {
        Lote lote = new Lote(101, 500.0, "N/A", "nuevo");
        lote.reservar();
        lote.vender(150000.0);
        assertEquals(lote.getEstado(), Lote.VENDIDO);
        assertEquals(lote.getMontoVenta(), 150000.0);
    }

    @Test
    void testReservarLoteDisponible()  {
        Lote lote = new Lote(102, 300.0, "N/A", "nuevo");
        lote.reservar();
        assertEquals(lote.getEstado(),Lote.RESERVADO);
    }
    @Test
    public void testReservarLoteReservadoLanzaExcepcion()  {
        Lote lote = new Lote(1, 100.0, Lote.RESERVADO, null, 0.0,"N/A", "nuevo");
        Exception e = assertThrows(Exception.class,() ->  lote.reservar());
        assertEquals("El lote 1 no está disponible para reservar.", e.getMessage());
    }
    @Test
    void testReservarLoteVendidoLanzaExcepcion(){
        Lote lote = new Lote(104, 250.0,"N/A", "nuevo");
        lote.reservar();
        lote.vender(100000.0);
        Exception excepcion  =  assertThrows(Exception.class, () -> lote.reservar());
        assertEquals("El lote 104 no está disponible para reservar.", excepcion.getMessage());
    }

    @Test
    void testVenderLoteReservado()  {
        Lote lote = new Lote(103, 400.0,"N/A", "nuevo");
        lote.reservar();
        lote.vender(200000.0);
        assertEquals(lote.getEstado(), Lote.VENDIDO);
    }
    @Test
    void testVenderLoteVendidoLanzaExcepcion()  {
        Lote lote = new Lote(104, 250.0,"N/A", "nuevo");
        lote.reservar();
        lote.vender(100000.0);
        Exception excepcion  =  assertThrows(Exception.class, () -> lote.vender(55000.0));
        assertEquals("El lote 104 no puede ser vendido en su estado actual.", excepcion.getMessage());
    }

    @DisplayName("debe cambiar el estado a 'disponible'")
    @Test
    void testCancelarReservaLoteReservado()  {
        Lote lote = new Lote(102, 300.0,"N/A", "nuevo");
        lote.reservar();
        lote.cancelarReserva();
        assertEquals(lote.getEstado(), Lote.DISPONIBLE);
    }

    @Test
    void testCancelarReservaLoteVendidoLanzaExcepcion()  {
        Lote lote = new Lote(104, 250.0, "N/A", "nuevo");
        lote.reservar();
        lote.vender(100000.0);
        Exception excepcion  =  assertThrows(Exception.class, () -> lote.cancelarReserva());
        assertEquals("No se puede cancelar la reserva del lote", excepcion.getMessage());
    }

    @Test
    void testCancelarReservaLoteDisponibleLanzaExcepcion()  {
        Lote lote = new Lote(104, 250.0, "N/A", "nuevo");
        Exception excepcion  =  assertThrows(Exception.class, () -> lote.cancelarReserva());
        assertEquals("No se puede cancelar la reserva del lote", excepcion.getMessage());
    }


    @Test
    public void testSuperficeNegativaLanzaExcepcion(){
        Exception exception = assertThrows(Exception.class, ()->{ new Lote(12, -500.0,"N/A", "nuevo");});
        assertEquals("La superficie debe ser un valor positivo", exception.getMessage());
    }

    @Test
    public void testIdentificacionNegativaLanzaExcepcion(){
        Exception e = assertThrows(Exception.class, ()->new Lote(-12, 500.0,"N/A", "nuevo"));
        assertEquals("La identificación del lote es un numero postivo", e.getMessage());
    }

    @Test
    void testMontoVentaNegativoLanzaExcepcion()   {
        Lote lote = new Lote(105, 100.0, "N/A", "nuevo");
        lote.reservar();
        Exception excepcion  =  assertThrows(Exception.class, () -> lote.vender(-50.0));
        assertEquals("El monto de venta debe ser positivo.", excepcion.getMessage());
    }

}
