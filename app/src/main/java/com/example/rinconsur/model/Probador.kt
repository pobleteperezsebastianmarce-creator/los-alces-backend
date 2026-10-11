package com.example.rinconsur.model

enum class FiltroProbadores {
    TODOS,
    DISPONIBLES,
    OCUPADOS,
    ALERTAS
}

enum class EstadoProbador {
    DISPONIBLE,
    OCUPADO,
    EXCESO
}

data class Probador(
    val numero: Int,
    val ocupado: Boolean = false,
    val enExceso: Boolean = false,
    val prendas: Int = 0,
    val inicioUsoMillis: Long? = null,
    val alertaEnviada: Boolean = false,
    val usadoEnTurno: Boolean = false
) {
    fun estadoVisual(): EstadoProbador = when {
        !ocupado -> EstadoProbador.DISPONIBLE
        enExceso -> EstadoProbador.EXCESO
        else -> EstadoProbador.OCUPADO
    }

    fun millisTranscurridos(ahora: Long): Long {
        val inicio = inicioUsoMillis ?: return 0L
        return (ahora - inicio).coerceAtLeast(0L)
    }
}

const val LIMITE_USO_MILLIS = 12 * 60 * 1000L
const val MIN_PROBADORES = 1
const val MAX_PROBADORES = 24
const val MIN_PRENDAS = 1
const val MAX_PRENDAS = 30

fun formatoTiempo(millis: Long): String {
    val totalSegundos = (millis / 1000L).coerceAtLeast(0L)
    val minutos = totalSegundos / 60L
    val segundos = totalSegundos % 60L
    return "%d:%02d".format(minutos, segundos)
}

fun List<Probador>.aplicarFiltro(filtro: FiltroProbadores): List<Probador> = when (filtro) {
    FiltroProbadores.TODOS -> this
    FiltroProbadores.DISPONIBLES -> filter { probador -> !probador.ocupado }
    FiltroProbadores.OCUPADOS -> filter { probador -> probador.ocupado }
    FiltroProbadores.ALERTAS -> filter { probador -> probador.enExceso }
}
