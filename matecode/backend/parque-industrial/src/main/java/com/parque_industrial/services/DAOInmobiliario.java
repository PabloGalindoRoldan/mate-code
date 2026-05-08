package com.parque_industrial.services;
import com.parque_industrial.entities.Empresa;
import com.parque_industrial.entities.Lote;

import java.sql.SQLException;
import java.util.List;

public interface DAOInmobiliario {
    public List<Lote> LotesDisponibles() throws Exception;
    public void crearLote(Lote lote) throws Exception;
    public void venderLote(Lote lote) throws Exception;
    public void reservarLote(Lote lote) throws Exception;
}
// la interfaz es para invertir la dependencia, despues en jdbc tendriamos que implementarla


