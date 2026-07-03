// ==========================================
// ARCHIVO: SocioController.java
// QUÉ HACE: REST (CSR) RF02; CRUD de socios en /api/socios.
// FLUJO: Formularios y búsqueda (app.js) → SocioService → SocioRepository → tabla socio.
// ==========================================
package com.alces.deportes.controller;

import com.alces.deportes.dto.SocioRequestDTO;
import com.alces.deportes.dto.SocioResponseDTO;
import com.alces.deportes.service.SocioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// RF02 - CRUD socios. Base: /api/socios
@RestController
@RequestMapping("/api/socios")
@RequiredArgsConstructor
public class SocioController {

    private final SocioService socioService;

    // GET /api/socios?q=texto - búsqueda por nombre o RUT (LIKE en el repository)
    @GetMapping
    public ResponseEntity<List<SocioResponseDTO>> obtenerTodos(@RequestParam(required = false) String q) {
        return ResponseEntity.ok(socioService.buscar(q));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SocioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(socioService.obtenerPorId(id));
    }

    // POST /api/socios - alta desde form-socio-quick del dashboard
    @PostMapping
    public ResponseEntity<SocioResponseDTO> crear(@Valid @RequestBody SocioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(socioService.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SocioResponseDTO> actualizar(
            @PathVariable Long id, @Valid @RequestBody SocioRequestDTO dto) {
        return ResponseEntity.ok(socioService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        socioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
