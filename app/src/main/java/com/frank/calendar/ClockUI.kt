package com.frank.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.frank.calendar.ui.theme.monthOffset

@Composable
fun ClockUI(event: () -> Unit, modifier: Modifier = Modifier) {

    var textSize by remember("") { mutableStateOf(maxTextSize1) }
    var textSize2 by remember("") { mutableStateOf(maxTextSize2) }
    var textSize3 by remember("") { mutableStateOf(maxTextSize3) }
    Column(Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = time,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (it.hasVisualOverflow && textSize > minTextSize) {
                    textSize = (textSize.value - 1.0F).sp
                } else {
                    maxTextSize1 = textSize
                }
            },
            fontSize = textSize,
            color = textColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(1.2f, true)
                .clickable {
                    textColor = if (textColor == DarkGray) {
                        Color(0xFF018786)
                    } else {
                        DarkGray
                    }
                })
        Text(text = leftDate,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (it.hasVisualOverflow && textSize2 > minTextSize) {
                    textSize2 = (textSize2.value - 1.0F).sp
                } else {
                    maxTextSize2 = textSize2
                }
            },
            fontSize = textSize2,
            color = textColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clickable { event() }
                .weight(0.3f, true))
        Text(text = date,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (it.hasVisualOverflow && textSize3 > minTextSize) {
                    textSize3 = (textSize3.value - 1.0F).sp
                } else {
                    maxTextSize3 = textSize3
                }
            },
            fontSize = textSize3,
            color = if (isRed) Color(0xFFBB86FC) else textColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = modifier
                .weight(0.5f, true)
                .clickable {
                    monthOffset = 0
                    isClock = !isClock
                }
        )
    }
}
