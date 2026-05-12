package com.parque_industrial.persistence;

import com.parque_industrial.entities.Lote;
import com.parque_industrial.persistence.jdbc.LoteJDBC;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
// Variable de Entorno de TestLote  : DB_URL=jdbc:mysql://root:VCRqgvrYHmBogQSHuCFfHoejJaHuiBzp@yamabiko.proxy.rlwy.net:36494/railway;DB_USERNAME=root;DB_PASSWORD=VCRqgvrYHmBogQSHuCFfHoejJaHuiBzp

@SpringBootTest
public class LoteJDBCTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private LoteJDBC jdbc;
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
        Lote lote = new Lote(6, 200.0);
        jdbc.crearLote(lote);
        assertEquals(lote.getIdentificacion() , jdbc.buscarLotePorID(6).identificacion());
    }
}
