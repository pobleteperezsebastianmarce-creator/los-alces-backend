// ==========================================
// ARCHIVO: CategoriaService.java
// QUÉ HACE: CRUD categorías; valida rangos de edad antes de asignar jugador.
// FLUJO: CategoriaController → CategoriaRepository → tabla categoria_deportiva.
// ==========================================
package com.alces.deportes.service;

import com.alces.deportes.dto.CategoriaRequestDTO;
import com.alces.deportes.dto.CategoriaResponseDTO;
import com.alces.deportes.exception.BusinessException;
import com.alces.deportes.exception.ResourceNotFoundException;
import com.alces.deportes.model.CategoriaDeportiva;
import com.alces.deportes.repository.CategoriaRepository;
import com.alces.deportes.repository.JugadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final JugadorRepository jugadorRepository;

    public List<CategoriaResponseDTO> obtenerTodos() {
        return categoriaRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public CategoriaResponseDTO obtenerPorId(Long id) {
        return mapToDTO(buscarEntidad(id));
    }

    @Transactional
    public CategoriaResponseDTO guardar(CategoriaRequestDTO dto) {
        validarRangoEdades(dto.getEdadMinima(), dto.getEdadMaxima());
        CategoriaDeportiva categoria = new CategoriaDeportiva();
        categoria.setNombreCategoria(dto.getNombreCategoria().trim());
        categoria.setEdadMinima(dto.getEdadMinima());
        categoria.setEdadMaxima(dto.getEdadMaxima());
        return mapToDTO(categoriaRepository.save(categoria));
    }

    @Transactional
    public CategoriaResponseDTO actualizar(Long id, CategoriaRequestDTO dto) {
        validarRangoEdades(dto.getEdadMinima(), dto.getEdadMaxima());
        CategoriaDeportiva existente = buscarEntidad(id);
        existente.setNombreCategoria(dto.getNombreCategoria().trim());
        existente.setEdadMinima(dto.getEdadMinima());
        existente.setEdadMaxima(dto.getEdadMaxima());
        return mapToDTO(categoriaRepository.save(existente));
    }

    @Transactional
    public void eliminar(Long id) {
        buscarEntidad(id);
        if (!jugadorRepository.findByCategoriaId(id).isEmpty()) {
            throw new BusinessException("No se puede eliminar: existen jugadores en esta categoría");
        }
        categoriaRepository.deleteById(id);
    }

    public CategoriaDeportiva buscarEntidad(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));
    }

    private void validarRangoEdades(int edadMinima, int edadMaxima) {
        if (edadMinima > edadMaxima) {
            throw new BusinessException("La edad mínima no puede ser mayor que la edad máxima");
        }
    }

    private CategoriaResponseDTO mapToDTO(CategoriaDeportiva c) {
        return new CategoriaResponseDTO(c.getId(), c.getNombreCategoria(), c.getEdadMinima(), c.getEdadMaxima());
    }
}
