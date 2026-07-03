// ==========================================
// ARCHIVO: RespaldoController.java
// QUÉ HACE: REST (CSR) RF10; exporta y descarga respaldo JSON de la BD.
// FLUJO: btn-respaldo (app.js) → RespaldoService lee tablas → archivo en carpeta backups/.
// ==========================================
package com.alces.deportes.controller;

import com.alces.deportes.service.RespaldoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

// RF10 - exportar copia JSON de las tablas
@RestController
@RequestMapping("/api/respaldo")
@RequiredArgsConstructor
public class RespaldoController {

    private final RespaldoService respaldoService;

    // POST /api/respaldo/exportar - arma JSON con socios, jugadores, pagos, etc.
    @PostMapping("/exportar")
    public ResponseEntity<Map<String, Object>> exportar() throws IOException {
        return ResponseEntity.ok(respaldoService.exportarRespaldo());
    }

    // GET /api/respaldo/descargar/{nombre} - el front abre esto para bajar el archivo
    @GetMapping("/descargar/{nombreArchivo}")
    public ResponseEntity<Resource> descargar(@PathVariable String nombreArchivo) {
        Path archivo = respaldoService.obtenerArchivoRespaldo(nombreArchivo);
        Resource resource = new FileSystemResource(archivo);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + archivo.getFileName().toString() + "\"")
                .contentType(MediaType.APPLICATION_JSON)
                .body(resource);
    }
}
