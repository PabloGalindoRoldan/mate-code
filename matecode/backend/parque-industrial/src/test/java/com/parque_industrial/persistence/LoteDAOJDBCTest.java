package com.parque_industrial.persistence;

import com.parque_industrial.entities.Lote;
import com.parque_industrial.dto.lote.LoteDTO;
import com.parque_industrial.persistence.lote.LoteDAOJDBC;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
// Variable de Entorno de TestLote  : DB_URL=jdbc:mysql://root:VCRqgvrYHmBogQSHuCFfHoejJaHuiBzp@yamabiko.proxy.rlwy.net:36494/railway;DB_USERNAME=root;DB_PASSWORD=VCRqgvrYHmBogQSHuCFfHoejJaHuiBzp

@SpringBootTest
public class LoteDAOJDBCTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private LoteDAOJDBC jdbc;
  // datos que se encuentran en la bd, no cambian ya que luego de cada test se hace un rollback
/*  id	    superficie	    estado	        fechaVenta	    montoVenta
    1	    500	            disponible		                    0
    2	    500	            disponible		                    0
    3	    100	            reservado		                    0
    4	    200	            reservado		                    0
    5	    200	            vendido	        2025-03-09	       10000
    6	    300	            vendido	        2026-03-03	       50000             */
    @Test
    @Transactional // Esto asegura que la prueba se ejecute en una transacción y se haga rollback al final
    public void testCrearLote() throws Exception {
        Lote lote = new Lote(10, 200.0,"N/A", "nuevo");
        jdbc.crearLote(lote);
        assertEquals(lote.getIdentificacion() , jdbc.buscarLotePorID(10).identificacion());
    }
    @Test
    @Transactional
    public void testBuscarLotePorID() throws Exception {
        LoteDTO lote = jdbc.buscarLotePorID(6);
        assertEquals(6 ,lote.identificacion());
        assertEquals(300,lote.superficie());
        assertEquals(Lote.VENDIDO, lote.estado());
        assertEquals("2026-03-03", lote.fechaVenta().toString());

    }

    @Test
    @Transactional
    public void testReservarLote() throws Exception {
        LoteDTO l = jdbc.buscarLotePorID(1);
        jdbc.reservarLote(l.entidad());
        LoteDTO loteReservado= jdbc.buscarLotePorID(1);
        assertEquals(Lote.RESERVADO, loteReservado.estado());
    }
    @Test
    @Transactional
    public void testVenderLote() throws Exception {
        LoteDTO l = jdbc.buscarLotePorID(4);
        Lote lote = l.entidad();
        lote.vender(500.0);
        jdbc.venderLote(lote);
        LoteDTO loteVendido= jdbc.buscarLotePorID(4);
        assertEquals(Lote.VENDIDO, loteVendido.estado());
    }
    @Test
    @Transactional
    public void testCancelarReserva() throws Exception {
        LoteDTO l = jdbc.buscarLotePorID(3);
        jdbc.cancelarReserva(l.entidad());
        LoteDTO loteReservado = jdbc.buscarLotePorID(3);
        assertEquals(Lote.DISPONIBLE, loteReservado.estado());
    }
    @Test
    @Transactional
    public void testLotesDisponibles() throws Exception {
        assertEquals(2, jdbc.LotesDisponibles().size());
    }
    @Test
    @Transactional
    public void testLotesVendidos() throws Exception {
        assertEquals(2, jdbc.LotesVendidos().size());
    }
    @Test
    @Transactional
    public void testLotesReservados() throws Exception {
        assertEquals(2, jdbc.LotesReservados().size());
    }

}
