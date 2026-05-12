package com.parque_industrial.controllers;
import com.parque_industrial.controllers.dtos.lote.AnularReservaRequestDTO;
import com.parque_industrial.controllers.dtos.lote.CrearRequestDTO;
import com.parque_industrial.controllers.dtos.lote.ReservarRequestDTO;
import com.parque_industrial.controllers.dtos.lote.VentaRequestDTO;
import com.parque_industrial.entities.Lote;
import com.parque_industrial.services.FakeGestorInmobiliario;
import com.parque_industrial.persistence.dtos.LoteDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoteControllerTest {
    private FakeGestorInmobiliario   fakeGestor = new FakeGestorInmobiliario();
    private LoteController  controller = new LoteController(fakeGestor);


    @Test
    void testRegistrarLote_datosInvalidos_retornarBadRequest() {
        CrearRequestDTO requestInvalida = new CrearRequestDTO(-1, -50.0);
        ResponseEntity<String> respuesta = controller.crearUnLote(requestInvalida);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("Datos de venta inválidos", respuesta.getBody());
    }

    @Test
    void testRegistrarLote_claveDupicada_retornaConflict() throws Exception {
        fakeGestor.crearLote(new LoteDTO(200, 300.0));
        CrearRequestDTO duplicado = new CrearRequestDTO(200, 300.0);
        ResponseEntity<String> respuesta = controller.crearUnLote(duplicado);
        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
    }

    @Test
    void testProcesarVenta_loteReservado_retornaOK() {
        LoteDTO loteExistente = new LoteDTO(101, 500.0, Lote.RESERVADO, null, 0.0);
        fakeGestor.cargarLoteDePrueba(loteExistente);
        VentaRequestDTO request = new VentaRequestDTO(101, 5000.0);
        ResponseEntity<String> respuesta = controller.procesarVenta(request);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("Venta registrada exitosamente", respuesta.getBody());
    }
    @Test
    void testProcesarVenta_noExisteLote_retornaConflict() {
        VentaRequestDTO requestInvalida = new VentaRequestDTO(1, 5000.0);
        ResponseEntity<String> respuesta = controller.procesarVenta(requestInvalida);
        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals("Lote no encontrado", respuesta.getBody());
    }
    @Test
    void testProcesarVenta_datosInvalidos_retornarBadRequest() {
        VentaRequestDTO requestInvalida = new VentaRequestDTO(-1, -5000.0);
        ResponseEntity<String> respuesta = controller.procesarVenta(requestInvalida);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("Datos de venta inválidos", respuesta.getBody());
    }
    @Test
    void testVenderUnLote_YaVendido_RetornaConflict(){
        LoteDTO loteExistente = new LoteDTO(101, 500.0, Lote.VENDIDO, null, 0.0);
        fakeGestor.cargarLoteDePrueba(loteExistente);
        VentaRequestDTO request = new VentaRequestDTO(101, 50.0);
        ResponseEntity<String> respuesta = controller.procesarVenta(request);
        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals("El lote no está disponible para vender.", respuesta.getBody());
    }
    @Test
    void testReservarUnLoteDiponible_retornarOK() {
        LoteDTO loteExistente = new LoteDTO(101, 500.0);
        fakeGestor.cargarLoteDePrueba(loteExistente);
        ReservarRequestDTO request = new ReservarRequestDTO(101);
        ResponseEntity<String> respuesta = controller.reservarUnLote(request);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("Reserva registrada exitosamente", respuesta.getBody());
    }
    @Test
    void testReservarUnLote_noExisteLote_retornaConflict() {
        ReservarRequestDTO requestInvalida = new ReservarRequestDTO(1);
        ResponseEntity<String> respuesta = controller.reservarUnLote(requestInvalida);
        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals("Lote no encontrado", respuesta.getBody());
    }
    @Test
    void testReservarUnLoteDatosInvalidos_retornarBadRequest() {
        ReservarRequestDTO requestInvalida = new ReservarRequestDTO(-1);
        ResponseEntity<String> respuesta = controller.reservarUnLote(requestInvalida);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("Datos de venta inválidos", respuesta.getBody());
    }
    @Test
    void testReservarUnLote_YaReservado_RetornaConflict(){
        LoteDTO loteExistente = new LoteDTO(101, 500.0, Lote.RESERVADO, null, 0.0);
        fakeGestor.cargarLoteDePrueba(loteExistente);
        ReservarRequestDTO request = new ReservarRequestDTO(101);
        ResponseEntity<String> respuesta = controller.reservarUnLote(request);
        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals("El lote no está disponible para reservar.", respuesta.getBody());
    }
    @Test
    void testCancelarReserva_noExisteLote_retornaConflict() {
        AnularReservaRequestDTO requestInvalida = new AnularReservaRequestDTO(1);
        ResponseEntity<String> respuesta = controller.cancelarReserva(requestInvalida);
        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals("Lote no encontrado", respuesta.getBody());
    }
    @Test
    void testCancelarReserva_datosInvalidos_retornarBadRequest() {
        AnularReservaRequestDTO requestInvalida = new AnularReservaRequestDTO(-1);
        ResponseEntity<String> respuesta = controller.cancelarReserva(requestInvalida);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("Datos de venta inválidos", respuesta.getBody());
    }
    @Test
    void testCancelarReserva_NoReservado_RetornaConflict(){
        LoteDTO loteExistente = new LoteDTO(101, 500.0);
        fakeGestor.cargarLoteDePrueba(loteExistente);
        AnularReservaRequestDTO request = new AnularReservaRequestDTO(101);
        ResponseEntity<String> respuesta = controller.cancelarReserva(request);
        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals("El lote no está reservado, no se puede cancelar la reserva.", respuesta.getBody());
    }
    @Test
    public void testListarDisponibles() {
        LoteDTO loteExistente = new LoteDTO(101, 20.0);
        fakeGestor.cargarLoteDePrueba(loteExistente);
        ResponseEntity<List<LoteDTO>> respuesta = controller.listarDisponibles();
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        assertEquals(101, respuesta.getBody().get(0).identificacion());
        assertEquals(20.0, respuesta.getBody().get(0).superficie());
    }
    @Test
    public void testListarVendidos() {
        LoteDTO loteExistente = new LoteDTO(101, 500.0, Lote.VENDIDO, null, 0.0);
        fakeGestor.cargarLoteDePrueba(loteExistente);
        ResponseEntity<List<LoteDTO>> respuesta = controller.listarVendidos();
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        assertEquals(101, respuesta.getBody().get(0).identificacion());
        assertEquals(500.0, respuesta.getBody().get(0).superficie());
        assertEquals(Lote.VENDIDO, respuesta.getBody().get(0).estado());
    }
    @Test
    public void testListarReservados() {
        LoteDTO loteExistente = new LoteDTO(101, 500.0, Lote.RESERVADO, null, 0.0);
        fakeGestor.cargarLoteDePrueba(loteExistente);
        ResponseEntity<List<LoteDTO>> respuesta = controller.listarReservados();
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        assertEquals(101, respuesta.getBody().get(0).identificacion());
        assertEquals(500.0, respuesta.getBody().get(0).superficie());
        assertEquals(Lote.RESERVADO, respuesta.getBody().get(0).estado());
    }

}