package com.frank.calendar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

var isShowSettingDialog by mutableStateOf(false)
var theTime: LocalDateTime = LocalDateTime.now()
var sTB by mutableStateOf("")
var isLunar by mutableStateOf(false)
var isLeap by mutableStateOf(false)
var sYearName by mutableStateOf("")
var dateInfo by mutableStateOf("")
var oldTimeStamp = 0L
var oldChooseTime = 0


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdaySearch(navController: NavHostController) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp    // 屏幕宽度（dp）
    val screenHeight = configuration.screenHeightDp  // 屏幕高度（dp）

    Log.i("aaaa", "screenWidth = " + screenWidth)
    Log.i("aaaa", "screenHeight = " + screenHeight)

    val datePickerState =
        rememberDatePickerState(
            initialSelectedDateMillis = theTime.toLocalDate()
                .atStartOfDay(ZoneOffset.ofHours(0)).toInstant().toEpochMilli()
        )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        getDateInfo(datePickerState.selectedDateMillis)
        Column(
            modifier = Modifier
                .height(if (isPort) screenHeight.dp / 2 else screenHeight.dp)
                .width(screenWidth.dp)
                .align(if (isPort) Alignment.TopCenter else Alignment.BottomEnd)
                .padding(
                    top = if (isPort) 50.dp else 20.dp,
                    start = if (isPort) 0.dp else screenWidth.dp / 2,
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OldTime(
                modifier = Modifier
                    .weight(1.0f)
                    .padding(bottom = 50.dp),
                { index ->
                    chooseTime = index * 2
                }
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.height(50.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = sTB,
                    fontSize = 30.sp,
                    color = Color.Yellow
                )
            }
        }
        Box(
            modifier = Modifier
                .height(screenHeight.dp)
                .width(if (isPort) screenWidth.dp else screenWidth.dp / 2)
                .padding(
                    top = if (isPort) screenHeight.dp / 2 + 20.dp else 0.dp,
                ),
        ) {
            DatePicker(
                title = null,
                headline = null,
                state = datePickerState,
                showModeToggle = false,
            )
        }
        // 左上角按钮
        IconButton(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.TopEnd)
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
    }
}


fun getDateInfo(timeStamp: Long?) {
//    if (oldTimeStamp == timeStamp && oldChooseTime == cTime) {
//        return
//    }
//
    if (timeStamp != null) {
//        oldTimeStamp = timeStamp
//        oldChooseTime = cTime
        theTime = Instant.ofEpochMilli(timeStamp).atZone(ZoneOffset.ofHours(0)).toLocalDateTime()
    }
//    if (isLunar) {
//        val s = LunarUtil.lunarToSolar(
//            theTime.year,
//            theTime.monthValue,
//            theTime.dayOfMonth,
//            isLeap
//        )
//        theTime = LocalDateTime.of(
//            s[0],
//            s[1],
//            s[2],
//            chooseTime,
//            0,
//            0,
//            0
//        )
//
//        val formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日")
//        dateInfo = theTime.format(formatter)
//
//        val day: Int = theTime.dayOfWeek.value
//        val weekString = "一二三四五六日"
//        dateInfo += "  星期${weekString.substring(day - 1, day)}"
//    } else {
//        dateInfo = LunarCalendar.getLunarText(
//            theTime.year,
//            theTime.monthValue,
//            theTime.dayOfMonth,
//        )
//    }
    sTB = LunarCalendar.getMainBranch(
        theTime.year,
        theTime.monthValue,
        theTime.dayOfMonth,
        chooseTime
    )
//    sYearName = LunarCalendar.getYearName(
//        theTime.year,
//        theTime.monthValue,
//        theTime.dayOfMonth,
//    )
//    sYearName += "  " + LunarCalendar.getSixDay(
//        theTime.year,
//        theTime.monthValue,
//        theTime.dayOfMonth,
//    )
}

//https://juejin.cn/post/6892794891223760909
//https://github.com/Modificator/compose-navigator
//https://github.com/Modificator/compose-navigator.git
