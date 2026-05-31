// package com.parque_industrial.services;
//
// import com.parque_industrial.entities.Lote;
// import com.parque_industrial.persistence.lote.LoteDAO;
//
// import java.time.LocalDate;
// import java.util.ArrayList;
// import java.util.List;
//
// public class FakeLoteDAO implements LoteDAO {
// private Lote lote;
// private List<LoteDTO> listLotes;
//
// public FakeLoteDAO() {
// this.listLotes = new ArrayList<>();
// asignarLotes();
// }
//
// @Override
// public List<LoteDTO> LotesDisponibles() {
// return listLotes.stream().filter(loteDTO->
// loteDTO.estado().equals(Lote.DISPONIBLE)).toList();
// }
//
// @Override
// public void crearLote(Lote lote) {
// this.lote = lote;
// }
//
// @Override
// public void venderLote(Lote lote) {
// this.lote = lote;
// }
//
// @Override
// public void reservarLote(Lote lote) {
// this.lote = lote;
// }
//
// @Override
// public LoteDTO buscarLotePorID(int identificacion){
// return listLotes.stream().filter(loteDTO-> loteDTO.identificacion() ==
// identificacion).findFirst().get();
// }
//
// @Override
// public List<LoteDTO> LotesVendidos() {
// return listLotes.stream().filter(loteDTO->
// loteDTO.estado().equals(Lote.VENDIDO)).toList();
//
// }
//
// @Override
// public List<LoteDTO> LotesReservados() {
// return listLotes.stream().filter(loteDTO->
// loteDTO.estado().equals(Lote.RESERVADO)).toList();
// }
//
// @Override
// public List<LoteDTO> LotesNuevos() {
// return listLotes.stream().filter(l -> l.parque().equals("nuevo")).toList();
// }
//
// @Override
// public List<LoteDTO> LotesViejos() {
// return listLotes.stream().filter(l -> l.parque().equals("viejo")).toList();
//
// }
//
// @Override
// public void cancelarReserva(Lote lote) {
// this.lote = lote;
// }
//
// private void asignarLotes(){
// // 3 lotes vendidos
// listLotes.add(new LoteDTO(1, 100.0, Lote.VENDIDO, LocalDate.of(2023, 1, 1),
// 100000.0,"N/A", "lote", "nuevo"));
// listLotes.add(new LoteDTO(2, 150.0, Lote.VENDIDO, LocalDate.of(2023, 2, 1),
// 150000.0,"N/A", "lote","nuevo"));
// listLotes.add(new LoteDTO(3, 200.0, Lote.VENDIDO, LocalDate.of(2023, 3, 1),
// 200000.0,"N/A", "lote","nuevo"));
//
// // 3 lotes reservados
// listLotes.add(new LoteDTO(4, 120.0, Lote.RESERVADO, null, 0.0,"N/A", "lote",
// "nuevo"));
// listLotes.add(new LoteDTO(5, 180.0, Lote.RESERVADO, null, 0.0,"N/A", "lote",
// "nuevo"));
// listLotes.add(new LoteDTO(6, 220.0, Lote.RESERVADO, null, 0.0,"N/A", "lote",
// "nuevo"));
//
// // 3 lotes disponibles
// listLotes.add(new LoteDTO(7, 110.0, Lote.DISPONIBLE, null, 0.0,"N/A", "lote",
// "viejo"));
// listLotes.add(new LoteDTO(8, 160.0, Lote.DISPONIBLE, null, 0.0,"N/A", "lote",
// "viejo"));
// listLotes.add(new LoteDTO(9, 210.0, Lote.DISPONIBLE, null, 0.0,"N/A", "lote",
// "viejo"));
// }
// public Lote getLote() {
// return lote;
// }
// }
