package com.example.rinconsur.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rinconsur.R
import com.example.rinconsur.ui.screen.CerrarTurnoScreen
import com.example.rinconsur.ui.screen.ConfigurarTurnoScreen
import com.example.rinconsur.ui.screen.InformeScreen
import com.example.rinconsur.ui.screen.LoginScreen
import com.example.rinconsur.ui.screen.ProbadoresScreen
import com.example.rinconsur.ui.theme.AzulAccion
import com.example.rinconsur.ui.theme.AzulMarino
import com.example.rinconsur.ui.theme.Blanco
import com.example.rinconsur.ui.theme.Crema
import com.example.rinconsur.viewmodel.LoginViewModel
import com.example.rinconsur.viewmodel.TurnoViewModel

const val RUTA_LOGIN = "login"
const val RUTA_CONFIGURAR = "configurar"
const val RUTA_PROBADORES = "probadores"
const val RUTA_CERRAR = "cerrar"
const val RUTA_INFORME = "informe"

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel()
    val turnoViewModel: TurnoViewModel = viewModel()
    val turno by turnoViewModel.estado.collectAsState()

    if (!turno.cargado) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = AzulAccion)
        }
    } else {
        val inicio = when {
            turno.turnoActivo && turno.usuario.isNotBlank() -> RUTA_PROBADORES
            turno.usuario.isNotBlank() -> RUTA_CONFIGURAR
            else -> RUTA_LOGIN
        }
        NavHost(
            navController = navController,
            startDestination = inicio
        ) {
            composable(route = RUTA_LOGIN) {
                LoginScreen(
                    viewModel = loginViewModel,
                    onLoginOk = { usuario ->
                        turnoViewModel.registrarSesion(usuario = usuario)
                        navController.navigate(route = RUTA_CONFIGURAR) {
                            popUpTo(route = RUTA_LOGIN) { inclusive = true }
                        }
                    }
                )
            }
            composable(route = RUTA_CONFIGURAR) {
                ConfigurarTurnoScreen(
                    viewModel = turnoViewModel,
                    onTurnoIniciado = {
                        navController.navigate(route = RUTA_PROBADORES) {
                            popUpTo(route = RUTA_PROBADORES) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(route = RUTA_PROBADORES) {
                ProbadoresScreen(
                    viewModel = turnoViewModel,
                    onInforme = { navController.irSeccion(ruta = RUTA_INFORME) },
                    onCerrar = { navController.irSeccion(ruta = RUTA_CERRAR) },
                    onNuevoTurno = { navController.navigate(route = RUTA_CONFIGURAR) }
                )
            }
            composable(route = RUTA_CERRAR) {
                CerrarTurnoScreen(
                    viewModel = turnoViewModel,
                    onVolver = { navController.popBackStack() },
                    onCerrado = { navController.irSeccion(ruta = RUTA_INFORME) },
                    onInicio = { navController.irSeccion(ruta = RUTA_PROBADORES) },
                    onInforme = { navController.irSeccion(ruta = RUTA_INFORME) }
                )
            }
            composable(route = RUTA_INFORME) {
                InformeScreen(
                    viewModel = turnoViewModel,
                    onInicio = { navController.irSeccion(ruta = RUTA_PROBADORES) },
                    onCerrar = { navController.irSeccion(ruta = RUTA_CERRAR) }
                )
            }
        }
    }
}

private fun NavHostController.irSeccion(ruta: String) {
    navigate(route = ruta) {
        popUpTo(route = RUTA_PROBADORES) { inclusive = false }
        launchSingleTop = true
    }
}

@Composable
fun BarraNavegacion(
    seleccionado: String,
    onInicio: () -> Unit,
    onInforme: () -> Unit,
    onCerrar: () -> Unit
) {
    NavigationBar(containerColor = Crema) {
        NavigationBarItem(
            selected = seleccionado == RUTA_PROBADORES,
            onClick = onInicio,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_inicio),
                    contentDescription = stringResource(id = R.string.cd_inicio),
                    modifier = Modifier.size(size = 24.dp),
                    tint = if (seleccionado == RUTA_PROBADORES) Blanco else AzulMarino
                )
            },
            colors = coloresBarra()
        )
        NavigationBarItem(
            selected = seleccionado == RUTA_INFORME,
            onClick = onInforme,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendario),
                    contentDescription = stringResource(id = R.string.cd_calendario),
                    modifier = Modifier.size(size = 24.dp),
                    tint = if (seleccionado == RUTA_INFORME) Blanco else AzulMarino
                )
            },
            colors = coloresBarra()
        )
        NavigationBarItem(
            selected = seleccionado == RUTA_CERRAR,
            onClick = onCerrar,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cuenta),
                    contentDescription = stringResource(id = R.string.cd_cerrar),
                    modifier = Modifier.size(size = 24.dp),
                    tint = if (seleccionado == RUTA_CERRAR) Blanco else AzulMarino
                )
            },
            colors = coloresBarra()
        )
    }
}

@Composable
private fun coloresBarra() = NavigationBarItemDefaults.colors(
    selectedIconColor = Blanco,
    unselectedIconColor = AzulMarino,
    indicatorColor = AzulMarino
)
