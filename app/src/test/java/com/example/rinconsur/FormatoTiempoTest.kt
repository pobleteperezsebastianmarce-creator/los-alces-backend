package com.example.rinconsur

import com.example.rinconsur.model.FiltroProbadores
import com.example.rinconsur.model.LIMITE_USO_MILLIS
import com.example.rinconsur.model.Probador
import com.example.rinconsur.model.aplicarFiltro
import com.example.rinconsur.model.formatoTiempo
import org.junit.Assert.assertEquals
import org.junit.Test

class FormatoTiempoTest {

    @Test
    fun formato_minutosYSegundos() {
        assertEquals("7:15", formatoTiempo(millis = 7 * 60 * 1000L + 15 * 1000L))
        assertEquals("15:03", formatoTiempo(millis = 15 * 60 * 1000L + 3 * 1000L))
        assertEquals("0:00", formatoTiempo(millis = 0L))
    }

    @Test
    fun limite_sonDoceMinutos() {
        assertEquals(12 * 60 * 1000L, LIMITE_USO_MILLIS)
    }

    @Test
    fun filtro_separaDisponiblesOcupadosYAlertas() {
        val rooms = listOf(
            Probador(numero = 1),
            Probador(numero = 2, ocupado = true, prendas = 3),
            Probador(numero = 3, ocupado = true, enExceso = true, prendas = 5)
        )
        assertEquals(listOf(1), rooms.aplicarFiltro(FiltroProbadores.DISPONIBLES).map { it.numero })
        assertEquals(listOf(2, 3), rooms.aplicarFiltro(FiltroProbadores.OCUPADOS).map { it.numero })
        assertEquals(listOf(3), rooms.aplicarFiltro(FiltroProbadores.ALERTAS).map { it.numero })
    }
}
