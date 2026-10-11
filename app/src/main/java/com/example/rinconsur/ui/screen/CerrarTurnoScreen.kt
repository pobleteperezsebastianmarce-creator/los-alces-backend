package com.example.rinconsur.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.rinconsur.model.Informe
import com.example.rinconsur.navigation.BarraNavegacion
import com.example.rinconsur.navigation.RUTA_CERRAR
import com.example.rinconsur.ui.theme.AzulAccion
import com.example.rinconsur.ui.theme.AzulMarino
import com.example.rinconsur.ui.theme.Blanco
import com.example.rinconsur.ui.theme.TextoSuave
import com.example.rinconsur.viewmodel.TurnoViewModel

@Composable
fun CerrarTurnoScreen(
    viewModel: TurnoViewModel,
    onVolver: () -> Unit,
    onCerrado: () -> Unit,
    onInicio: () -> Unit,
    onInforme: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val resumen = viewModel.resumenCerrar()
    val padding = dimensionResource(id = R.dimen.padding_pantalla)
    val altoBoton = dimensionResource(id = R.dimen.alto_boton)
    val nota = if (estado.turnoActivo) {
        stringResource(id = R.string.informe_en_curso)
    } else {
        stringResource(id = R.string.informe_guardado)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(weight = 1f)
                .verticalScroll(state = rememberScrollState())
                .padding(all = padding),
            verticalArrangement = Arrangement.spacedBy(space = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.termino_turno),
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
            TarjetaResumen(informe = resumen, nota = nota)
            Button(
                onClick = { viewModel.cerrarTurno(onListo = onCerrado) },
                enabled = estado.turnoActivo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = altoBoton),
                shape = RoundedCornerShape(percent = 50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulAccion,
                    contentColor = Blanco
                )
            ) {
                Text(text = stringResource(id = R.string.cerrar_turno))
            }
            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = altoBoton),
                shape = RoundedCornerShape(percent = 50)
            ) {
                Text(text = stringResource(id = R.string.volver), color = AzulMarino)
            }
        }
        BarraNavegacion(
            seleccionado = RUTA_CERRAR,
            onInicio = onInicio,
            onInforme = onInforme,
            onCerrar = {}
        )
    }
}

@Composable
fun TarjetaResumen(informe: Informe, nota: String) {
    androidx.compose.material3.Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(size = 18.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = androidx.compose.ui.graphics.Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(all = 18.dp),
            verticalArrangement = Arrangement.spacedBy(space = 10.dp)
        ) {
            Text(text = nota, color = TextoSuave)
            FilaResumen(
                etiqueta = stringResource(id = R.string.probadores_usados),
                valor = informe.probadoresUsados.toString()
            )
            FilaResumen(
                etiqueta = stringResource(id = R.string.prendas_totales),
                valor = informe.prendasTotales.toString()
            )
            FilaResumen(
                etiqueta = stringResource(id = R.string.excesos_doce),
                valor = informe.excesosTiempo.toString()
            )
        }
    }
}

@Composable
private fun FilaResumen(etiqueta: String, valor: String) {
    Column {
        Text(text = etiqueta, color = TextoSuave)
        Text(
            text = valor,
            color = AzulMarino,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
