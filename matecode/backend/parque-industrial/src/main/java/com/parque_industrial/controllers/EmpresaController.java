package com.parque_industrial.controllers;

import com.parque_industrial.dto.empresa.AsignarLoteRequestDTO;
import com.parque_industrial.dto.empresa.CrearRequestDTO;
import com.parque_industrial.dto.empresa.EmpresaDTO;
import com.parque_industrial.dto.empresa.EstadoRadicacionDTO;
import com.parque_industrial.dto.empresa.OcupacionLoteDTO;
import com.parque_industrial.services.GestorEmpresa;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {
    private final GestorEmpresa gestor;

    public EmpresaController(GestorEmpresa gestor) {
        this.gestor = gestor;
    }

    @PostMapping("/registrar")
    public void registrarEmpresa(@RequestBody CrearRequestDTO datosEntrada) {
        gestor.CrearEmpresa(new EmpresaDTO(datosEntrada.identificacion(), datosEntrada.razonSocial(), false));
    }

    @GetMapping("/rad")
    public ResponseEntity<List<EmpresaDTO>> listarEmpresasRadicadas() {
        List<EmpresaDTO> lista = gestor.empresasRadicadas();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/noRad")
    public ResponseEntity<List<EmpresaDTO>> listarEmpresasNoRadicadas() {
        List<EmpresaDTO> lista = gestor.empresasNoRedicadas();
        return ResponseEntity.ok(lista);
    }

    @GetMapping
    public ResponseEntity<List<EmpresaDTO>> listarEmpresas() {
        List<EmpresaDTO> lista = gestor.empresas();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{cuit}")
    public ResponseEntity<EmpresaDTO> buscarEmpresaPorCuit(@PathVariable String cuit) {
        EmpresaDTO empresa = gestor.buscarEmpresaPorCuit(cuit);
        return ResponseEntity.ok(empresa);
    }

    @PostMapping("/AsignarLote")
    public void AsignarLote(@RequestBody AsignarLoteRequestDTO datosEntrada) {
        gestor.asignarLoteAEmpresa(datosEntrada.cuit(), datosEntrada.idlote());
    }

    @PostMapping("/ocupar")
    public ResponseEntity<Void> ocuparLote(@RequestBody OcupacionLoteDTO request) {
        gestor.ocuparLote(
                request.cuit(),
                request.idlote());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{cuit}/radicacion")
    public ResponseEntity<Void> actualizarRadicacion(
            @PathVariable String cuit,
            @RequestBody EstadoRadicacionDTO request) {

        gestor.actualizarEstadoRadicacion(
                cuit,
                request.radicada());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/desocupar")
    public ResponseEntity<Void> desocuparLote(@RequestBody OcupacionLoteDTO request) {
        gestor.desocuparLote(
                request.cuit());
        return ResponseEntity.ok().build();
    }

}