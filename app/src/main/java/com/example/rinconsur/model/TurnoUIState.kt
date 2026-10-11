package com.example.rinconsur.model

enum class DialogoTurno {
    ASIGNAR,
    LIBERAR
}

data class TurnoUiState(
    val cargado: Boolean = false,
    val usuario: String = "",
    val seccion: String = "Vestuario Dama 1F",
    val cantidadProbadores: Int = 12,
    val iniciando: Boolean = false,
    val turnoActivo: Boolean = false,
    val probadores: List<Probador> = emptyList(),
    val filtro: FiltroProbadores = FiltroProbadores.TODOS,
    val ahoraMillis: Long = 0L,
    val dialogo: DialogoTurno? = null,
    val probadorDialogo: Int? = null,
    val prendasDialogo: Int = 1,
    val informeEnCurso: Informe = Informe(),
    val informeGuardado: Informe? = null,
    val mensajeSoporte: String? = null,
    val mensajeBreve: String? = null,
    val avisoNotificaciones: String? = null
)
