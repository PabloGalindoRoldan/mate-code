package com.parque_industrial.controllers;

import com.parque_industrial.dto.lote.CrearRequestDTO;
import com.parque_industrial.dto.lote.ReservarRequestDTO;
import com.parque_industrial.dto.lote.VentaRequestDTO;
import com.parque_industrial.dto.lote.AnularReservaRequestDTO;
import com.parque_industrial.dto.lote.LoteDTO;
import com.parque_industrial.services.GestorInmobiliario;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController // Indica que esta clase es un controlador de API
@RequestMapping("/api/lotes") // La URL base para React
public class LoteController {
    private final GestorInmobiliario gestor;
    public LoteController(GestorInmobiliario gestor) {
        this.gestor = gestor;
    }
    @PostMapping("/vender")
    public void procesarVenta(@RequestBody VentaRequestDTO datosEntrada) {
        LoteDTO dto = gestor.buscarLote(datosEntrada.identificacion());
        gestor.VenderLote(dto, datosEntrada.monto());
    }
    @PostMapping("/reservar")
    public void reservarUnLote(@RequestBody ReservarRequestDTO datosEntrada) {
        LoteDTO dto = gestor.buscarLote(datosEntrada.identificacion());
        gestor.ReservarLote(dto);
    }
    @PostMapping("/registrar")
    public void crearUnLote(@RequestBody CrearRequestDTO datosEntrada) {
        LoteDTO lote = new LoteDTO(datosEntrada.identificacion(), datosEntrada.superficie(), datosEntrada.nc(), datosEntrada.parque());
        gestor.crearLote(lote);
    }
    @PostMapping("/cancelarReserva")//este es por si no cumplio y se le quita el lote
    public void cancelarReserva(@RequestBody AnularReservaRequestDTO datosEntrada) {
        LoteDTO dto = gestor.buscarLote(datosEntrada.identificacion());
        gestor.cancelarReserva(dto);
    }
    @GetMapping("/disponibles")
    public ResponseEntity<List<LoteDTO>> listarDisponibles()  {
        List<LoteDTO>  lista = gestor.LotesDisponibles();
        return ResponseEntity.ok(lista);
    }
    @GetMapping("/vendidos")
    public ResponseEntity<List<LoteDTO>> listarVendidos()  {
        List<LoteDTO>    lista = gestor.LotesVendidos();
        return ResponseEntity.ok(lista);
    }
    @GetMapping("/reservados")
    public ResponseEntity<List<LoteDTO>> listarReservados()  {
        List<LoteDTO> lista=  gestor.LotesReservados();
        return ResponseEntity.ok(lista);
    }
    @GetMapping("/nuevos")
    public ResponseEntity<List<LoteDTO>> listarLotesDeParqueNuevo()  {
        List<LoteDTO> lista = gestor.LotesDeParqueNuevo();
        return ResponseEntity.ok(lista);
    }
    @GetMapping("/viejos")
    public ResponseEntity<List<LoteDTO>> listarLotesDeParqueViejo()  {
        List<LoteDTO> lista = gestor.LotesDeParqueViejo();
        return ResponseEntity.ok(lista);
    }
}