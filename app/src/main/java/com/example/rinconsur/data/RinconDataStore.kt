package com.example.rinconsur.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.rinconDataStore by preferencesDataStore(name = "rincon_sur")

data class SesionGuardada(
    val usuario: String = "",
    val seccion: String = "Vestuario Dama 1F",
    val cantidadProbadores: Int = 12,
    val turnoActivo: Boolean = false,
    val informeGuardado: Boolean = false,
    val probadoresUsados: Int = 0,
    val prendasTotales: Int = 0,
    val excesosTiempo: Int = 0
)

class RinconDataStore(private val context: Context) {

    private val usuarioKey = stringPreferencesKey(name = "usuario")
    private val seccionKey = stringPreferencesKey(name = "seccion")
    private val cantidadKey = intPreferencesKey(name = "cantidad_probadores")
    private val turnoActivoKey = booleanPreferencesKey(name = "turno_activo")
    private val informeGuardadoKey = booleanPreferencesKey(name = "informe_guardado")
    private val usadosKey = intPreferencesKey(name = "informe_usados")
    private val prendasKey = intPreferencesKey(name = "informe_prendas")
    private val excesosKey = intPreferencesKey(name = "informe_excesos")

    fun observar(): Flow<SesionGuardada> {
        return context.rinconDataStore.data.map { preferencias ->
            SesionGuardada(
                usuario = preferencias[usuarioKey].orEmpty(),
                seccion = preferencias[seccionKey] ?: "Vestuario Dama 1F",
                cantidadProbadores = preferencias[cantidadKey] ?: 12,
                turnoActivo = preferencias[turnoActivoKey] ?: false,
                informeGuardado = preferencias[informeGuardadoKey] ?: false,
                probadoresUsados = preferencias[usadosKey] ?: 0,
                prendasTotales = preferencias[prendasKey] ?: 0,
                excesosTiempo = preferencias[excesosKey] ?: 0
            )
        }
    }

    suspend fun guardarUsuario(usuario: String) {
        context.rinconDataStore.edit { preferencias ->
            preferencias[usuarioKey] = usuario
        }
    }

    suspend fun guardarTurno(seccion: String, cantidad: Int, activo: Boolean) {
        context.rinconDataStore.edit { preferencias ->
            preferencias[seccionKey] = seccion
            preferencias[cantidadKey] = cantidad
            preferencias[turnoActivoKey] = activo
        }
    }

    suspend fun guardarInforme(
        usados: Int,
        prendas: Int,
        excesos: Int,
        turnoActivo: Boolean
    ) {
        context.rinconDataStore.edit { preferencias ->
            preferencias[usadosKey] = usados
            preferencias[prendasKey] = prendas
            preferencias[excesosKey] = excesos
            preferencias[informeGuardadoKey] = true
            preferencias[turnoActivoKey] = turnoActivo
        }
    }
}
