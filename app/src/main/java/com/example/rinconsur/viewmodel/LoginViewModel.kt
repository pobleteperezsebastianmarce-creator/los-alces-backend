package com.example.rinconsur.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.rinconsur.R
import com.example.rinconsur.model.LoginErrores
import com.example.rinconsur.model.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _estado = MutableStateFlow(value = LoginUiState())
    val estado: StateFlow<LoginUiState> = _estado

    fun onUsuarioChange(valor: String) {
        _estado.update {
            it.copy(
                usuario = valor,
                mensajeOlvido = null,
                errores = it.errores.copy(usuario = null)
            )
        }
    }

    fun onClaveChange(valor: String) {
        _estado.update {
            it.copy(
                clave = valor,
                mensajeOlvido = null,
                errores = it.errores.copy(clave = null)
            )
        }
    }

    fun onOlvidoClave() {
        val mensaje = getApplication<Application>().getString(R.string.mensaje_olvido)
        _estado.update { it.copy(mensajeOlvido = mensaje) }
    }

    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value
        val app = getApplication<Application>()
        val usuario = estadoActual.usuario.trim()
        val clave = estadoActual.clave
        val errores = LoginErrores(
            usuario = when {
                usuario.isBlank() -> app.getString(R.string.error_campo_obligatorio)
                usuario != USUARIO_DEMO -> app.getString(R.string.error_credenciales)
                else -> null
            },
            clave = if (clave.isBlank()) app.getString(R.string.error_campo_obligatorio) else null
        )

        val hayErrores = listOfNotNull(
            errores.usuario,
            errores.clave
        ).isNotEmpty()

        _estado.update { it.copy(usuario = usuario, errores = errores) }

        return !hayErrores
    }

    companion object {
        const val USUARIO_DEMO = "empleado.123"
    }
}
