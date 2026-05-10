package com.parque_industrial.services;
import com.parque_industrial.entities.Empresa;
import com.parque_industrial.entities.Lote;
import com.parque_industrial.persistence.dtos.LoteDTO;
import org.springframework.stereotype.Service; // Importar la anotación @Service
import java.util.List;
@Service // Indicar que esta clase es un servicio Spring
public class GestorInmobiliario{
    private DAOInmobiliario dao;
    // la interfaz es para invertir la dependencia, despues en jdbc tendriamos que implementarla
    public void VenderLote(LoteDTO lote, double montoVenta) throws Exception {
        Lote lote1 = lote.entidad();
        lote1.vender(montoVenta);
        dao.venderLote(lote1);
    }
    public void ReservarLote(LoteDTO lote) throws Exception {
        Lote lote1 = lote.entidad();
        dao.reservarLote(lote1);
    }
    public void crearLote(LoteDTO lote) throws Exception {
        Lote lote1 = lote.entidad();
        dao.crearLote(lote1);
    }
    public List<LoteDTO> LotesDisponibles() throws Exception {
       return dao.LotesDisponibles();
    }
    public LoteDTO buscarLote(int identificacion) throws Exception {
        return dao.buscarLotePorID(identificacion) ;
    }


    public List<LoteDTO> LotesVendidos() throws Exception{
        return dao.LotesVendidos();
    }
    public List<LoteDTO> LotesReservados() throws Exception{
        return dao.LotesReservados();
    }
}
