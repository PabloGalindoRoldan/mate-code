package com.parque_industrial.services;

import com.parque_industrial.entities.Lote;
import com.parque_industrial.persistence.dtos.LoteDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GestorInmobiliarioTest {
    private FakeDaoInmobiliario fake = new FakeDaoInmobiliario();
    private GestorInmobiliario gestor = new GestorInmobiliario(fake);
    // lotes id 1,2 y 3vendidos -- 4,5 y 6 reservados-- 7,8 y 9 disponibles

    @Test
    public void testCrearLote() throws Exception {
        LoteDTO lote = new LoteDTO(10, 500.00);
        this.gestor.crearLote(lote);
        assertEquals(fake.getLote().getIdentificacion(), 10);
        assertEquals(fake.getLote().getSuperficie(), 500.00);
    }
    @Test
    public void testReservarLote() throws Exception {
        LoteDTO lote= fake.buscarLotePorID(7); // lote id 7 disponible
        this.gestor.ReservarLote(lote);
        assertEquals(fake.getLote().getEstado(), Lote.RESERVADO);
    }
    @Test
    public void testCancelarReservaLote()throws Exception{
        LoteDTO lote= fake.buscarLotePorID(4); // lote id 4 reservado
        this.gestor.cancelarReserva(lote);
        assertEquals(fake.getLote().getEstado(), Lote.DISPONIBLE);
    }
    @Test
    public void testVenderLote()throws Exception{
        LoteDTO lote= fake.buscarLotePorID(4); // lote id 4 reservado
        this.gestor.VenderLote(lote, 500.0);
        assertEquals(fake.getLote().getEstado(), Lote.VENDIDO);
    }



}
