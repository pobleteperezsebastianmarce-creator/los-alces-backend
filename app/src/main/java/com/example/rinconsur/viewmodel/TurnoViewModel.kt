package com.example.rinconsur.viewmodel

import android.Manifest
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rinconsur.R
import com.example.rinconsur.data.RinconDataStore
import com.example.rinconsur.model.DialogoTurno
import com.example.rinconsur.model.FiltroProbadores
import com.example.rinconsur.model.Informe
import com.example.rinconsur.model.LIMITE_USO_MILLIS
import com.example.rinconsur.model.MAX_PRENDAS
import com.example.rinconsur.model.MAX_PROBADORES
import com.example.rinconsur.model.MIN_PRENDAS
import com.example.rinconsur.model.MIN_PROBADORES
import com.example.rinconsur.model.Probador
import com.example.rinconsur.model.TurnoUiState
import com.example.rinconsur.model.formatoTiempo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TurnoViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = RinconDataStore(context = application)

    private val _estado = MutableStateFlow(
        value = TurnoUiState(ahoraMillis = SystemClock.elapsedRealtime())
    )
    val estado: StateFlow<TurnoUiState> = _estado

    val secciones = listOf(
        "Vestuario Dama 1F",
        "Vestuario Dama 2F",
        "Vestuario Hombre",
        "Calzado",
        "Accesorios"
    )

    init {
        asegurarCanal()
        cargarSesion()
        viewModelScope.launch {
            while (isActive) {
                delay(timeMillis = 1000)
                tick()
            }
        }
    }

    private fun cargarSesion() {
        viewModelScope.launch {
            val sesion = dataStore.observar().first()
            val informe = if (sesion.informeGuardado) {
                Informe(
                    probadoresUsados = sesion.probadoresUsados,
                    prendasTotales = sesion.prendasTotales,
                    excesosTiempo = sesion.excesosTiempo
                )
            } else {
                null
            }
            _estado.update {
                it.copy(
                    cargado = true,
                    usuario = sesion.usuario,
                    seccion = sesion.seccion.ifBlank { secciones.first() },
                    cantidadProbadores = sesion.cantidadProbadores.coerceIn(MIN_PROBADORES, MAX_PROBADORES),
                    turnoActivo = sesion.turnoActivo && sesion.usuario.isNotBlank(),
                    probadores = if (sesion.turnoActivo && sesion.usuario.isNotBlank()) {
                        crearProbadores(cantidad = sesion.cantidadProbadores)
                    } else {
                        emptyList()
                    },
                    informeGuardado = informe,
                    ahoraMillis = SystemClock.elapsedRealtime()
                )
            }
        }
    }

    fun registrarSesion(usuario: String) {
        _estado.update { it.copy(usuario = usuario.trim()) }
        viewModelScope.launch {
            dataStore.guardarUsuario(usuario = usuario.trim())
        }
    }

    fun onSeccionChange(valor: String) {
        _estado.update { it.copy(seccion = valor) }
    }

    fun onCantidadChange(delta: Int) {
        _estado.update {
            val nueva = (it.cantidadProbadores + delta).coerceIn(MIN_PROBADORES, MAX_PROBADORES)
            it.copy(cantidadProbadores = nueva)
        }
    }

    fun onSoporte() {
        val mensaje = texto(id = R.string.mensaje_soporte)
        _estado.update { it.copy(mensajeSoporte = mensaje) }
    }

    fun iniciarTurno(onListo: () -> Unit) {
        if (_estado.value.iniciando) return
        _estado.update { it.copy(iniciando = true, mensajeSoporte = null) }
        viewModelScope.launch {
            delay(timeMillis = 1200)
            val actual = _estado.value
            val cantidad = actual.cantidadProbadores.coerceIn(MIN_PROBADORES, MAX_PROBADORES)
            dataStore.guardarTurno(
                seccion = actual.seccion,
                cantidad = cantidad,
                activo = true
            )
            _estado.update {
                it.copy(
                    iniciando = false,
                    turnoActivo = true,
                    cantidadProbadores = cantidad,
                    probadores = crearProbadores(cantidad = cantidad),
                    informeEnCurso = Informe(),
                    filtro = FiltroProbadores.TODOS,
                    dialogo = null,
                    ahoraMillis = SystemClock.elapsedRealtime()
                )
            }
            onListo()
        }
    }

    fun onFiltro(filtro: FiltroProbadores) {
        _estado.update { it.copy(filtro = filtro) }
    }

    fun onProbadorClick(numero: Int) {
        val probador = _estado.value.probadores.firstOrNull { it.numero == numero } ?: return
        if (!probador.ocupado) {
            _estado.update {
                it.copy(
                    dialogo = DialogoTurno.ASIGNAR,
                    probadorDialogo = numero,
                    prendasDialogo = MIN_PRENDAS
                )
            }
        } else {
            _estado.update {
                it.copy(
                    dialogo = DialogoTurno.LIBERAR,
                    probadorDialogo = numero
                )
            }
        }
    }

    fun onPrendasDialogoChange(delta: Int) {
        _estado.update {
            val nueva = (it.prendasDialogo + delta).coerceIn(MIN_PRENDAS, MAX_PRENDAS)
            it.copy(prendasDialogo = nueva)
        }
    }

    fun confirmarAsignacion() {
        val numero = _estado.value.probadorDialogo ?: return
        val prendas = _estado.value.prendasDialogo.coerceIn(MIN_PRENDAS, MAX_PRENDAS)
        val ahora = SystemClock.elapsedRealtime()
        _estado.update { actual ->
            val anterior = actual.probadores.firstOrNull { it.numero == numero } ?: return@update actual
            val rooms = actual.probadores.map { probador ->
                if (probador.numero == numero) {
                    probador.copy(
                        ocupado = true,
                        enExceso = false,
                        prendas = prendas,
                        inicioUsoMillis = ahora,
                        alertaEnviada = false,
                        usadoEnTurno = true
                    )
                } else {
                    probador
                }
            }
            val sumaUsados = if (anterior.usadoEnTurno) 0 else 1
            actual.copy(
                probadores = rooms,
                dialogo = null,
                probadorDialogo = null,
                ahoraMillis = ahora,
                informeEnCurso = actual.informeEnCurso.copy(
                    probadoresUsados = actual.informeEnCurso.probadoresUsados + sumaUsados
                )
            )
        }
    }

    fun confirmarLiberacion() {
        val numero = _estado.value.probadorDialogo ?: return
        val app = getApplication<Application>()
        val manager = ContextCompat.getSystemService(app, NotificationManager::class.java)
        manager?.cancel(numero)
        _estado.update { actual ->
            val anterior = actual.probadores.firstOrNull { it.numero == numero } ?: return@update actual
            val rooms = actual.probadores.map { probador ->
                if (probador.numero == numero) {
                    probador.copy(
                        ocupado = false,
                        enExceso = false,
                        prendas = 0,
                        inicioUsoMillis = null,
                        alertaEnviada = false
                    )
                } else {
                    probador
                }
            }
            actual.copy(
                probadores = rooms,
                dialogo = null,
                probadorDialogo = null,
                informeEnCurso = actual.informeEnCurso.copy(
                    prendasTotales = actual.informeEnCurso.prendasTotales + anterior.prendas
                )
            )
        }
    }

    fun cancelarDialogo() {
        _estado.update { it.copy(dialogo = null, probadorDialogo = null) }
    }

    fun onCampana() {
        val alertas = _estado.value.probadores.count { it.enExceso }
        val mensaje = if (alertas == 0) {
            texto(id = R.string.sin_alertas)
        } else {
            texto(id = R.string.hay_alertas, arg = alertas)
        }
        mostrarMensaje(mensaje)
    }

    fun onAvatar() {
        val usuario = _estado.value.usuario.ifBlank { "—" }
        mostrarMensaje(texto(id = R.string.sesion_de, arg = usuario))
    }

    fun onPermisoNotificaciones(concedido: Boolean) {
        _estado.update {
            it.copy(
                avisoNotificaciones = if (concedido) {
                    null
                } else {
                    texto(id = R.string.aviso_sin_notificaciones)
                }
            )
        }
    }

    fun resumenCerrar(): Informe {
        val actual = _estado.value
        if (!actual.turnoActivo && actual.informeGuardado != null) {
            return actual.informeGuardado
        }
        return calcularEnCurso(estado = actual)
    }

    fun cerrarTurno(onListo: () -> Unit) {
        if (!_estado.value.turnoActivo) {
            onListo()
            return
        }
        val resumen = calcularEnCurso()
        _estado.update {
            it.copy(
                turnoActivo = false,
                informeGuardado = resumen,
                informeEnCurso = Informe(),
                dialogo = null
            )
        }
        viewModelScope.launch {
            dataStore.guardarInforme(
                usados = resumen.probadoresUsados,
                prendas = resumen.prendasTotales,
                excesos = resumen.excesosTiempo,
                turnoActivo = false
            )
            onListo()
        }
    }

    private fun tick() {
        val ahora = SystemClock.elapsedRealtime()
        val actual = _estado.value
        if (!actual.turnoActivo) {
            _estado.update { it.copy(ahoraMillis = ahora) }
            return
        }
        var excesosNuevos = 0
        val rooms = actual.probadores.map { probador ->
            val inicio = probador.inicioUsoMillis
            if (probador.ocupado && inicio != null && !probador.alertaEnviada) {
                val transcurrido = ahora - inicio
                if (transcurrido >= LIMITE_USO_MILLIS) {
                    excesosNuevos += 1
                    notificarExceso(numero = probador.numero, transcurrido = transcurrido)
                    vibrar()
                    probador.copy(enExceso = true, alertaEnviada = true)
                } else {
                    probador
                }
            } else {
                probador
            }
        }
        _estado.update {
            it.copy(
                ahoraMillis = ahora,
                probadores = rooms,
                informeEnCurso = it.informeEnCurso.copy(
                    excesosTiempo = it.informeEnCurso.excesosTiempo + excesosNuevos
                )
            )
        }
    }

    private fun calcularEnCurso(estado: TurnoUiState = _estado.value): Informe {
        val prendasEnUso = estado.probadores.filter { it.ocupado }.sumOf { it.prendas }
        return estado.informeEnCurso.copy(
            prendasTotales = estado.informeEnCurso.prendasTotales + prendasEnUso
        )
    }

    private fun crearProbadores(cantidad: Int): List<Probador> {
        val total = cantidad.coerceIn(MIN_PROBADORES, MAX_PROBADORES)
        return (1..total).map { numero -> Probador(numero = numero) }
    }

    private fun mostrarMensaje(mensaje: String) {
        _estado.update { it.copy(mensajeBreve = mensaje) }
        viewModelScope.launch {
            delay(timeMillis = 2500)
            _estado.update { if (it.mensajeBreve == mensaje) it.copy(mensajeBreve = null) else it }
        }
    }

    private fun notificarExceso(numero: Int, transcurrido: Long) {
        val app = getApplication<Application>()
        if (!puedeNotificar(context = app)) {
            _estado.update {
                it.copy(avisoNotificaciones = texto(id = R.string.aviso_sin_notificaciones))
            }
            return
        }
        asegurarCanal()
        val titulo = texto(id = R.string.alerta_titulo)
        val exceso = texto(id = R.string.alerta_exceso, arg = numero)
        val cuerpo = texto(id = R.string.alerta_cuerpo_tiempo, arg = formatoTiempo(millis = transcurrido))
        val abrir = app.packageManager.getLaunchIntentForPackage(app.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pending = abrir?.let { intent ->
            PendingIntent.getActivity(
                app,
                numero,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
        val notificacion = NotificationCompat.Builder(app, CANAL_ALERTAS)
            .setSmallIcon(R.drawable.ic_stat_alerta)
            .setContentTitle(titulo)
            .setContentText(exceso)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .setBigContentTitle(titulo)
                    .bigText("$exceso\n$cuerpo")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pending)
            .setColor(ContextCompat.getColor(app, R.color.rojo_exceso))
            .build()
        val manager = ContextCompat.getSystemService(app, NotificationManager::class.java)
        try {
            manager?.notify(numero, notificacion)
        } catch (_: SecurityException) {
            _estado.update {
                it.copy(avisoNotificaciones = texto(id = R.string.aviso_sin_notificaciones))
            }
        }
    }

    private fun vibrar() {
        val app = getApplication<Application>()
        val vibrador = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            app.getSystemService(VibratorManager::class.java)?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            app.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        } ?: return
        if (!vibrador.hasVibrator()) return
        val patron = longArrayOf(0, 400, 180, 400, 180, 600)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrador.vibrate(VibrationEffect.createWaveform(patron, -1))
        } else {
            @Suppress("DEPRECATION")
            vibrador.vibrate(patron, -1)
        }
    }

    private fun asegurarCanal() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val app = getApplication<Application>()
        val manager = app.getSystemService(NotificationManager::class.java) ?: return
        if (manager.getNotificationChannel(CANAL_ALERTAS) != null) return
        val canal = NotificationChannel(
            CANAL_ALERTAS,
            texto(id = R.string.canal_alertas),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = texto(id = R.string.canal_alertas_desc)
            lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
            enableVibration(false)
            setShowBadge(true)
        }
        manager.createNotificationChannel(canal)
    }

    private fun puedeNotificar(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun texto(id: Int, arg: Any? = null): String {
        val app = getApplication<Application>()
        return if (arg == null) app.getString(id) else app.getString(id, arg)
    }

    companion object {
        const val CANAL_ALERTAS = "alertas_probador"
    }
}
