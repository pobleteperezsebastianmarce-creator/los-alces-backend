// ==========================================
// ARCHIVO: AlertaMorosidadService.java
// QUÉ HACE: RF07; arma listado de deudores con días de atraso.
// FLUJO: AlertaController → pagos MOROSO + calcularDiasAtraso → DTO para tabla-alertas.
// ==========================================
package com.alces.deportes.service;

import com.alces.deportes.dto.AlertaMorosidadDTO;
import com.alces.deportes.model.PagoMensual;
import com.alces.deportes.model.enums.EstadoPago;
import com.alces.deportes.repository.PagoMensualRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertaMorosidadService {

    private final PagoMensualRepository pagoMensualRepository;
    private final PagoMensualService pagoMensualService;

    // Trae pagos MOROSO y calcula cuántos días van de atraso
    public List<AlertaMorosidadDTO> listarAlertas() {
        pagoMensualService.procesarMorosidad();
        log.info("Generando listado de alertas de morosidad activas para el club");
        return pagoMensualRepository.findByEstado(EstadoPago.MOROSO).stream()
                .map(this::mapToDTO)
                .toList();
    }

    private AlertaMorosidadDTO mapToDTO(PagoMensual pago) {
        return new AlertaMorosidadDTO(
                pago.getId(),
                pago.getSocio().getId(),
                pago.getSocio().getNombre(),
                pago.getSocio().getRut(),
                pago.getConcepto(),
                pago.getMonto(),
                pago.getFechaVencimiento(),
                (int) pagoMensualService.calcularDiasAtraso(pago),
                pago.getSocio().getEstadoMembresia());
    }
}
