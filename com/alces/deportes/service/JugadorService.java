// ==========================================
// ARCHIVO: JugadorService.java
// QUÉ HACE: Reglas RF01; jugador por categoría/edad, búsqueda dinámica.
// FLUJO: JugadorController → JugadorRepository → relaciona categoria y administrador.
// ==========================================
package com.alces.deportes.service;

import com.alces.deportes.dto.JugadorRequestDTO;
import com.alces.deportes.dto.JugadorResponseDTO;
import com.alces.deportes.exception.BusinessException;
import com.alces.deportes.exception.ResourceNotFoundException;
import com.alces.deportes.model.CategoriaDeportiva;
import com.alces.deportes.model.Jugador;
import com.alces.deportes.model.Usuario;
import com.alces.deportes.repository.JugadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JugadorService {

    private static final String ESTADO_ACTIVO = "ACTIVO";

    private final JugadorRepository jugadorRepository;
    private final CategoriaService categoriaService;
    private final UsuarioActualService usuarioActualService;

    public List<JugadorResponseDTO> obtenerTodos() {
        return jugadorRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public JugadorResponseDTO obtenerPorId(Long id) {
        return mapToDTO(buscarEntidad(id));
    }

    public List<JugadorResponseDTO> obtenerPorCategoria(Long categoriaId) {
        categoriaService.buscarEntidad(categoriaId);
        return jugadorRepository.findByCategoriaId(categoriaId).stream().map(this::mapToDTO).toList();
    }

    // RF09 - búsqueda dinámica que usa el JPQL del JugadorRepository
    public List<JugadorResponseDTO> buscar(String termino, Long categoriaId) {
        if (termino == null || termino.isBlank()) {
            return categoriaId != null ? obtenerPorCategoria(categoriaId) : obtenerTodos();
        }
        String q = termino.trim();
        List<Jugador> resultado = categoriaId != null
                ? jugadorRepository.buscarPorCategoriaYTermino(categoriaId, q)
                : jugadorRepository.buscarPorNombreRutOCategoria(q);
        return resultado.stream().map(this::mapToDTO).toList();
    }

    @Transactional
    public JugadorResponseDTO guardar(JugadorRequestDTO dto) {
        validarRutUnico(dto.getRut(), null);
        CategoriaDeportiva categoria = categoriaService.buscarEntidad(dto.getCategoriaId());
        validarEdadEnCategoria(dto.getEdad(), categoria);

        Jugador jugador = new Jugador(
                null,
                dto.getRut().trim(),
                dto.getNombre().trim(),
                dto.getEdad(),
                resolverEstado(dto.getEstado()),
                categoria,
                usuarioActualService.obtenerUsuarioActual(),
                null);

        return mapToDTO(jugadorRepository.save(jugador));
    }

    @Transactional
    public JugadorResponseDTO actualizar(Long id, JugadorRequestDTO dto) {
        Jugador existente = buscarEntidad(id);
        validarRutUnico(dto.getRut(), id);
        CategoriaDeportiva categoria = categoriaService.buscarEntidad(dto.getCategoriaId());
        validarEdadEnCategoria(dto.getEdad(), categoria);

        existente.setRut(dto.getRut().trim());
        existente.setNombre(dto.getNombre().trim());
        existente.setEdad(dto.getEdad());
        existente.setEstado(resolverEstado(dto.getEstado()));
        existente.setCategoria(categoria);

        return mapToDTO(jugadorRepository.save(existente));
    }

    @Transactional
    public void eliminar(Long id) {
        buscarEntidad(id);
        jugadorRepository.deleteById(id);
    }

    public Jugador buscarEntidad(Long id) {
        return jugadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jugador no encontrado con id: " + id));
    }

    private void validarRutUnico(String rut, Long idExcluir) {
        boolean duplicado = idExcluir == null
                ? jugadorRepository.existsByRut(rut.trim())
                : jugadorRepository.existsByRutAndIdNot(rut.trim(), idExcluir);
        if (duplicado) {
            throw new BusinessException("Ya existe un jugador con el RUT: " + rut);
        }
    }

    private void validarEdadEnCategoria(int edad, CategoriaDeportiva categoria) {
        if (edad < categoria.getEdadMinima() || edad > categoria.getEdadMaxima()) {
            throw new BusinessException(String.format(
                    "La edad %d no corresponde a la categoría %s (%d-%d años)",
                    edad, categoria.getNombreCategoria(), categoria.getEdadMinima(), categoria.getEdadMaxima()));
        }
    }

    private String resolverEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            return ESTADO_ACTIVO;
        }
        return estado.trim().toUpperCase();
    }

    private JugadorResponseDTO mapToDTO(Jugador j) {
        return new JugadorResponseDTO(
                j.getId(),
                j.getRut(),
                j.getNombre(),
                j.getEdad(),
                j.getEstado(),
                j.getCategoria().getId(),
                j.getCategoria().getNombreCategoria());
    }
}
