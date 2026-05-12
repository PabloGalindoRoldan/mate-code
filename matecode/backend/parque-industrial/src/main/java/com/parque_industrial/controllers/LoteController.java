package com.parque_industrial.controllers;

import com.parque_industrial.controllers.dtos.lote.CrearRequestDTO;
import com.parque_industrial.controllers.dtos.lote.ReservarRequestDTO;
import com.parque_industrial.controllers.dtos.lote.VentaRequestDTO;
import com.parque_industrial.controllers.dtos.lote.AnularReservaRequestDTO;
import com.parque_industrial.persistence.dtos.LoteDTO;
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
    // React enviará un POST a http://tu-servidor/api/lotes/vender
    @PostMapping("/vender")
    public ResponseEntity<String> procesarVenta(@RequestBody VentaRequestDTO datosEntrada) {
       // el framework cuendo se realiza la peticion, convierte el json en VentaRequestDTO
        // Si el frontend manda basura, devolvemos error 400
        if (datosEntrada.identificacion() < 0 || datosEntrada.monto() <= 0) {
            return ResponseEntity.badRequest().body("Datos de venta inválidos");
        }
        try {
            LoteDTO dto = gestor.buscarLote(datosEntrada.identificacion());
            gestor.VenderLote(dto, datosEntrada.monto());
            // 4. Respuesta Exitosa:
            // Informamos a React que todo salió bien (HTTP 200) [Historial].
            return ResponseEntity.ok("Venta registrada exitosamente");
        } catch (Exception e) {
            // 5. Manejo de Errores de Negocio:
            // Si la entidad Lote lanza error (ej: ya estaba vendido), mandamos 409 Conflict [Historial].
            // o no se encontro ese lote en la bd
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }
    @PostMapping("/reservar")
    public ResponseEntity<String> reservarUnLote(@RequestBody ReservarRequestDTO datosEntrada) {
        if (datosEntrada.identificacion() < 0) {
            return ResponseEntity.badRequest().body("Datos de venta inválidos");
        }
        try {
            LoteDTO dto = gestor.buscarLote(datosEntrada.identificacion());
            gestor.ReservarLote(dto);
            return ResponseEntity.ok("Reserva registrada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }
    @PostMapping("/registrar")
    public ResponseEntity<String> crearUnLote(@RequestBody CrearRequestDTO datosEntrada) {
        if (datosEntrada.identificacion() < 0 || datosEntrada.superficie() <= 0) {
            return ResponseEntity.badRequest().body("Datos de venta inválidos");
        }
        try {
            LoteDTO lote = new LoteDTO(datosEntrada.identificacion(), datosEntrada.superficie());
           gestor.crearLote(lote);
            return ResponseEntity.ok("Lote registrado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }
    @GetMapping("/disponibles")
    public ResponseEntity<List<LoteDTO>> listarDisponibles()  {
        List<LoteDTO> lista = null;
        try {
            lista = gestor.LotesDisponibles();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(lista); // Retorna 200 OK con la lista [Historial]
    }
    @GetMapping("/vendidos")
    public ResponseEntity<List<LoteDTO>> listarVendidos()  {
        List<LoteDTO> lista = null;
        try {
            lista = gestor.LotesVendidos();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(lista);
    }
    @GetMapping("/reservados")
    public ResponseEntity<List<LoteDTO>> listarReservados()  {
        List<LoteDTO> lista = null;
        try {
            lista = gestor.LotesReservados();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(lista);
    }
    @PostMapping("/cancelarReserva")//este es por si no cumplio y se le quita el lote
    public ResponseEntity<String> cancelarReserva(@RequestBody AnularReservaRequestDTO datosEntrada) {
        if (datosEntrada.identificacion() < 0) {
            return ResponseEntity.badRequest().body("Datos de venta inválidos");
        }
        try {
            LoteDTO dto = gestor.buscarLote(datosEntrada.identificacion());
            gestor.cancelarReserva(dto);
            return ResponseEntity.ok("Reserva cancelada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }
}