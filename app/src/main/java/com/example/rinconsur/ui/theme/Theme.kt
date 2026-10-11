package com.example.rinconsur.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = AzulAccion,
    onPrimary = Blanco,
    secondary = AzulMarino,
    onSecondary = Crema,
    tertiary = VerdeDisponible,
    background = Crema,
    onBackground = AzulMarino,
    surface = CremaClara,
    onSurface = AzulMarino,
    error = RojoExceso,
    onError = Blanco
)

private val DarkColorScheme = darkColorScheme(
    primary = AzulAccion,
    onPrimary = Blanco,
    secondary = Crema,
    onSecondary = AzulMarino,
    background = AzulMarinoOscuro,
    onBackground = Crema,
    surface = AzulMarino,
    onSurface = Crema,
    error = RojoExceso
)

private val Formas = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun RinconSurTheme(
    // La paleta de la tienda es clara. El color dinámico del sistema la tapa.
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        dynamicColor -> LightColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Formas,
        content = content
    )
}
