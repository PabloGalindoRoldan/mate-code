package com.parque_industrial.services;
import com.parque_industrial.entities.Lote;
import com.parque_industrial.persistence.dtos.LoteDTO;

import java.util.List;

public interface DAOInmobiliario {
    public void crearLote(Lote lote) throws Exception;
    public void venderLote(Lote lote) throws Exception;
    public void reservarLote(Lote lote) throws Exception;
    public LoteDTO buscarLotePorID(int identificacion) throws Exception;
    public void cancelarReserva(Lote lote) throws Exception;
    public List<LoteDTO> LotesDisponibles() throws Exception;
    public List<LoteDTO> LotesVendidos() throws Exception;
    public List<LoteDTO> LotesReservados() throws Exception;
}
// la interfaz es para invertir la dependencia, despues en jdbc tendriamos que implementarla


