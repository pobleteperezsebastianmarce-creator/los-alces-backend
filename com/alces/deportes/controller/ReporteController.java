// ==========================================
// ARCHIVO: ReporteController.java
// QUÉ HACE: REST (CSR) RF04; reporte financiero GET /api/reportes/financiero.
// FLUJO: panel-reporte (app.js) → ReporteService suma pagos PAGADOS → devuelve balance.
// ==========================================
package com.alces.deportes.controller;

import com.alces.deportes.dto.ReporteFinancieroDTO;
import com.alces.deportes.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// RF04 - balance ingresos/egresos desde pagos PAGADOS
@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    // GET /api/reportes/financiero - lo pinta panel-reporte en app.js
    @GetMapping("/financiero")
    public ResponseEntity<ReporteFinancieroDTO> reporteFinanciero() {
        return ResponseEntity.ok(reporteService.generarReporteFinanciero());
    }
}
