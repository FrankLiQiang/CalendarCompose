package com.frank.calendar

import android.R.attr.contentDescription
import android.app.DatePickerDialog
import android.content.DialogInterface
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.frank.calendar.ui.theme.HorizontalPagerSample
import com.frank.calendar.ui.theme.firstOffset
import com.frank.calendar.ui.theme.jumpToPage
import com.frank.calendar.ui.theme.monthOffset
import com.google.accompanist.pager.ExperimentalPagerApi
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PerpetualCalendar(navController: NavHostController) {
    val c = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize(), //contentAlignment = Alignment.TopEnd
    ) {
        HorizontalPagerSample(false, navController)

        // 左上角按钮
        IconButton(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(80.dp)
                .padding(10.dp)
                .background(
                    color = defaultColor.copy(alpha = 0.4f), // 半透明黑色背景
                    CircleShape
                )
                .shadow(4.dp, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "关闭",
                tint = Color.White
            )
        }

        IconButton(
            onClick = {
                val dpd = DatePickerDialog(
                    c, { _, year, month, day ->
                        wantDate = LocalDateTime.of(year, month + 1, 1, 0, 0, 0, 0)
                        val currentDay = LocalDateTime.of(
                            LocalDateTime.now().year, LocalDateTime.now().month, 1, 0, 0, 0, 0
                        )
                        monthOffset = ChronoUnit.MONTHS.between(currentDay, wantDate).toInt()
                        jumpToPage(monthOffset + firstOffset)
                        isRedraw = 1 - isRedraw
                    }, wantDate.year, wantDate.monthValue - 1, wantDate.dayOfMonth
                ).apply {
                    setButton(DialogInterface.BUTTON_NEUTRAL, "回到今天") { _, _ ->
                        monthOffset = 0
                        jumpToPage(firstOffset)
                        isRedraw = 1 - isRedraw
                    }
                }
                dpd.show()
            },
            modifier = Modifier
                .size(80.dp)
                .padding(10.dp)
                .align(Alignment.TopEnd)
                .background(
                    color = defaultColor.copy(alpha = 0.4f), // 半透明黑色背景
                    shape = CircleShape
                )
                .shadow(4.dp, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowForward,
                contentDescription = contentDescription.toString(),
                tint = defaultColor
            )
        }
    }
}

