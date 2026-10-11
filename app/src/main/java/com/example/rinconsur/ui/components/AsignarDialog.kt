package com.example.rinconsur.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.rinconsur.R
import com.example.rinconsur.ui.theme.AzulAccion
import com.example.rinconsur.ui.theme.AzulMarino
import com.example.rinconsur.ui.theme.Blanco
import kotlinx.coroutines.delay

@Composable
fun AsignarDialog(
    visible: Boolean,
    numero: Int,
    prendas: Int,
    onPrendasChange: (Int) -> Unit,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    DialogoAnimado(visible = visible, onDismiss = onCancelar) {
        val radio = dimensionResource(id = R.dimen.radio_dialogo)
        val altoBoton = dimensionResource(id = R.dimen.alto_boton)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            shape = RoundedCornerShape(size = radio),
            colors = CardDefaults.cardColors(containerColor = Blanco),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(all = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(space = 12.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.asignar_titulo, numero),
                    color = AzulMarino,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(id = R.string.numero_prendas),
                    color = AzulMarino,
                    fontSize = 16.sp
                )
                StepperNumero(
                    valor = prendas,
                    onMenos = { onPrendasChange(-1) },
                    onMas = { onPrendasChange(1) }
                )
                Button(
                    onClick = onConfirmar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = altoBoton),
                    shape = RoundedCornerShape(percent = 50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulAccion,
                        contentColor = Blanco
                    )
                ) {
                    Text(text = stringResource(id = R.string.iniciar_uso))
                }
                OutlinedButton(
                    onClick = onCancelar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = altoBoton),
                    shape = RoundedCornerShape(percent = 50)
                ) {
                    Text(text = stringResource(id = R.string.cancelar), color = AzulMarino)
                }
            }
        }
    }
}

@Composable
fun StepperNumero(
    valor: Int,
    onMenos: () -> Unit,
    onMas: () -> Unit,
    colorIcono: Color = AzulMarino,
    colorNumero: Color = AzulMarino
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 18.dp)
    ) {
        Box(
            modifier = Modifier
                .size(size = 40.dp)
                .border(width = 1.5.dp, color = colorIcono, shape = CircleShape)
                .clickable(onClick = onMenos),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.material3.Icon(
                painter = painterResource(id = R.drawable.ic_menos),
                contentDescription = stringResource(id = R.string.cd_menos),
                tint = colorIcono
            )
        }
        Text(
            text = valor.toString(),
            color = colorNumero,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp
        )
        Box(
            modifier = Modifier
                .size(size = 40.dp)
                .border(width = 1.5.dp, color = colorIcono, shape = CircleShape)
                .clickable(onClick = onMas),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.material3.Icon(
                painter = painterResource(id = R.drawable.ic_mas),
                contentDescription = stringResource(id = R.string.cd_mas),
                tint = colorIcono
            )
        }
    }
}

@Composable
fun DialogoAnimado(
    visible: Boolean,
    onDismiss: () -> Unit,
    contenido: @Composable () -> Unit
) {
    var montado by remember { mutableStateOf(value = false) }
    LaunchedEffect(key1 = visible) {
        if (visible) {
            montado = true
        } else if (montado) {
            delay(timeMillis = 180)
            montado = false
        }
    }
    if (montado) {
        Dialog(onDismissRequest = onDismiss) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(durationMillis = 220)) + scaleIn(
                    initialScale = 0.92f,
                    animationSpec = tween(durationMillis = 220)
                ),
                exit = fadeOut(animationSpec = tween(durationMillis = 160)) + scaleOut(
                    targetScale = 0.96f,
                    animationSpec = tween(durationMillis = 160)
                )
            ) {
                contenido()
            }
        }
    }
}
