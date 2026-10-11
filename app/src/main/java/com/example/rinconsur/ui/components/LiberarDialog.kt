package com.example.rinconsur.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rinconsur.R
import com.example.rinconsur.ui.theme.AzulAccion
import com.example.rinconsur.ui.theme.AzulMarino
import com.example.rinconsur.ui.theme.Blanco

@Composable
fun LiberarDialog(
    visible: Boolean,
    numero: Int,
    prendas: Int,
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
                    text = stringResource(id = R.string.liberar_titulo, numero),
                    color = AzulMarino,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(id = R.string.prendas_confirmar),
                    color = AzulMarino,
                    fontSize = 16.sp
                )
                Text(
                    text = prendas.toString(),
                    color = AzulMarino,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp
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
                    Text(text = stringResource(id = R.string.confirmar_liberacion))
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
