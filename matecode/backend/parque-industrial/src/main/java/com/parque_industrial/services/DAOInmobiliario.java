package com.parque_industrial.services;
import com.parque_industrial.entities.Lote;
import com.parque_industrial.persistence.dtos.LoteDTO;

import java.util.List;

public interface DAOInmobiliario {
    public List<LoteDTO> LotesDisponibles() throws Exception;
    public void crearLote(Lote lote) throws Exception;
    public void venderLote(Lote lote) throws Exception;
    public void reservarLote(Lote lote) throws Exception;
    public LoteDTO buscarLotePorID(int identificacion) throws Exception;
    List<LoteDTO> LotesVendidos() throws Exception;

    List<LoteDTO> LotesReservados() throws Exception;
}
// la interfaz es para invertir la dependencia, despues en jdbc tendriamos que implementarla


