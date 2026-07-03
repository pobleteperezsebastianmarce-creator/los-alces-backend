// ==========================================
// ARCHIVO: DocumentoJugadorService.java
// QUÉ HACE: RF08; valida PDF/JPG y guarda archivo en uploads/documentos.
// FLUJO: DocumentoJugadorController → filesystem + DocumentoJugadorRepository.
// ==========================================
package com.alces.deportes.service;

import com.alces.deportes.dto.DocumentoJugadorResponseDTO;
import com.alces.deportes.exception.BusinessException;
import com.alces.deportes.exception.ResourceNotFoundException;
import com.alces.deportes.model.DocumentoJugador;
import com.alces.deportes.model.Jugador;
import com.alces.deportes.repository.DocumentoJugadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentoJugadorService {

    private final DocumentoJugadorRepository documentoJugadorRepository;
    private final JugadorService jugadorService;

    @Value("${app.upload.dir:uploads/documentos}")
    private String uploadDir;

    public List<DocumentoJugadorResponseDTO> listarPorJugador(Long jugadorId) {
        jugadorService.buscarEntidad(jugadorId);
        return documentoJugadorRepository.findByJugadorId(jugadorId).stream().map(this::mapToDTO).toList();
    }

    // Guarda archivo en disco y deja ruta en tabla documento_jugador
    @Transactional
    public DocumentoJugadorResponseDTO subir(Long jugadorId, MultipartFile archivo) {
        validarArchivo(archivo);
        Jugador jugador = jugadorService.buscarEntidad(jugadorId);

        String extension = obtenerExtension(archivo.getOriginalFilename());
        String nombreGuardado = UUID.randomUUID() + extension;

        Path directorio = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(directorio);
            Path destino = directorio.resolve(nombreGuardado);
            archivo.transferTo(destino.toFile());

            DocumentoJugador documento = new DocumentoJugador(
                    null,
                    jugador,
                    archivo.getOriginalFilename(),
                    destino.toString(),
                    archivo.getContentType() != null ? archivo.getContentType() : "application/octet-stream",
                    LocalDateTime.now());

            return mapToDTO(documentoJugadorRepository.save(documento));
        } catch (IOException ex) {
            throw new BusinessException("No se pudo guardar el archivo: " + ex.getMessage());
        }
    }

    @Transactional
    public void eliminar(Long id) {
        DocumentoJugador documento = documentoJugadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Documento no encontrado con id: " + id));
        try {
            Files.deleteIfExists(Paths.get(documento.getRutaArchivo()));
        } catch (IOException ignored) {
            // Si el archivo ya no existe en disco, igual eliminamos el registro.
        }
        documentoJugadorRepository.delete(documento);
    }

    private void validarArchivo(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new BusinessException("Debe adjuntar un archivo PDF o JPG");
        }
        String nombre = archivo.getOriginalFilename() != null ? archivo.getOriginalFilename().toLowerCase(Locale.ROOT) : "";
        if (!nombre.endsWith(".pdf") && !nombre.endsWith(".jpg") && !nombre.endsWith(".jpeg")) {
            throw new BusinessException("Solo se permiten archivos PDF, JPG o JPEG");
        }
    }

    private String obtenerExtension(String nombre) {
        if (nombre == null || !nombre.contains(".")) {
            return "";
        }
        return nombre.substring(nombre.lastIndexOf('.'));
    }

    private DocumentoJugadorResponseDTO mapToDTO(DocumentoJugador d) {
        String nombreArchivo = Paths.get(d.getRutaArchivo()).getFileName().toString();
        return new DocumentoJugadorResponseDTO(
                d.getId(),
                d.getJugador().getId(),
                d.getJugador().getNombre(),
                d.getNombreOriginal(),
                d.getTipoArchivo(),
                d.getFechaSubida(),
                "/archivos/" + nombreArchivo);
    }
}
