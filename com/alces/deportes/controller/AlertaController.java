// ==========================================
// ARCHIVO: AlertaController.java
// QUÉ HACE: REST (CSR) RF07; lista morosos GET /api/alertas/morosidad.
// FLUJO: tabla-alertas (app.js) → AlertaMorosidadService → pagos estado MOROSO.
// ==========================================
package com.alces.deportes.controller;

import com.alces.deportes.dto.AlertaMorosidadDTO;
import com.alces.deportes.service.AlertaMorosidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// RF07 - listado de morosos para #tabla-alertas
@RestController
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
public class AlertaController {

    private final AlertaMorosidadService alertaMorosidadService;

    // GET /api/alertas/morosidad
    @GetMapping("/morosidad")
    public ResponseEntity<List<AlertaMorosidadDTO>> alertasMorosidad() {
        return ResponseEntity.ok(alertaMorosidadService.listarAlertas());
    }
}
