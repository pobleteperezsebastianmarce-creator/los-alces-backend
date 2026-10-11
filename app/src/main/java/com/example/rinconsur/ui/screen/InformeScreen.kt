package com.example.rinconsur.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.rinconsur.R
import com.example.rinconsur.navigation.BarraNavegacion
import com.example.rinconsur.navigation.RUTA_INFORME
import com.example.rinconsur.ui.theme.AzulMarino
import com.example.rinconsur.ui.theme.TextoSuave
import com.example.rinconsur.viewmodel.TurnoViewModel

@Composable
fun InformeScreen(
    viewModel: TurnoViewModel,
    onInicio: () -> Unit,
    onCerrar: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val padding = dimensionResource(id = R.dimen.padding_pantalla)
    val guardado = estado.informeGuardado

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(weight = 1f)
                .verticalScroll(state = rememberScrollState())
                .padding(all = padding),
            verticalArrangement = Arrangement.spacedBy(space = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.informe_titulo),
                style = MaterialTheme.typography.headlineMedium,
                color = AzulMarino,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(id = R.string.seccion_actual, estado.seccion),
                color = TextoSuave,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            if (guardado == null) {
                Text(
                    text = stringResource(id = R.string.informe_vacio),
                    color = AzulMarino,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                TarjetaResumen(
                    informe = guardado,
                    nota = stringResource(id = R.string.informe_guardado)
                )
            }
        }
        BarraNavegacion(
            seleccionado = RUTA_INFORME,
            onInicio = onInicio,
            onInforme = {},
            onCerrar = onCerrar
        )
    }
}
