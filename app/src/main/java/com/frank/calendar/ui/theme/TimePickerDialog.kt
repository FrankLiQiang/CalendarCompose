package com.frank.calendar.ui.theme

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.frank.calendar.LunarCalendar
import com.frank.calendar.LunarUtil.lunarToSolar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

var isShowSettingDialog by mutableStateOf(false)
var isLunar by mutableStateOf(false)
var isLeap by mutableStateOf(false)
var sTime by mutableStateOf("")
var sTB by mutableStateOf("")
var theTime = LocalDateTime.now()

@Composable
private fun ShowSettingDialog(
    onDismiss: () -> Unit,
    onNegativeClick: () -> Unit,
) {

    val df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    sTime = df.format(theTime)

    sTB = LunarCalendar.getMainBranch(
        theTime.year,
        theTime.monthValue,
        theTime.dayOfMonth,
        theTime.hour
    )

    val current = LocalContext.current
    Dialog(
        onDismissRequest = onDismiss,
//        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
        ) {

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = sTB,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp)) //.background(color = Color.LightGray))
                Text(
                    text = sTime,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(8.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Checkbox(
                        checked = isLunar,
                        onCheckedChange = {
                            isLunar = !isLunar
                        },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        text = "农历",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    if (isLunar) {
                        Checkbox(
                            checked = isLeap,
                            onCheckedChange = {
                                isLeap = !isLeap
                            },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Text(
                            text = "闰年",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                    TextButton(onClick = {
                        val dpd = DatePickerDialog(
                            current,
                            { _, year, month, day ->
                                theTime = LocalDateTime.of(
                                    year,
                                    month + 1,
                                    day,
                                    theTime.hour,
                                    theTime.minute,
                                    0,
                                    0
                                )
                                val df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                                if (isLunar) {
                                    val s = lunarToSolar(
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
                            },
                            theTime.year,
                            theTime.monthValue - 1,
                            theTime.dayOfMonth
                        )
                        dpd.show()
                    }) {
                        Text(text = "请选择日期")
                    }
                }
                TextButton(onClick = {
                    val timePickerDialog = TimePickerDialog(
                        current,
                        { _, hourOfDay, minute ->
                            theTime = LocalDateTime.of(
                                theTime.year,
                                theTime.month,
                                theTime.dayOfMonth,
                                hourOfDay,
                                minute,
                                0,
                                0
                            )
                            val df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                            if (isLunar) {
                                val s = lunarToSolar(
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
                        },
                        theTime.hour,
                        theTime.minute,
                        true
                    )
                    timePickerDialog.show()
                }) {
                    Text(text = "请选择时间")
                }
                Spacer(modifier = Modifier.height(8.dp)) //.background(color = Color.LightGray))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onNegativeClick) {
                        Text(text = "Close")
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
