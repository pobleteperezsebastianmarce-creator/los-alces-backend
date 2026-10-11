package com.example.rinconsur.model

data class LoginUiState(
    val usuario: String = "",
    val clave: String = "",
    val errores: LoginErrores = LoginErrores(),
    val mensajeOlvido: String? = null
)
