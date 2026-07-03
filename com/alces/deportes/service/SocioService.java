// ==========================================
// ARCHIVO: SocioService.java
// QUÉ HACE: Reglas RF02; alta/edición socio, RUT único, búsqueda LIKE.
// FLUJO: SocioController → valida DTO → SocioRepository.save → MySQL socio.
// ==========================================
package com.alces.deportes.service;

import com.alces.deportes.dto.SocioRequestDTO;
import com.alces.deportes.dto.SocioResponseDTO;
import com.alces.deportes.exception.BusinessException;
import com.alces.deportes.exception.ResourceNotFoundException;
import com.alces.deportes.model.Socio;
import com.alces.deportes.model.enums.EstadoMembresia;
import com.alces.deportes.repository.SocioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SocioService {

    private final SocioRepository socioRepository;
    private final UsuarioActualService usuarioActualService;

    public List<SocioResponseDTO> obtenerTodos() {
        return socioRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public SocioResponseDTO obtenerPorId(Long id) {
        return mapToDTO(buscarEntidad(id));
    }

    // RF09 - si viene ?q= hace LIKE en nombre/RUT; si no, trae todos
    public List<SocioResponseDTO> buscar(String termino) {
        if (termino == null || termino.isBlank()) {
            return obtenerTodos();
        }
        return socioRepository.buscarPorNombreORut(termino.trim()).stream().map(this::mapToDTO).toList();
    }

    // Persiste en tabla socio (Flyway ya creó las columnas)
    @Transactional
    public SocioResponseDTO guardar(SocioRequestDTO dto) {
        validarRutUnico(dto.getRut(), null);
        Socio socio = new Socio(
                null,
                dto.getRut().trim(),
                dto.getNombre().trim(),
                dto.getEmail(),
                dto.getTelefono(),
                dto.getTipoMembresia().trim(),
                dto.getEstadoMembresia() != null ? dto.getEstadoMembresia() : EstadoMembresia.ACTIVA,
                dto.getFechaInscripcion(),
                usuarioActualService.obtenerUsuarioActual(),
                null);
        return mapToDTO(socioRepository.save(socio));
    }

    @Transactional
    public SocioResponseDTO actualizar(Long id, SocioRequestDTO dto) {
        Socio existente = buscarEntidad(id);
        validarRutUnico(dto.getRut(), id);
        existente.setRut(dto.getRut().trim());
        existente.setNombre(dto.getNombre().trim());
        existente.setEmail(dto.getEmail());
        existente.setTelefono(dto.getTelefono());
        existente.setTipoMembresia(dto.getTipoMembresia().trim());
        if (dto.getEstadoMembresia() != null) {
            existente.setEstadoMembresia(dto.getEstadoMembresia());
        }
        existente.setFechaInscripcion(dto.getFechaInscripcion());
        return mapToDTO(socioRepository.save(existente));
    }

    @Transactional
    public void eliminar(Long id) {
        buscarEntidad(id);
        socioRepository.deleteById(id);
    }

    public Socio buscarEntidad(Long id) {
        return socioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con id: " + id));
    }

    private void validarRutUnico(String rut, Long idExcluir) {
        boolean duplicado = idExcluir == null
                ? socioRepository.existsByRut(rut.trim())
                : socioRepository.existsByRutAndIdNot(rut.trim(), idExcluir);
        if (duplicado) {
            throw new BusinessException("Ya existe un socio con el RUT: " + rut);
        }
    }

    private SocioResponseDTO mapToDTO(Socio s) {
        return new SocioResponseDTO(
                s.getId(),
                s.getRut(),
                s.getNombre(),
                s.getEmail(),
                s.getTelefono(),
                s.getTipoMembresia(),
                s.getEstadoMembresia(),
                s.getFechaInscripcion());
    }
}
