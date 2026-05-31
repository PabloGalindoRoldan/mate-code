package com.parque_industrial.services;

import com.parque_industrial.entities.Lote;
import com.parque_industrial.persistence.lote.LoteDAO;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class GestorInmobiliario {

    private final LoteDAO loteDAO;

    public GestorInmobiliario(LoteDAO loteDAO) {
        this.loteDAO = loteDAO;
    }

    public void reservarLote(int id) {
        Lote lote = buscar(id);
        lote.reservar();
        loteDAO.actualizar(lote);
    }

    public void cancelarReserva(int id) {

        Lote lote = buscar(id);

        lote.cancelarReserva();

        loteDAO.actualizar(lote);
    }

    public void venderLote(int id, BigDecimal monto, LocalDate fecha) {

        Lote lote = buscar(id);

        lote.vender(monto, fecha);

        loteDAO.actualizar(lote);
    }

    public List<Lote> listarLotes() {
        return loteDAO.buscarTodos();
    }

    public Lote buscar(int id) {

        Lote lote = loteDAO.buscarPorID(id);

        if (lote == null) {
            throw new IllegalArgumentException("No existe el lote");
        }

        return lote;
    }
}