package com.example.rinconsur.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rinconsur.R
import com.example.rinconsur.ui.theme.AzulAccion
import com.example.rinconsur.ui.theme.AzulMarino
import com.example.rinconsur.ui.theme.Blanco
import com.example.rinconsur.ui.theme.TextoSuave
import com.example.rinconsur.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginOk: (String) -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val padding = dimensionResource(id = R.dimen.padding_pantalla)
    val altoBoton = dimensionResource(id = R.dimen.alto_boton)
    val logo = dimensionResource(id = R.dimen.logo_lado)
    val espacio = dimensionResource(id = R.dimen.espacio_campos)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .padding(horizontal = padding, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = espacio)
    ) {
        Spacer(modifier = Modifier.height(height = 12.dp))
        Box(
            modifier = Modifier.size(size = logo),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_rincon),
                contentDescription = stringResource(id = R.string.cd_logo),
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .align(alignment = Alignment.Center)
                    .padding(top = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.logo_rincon),
                    color = Blanco,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = stringResource(id = R.string.logo_sur),
                    color = Blanco,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        Text(
            text = stringResource(id = R.string.bienvenido),
            style = MaterialTheme.typography.headlineMedium,
            color = AzulMarino,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(id = R.string.login_subtitulo),
            color = TextoSuave,
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )

        OutlinedTextField(
            value = estado.usuario,
            onValueChange = viewModel::onUsuarioChange,
            label = { Text(text = stringResource(id = R.string.usuario)) },
            placeholder = { Text(text = stringResource(id = R.string.usuario_hint)) },
            leadingIcon = {
                androidx.compose.material3.Icon(
                    painter = painterResource(id = R.drawable.ic_persona),
                    contentDescription = stringResource(id = R.string.cd_usuario),
                    tint = AzulMarino
                )
            },
            singleLine = true,
            isError = estado.errores.usuario != null,
            supportingText = {
                estado.errores.usuario?.let { mensaje ->
                    Text(text = mensaje, color = MaterialTheme.colorScheme.error)
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(size = 10.dp),
            colors = coloresCampo()
        )

        OutlinedTextField(
            value = estado.clave,
            onValueChange = viewModel::onClaveChange,
            label = { Text(text = stringResource(id = R.string.contrasena)) },
            leadingIcon = {
                androidx.compose.material3.Icon(
                    painter = painterResource(id = R.drawable.ic_candado),
                    contentDescription = stringResource(id = R.string.cd_contrasena),
                    tint = AzulMarino
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            isError = estado.errores.clave != null,
            supportingText = {
                estado.errores.clave?.let { mensaje ->
                    Text(text = mensaje, color = MaterialTheme.colorScheme.error)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (viewModel.validarFormulario()) {
                        onLoginOk(estado.usuario.trim())
                    }
                }
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(size = 10.dp),
            colors = coloresCampo()
        )

        Button(
            onClick = {
                if (viewModel.validarFormulario()) {
                    onLoginOk(viewModel.estado.value.usuario.trim())
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(height = altoBoton),
            shape = RoundedCornerShape(percent = 50),
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulAccion,
                contentColor = Blanco
            )
        ) {
            Text(text = stringResource(id = R.string.login))
        }

        TextButton(onClick = viewModel::onOlvidoClave) {
            Text(
                text = stringResource(id = R.string.olvidaste),
                color = AzulAccion
            )
        }
        AnimatedVisibility(visible = estado.mensajeOlvido != null) {
            Text(
                text = estado.mensajeOlvido.orEmpty(),
                color = AzulMarino,
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun coloresCampo() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = AzulAccion,
    unfocusedBorderColor = AzulMarino.copy(alpha = 0.35f),
    focusedLabelColor = AzulMarino,
    unfocusedLabelColor = TextoSuave,
    cursorColor = AzulAccion,
    focusedTextColor = AzulMarino,
    unfocusedTextColor = AzulMarino
)
