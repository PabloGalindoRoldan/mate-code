package com.parque_industrial.persistence.lote;
import com.parque_industrial.entities.Lote;
import com.parque_industrial.dto.lote.LoteDTO;

import java.util.List;

public interface LoteDAO {
    public void crearLote(Lote lote) ;
    public void venderLote(Lote lote);
    public void reservarLote(Lote lote);
    public LoteDTO buscarLotePorID(int identificacion) ;
    public void cancelarReserva(Lote lote) ;
    public List<LoteDTO> LotesDisponibles();
    public List<LoteDTO> LotesVendidos() ;
    public List<LoteDTO> LotesReservados() ;
    public List<LoteDTO> LotesNuevos();
    public List<LoteDTO> LotesViejos();

}
// la interfaz es para invertir la dependencia, despues en jdbc tendriamos que implementarla


