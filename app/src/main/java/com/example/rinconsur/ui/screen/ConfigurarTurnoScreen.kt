package com.example.rinconsur.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rinconsur.R
import com.example.rinconsur.ui.components.StepperNumero
import com.example.rinconsur.ui.theme.AzulMarino
import com.example.rinconsur.ui.theme.Blanco
import com.example.rinconsur.ui.theme.Crema
import com.example.rinconsur.ui.theme.TextoSuave
import com.example.rinconsur.viewmodel.TurnoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigurarTurnoScreen(
    viewModel: TurnoViewModel,
    onTurnoIniciado: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val padding = dimensionResource(id = R.dimen.padding_pantalla)
    val altoBoton = dimensionResource(id = R.dimen.alto_boton)
    var menuAbierto by remember { mutableStateOf(value = false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .padding(horizontal = padding, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.bienvenido_de_nuevo, estado.usuario),
            style = MaterialTheme.typography.titleLarge,
            color = AzulMarino,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 12.dp, bottom = 20.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(size = 22.dp),
            colors = CardDefaults.cardColors(containerColor = AzulMarino)
        ) {
            Column(
                modifier = Modifier.padding(all = dimensionResource(id = R.dimen.padding_card)),
                verticalArrangement = Arrangement.spacedBy(space = 12.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.configuracion_turno),
                    color = Blanco,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(id = R.string.seccion_asignada),
                    color = Blanco.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
                ExposedDropdownMenuBox(
                    expanded = menuAbierto,
                    onExpandedChange = { menuAbierto = it }
                ) {
                    OutlinedTextField(
                        value = estado.seccion,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuAbierto)
                        },
                        modifier = Modifier
                            .menuAnchor(
                                type = MenuAnchorType.PrimaryNotEditable,
                                enabled = true
                            )
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(size = 10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Crema,
                            unfocusedContainerColor = Crema,
                            focusedTextColor = AzulMarino,
                            unfocusedTextColor = AzulMarino,
                            focusedBorderColor = Crema,
                            unfocusedBorderColor = Crema,
                            focusedTrailingIconColor = AzulMarino,
                            unfocusedTrailingIconColor = AzulMarino
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = menuAbierto,
                        onDismissRequest = { menuAbierto = false }
                    ) {
                        viewModel.secciones.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(text = opcion) },
                                onClick = {
                                    viewModel.onSeccionChange(valor = opcion)
                                    menuAbierto = false
                                }
                            )
                        }
                    }
                }
                Text(
                    text = stringResource(id = R.string.probadores_activos),
                    color = Blanco.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(size = 10.dp),
                    color = Crema
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        StepperNumero(
                            valor = estado.cantidadProbadores,
                            onMenos = { viewModel.onCantidadChange(delta = -1) },
                            onMas = { viewModel.onCantidadChange(delta = 1) },
                            colorIcono = AzulMarino,
                            colorNumero = AzulMarino
                        )
                    }
                }
                Button(
                    onClick = { viewModel.iniciarTurno(onListo = onTurnoIniciado) },
                    enabled = !estado.iniciando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = altoBoton),
                    shape = RoundedCornerShape(percent = 50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Crema,
                        contentColor = AzulMarino,
                        disabledContainerColor = Crema.copy(alpha = 0.7f),
                        disabledContentColor = AzulMarino
                    )
                ) {
                    if (estado.iniciando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(size = 22.dp),
                            strokeWidth = 2.dp,
                            color = AzulMarino
                        )
                    } else {
                        Text(text = stringResource(id = R.string.iniciar_turno))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(height = 28.dp))
        TextButton(onClick = viewModel::onSoporte) {
            Text(text = stringResource(id = R.string.soporte), color = TextoSuave)
        }
        AnimatedVisibility(visible = estado.mensajeSoporte != null) {
            Text(
                text = estado.mensajeSoporte.orEmpty(),
                color = AzulMarino,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Text(
            text = stringResource(id = R.string.tienda),
            color = TextoSuave,
            fontSize = 13.sp
        )
    }
}
