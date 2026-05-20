package com.parque_industrial.services;

import com.parque_industrial.dto.inventario.BajaElementoRequestDTO;
import com.parque_industrial.dto.inventario.ElementoRequestDTO;
import com.parque_industrial.dto.inventario.ElementoResponseDTO;
import com.parque_industrial.entities.Elemento;
import com.parque_industrial.persistence.inventario.InventarioDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventarioServiceImpl implements InventarioService {

    private final InventarioDAO inventarioDAO;

    // Inyección de dependencias por constructor
    public InventarioServiceImpl(InventarioDAO inventarioDAO) {
        this.inventarioDAO = inventarioDAO;
    }

    @Override
    @Transactional
    public ElementoResponseDTO crearElemento(ElementoRequestDTO request) {
        // Mapeamos el DTO de entrada a nuestra entidad de negocio
        Elemento nuevoElemento = new Elemento(request.getNombre(), request.getCategoria());

        // Persistimos mediante el DAO nativo
        Elemento elementoGuardado = inventarioDAO.guardar(nuevoElemento);

        // Retornamos la respuesta mapeada
        return new ElementoResponseDTO(elementoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public ElementoResponseDTO buscarPorId(Integer id) {
        Elemento elemento = inventarioDAO.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró ningún elemento en el inventario con el ID: " + id));
        return new ElementoResponseDTO(elemento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ElementoResponseDTO> obtenerTodosLosElementos() {
        return inventarioDAO.listarTodos().stream()
                .map(ElementoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ElementoResponseDTO> obtenerElementosActivos() {
        return inventarioDAO.listarActivos().stream()
                .map(ElementoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ElementoResponseDTO procesarBajaLogica(Integer id, BajaElementoRequestDTO request) {
        // 1. Validamos la existencia previa del elemento
        Elemento elemento = inventarioDAO.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Imposible dar de baja: No existe el elemento con ID: " + id));

        // 2. Comprobamos que no esté ya de baja para evitar redundancia
        if (!elemento.isActivo()) {
            throw new IllegalStateException("El elemento con ID " + id + " ya se encuentra inactivo en el sistema.");
        }

        // 3. Aplicamos la lógica semántica de la entidad
        elemento.darDeBaja(request.getRazon(), request.getObservacion());

        // 4. Impactamos los cambios en la BD mediante el método actualizar de JDBC
        inventarioDAO.actualizar(elemento);

        return new ElementoResponseDTO(elemento);
    }
}