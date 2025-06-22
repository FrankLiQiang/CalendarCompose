package com.frank.calendar.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.frank.calendar.LunarCalendar
import com.frank.calendar.LunarUtil
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

var isShowSettingDialog by mutableStateOf(false)
var isLunar by mutableStateOf(false)
var isLeap by mutableStateOf(false)
var theTime: LocalDateTime = LocalDateTime.now()
var chooseTime by mutableIntStateOf(theTime.hour)
var sTB by mutableStateOf("")
var sYearName by mutableStateOf("")
var dateInfo by mutableStateOf("")
var oldTimeStamp = 0L
var oldChooseTime = 0


//val menuItemData = List(100) { "Option ${it + 1}" }
val menuItemData = arrayOf(
    "子 23:00〜01:00",
    "丑 01:00〜03:00",
    "寅 03:00〜05:00",
    "卯 05:00〜07:00",
    "辰 07:00〜09:00",
    "巳 09:00〜11:00",
    "午 11:00〜13:00",
    "未 13:00〜15:00",
    "申 15:00〜17:00",
    "酉 17:00〜19:00",
    "戌 19:00〜21:00",
    "亥 21:00〜23:00"
)
var expanded by mutableStateOf(false)
//var chosenDate by mutableStateOf(theTime.toLocalDate())
@OptIn(ExperimentalMaterial3Api::class)
lateinit var datePickerState: DatePickerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSettingDialog(
    event: () -> Unit
) {

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                datePickerState =
                    rememberDatePickerState(
                        initialSelectedDateMillis = theTime.toLocalDate()
                            .atStartOfDay(ZoneOffset.ofHours(0)).toInstant().toEpochMilli()
                    )
                getDateInfo(datePickerState.selectedDateMillis, chooseTime)
                Spacer(modifier = Modifier.height(26.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(), // 使 Row 填满宽度
                    horizontalArrangement = Arrangement.Center // 水平居中
                ) {
                    Text("生辰八字计算", color = Color.Yellow, fontSize = 32.sp)
                }
                Spacer(modifier = Modifier.height(26.dp))
                Box() {
                    DatePicker(
                        title = null,
                        state = datePickerState,
                        showModeToggle = false,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(onClick = {
                        datePickerState.setSelection(LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(9)) * 1000)
                    }) {
                        Icon(Icons.Default.DateRange, contentDescription = "More options")
                    }
                    Checkbox(
                        checked = isLunar, onCheckedChange = {
                            isLunar = !isLunar
                        }, modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        text = "农历",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    if (isLunar) {
                        Checkbox(
                            checked = isLeap, onCheckedChange = {
                                isLeap = !isLeap
                            }, modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Text(
                            text = "闰年",
                            color = Color.LightGray,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                    IconButton(onClick = {
                        expanded = !expanded
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "More options")
                    }
                    IconButton(onClick = {
                        event()
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "More options")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        menuItemData.forEachIndexed { index, value ->
                            DropdownMenuItem(
                                text = { Text(value) },
                                onClick = {
                                    chooseTime = index * 2
                                    getDateInfo(datePickerState.selectedDateMillis, chooseTime)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sTB.substring(5),
                        fontSize = 30.sp,
                        color = Color.Yellow,
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sTB.substring(0, 4) + dateInfo,
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sYearName,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

//@Composable
//fun SetSettingDialog() {
//    if (isShowSettingDialog) {
//        ShowSettingDialog(
//            onDismiss = {
//                closeDialog()
//            },
//            onNegativeClick = {
//                closeDialog()
//            },
//        )
//    }
//}

fun closeDialog() {
    isShowSettingDialog = !isShowSettingDialog
}

fun getDateInfo(timeStamp: Long?, cTime: Int) {
    if (oldTimeStamp == timeStamp && oldChooseTime == cTime) {
        return
    }

    if (timeStamp != null) {
        oldTimeStamp = timeStamp
        oldChooseTime = cTime
        theTime = Instant.ofEpochMilli(timeStamp).atZone(ZoneOffset.ofHours(0)).toLocalDateTime()
    }
    if (isLunar) {
        val s = LunarUtil.lunarToSolar(
            theTime.year,
            theTime.monthValue,
            theTime.dayOfMonth,
            isLeap
        )
        theTime = LocalDateTime.of(
            s[0],
            s[1],
            s[2],
            chooseTime,
            0,
            0,
            0
        )

        val formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日")
        dateInfo = theTime.format(formatter)

        val day: Int = theTime.dayOfWeek.value
        val weekString = "一二三四五六日"
        dateInfo += "  星期${weekString.substring(day - 1, day)}"
    } else {
        dateInfo = LunarCalendar.getLunarText(
            theTime.year,
            theTime.monthValue,
            theTime.dayOfMonth,
        )
    }
    sTB = LunarCalendar.getMainBranch(
        theTime.year,
        theTime.monthValue,
        theTime.dayOfMonth,
        chooseTime
    )
    sYearName = LunarCalendar.getYearName(
        theTime.year,
        theTime.monthValue,
        theTime.dayOfMonth,
    )
    sYearName += "  " + LunarCalendar.getSixDay(
        theTime.year,
        theTime.monthValue,
        theTime.dayOfMonth,
    )
}

//https://juejin.cn/post/6892794891223760909
//https://github.com/Modificator/compose-navigator
//https://github.com/Modificator/compose-navigator.git
