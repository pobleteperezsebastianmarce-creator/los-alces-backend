// ==========================================
// ARCHIVO: DocumentoJugadorController.java
// QUÉ HACE: REST (CSR) RF08; sube PDF/JPG multipart a /api/documentos/jugador/{id}.
// FLUJO: form-upload-doc (app.js) → DocumentoJugadorService → disco + tabla documento_jugador.
// ==========================================
package com.alces.deportes.controller;

import com.alces.deportes.dto.DocumentoJugadorResponseDTO;
import com.alces.deportes.service.DocumentoJugadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// RF08 - PDF/JPG de fichas médicas u otra doc del jugador
@RestController
@RequestMapping("/api/documentos")
@RequiredArgsConstructor
public class DocumentoJugadorController {

    private final DocumentoJugadorService documentoJugadorService;

    @GetMapping("/jugador/{jugadorId}")
    public ResponseEntity<List<DocumentoJugadorResponseDTO>> listarPorJugador(@PathVariable Long jugadorId) {
        return ResponseEntity.ok(documentoJugadorService.listarPorJugador(jugadorId));
    }

    // POST /api/documentos/jugador/{id} multipart campo "archivo" (form-upload-doc)
    @PostMapping(value = "/jugador/{jugadorId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentoJugadorResponseDTO> subir(
            @PathVariable Long jugadorId,
            @RequestPart("archivo") MultipartFile archivo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentoJugadorService.subir(jugadorId, archivo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        documentoJugadorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
