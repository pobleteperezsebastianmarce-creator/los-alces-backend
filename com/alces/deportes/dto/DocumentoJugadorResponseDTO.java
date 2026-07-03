package com.alces.deportes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DocumentoJugadorResponseDTO {
    private Long id;
    private Long jugadorId;
    private String nombreJugador;
    private String nombreOriginal;
    private String tipoArchivo;
    private LocalDateTime fechaSubida;
    private String urlDescarga;
}
