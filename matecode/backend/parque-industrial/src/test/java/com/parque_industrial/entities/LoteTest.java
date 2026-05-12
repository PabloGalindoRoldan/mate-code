package com.parque_industrial.entities;

import com.parque_industrial.persistence.dtos.LoteDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LoteTest {
    @Test
    public void testCrearLote() throws Exception {
        Lote lote = new Lote(101, 500.0);
        assertEquals(lote.getIdentificacion(), 101);
        assertEquals(lote.getSuperficie(), 500.0);
        assertEquals(lote.getEstado(), Lote.DISPONIBLE);
        assertEquals(lote.getMontoVenta(), 0.0);
    }
    @Test
    @DisplayName("debe cambiar el estado a 'disponible'")
    void testVenderLoteDisponible() throws Exception {
        Lote lote = new Lote(101, 500.0);
        lote.reservar();
        lote.vender(150000.0);
        assertEquals(lote.getEstado(), Lote.VENDIDO);
        assertEquals(lote.getMontoVenta(), 150000.0);
    }

    @Test
    void testReservarLoteDisponible() throws Exception {
        Lote lote = new Lote(102, 300.0);
        lote.reservar();
        assertEquals(lote.getEstado(),Lote.RESERVADO);
    }
    @Test
    public void testReservarLoteReservadoLanzaExcepcion() throws Exception {
        Lote lote = new Lote(1, 100.0, Lote.RESERVADO, null, 0.0);
        Exception e = assertThrows(Exception.class,() ->  lote.reservar());
        assertEquals("El lote 1 no está disponible para reservar.", e.getMessage());
    }
    @Test
    void testReservarLoteVendidoLanzaExcepcion() throws Exception {
        Lote lote = new Lote(104, 250.0);
        lote.reservar();
        lote.vender(100000.0);
        Exception excepcion  =  assertThrows(Exception.class, () -> lote.reservar());
        assertEquals("El lote 104 no está disponible para reservar.", excepcion.getMessage());
    }

    @Test
    void testVenderLoteReservado() throws Exception {
        Lote lote = new Lote(103, 400.0);
        lote.reservar();
        lote.vender(200000.0);
        assertEquals(lote.getEstado(), Lote.VENDIDO);
    }
    @Test
    void testVenderLoteVendidoLanzaExcepcion() throws Exception {
        Lote lote = new Lote(104, 250.0);
        lote.reservar();
        lote.vender(100000.0);
        Exception excepcion  =  assertThrows(Exception.class, () -> lote.vender(55000.0));
        assertEquals("El lote 104 no puede ser vendido en su estado actual.", excepcion.getMessage());
    }

    @DisplayName("debe cambiar el estado a 'disponible'")
    @Test
    void testCancelarReservaLoteReservado() throws Exception {
        Lote lote = new Lote(102, 300.0);
        lote.reservar();
        lote.cancelarReserva();
        assertEquals(lote.getEstado(), Lote.DISPONIBLE);
    }

    @Test
    void testCancelarReservaLoteVendidoLanzaExcepcion() throws Exception {
        Lote lote = new Lote(104, 250.0);
        lote.reservar();
        lote.vender(100000.0);
        Exception excepcion  =  assertThrows(Exception.class, () -> lote.cancelarReserva());
        assertEquals("No se puede cancelar la reserva del lote", excepcion.getMessage());
    }

    @Test
    void testCancelarReservaLoteDisponibleLanzaExcepcion() throws Exception {
        Lote lote = new Lote(104, 250.0);
        Exception excepcion  =  assertThrows(Exception.class, () -> lote.cancelarReserva());
        assertEquals("No se puede cancelar la reserva del lote", excepcion.getMessage());
    }


    @Test
    public void testSuperficeNegativaLanzaExcepcion(){
        Exception exception = assertThrows(Exception.class, ()->{ new Lote(12, -500.0);});
        assertEquals("La superficie debe ser un valor positivo", exception.getMessage());
    }

    @Test
    public void testIdentificacionNegativaLanzaExcepcion(){
        Exception e = assertThrows(Exception.class, ()->new Lote(-12, 500.0));
        assertEquals("La identificación del lote es un numero postivo", e.getMessage());
    }

    @Test
    void testMontoVentaNegativoLanzaExcepcion() throws Exception {
        Lote lote = new Lote(105, 100.0);
        lote.reservar();
        Exception excepcion  =  assertThrows(Exception.class, () -> lote.vender(-50.0));
        assertEquals("El monto de venta debe ser positivo.", excepcion.getMessage());
    }

}
