package com.parque_industrial.services;
import com.parque_industrial.entities.Lote;
import com.parque_industrial.dto.lote.LoteDTO;
import com.parque_industrial.persistence.lote.LoteDAO;
import org.springframework.stereotype.Service; // Importar la anotación @Service
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service // Indicar que esta clase es un servicio Spring
@Transactional
public class GestorInmobiliario{
    private LoteDAO dao;
    public GestorInmobiliario(LoteDAO dao) {
        this.dao = dao;
    }
    // la interfaz es para invertir la dependencia, despues en jdbc tendriamos que implementarla
    public void crearLote(LoteDTO lote) {
        Lote lote1 = new Lote(lote.identificacion(), lote.superficie(), lote.nc(), lote.parque());
        dao.crearLote(lote1);
    }
    public void ReservarLote(LoteDTO lote)  {
        Lote lote1 = lote.entidad();
        lote1.reservar();
        dao.reservarLote(lote1);
    }
    public void cancelarReserva(LoteDTO lote)  {
        Lote lote1 = lote.entidad();
        lote1.cancelarReserva();
        dao.cancelarReserva(lote1);
    }
    public void VenderLote(LoteDTO lote, double montoVenta)   {
        Lote lote1 = lote.entidad();
        lote1.vender(montoVenta);
        dao.venderLote(lote1);
    }
    public LoteDTO buscarLote(int identificacion)  {
        return dao.buscarLotePorID(identificacion) ;
    }

    public List<LoteDTO> LotesDisponibles()  {
       return dao.LotesDisponibles();
    }

    public List<LoteDTO> LotesVendidos() {
        return dao.LotesVendidos();
    }
    public List<LoteDTO> LotesReservados()  {
        return dao.LotesReservados();
    }
    public List<LoteDTO> LotesDeParqueNuevo() {
        return dao.LotesNuevos();
    }
    public List<LoteDTO> LotesDeParqueViejo() {
        return dao.LotesViejos();
    }

}
