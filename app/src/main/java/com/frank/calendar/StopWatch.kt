/*The MIT License (MIT)

Copyright (c) 2016 Danylyk Dmytro

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

//https://github.com/amit-bhandari/Stopwatch-Jetpack-Compose?tab=readme-ov-file
//https://github.com/amit-bhandari/Stopwatch-Jetpack-Compose.git
//https://amit-bhandari.github.io/posts/jetpack-compose-custom-view/
*/

package com.frank.calendar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.LifecycleCoroutineScope
import com.frank.calendar.extensions.degreesToRadians
import com.frank.calendar.extensions.isDivisible
import com.frank.calendar.extensions.isDivisible2
import com.frank.calendar.extensions.toRange0To360
import com.frank.calendar.ui.theme.CalendarTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.math.cos
import kotlin.math.sin

enum class Colors(val value: Color) {
    YELLOW(Color("#ffa00c".toColorInt())),
    BLACK(Color("#000000".toColorInt())),
    WHITE(Color("#ffffff".toColorInt())),
    RED(Color("#ff0000".toColorInt())),
    GRAY(Color("#464449".toColorInt())),
}

const val NOTCH_COUNT = 240
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
        modifier = modifier
            .fillMaxSize()
            .background(Colors.BLACK.value)
            .padding(16.dp)
    ) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val circleRadius = if (size.width > size.height) size.height / 2 else size.width / 2

        //notches on boundary of circle
        repeat(NOTCH_COUNT) { notchNumber ->
            val circlePerimeterAngle = 270f + (360f / NOTCH_COUNT) * notchNumber

            val rectLength =
                if (notchNumber.isDivisible(hourInterval))
                    PRIMARY_NOTCH_LENGTH
                else if (notchNumber.isDivisible2(hourInterval))
                    (PRIMARY_NOTCH_LENGTH + SECONDARY_NOTCH_LENGTH) / 2
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
            AnnotatedString(time),
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

        if (iLeftDays > 0) {
            val tr = textMeasure.measure(
                AnnotatedString(iLeftDays.toString()),
                style = TextStyle(
                    fontSize = 24.sp,
                    color = Colors.WHITE.value
                )
            )
            drawText(
                tr,
                topLeft = Offset(
                    centerX - tr.size.width / 2,
                    centerY - circleRadius / 2 //tr.size.height * 2.5f
                )
            )
        }

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
                color = todayColor,
                topLeft = Offset(
                    centerX - ROTATING_HAND_EXTENSION,
                    centerY - ROTATING_HAND_WIDTH / 2
                ),
                size = Size(circleRadius + ROTATING_HAND_EXTENSION, ROTATING_HAND_WIDTH),
            )
        }
        //center stroke circle
        drawCircle(
            color = todayColor,
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

@OptIn(ExperimentalMaterial3Api::class)
fun startTimer(lifecycleScope: LifecycleCoroutineScope) {
    lifecycleScope.launch {
        timerRunning = true
        dayOfMonth0 = LocalDateTime.now().dayOfMonth
        var tmp = 0
        while (timerRunning) {
            hourState =
                LocalDateTime.now().hour * 5 * 1000L + LocalDateTime.now().minute * 5000L / 60 + LocalDateTime.now().second * 5000 / 3600   //小时
            minuteState =
                LocalDateTime.now().minute * 1000L + LocalDateTime.now().second * 1000 / 60    //分钟
            secondState =
                LocalDateTime.now().second * 1000 + LocalDateTime.now().nano / 1_000_000L      //秒
            if (dayOfMonth0 != LocalDateTime.now().dayOfMonth) {
                dayOfMonth0 = LocalDateTime.now().dayOfMonth
                isRedraw = 1 - isRedraw
            }

            if (tmp == 0) {
                time = getCurrentTime()
                leftDate = getNongLiDate()
                trunck_branch = main_branch()
                year_name = jp_year_name()
                getCurrentDate()
                isRed = dateColor
                now = LocalDateTime.now()
                if (now.hour == 7 && now.minute == 0) {
                    defaultColor = Color(0xFF018786)
                    isRedraw = 1 - isRedraw
                }
                if (now.hour == 23 && now.minute == 0) {
                    defaultColor = DarkGray
                    isRedraw = 1 - isRedraw
                }
            }

            tmp++
            if (tmp == 50) {
                tmp = 0
            }

            delay(20)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoubleView(keyValue: String) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        val configuration = LocalConfiguration.current
        val isTablet = remember(configuration) {
            configuration.screenWidthDp >= 600
        }
        Box(
            modifier = Modifier
                .padding(start = if (isTablet) 35.dp else 6.dp)
                .weight(1f)
        ) {
            StopWatch(
                modifier = Modifier,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1.0f),
            contentAlignment = Alignment.Center // 内容上下居中
        ) {
            key(keyValue) {
                CustomCalendar()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoubleView1(keyValue: String) {
    val configuration = LocalConfiguration.current
    val isTablet = remember(configuration) {
        configuration.screenWidthDp >= 600
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = if (isTablet) 50.dp else 2.dp)
            .background(Color.Black)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            StopWatch(modifier = Modifier)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center // 内容上下居中
        ) {
            key(keyValue) {
                CustomCalendar()
            }
        }
    }
}
