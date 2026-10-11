package com.example.rinconsur.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rinconsur.R
import com.example.rinconsur.model.EstadoProbador
import com.example.rinconsur.model.Probador
import com.example.rinconsur.ui.theme.Blanco
import com.example.rinconsur.ui.theme.NaranjaOcupado
import com.example.rinconsur.ui.theme.RojoExceso
import com.example.rinconsur.ui.theme.VerdeDisponible

@Composable
fun ProbadorCard(
    probador: Probador,
    tiempo: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val color by animateColorAsState(
        targetValue = when (probador.estadoVisual()) {
            EstadoProbador.DISPONIBLE -> VerdeDisponible
            EstadoProbador.OCUPADO -> NaranjaOcupado
            EstadoProbador.EXCESO -> RojoExceso
        },
        animationSpec = tween(durationMillis = 500),
        label = "colorProbador"
    )
    val altoMinimo = dimensionResource(id = R.dimen.alto_min_probador)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = altoMinimo)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(size = 14.dp),
        color = color
    ) {
        Column(
            modifier = Modifier.padding(all = 8.dp),
            verticalArrangement = Arrangement.spacedBy(space = 2.dp)
        ) {
            when (probador.estadoVisual()) {
                EstadoProbador.OCUPADO -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_reloj),
                        contentDescription = stringResource(id = R.string.cd_reloj),
                        tint = Blanco,
                        modifier = Modifier.size(size = 16.dp)
                    )
                }
                EstadoProbador.EXCESO -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_alerta),
                        contentDescription = stringResource(id = R.string.cd_alerta),
                        tint = Blanco,
                        modifier = Modifier.size(size = 16.dp)
                    )
                }
                EstadoProbador.DISPONIBLE -> Unit
            }
            Text(
                text = stringResource(id = R.string.probador_nombre, probador.numero),
                color = Blanco,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            when (probador.estadoVisual()) {
                EstadoProbador.DISPONIBLE -> {
                    Text(
                        text = stringResource(id = R.string.disponible),
                        color = Blanco,
                        fontSize = 12.sp
                    )
                }
                EstadoProbador.OCUPADO -> {
                    Text(
                        text = stringResource(id = R.string.tiempo, tiempo),
                        color = Blanco,
                        fontSize = 11.sp
                    )
                    FilaArticulos(prendas = probador.prendas)
                }
                EstadoProbador.EXCESO -> {
                    Text(
                        text = stringResource(id = R.string.exceso_tiempo),
                        color = Blanco,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp
                    )
                    Text(
                        text = stringResource(id = R.string.tiempo, tiempo),
                        color = Blanco,
                        fontSize = 11.sp
                    )
                    FilaArticulos(prendas = probador.prendas)
                }
            }
        }
    }
}

@Composable
private fun FilaArticulos(prendas: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = R.drawable.ic_percha),
            contentDescription = stringResource(id = R.string.cd_articulos),
            tint = Blanco,
            modifier = Modifier.size(size = 14.dp)
        )
        Text(
            text = stringResource(id = R.string.articulos, prendas),
            color = Blanco,
            fontSize = 11.sp,
            modifier = Modifier.padding(start = 2.dp)
        )
    }
}
