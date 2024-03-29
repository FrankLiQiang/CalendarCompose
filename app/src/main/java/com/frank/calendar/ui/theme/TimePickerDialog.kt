package com.frank.calendar.ui.theme

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
var theTime = LocalDateTime.now()
var chooseTime by mutableStateOf(theTime.hour)
var sTB by mutableStateOf("")
var sYearName by mutableStateOf("")
var dateInfo by mutableStateOf("")
var oldTimeStamp = 0L
var oldChooseTime = 0

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShowSettingDialog(
    onDismiss: () -> Unit,
    onNegativeClick: () -> Unit,
) {

    Dialog(
        onDismissRequest = onDismiss,
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
                val datePickerState =
                    rememberDatePickerState(
                        initialSelectedDateMillis = theTime.toLocalDate()
                            .atStartOfDay(ZoneOffset.ofHours(0)).toInstant().toEpochMilli()
                    )
                getDateInfo(datePickerState.selectedDateMillis, chooseTime)
                Box() {
                    DatePicker(
                        title = null,
                        state = datePickerState,
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        TextButton(
                            modifier = Modifier
                                .background(Color.Black),
                            onClick = {
                            }) {
                            Text(
                                text = "        ",
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp
                            )
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

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
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sTB,
                        fontSize = 30.sp
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dateInfo,
                        fontSize = 30.sp
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sYearName,
                        fontSize = 30.sp
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${chooseTime} 时",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                }
                Slider(
                    modifier = Modifier.padding(18.dp),
                    value = chooseTime.toFloat(),
                    onValueChange = {
                        chooseTime = it.toInt()
                        getDateInfo(datePickerState.selectedDateMillis, chooseTime)
                    },
                    valueRange = 0f..23f,
                )
                Row(
                    modifier = Modifier.weight(1.0f)
                ) {}
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.DarkGray)
                ) {}
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(modifier = Modifier.weight(1.0f)) {}
                    TextButton(onClick = onNegativeClick) {
                        Text(
                            text = "Close",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SetSettingDialog() {
    if (isShowSettingDialog) {
        ShowSettingDialog(
            onDismiss = {
                closeDialog()
            },
            onNegativeClick = {
                closeDialog()
            },
        )
    }
}

fun closeDialog() {
    isShowSettingDialog = !isShowSettingDialog
}

fun getDateInfo(timeStamp: Long?, cTime: Int) {
    Log.i("AAA", "BBB")
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
