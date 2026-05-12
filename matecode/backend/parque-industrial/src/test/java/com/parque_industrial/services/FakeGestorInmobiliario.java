package com.parque_industrial.services;
import com.parque_industrial.entities.Lote;
import com.parque_industrial.persistence.dtos.LoteDTO;

import java.util.*;

public class FakeGestorInmobiliario extends GestorInmobiliario {
// Esta clase es para el test de LoteController
    private final Map<Integer, LoteDTO> lotes = new HashMap<>();

    public FakeGestorInmobiliario() {
        super(null);
    }
    @Override
    public LoteDTO buscarLote(int id) throws Exception {
        if (!lotes.containsKey(id)) throw new Exception("Lote no encontrado");
        return lotes.get(id);
    }
    @Override
    public void VenderLote(LoteDTO dto, double monto) throws Exception {
        if (!dto.estado().equals(Lote.RESERVADO)) {
            throw new Exception("El lote no está disponible para vender.");
        }
        lotes.put(dto.identificacion(), dto);
    }
    @Override
    public void crearLote(LoteDTO lote) throws Exception {
        if (lotes.containsKey(lote.identificacion())) throw new Exception("Lote duplicado");
        lotes.put(lote.identificacion(), lote);
    }
    @Override
    public void ReservarLote(LoteDTO lote) throws Exception {
        if (!lote.estado().equals(Lote.DISPONIBLE)) {
            throw new Exception("El lote no está disponible para reservar.");
        }
        lotes.put(lote.identificacion(), lote);
    }
    @Override
    public void cancelarReserva(LoteDTO lote) throws Exception {
        if (!lote.estado().equals(Lote.RESERVADO)) {
            throw new Exception("El lote no está reservado, no se puede cancelar la reserva.");
        }
        lotes.put(lote.identificacion(), lote);
    }
    @Override
    public List<LoteDTO> LotesVendidos() {
        return new ArrayList<>(lotes.values().stream().filter(l -> l.estado().equals(Lote.VENDIDO)).toList());
    }
    @Override
    public List<LoteDTO> LotesReservados() {
        return new ArrayList<>(lotes.values().stream().filter(l -> l.estado().equals(Lote.RESERVADO)).toList());
    }
    @Override
    public List<LoteDTO> LotesDisponibles() {
        return new ArrayList<>(lotes.values().stream().filter(l -> l.estado().equals(Lote.DISPONIBLE)).toList());
    }
    public void cargarLoteDePrueba(LoteDTO lote) {
        lotes.put(lote.identificacion(), lote);
    }
}