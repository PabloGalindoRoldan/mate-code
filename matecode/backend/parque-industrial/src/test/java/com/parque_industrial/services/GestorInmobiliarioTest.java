package com.parque_industrial.services;

import com.parque_industrial.entities.Lote;
import com.parque_industrial.dto.lote.LoteDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GestorInmobiliarioTest {
    private FakeLoteDAO fake = new FakeLoteDAO();
    private GestorInmobiliario gestor = new GestorInmobiliario(fake);
    // lotes id 1,2 y 3vendidos -- 4,5 y 6 reservados-- 7,8 y 9 disponibles

    @Test
    public void testCrearLote()  {
        LoteDTO lote = new LoteDTO(10, 500.00, "N/A", "nuevo");
        this.gestor.crearLote(lote);
        assertEquals(fake.getLote().getIdentificacion(), 10);
        assertEquals(fake.getLote().getSuperficie(), 500.00);
    }
    @Test
    public void testReservarLote()  {
        LoteDTO lote= fake.buscarLotePorID(7); // lote id 7 disponible
        this.gestor.ReservarLote(7);
        assertEquals(fake.getLote().getEstado(), Lote.RESERVADO);
    }
    @Test
    public void testCancelarReservaLote(){
        this.gestor.cancelarReserva(4);
        LoteDTO lote= fake.buscarLotePorID(4); // lote id 4 reservado
        assertEquals(fake.getLote().getEstado(), Lote.DISPONIBLE);
    }
    @Test
    public void testVenderLote(){
        this.gestor.VenderLote(4, 500.0);
        LoteDTO lote= fake.buscarLotePorID(4); // lote id 4 reservado
        assertEquals(fake.getLote().getEstado(), Lote.VENDIDO);
    }
    @Test
    public void testListarDisponibles()  {
        assertEquals(3, this.gestor.LotesDisponibles().size());
    }
    @Test
    public void testCambiarEstado(){
        this.gestor.CambiarEstadoLote(4,Lote.DISPONIBLE );
        LoteDTO lote= fake.buscarLotePorID(4); // lote id 4 reservado
        assertEquals(fake.getLote().getEstado(), Lote.DISPONIBLE);

        this.gestor.CambiarEstadoLote(4,Lote.VENDIDO );
        LoteDTO lote2= fake.buscarLotePorID(4); // lote id 4 reservado
        assertEquals(fake.getLote().getEstado(), Lote.VENDIDO);

        this.gestor.CambiarEstadoLote(4,Lote.RESERVADO );
        LoteDTO lote3= fake.buscarLotePorID(4); // lote id 4 reservado
        assertEquals(fake.getLote().getEstado(), Lote.RESERVADO);
    }

    @Test
    public void testListarReservados()  {
        assertEquals(3, this.gestor.LotesReservados().size());
    }
    @Test
    public void testListarLotesVendidos()  {
        assertEquals(3, this.gestor.LotesVendidos().size());
    }
    @Test
    public void testListarLotesDeParqueNuevo()  {
        assertEquals(6, this.gestor.LotesDeParqueNuevo().size());
    }
    @Test
    public void testListarLotesDeParqueViejo()  {
        assertEquals(3, this.gestor.LotesDeParqueViejo().size());
    }



}
