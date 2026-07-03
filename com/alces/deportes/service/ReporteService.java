// ==========================================
// ARCHIVO: ReporteService.java
// QUÉ HACE: RF04; calcula ingresos, egresos y balance desde pagos PAGADOS.
// FLUJO: ReporteController → consultas agregadas en PagoMensualRepository → JSON al front.
// ==========================================
package com.alces.deportes.service;

import com.alces.deportes.dto.ReporteFinancieroDTO;
import com.alces.deportes.model.enums.EstadoPago;
import com.alces.deportes.model.enums.EstadoMembresia;
import com.alces.deportes.model.enums.TipoMovimiento;
import com.alces.deportes.repository.PagoMensualRepository;
import com.alces.deportes.repository.SocioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// Suma ingresos/egresos de pagos PAGADOS en MySQL (RF04)
@Service
@RequiredArgsConstructor
public class ReporteService {

    private final PagoMensualRepository pagoMensualRepository;
    private final SocioRepository socioRepository;
    private final PagoMensualService pagoMensualService;

    public ReporteFinancieroDTO generarReporteFinanciero() {
        pagoMensualService.procesarMorosidad(); // números al día antes de sumar

        Double ingresos = pagoMensualRepository.sumarMontoPorTipoPagado(TipoMovimiento.INGRESO);
        Double egresos = pagoMensualRepository.sumarMontoPorTipoPagado(TipoMovimiento.EGRESO);
        ingresos = ingresos != null ? ingresos : 0.0;
        egresos = egresos != null ? egresos : 0.0;

        long pendientes = pagoMensualRepository.findByEstado(EstadoPago.PENDIENTE).size();
        long morosos = pagoMensualRepository.findByEstado(EstadoPago.MOROSO).size();
        long sociosMorosos = socioRepository.findByEstadoMembresia(EstadoMembresia.MOROSA).size();

        // balance = ingresos - egresos (solo movimientos marcados PAGADO)
        return new ReporteFinancieroDTO(
                ingresos,
                egresos,
                ingresos - egresos,
                pendientes,
                morosos,
                sociosMorosos);
    }
}
