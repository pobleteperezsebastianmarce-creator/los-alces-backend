// ==========================================
// ARCHIVO: RespaldoService.java
// QUÉ HACE: RF10; vuelca tablas a JSON timestamped en backups/.
// FLUJO: RespaldoController → lee todos los Repository → escribe archivo .json.
// ==========================================
package com.alces.deportes.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.alces.deportes.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RespaldoService {

    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SocioRepository socioRepository;
    private final JugadorRepository jugadorRepository;
    private final PagoMensualRepository pagoMensualRepository;
    private final DocumentoJugadorRepository documentoJugadorRepository;

    @Value("${app.backup.dir:backups}")
    private String backupDir;

    public Map<String, Object> exportarRespaldo() throws IOException {
        Map<String, Object> respaldo = new LinkedHashMap<>();
        respaldo.put("fecha", LocalDateTime.now().toString());
        // Volcado de tablas MySQL a un .json en carpeta backups/
        respaldo.put("categorias", categoriaRepository.findAll());
        respaldo.put("usuarios", usuarioRepository.findAll().stream()
                .map(u -> Map.of(
                        "id", u.getId(),
                        "username", u.getUsername(),
                        "rol", u.getRol().name(),
                        "nombre", u.getNombre(),
                        "activo", u.isActivo()))
                .toList());
        respaldo.put("socios", socioRepository.findAll());
        respaldo.put("jugadores", jugadorRepository.findAll());
        respaldo.put("pagos", pagoMensualRepository.findAll());
        respaldo.put("documentos", documentoJugadorRepository.findAll());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Path directorio = Paths.get(backupDir).toAbsolutePath().normalize();
        Files.createDirectories(directorio);
        String nombre = "respaldo_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".json";
        Path archivo = directorio.resolve(nombre);
        mapper.writerWithDefaultPrettyPrinter().writeValue(archivo.toFile(), respaldo);

        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("fecha", respaldo.get("fecha"));
        resultado.put("nombreArchivo", nombre);
        resultado.put("archivoGenerado", archivo.toString());
        return resultado;
    }

    public Path obtenerArchivoRespaldo(String nombreArchivo) {
        Path directorio = Paths.get(backupDir).toAbsolutePath().normalize();
        Path archivo = directorio.resolve(nombreArchivo).normalize();
        if (!archivo.startsWith(directorio) || !Files.exists(archivo)) {
            throw new com.alces.deportes.exception.ResourceNotFoundException("Archivo de respaldo no encontrado");
        }
        return archivo;
    }
}
