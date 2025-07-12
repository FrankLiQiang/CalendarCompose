package com.frank.calendar


import android.R.attr.contentDescription
import android.app.DatePickerDialog
import android.content.DialogInterface
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PerpetualCalendar(navController: NavHostController) {
    val c = LocalContext.current
    Box(
        modifier = Modifier
            .background(color = Color.Black)
            .fillMaxSize(),
    ) {
        ClockAndCalendar(false, navController)

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
//                .background(
//                    color = defaultColor.copy(alpha = 0.4f), // 半透明黑色背景
//                    shape = CircleShape
//                )
//                .shadow(4.dp, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowForward,
                contentDescription = contentDescription.toString(),
                tint = defaultColor
            )
        }
    }
}

@Composable
fun TowToolsV(navController: NavHostController) {
    val c = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedButton(
                onClick = {
                    navController.navigate("calendar") {}
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(86.dp)
                    .padding(8.dp),
                shape = CircleShape,
                border = BorderStroke(1.dp, defaultColor)
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = defaultColor
                )
                Spacer(Modifier.width(8.dp))
                Text("万年历", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = defaultColor)
            }

            OutlinedButton(
                onClick = {
                    val dpd = DatePickerDialog(
                        c, { _, year, month, day ->
                            toDate = LocalDateTime.of(year, month + 1, day, 0, 0, 0, 0)
                            now = LocalDateTime.now()
                            val cNow = LocalDateTime.of(
                                now.year,
                                now.monthValue,
                                now.dayOfMonth,
                                0,
                                0,
                                0,
                                0
                            )
                            iLeftDays = ChronoUnit.DAYS.between(cNow, toDate)
                            leftDays = if (iLeftDays > 0) "  还剩 $iLeftDays 天" else ""
                            saveTimePerSet()
                        },
                        toDate.year, toDate.monthValue - 1, toDate.dayOfMonth
                    ).apply {
                        setButton(DialogInterface.BUTTON_NEUTRAL, "取消设置") { _, _ ->
                            toDate = LocalDateTime.now().minusDays(2)
                            leftDays = ""
                            iLeftDays = 0
                            saveTimePerSet()
                        }
                    }
                    dpd.show()
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(86.dp)
                    .padding(8.dp),
                shape = CircleShape,
                border = BorderStroke(1.dp, defaultColor)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = defaultColor
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "重要日期设置",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = defaultColor
                )
            }

            OutlinedButton(
                onClick = {
                    navController.navigate("birthday") {}
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(86.dp)
                    .padding(8.dp),
                shape = CircleShape,
                border = BorderStroke(1.dp, defaultColor)
            ) {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = defaultColor
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "生辰八字查询",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = defaultColor
                )
            }
        }
    }
}

@Composable
fun TowToolsL(navController: NavHostController) {
    val c = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedButton(
            onClick = {
                navController.navigate("calendar") {}
            },
            modifier = Modifier
                .width(240.dp)
                .height(86.dp)
                .padding(8.dp),
            shape = CircleShape,
            border = BorderStroke(1.dp, defaultColor)
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = defaultColor
            )
            Spacer(Modifier.width(8.dp))
            Text("万年历", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = defaultColor)
        }

        OutlinedButton(
            onClick = {
                val dpd = DatePickerDialog(
                    c, { _, year, month, day ->
                        toDate = LocalDateTime.of(year, month + 1, day, 0, 0, 0, 0)
                        saveTimePerSet()
                    },
                    toDate.year, toDate.monthValue - 1, toDate.dayOfMonth
                ).apply {
                    setButton(DialogInterface.BUTTON_NEUTRAL, "取消设置") { _, _ ->
                        toDate = LocalDateTime.now().minusDays(2)
                        saveTimePerSet()
                    }
                }
                dpd.show()
            },
            modifier = Modifier
                .width(240.dp)
                .height(86.dp)
                .padding(8.dp),
            shape = CircleShape,
            border = BorderStroke(1.dp, defaultColor)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = defaultColor
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "重要日期设置",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = defaultColor
            )
        }

        OutlinedButton(
            onClick = {
                navController.navigate("birthday") {}
            },
            modifier = Modifier
                .width(240.dp)
                .height(86.dp)
                .padding(8.dp),
            shape = CircleShape,
            border = BorderStroke(1.dp, defaultColor)
        ) {
            Icon(
                imageVector = Icons.Default.Face,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = defaultColor
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "生辰八字查询",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = defaultColor
            )
        }
    }
}


@Composable
fun TowTools(navController: NavHostController) {
    if (isPort) {
        TowToolsV(navController)
    } else {
        TowToolsL(navController)
    }

}
