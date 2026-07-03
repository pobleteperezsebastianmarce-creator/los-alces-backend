// ==========================================
// ARCHIVO: JugadorController.java
// QUÉ HACE: REST (CSR) RF01; CRUD jugadores y filtros en /api/jugadores.
// FLUJO: app.js (?q=, categoriaId) → JugadorService → JugadorRepository → tabla jugador.
// ==========================================
package com.alces.deportes.controller;

import com.alces.deportes.dto.JugadorRequestDTO;
import com.alces.deportes.dto.JugadorResponseDTO;
import com.alces.deportes.service.JugadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// RF01 - jugadores. Base: /api/jugadores
@RestController
@RequestMapping("/api/jugadores")
@RequiredArgsConstructor
public class JugadorController {

    private final JugadorService jugadorService;

    // GET /api/jugadores?q= &categoriaId= filtra en BD (nombre, rut o categoría)
    @GetMapping
    public ResponseEntity<List<JugadorResponseDTO>> obtenerTodos(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categoriaId) {
        return ResponseEntity.ok(jugadorService.buscar(q, categoriaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JugadorResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(jugadorService.obtenerPorId(id));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<JugadorResponseDTO>> obtenerPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(jugadorService.obtenerPorCategoria(categoriaId));
    }

    @PostMapping
    public ResponseEntity<JugadorResponseDTO> crear(@Valid @RequestBody JugadorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jugadorService.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JugadorResponseDTO> actualizar(
            @PathVariable Long id, @Valid @RequestBody JugadorRequestDTO dto) {
        return ResponseEntity.ok(jugadorService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        jugadorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
