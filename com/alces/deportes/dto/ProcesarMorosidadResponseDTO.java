package com.alces.deportes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProcesarMorosidadResponseDTO {
    private int pagosActualizados;
    private int sociosMarcadosMorosos;
    private String mensaje;
}
