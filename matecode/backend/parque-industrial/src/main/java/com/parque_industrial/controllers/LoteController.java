//package com.parque_industrial.controllers;
//
//import com.parque_industrial.persistence.dtos.LoteDTO;
//import com.parque_industrial.services.GestorInmobiliario;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.http.ResponseEntity;
//
//@RestController // Indica que esta clase es un controlador de API
//@RequestMapping("/api/lotes") // La URL base para React
//public class LoteController {
//    private final GestorInmobiliario gestor;
//    public LoteController(GestorInmobiliario gestor) {
//        this.gestor = gestor;
//    }
//    // React enviará un POST a http://tu-servidor/api/lotes/vender
//    @PostMapping("/vender")
//    public ResponseEntity<String> procesarVenta(@RequestBody VentaRequestDTO datosEntrada) {
//       // el framework cuendo se realiza la peticion, convierte el json en VentaRequestDTO
//        // Si el frontend manda basura, devolvemos error 400
//        if (datosEntrada.identificacion() < 0 || datosEntrada.monto() <= 0) {
//            return ResponseEntity.badRequest().body("Datos de venta inválidos");
//        }
//        try {
//            LoteDTO dto = new LoteDTO(datosEntrada.identificacion());
//            gestor.VenderLote(dto, datosEntrada.monto());
//            // 4. Respuesta Exitosa:
//            // Informamos a React que todo salió bien (HTTP 200) [Historial].
//            return ResponseEntity.ok("Venta registrada exitosamente en Railway");
//        } catch (Exception e) {
//            // 5. Manejo de Errores de Negocio:
//            // Si la entidad Lote lanza error (ej: ya estaba vendido), mandamos 409 Conflict [Historial].
//            return ResponseEntity.status(409).body(e.getMessage());
//        }
//    }
//
//}