package com.frank.calendar.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.frank.calendar.LunarCalendar
import com.frank.calendar.LunarUtil
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

var isShowSettingDialog by mutableStateOf(false)
var isLunar by mutableStateOf(false)
var isLeap by mutableStateOf(false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShowSettingDialog(
    onDismiss: () -> Unit,
    onNegativeClick: () -> Unit,
) {

    val df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    sTime = df.format(theTime)

    sTB = LunarCalendar.getMainBranch(
        theTime.year, theTime.monthValue, theTime.dayOfMonth, theTime.hour
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .align(alignment = Alignment.BottomEnd),
                verticalAlignment = Alignment.Bottom
            ) {
                val timePickerState = rememberTimePickerState(
                    initialHour = theTime.hour,
                    initialMinute = theTime.minute,
                )
                val datePickerState =
                    rememberDatePickerState(
                        initialDisplayMode = DisplayMode.Picker,
                        initialSelectedDateMillis = System.currentTimeMillis()
                    )
                DatePicker(
                    state = datePickerState,
                    modifier = Modifier
                        .size(350.dp, 400.dp)
                        .weight(2f),
                )
                TimePicker(
                    state = timePickerState,
                    layoutType = TimePickerLayoutType.Vertical,
                    modifier = Modifier.weight(2f),
                )
                Column(
                    modifier = Modifier
                        .weight(1.2f)
                        .fillMaxSize()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Checkbox(
                            checked = isLunar, onCheckedChange = {
                                isLunar = !isLunar
                            }, modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Text(
                            text = "农历",
                            style = MaterialTheme.typography.bodyLarge,
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
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1.0f)
                    ) {}
                    getTB(
                        datePickerState.selectedDateMillis,
                        timePickerState.hour,
                        timePickerState.minute
                    )

                    Text(
                        text = sTB,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1.0f)
                    ) {}
                    Row(
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1.0f)
                        ) {}
                        TextButton(onClick = onNegativeClick) {
                            Text(text = "Close")
                        }
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

var sTime by mutableStateOf("")
var sTB by mutableStateOf("")
var theTime = LocalDateTime.now()

fun getTB(timeStamp: Long?, hour: Int, minute: Int) {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = if (timeStamp == null) 0L else timeStamp
    theTime = LocalDateTime.of(
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH) + 1,
        calendar.get(Calendar.DAY_OF_MONTH),
        hour,
        minute
    )

    val df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
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
            theTime.hour,
            theTime.minute,
            0,
            0
        )
    }
    sTime = df.format(theTime)
    sTB = LunarCalendar.getMainBranch(
        theTime.year,
        theTime.monthValue,
        theTime.dayOfMonth,
        theTime.hour
    )
}

//https://juejin.cn/post/6892794891223760909
//https://github.com/Modificator/compose-navigator
//https://github.com/Modificator/compose-navigator.git