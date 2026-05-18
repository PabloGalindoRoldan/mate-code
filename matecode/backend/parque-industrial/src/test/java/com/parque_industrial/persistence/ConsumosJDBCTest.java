package com.parque_industrial.persistence;

import com.parque_industrial.persistence.dtos.ConsumosDTO;
import com.parque_industrial.persistence.jdbc.ConsumosJDBC;
import com.parque_industrial.persistence.jdbc.LoteJDBC;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ConsumosJDBCTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private ConsumosJDBC jdbc;
    // DB_URL=jdbc:mysql://root:FtAcYUVAsZndlHEqSzxdhDUfFrTNPbok@mysql.railway.internal:3306/railway;DB_USERNAME=root;DB_PASSWORD=FtAcYUVAsZndlHEqSzxdhDUfFrTNPbok
    //
    //DB_URL=jdbc:mysql://yamabiko.proxy.rlwy.net:36494/railway;DB_USERNAME=root;DB_PASSWORD=VCRqgvrYHmBogQSHuCFfHoejJaHuiBzp
    @Test
  //  @Transactional
    public void insertarConsumo(){
        ConsumosDTO consumosDTO = new ConsumosDTO(500.0F, 500.0F , 500.0F , 500 , 15, 15, 204555578962L);
        jdbc.cargarConsumosDeEmpresa(consumosDTO);
        ConsumosDTO c = jdbc.generarReporteConsumoEmpresa(consumosDTO.idEmpresa());
        int ano = LocalDate.now().getYear();
        int mes = LocalDate.now().getMonthValue();
        assertEquals(ano , c.año());
        assertEquals(mes, c.mes());
        assertEquals(consumosDTO.luz(), c.luz());
        assertEquals(consumosDTO.gas(), c.gas());
        assertEquals(consumosDTO.agua(), c.agua());
        assertEquals(consumosDTO.cantEmpleados(), c.cantEmpleados());
        assertEquals(consumosDTO.cantVehiculos(), c.cantVehiculos());
    }
    @Test
    @Transactional
    public void testAsignarCantEmpleados(){
        jdbc.asignarCantEmpleados(204555578962L, 20);
        ConsumosDTO c = jdbc.generarReporteConsumoEmpresa(204555578962L);
        assertEquals(20, c.cantEmpleados());
    }
    @Test
    @Transactional
    public void testAsignarCantVehiculos(){
        jdbc.asignarCantVheiculos(204555578962L, 20);
        ConsumosDTO c = jdbc.generarReporteConsumoEmpresa(204555578962L);
        assertEquals(20, c.cantVehiculos());
    }

    @Test
    @Transactional
    public void testAsignarGas(){
        jdbc.asignarConsumoGas(204555578962L, 600.0F);
        ConsumosDTO c = jdbc.generarReporteConsumoEmpresa(204555578962L);
        assertEquals(600.0F, c.gas());
    }

    @Test
    @Transactional
    public void testAsignarLuz(){
        jdbc.asignarConsumoLuz(204555578962L, 600.0F);
        ConsumosDTO c = jdbc.generarReporteConsumoEmpresa(204555578962L);
        assertEquals(600.0F, c.luz());
    }

    @Test
    @Transactional
    public void testAsignarAgua(){
        jdbc.asignarConsumoAgua(204555578962L, 600.0F);
        ConsumosDTO c = jdbc.generarReporteConsumoEmpresa(204555578962L);
        assertEquals(600.0F, c.agua());
    }

    @Test
    @Transactional
    public void testAsignarResiduos(){
        jdbc.asignarConsumoResiduos(204555578962L, 100);
        ConsumosDTO c = jdbc.generarReporteConsumoEmpresa(204555578962L);
        assertEquals(100, c.residuos());
    }
    @Test
    @Transactional
    public void testGenerarReporteParque(){
        List<ConsumosDTO> consumos = jdbc.generarReporteConsumoTotalParque();
        assertEquals(3, consumos.size());
    }
    @Test
    @Transactional
    public void testGenerarReporteConsumoTotalEmpresa(){
        List<ConsumosDTO> consumos = jdbc.generarReporteConsumoTotalEmpresa(204555578962L);
        assertEquals(2, consumos.size());
    }
    @Test
    @Transactional
    public void testGenerarReporteConsumoActualEmpresa(){
        ConsumosDTO c = jdbc.generarReporteConsumoEmpresa(204555578962L);
        int ano = LocalDate.now().getYear();
        int mes = LocalDate.now().getMonthValue();
        assertEquals(ano , c.año());
        assertEquals(mes, c.mes());
        assertEquals(50 , c.luz());
        assertEquals(50, c.gas());
        assertEquals(50, c.agua());
        assertEquals(50, c.cantEmpleados());
        assertEquals(50, c.cantVehiculos());

    }

}
