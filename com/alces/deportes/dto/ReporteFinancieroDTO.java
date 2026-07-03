package com.alces.deportes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReporteFinancieroDTO {
    private Double totalIngresos;
    private Double totalEgresos;
    private Double balance;
    private long pagosPendientes;
    private long pagosMorosos;
    private long sociosMorosos;
}
