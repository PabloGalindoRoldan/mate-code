package com.parque_industrial.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LoteTest {
    @Test
    @DisplayName("debe cambiar el estado a 'disponible'")
    void testVenderLoteDisponible() throws Exception {
        Lote lote = new Lote(101, 500.0);
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
    void testVenderLoteReservado() throws Exception {
        Lote lote = new Lote(103, 400.0);
        lote.reservar();
        lote.vender(200000.0);
        assertEquals(lote.getEstado(), Lote.VENDIDO);
    }

    @Test
    void testReservarLoteVendido() throws Exception {
        Lote lote = new Lote(104, 250.0);
        lote.vender(100000.0);
        Exception excepcion  =  assertThrows(Exception.class, () -> lote.reservar());
        assertEquals("El lote 104 no está disponible para reservar.", excepcion.getMessage());
    }
    @Test
    public void testAsignaDisponibleAlCrearLote() throws Exception {
        Lote lote = new Lote(12, 500.0);
        assertEquals(lote.getEstado(), Lote.DISPONIBLE);
    }
    @Test
    public void testSuperficeNegativa(){
        Exception exception = assertThrows(Exception.class, ()->{ new Lote(12, -500.0);});
        assertEquals("La superficie debe ser un valor positivo", exception.getMessage());
    }
    @Test
    public void testIdentificacionNegativa(){
        Exception e = assertThrows(Exception.class, ()->{new Lote(-12, 500.0);});
        assertEquals("La identificación del lote es un numero postivo", e.getMessage());
    }


    @Test
    void testMontoVentaNegativoLanzaExcepcion() throws Exception {
        Lote lote = new Lote(105, 100.0);
        Exception excepcion  =  assertThrows(Exception.class, () -> lote.vender(-50.0));
        assertEquals("El monto de venta debe ser positivo.", excepcion.getMessage());
    }

}
