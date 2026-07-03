// ==========================================
// ARCHIVO: PagoMensualService.java
// QUÉ HACE: RF03 cuotas y RF07 morosidad (vencimiento < hoy → MOROSO, socio MOROSA).
// FLUJO: PagoMensualController → PagoMensualRepository/SocioRepository → tablas pago y socio.
// ==========================================
package com.alces.deportes.service;

import com.alces.deportes.dto.PagoMensualRequestDTO;
import com.alces.deportes.dto.PagoMensualResponseDTO;
import com.alces.deportes.exception.ResourceNotFoundException;
import com.alces.deportes.model.PagoMensual;
import com.alces.deportes.model.Socio;
import com.alces.deportes.model.enums.EstadoMembresia;
import com.alces.deportes.model.enums.EstadoPago;
import com.alces.deportes.repository.PagoMensualRepository;
import com.alces.deportes.repository.SocioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

// Lógica RF03 y RF07 sobre tabla pago_mensual + actualiza estado del socio
@Service
@RequiredArgsConstructor
public class PagoMensualService {

    private final PagoMensualRepository pagoMensualRepository;
    private final SocioService socioService;
    private final SocioRepository socioRepository;

    @Transactional
    public List<PagoMensualResponseDTO> obtenerTodos() {
        procesarMorosidad(); // antes de listar, actualiza morosos por fecha
        return pagoMensualRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public PagoMensualResponseDTO obtenerPorId(Long id) {
        return mapToDTO(buscarEntidad(id));
    }

    public List<PagoMensualResponseDTO> obtenerPorSocio(Long socioId) {
        socioService.buscarEntidad(socioId);
        return pagoMensualRepository.findBySocioId(socioId).stream().map(this::mapToDTO).toList();
    }

    // Alta cuota: queda PENDIENTE hasta que la marquen pagada
    @Transactional
    public PagoMensualResponseDTO registrar(PagoMensualRequestDTO dto) {
        Socio socio = socioService.buscarEntidad(dto.getSocioId());
        PagoMensual pago = new PagoMensual(
                null,
                socio,
                dto.getConcepto().trim(),
                dto.getMonto(),
                dto.getTipoMovimiento(),
                dto.getMes(),
                dto.getAnio(),
                dto.getFechaVencimiento(),
                null,
                EstadoPago.PENDIENTE);
        return mapToDTO(pagoMensualRepository.save(pago));
    }

    @Transactional
    public PagoMensualResponseDTO marcarComoPagado(Long id) {
        PagoMensual pago = buscarEntidad(id);
        pago.setEstado(EstadoPago.PAGADO);
        pago.setFechaPago(LocalDate.now());
        PagoMensual guardado = pagoMensualRepository.save(pago);
        actualizarEstadoMembresiaSocio(guardado.getSocio().getId());
        return mapToDTO(guardado);
    }

    @Transactional
    public void eliminar(Long id) {
        buscarEntidad(id);
        pagoMensualRepository.deleteById(id);
    }

    // RF07 - si hoy pasó la fecha de vencimiento y sigue PENDIENTE -> MOROSO y socio pasa a MOROSA
    @Transactional
    public int procesarMorosidad() {
        LocalDate hoy = LocalDate.now();
        List<PagoMensual> pendientesVencidos = pagoMensualRepository
                .findByEstadoAndFechaVencimientoBefore(EstadoPago.PENDIENTE, hoy);

        int actualizados = 0;
        for (PagoMensual pago : pendientesVencidos) {
            pago.setEstado(EstadoPago.MOROSO);
            pagoMensualRepository.save(pago);
            Socio socio = pago.getSocio();
            socio.setEstadoMembresia(EstadoMembresia.MOROSA);
            socioRepository.save(socio);
            actualizados++;
        }
        return actualizados;
    }

    private void actualizarEstadoMembresiaSocio(Long socioId) {
        List<PagoMensual> morosos = pagoMensualRepository.findBySocioId(socioId).stream()
                .filter(p -> p.getEstado() == EstadoPago.MOROSO)
                .toList();
        Socio socio = socioService.buscarEntidad(socioId);
        if (morosos.isEmpty()) {
            socio.setEstadoMembresia(EstadoMembresia.ACTIVA);
        } else {
            socio.setEstadoMembresia(EstadoMembresia.MOROSA);
        }
        socioRepository.save(socio);
    }

    public PagoMensual buscarEntidad(Long id) {
        return pagoMensualRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con id: " + id));
    }

    private PagoMensualResponseDTO mapToDTO(PagoMensual p) {
        return new PagoMensualResponseDTO(
                p.getId(),
                p.getSocio().getId(),
                p.getSocio().getNombre(),
                p.getConcepto(),
                p.getMonto(),
                p.getTipoMovimiento(),
                p.getMes(),
                p.getAnio(),
                p.getFechaVencimiento(),
                p.getFechaPago(),
                p.getEstado());
    }

    // Días que lleva atrasado (para la tabla de alertas)
    public long calcularDiasAtraso(PagoMensual pago) {
        if (pago.getFechaVencimiento() == null) {
            return 0;
        }
        return Math.max(0, ChronoUnit.DAYS.between(pago.getFechaVencimiento(), LocalDate.now()));
    }
}
