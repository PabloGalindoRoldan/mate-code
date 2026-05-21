package com.parque_industrial.controllers;

import com.parque_industrial.dto.empresa.AsignarLoteRequestDTO;
import com.parque_industrial.dto.empresa.CrearRequestDTO;
import com.parque_industrial.dto.empresa.EmpresaDTO;
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
    public void AsignarLote(@RequestBody AsignarLoteRequestDTO datosEntrada){
        gestor.asignarLoteAEmpresa(datosEntrada.cuit(), datosEntrada.idlote());
        // este metodo le cambia al lote que le asigna el estado a vendido
        // y la empresa pasa a ser radicada
        //si querias dejarlo en el reservado solo hay que cambia el estado metodo Asignarlote de empresadaojdbc
        //depues en el controller de lote esta el endpoitn par cambiar el estado a un lote sin importar el estado anterior
    }
}