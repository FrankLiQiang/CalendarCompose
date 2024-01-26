package com.frank.calendar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

@Composable
fun CalendarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
        }
    }

    val myColors = if (darkTheme) {
        myDarkColors
    } else {
        myLightColors
    }

    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )

    CompositionLocalProvider(
        LocalMyColors provides myColors
    ) {
        MaterialTheme(
            colorScheme = DarkColorScheme,
            typography = Typography,
            content = content
        )
//        MaterialTheme(
//            colors = colors,
//            typography = Typography,
//            shapes = Shapes,
//            content = content
//        )
    }
}

//创建CompositionLocal
val LocalMyColors = staticCompositionLocalOf {
    myLightColors
}

object MyTheme {
    val colors: MyColor
        @Composable
        get() = LocalMyColors.current
}

data class MyColor(
    val textPrimary: Color,
    val textSecondary: Color,
    val background: Color
)

//Light Mode
val myLightColors = MyColor(
    textPrimary = Color(0xFF333333),
    textSecondary = Color(0xFF666666),
    background = Color(0xFFFFFFFF)
//    background = Color(0xFF888888)
)

//Dark Mode
val myDarkColors = MyColor(
    textPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(0xCCFFFFFF),
    background = Color(0xFF000000)
//    background = Color(0xFFFFFFFF)
)
