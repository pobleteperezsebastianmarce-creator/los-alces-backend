// ==========================================
// ARCHIVO: PagoMensualController.java
// QUÉ HACE: REST (CSR) RF03/RF07; pagos en /api/pagos y procesar morosidad.
// FLUJO: form-nuevo-pago y tabla-pagos (app.js) → PagoMensualService → tabla pago_mensual.
// ==========================================
package com.alces.deportes.controller;

import com.alces.deportes.dto.PagoMensualRequestDTO;
import com.alces.deportes.dto.PagoMensualResponseDTO;
import com.alces.deportes.service.PagoMensualService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// RF03 - cuotas mensuales ligadas a un socio
@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoMensualController {

    private final PagoMensualService pagoMensualService;

    // GET /api/pagos - lista para #tabla-pagos del front
    @GetMapping
    public ResponseEntity<List<PagoMensualResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(pagoMensualService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoMensualResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoMensualService.obtenerPorId(id));
    }

    @GetMapping("/socio/{socioId}")
    public ResponseEntity<List<PagoMensualResponseDTO>> obtenerPorSocio(@PathVariable Long socioId) {
        return ResponseEntity.ok(pagoMensualService.obtenerPorSocio(socioId));
    }

    // POST /api/pagos - body: socioId, concepto, monto, mes, anio, fechaVencimiento
    @PostMapping
    public ResponseEntity<PagoMensualResponseDTO> registrar(@Valid @RequestBody PagoMensualRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoMensualService.registrar(dto));
    }

    // PATCH /api/pagos/{id}/pagar - botón "Marcar pagado" del front
    @PatchMapping("/{id}/pagar")
    public ResponseEntity<PagoMensualResponseDTO> marcarPagado(@PathVariable Long id) {
        return ResponseEntity.ok(pagoMensualService.marcarComoPagado(id));
    }

    // RF07 - botón "Procesar morosidad" llama acá
    @PostMapping("/procesar-morosidad")
    public ResponseEntity<Map<String, Integer>> procesarMorosidad() {
        int actualizados = pagoMensualService.procesarMorosidad();
        return ResponseEntity.ok(Map.of("pagosActualizados", actualizados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pagoMensualService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
