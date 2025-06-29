package com.frank.calendar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frank.calendar.LunarCalendar.Branch
import com.frank.calendar.extensions.degreesToRadians
import com.frank.calendar.extensions.isDivisible
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun CustomTimePickerDialog(event: (Int) -> Unit) {
    var selectedHour by remember { mutableStateOf(0) }
    if (showBranchDialog) {
        selectedHour = 0
        oldTime = "子时"
        AlertDialog(
            modifier = Modifier
                .width(440.dp)
                .height(440.dp),
            onDismissRequest = { showBranchDialog = false },
            title = {
                Text(text = oldTime)
            },
            text = { OldTime(modifier = Modifier, {index->
                selectedHour = index
            }
            ) },
            confirmButton = {
                TextButton(onClick = {
                    showBranchDialog = false
                    event(selectedHour)
                }) {
                    Text(text = "确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBranchDialog = false }) {
                    Text(text = "取消")
                }
            }
        )
    }
}

data class Point(val x: Float, val y: Float)

fun findIntersectionWithCircle(
    cx: Float, cy: Float, r: Float, // 圆心 (cx, cy) 和半径 r
    x1: Float, y1: Float            // 另一个点 (x1, y1)
): Point {
    // 计算方向向量
    val dx = x1 - cx
    val dy = y1 - cy

    // 计算方向向量的长度
    val length = sqrt(dx * dx + dy * dy)

    // 计算交点坐标
    val ix = cx + r * (dx / length)
    val iy = cy + r * (dy / length)

    return Point(ix, iy)
}

fun calculateAngle(cx: Float, cy: Float, ix: Float, iy: Float): Float {
    // 使用 atan2 计算角度（弧度转角度）
    var angle = Math.toDegrees(atan2((iy - cy).toDouble(), (ix - cx).toDouble())).toFloat()

    // 调整角度到 0 到 360 范围
    if (angle < 0) {
        angle += 360
    }
    angle -= (270 - 15)
    if (angle < 0) {
        angle += 360
    }

    return angle
}

@Composable
fun OldTime(
    modifier: Modifier = Modifier,
    event0: (Int) -> Unit
) {
    val textMeasure = rememberTextMeasurer()
    val hourInterval = NOTCH_COUNT / 12

    // 用于存储触摸点的位置
    var touchPoint by remember { mutableStateOf<Offset?>(null) }
    var isFirstDraw = true
    event0(0)

    Canvas(
        modifier = Modifier
            .fillMaxSize()
//            .background(Colors.BLACK.value)
            .padding(16.dp)
            .pointerInput(Unit) {
                // 监听触摸事件
                awaitPointerEventScope {
                    while (true) {
                        // 等待手势事件
                        val event = awaitPointerEvent()
                        // 获取触摸点的位置
                        val position = event.changes.first().position
                        touchPoint = position
                    }
                }
            }) {

        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val circleRadius = size.width * 5 / 12

        //notches on boundary of circle
        repeat(NOTCH_COUNT) { notchNumber ->
            val circlePerimeterAngle = 270f + (360f / NOTCH_COUNT) * (notchNumber + 10)

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
        }
        if (isFirstDraw) {
            isFirstDraw = false
            val x = findIntersectionWithCircle(centerX, centerY, 1.2f * circleRadius, centerX, centerY - circleRadius)
            drawCircle(
                color = defaultColor,
                radius = 50f,
                center = Offset(x.x,x.y)
            )
            drawLine(
                color = defaultColor,
                start = Offset(centerX, centerY),
                end = Offset(x.x, x.y),
                strokeWidth = 10f,
            )
        }
        // 在触摸点绘制一个圆
        touchPoint?.let {
            val x = findIntersectionWithCircle(centerX, centerY, 1.2f * circleRadius, it.x, it.y)
            drawCircle(
                color = defaultColor,
                radius = 50f,
                center = Offset(x.x,x.y)
            )
            drawLine(
                color = defaultColor,
                start = Offset(centerX, centerY),
                end = Offset(x.x, x.y),
                strokeWidth = 10f,
            )
            val tmp = calculateAngle(centerX, centerY, x.x, x.y).toInt() / 30
            event0(tmp)
            oldTime = Branch?.get((tmp + 1) % 12) ?: ""
            oldTime += "时"
        }
        //notches on boundary of circle
        repeat(NOTCH_COUNT) { notchNumber ->
            val circlePerimeterAngle = 270f + (360f / NOTCH_COUNT) * (notchNumber + 10)

            if ((notchNumber + 10).isDivisible(hourInterval)) {
                val p1 =
                    centerX + 0.7f * circleRadius * cos(circlePerimeterAngle.degreesToRadians())
                val p2 =
                    centerY + 0.7f * circleRadius * sin(circlePerimeterAngle.degreesToRadians())

                val s = Branch?.get(((notchNumber + 10).div(hourInterval) + 1) % 12) ?: ""
                val tr = textMeasure.measure(
                    AnnotatedString(s),
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = if (s == oldTime.substring(0, 1)) Colors.RED.value else Colors.WHITE.value
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
            if ((notchNumber + 10).isDivisible(hourInterval / 2)) {
                val p1 =
                    centerX + 1.2f * circleRadius * cos(circlePerimeterAngle.degreesToRadians())
                val p2 =
                    centerY + 1.2f * circleRadius * sin(circlePerimeterAngle.degreesToRadians())

                val s = (notchNumber + 10).div(hourInterval / 2).toString()
                val tr = textMeasure.measure(
                    AnnotatedString(s),
                    style = TextStyle(
                        fontSize = 18.sp,
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
    }
}

