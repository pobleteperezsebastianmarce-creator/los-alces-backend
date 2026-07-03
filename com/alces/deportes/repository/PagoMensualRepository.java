// ==========================================
// ARCHIVO: PagoMensualRepository.java
// QUÉ HACE: Acceso tabla pago_mensual; morosos, pendientes, sumas para reporte.
// FLUJO: PagoMensualService/ReporteService → queries → filas PagoMensual.
// ==========================================
package com.alces.deportes.repository;

import com.alces.deportes.model.PagoMensual;
import com.alces.deportes.model.enums.EstadoPago;
import com.alces.deportes.model.enums.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PagoMensualRepository extends JpaRepository<PagoMensual, Long> {

    List<PagoMensual> findBySocioId(Long socioId);

    List<PagoMensual> findByEstado(EstadoPago estado);

    List<PagoMensual> findByEstadoAndFechaVencimientoBefore(EstadoPago estado, LocalDate fecha);

    @Query("""
            SELECT COALESCE(SUM(p.monto), 0) FROM PagoMensual p
            WHERE p.tipoMovimiento = :tipo AND p.estado = 'PAGADO'
            """)
    Double sumarMontoPorTipoPagado(TipoMovimiento tipo);
}
