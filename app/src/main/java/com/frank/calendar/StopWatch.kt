package com.frank.calendar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.frank.calendar.extensions.degreesToRadians
import com.frank.calendar.extensions.isDivisible
import com.frank.calendar.extensions.toRange0To360
import com.frank.calendar.ui.theme.CalendarTheme
import kotlin.math.cos
import kotlin.math.sin

enum class Colors(val value: Color) {
    YELLOW(Color("#ffa00c".toColorInt())),
    BLACK(Color("#000000".toColorInt())),
    WHITE(Color("#ffffff".toColorInt())),
    GRAY(Color("#464449".toColorInt())),
}

const val NOTCH_COUNT = 252
const val PRIMARY_NOTCH_LENGTH = 40F
const val SECONDARY_NOTCH_LENGTH = 25F

val PRIMARY_NOTCH_COLOR = Colors.WHITE.value
val SECONDARY_NOTCH_COLOR = Colors.GRAY.value

const val ROTATING_HAND_WIDTH = 14f
const val ROTATING_HAND_EXTENSION = 100f

@Composable
fun StopWatch(
    modifier: Modifier = Modifier,
) {
    val textMeasure = rememberTextMeasurer()
    val hourInterval = NOTCH_COUNT / 12

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.BLACK.value)
            .padding(16.dp)
    ) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val circleRadius = size.width / 2

        //notches on boundary of circle
        repeat(NOTCH_COUNT) { notchNumber ->
            val circlePerimeterAngle = 270f + (360f / NOTCH_COUNT) * notchNumber

            val rectLength =
                if (notchNumber.isDivisible(hourInterval))
                    PRIMARY_NOTCH_LENGTH
                else
                    SECONDARY_NOTCH_LENGTH

            val rectColor =
                if (notchNumber.isDivisible(hourInterval))
                    PRIMARY_NOTCH_COLOR
                else
                    SECONDARY_NOTCH_COLOR

            rotate(
                circlePerimeterAngle + 90f,
                pivot = Offset(
                    centerX + circleRadius * cos(circlePerimeterAngle.degreesToRadians()),
                    centerY + circleRadius * sin(circlePerimeterAngle.degreesToRadians())
                )
            ) {
                drawRect(
                    color = rectColor,
                    topLeft = Offset(
                        centerX + circleRadius * cos(circlePerimeterAngle.degreesToRadians()),
                        centerY + circleRadius * sin(circlePerimeterAngle.degreesToRadians())
                    ),
                    size = Size(ROTATING_HAND_WIDTH / 2f, rectLength),
                )
            }

            if (notchNumber.isDivisible(hourInterval)) {
                val p1 =
                    centerX + 0.8f * circleRadius * cos(circlePerimeterAngle.degreesToRadians())
                val p2 =
                    centerY + 0.8f * circleRadius * sin(circlePerimeterAngle.degreesToRadians())

                val s = if (notchNumber == 0) "12"
//                else (notchNumber.div(hourInterval).times(1)).toString()
                else notchNumber.div(hourInterval).toString()
                val tr = textMeasure.measure(
                    AnnotatedString(s),
                    style = TextStyle(
                        fontSize = 28.sp,
                        color = Colors.WHITE.value
                    )
                )
                drawText(
                    tr,
                    topLeft = Offset(
                        p1 - tr.size.width / 2,//centerX - tr.size.width / 2,
                        p2 - tr.size.height / 2,//centerY - tr.size.height / 2
                    )
                )
            }
        }

        //timer text
        val tr = textMeasure.measure(
            AnnotatedString("Li Qiang"),
            style = TextStyle(
                fontSize = 24.sp,
                color = Colors.WHITE.value
            )
        )
        drawText(
            tr,
            topLeft = Offset(
                centerX - tr.size.width / 2,
                centerY - tr.size.height / 2 + 200f
            )
        )

        //big rotating hand
        rotate(
            hourState.toRange0To360() + 270f,
            pivot = Offset(
                centerX,
                centerY
            )
        ) {
            drawRect(
                color = Colors.WHITE.value,
                topLeft = Offset(
                    centerX,
                    centerY - ROTATING_HAND_WIDTH
                ),
                size = Size(circleRadius * 3 / 5, ROTATING_HAND_WIDTH * 2),
            )
        }

        rotate(
            minuteState.toRange0To360() + 270f,
            pivot = Offset(
                centerX,
                centerY
            )
        ) {
            drawRect(
                color = Colors.WHITE.value,
                topLeft = Offset(
                    centerX,
                    centerY - ROTATING_HAND_WIDTH / 2
                ),
                size = Size(circleRadius * 4 / 5, ROTATING_HAND_WIDTH),
            )
        }
        rotate(
            secondState.toRange0To360() + 270f,
            pivot = Offset(
                centerX,
                centerY
            )
        ) {
            drawRect(
                color = Colors.YELLOW.value,
                topLeft = Offset(
                    centerX - ROTATING_HAND_EXTENSION,
                    centerY - ROTATING_HAND_WIDTH / 2
                ),
                size = Size(circleRadius + ROTATING_HAND_EXTENSION, ROTATING_HAND_WIDTH),
            )
        }
        //center stroke circle
        drawCircle(
            color = Colors.YELLOW.value,
            radius = 14f,
            center = Offset(centerX, centerY),
            style = Stroke(width = ROTATING_HAND_WIDTH)
        )
        drawCircle(
            Colors.BLACK.value,
            radius = 10f,
            center = Offset(centerX, centerY)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CalendarTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            StopWatch(
                modifier = Modifier
                    .padding(innerPadding)
            )
        }
    }
}