package com.frank.calendar

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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdaySearch(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val datePickerState =
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
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center // 内容上下居中
            ) {
                OutlinedButton(
                    onClick = {
                        showBranchDialog = !showBranchDialog
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .height(56.dp)
                        .padding(8.dp),
                    shape = CircleShape,
                    border = BorderStroke(1.dp, Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "时辰",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = {
                    datePickerState.setSelection(
                        LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(9)) * 1000
                    )
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
        // 左上角按钮
        IconButton(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
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
        CustomTimePickerDialog { index ->
            chooseTime = index * 2
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdaySearchL(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            val datePickerState =
                rememberDatePickerState(
                    initialSelectedDateMillis = theTime.toLocalDate()
                        .atStartOfDay(ZoneOffset.ofHours(0)).toInstant().toEpochMilli()
                )
            getDateInfo(datePickerState.selectedDateMillis, chooseTime)
            Box(
                modifier = Modifier.weight(1.0f)
            ) {
                DatePicker(
                    title = null,
                    headline = null,
                    state = datePickerState,
                    showModeToggle = false,
                )
            }
            Box(
                modifier = Modifier.weight(1.0f),
                contentAlignment = Alignment.Center // 内容上下居中
            ) {
                Column(
                    modifier = Modifier.padding(top = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(), // 使 Row 填满宽度
                        horizontalArrangement = Arrangement.Center // 水平居中
                    ) {
                        Text("生辰八字计算", color = Color.Yellow, fontSize = 32.sp)
                    }
                    Spacer(modifier = Modifier.height(9.dp))
                    Box(
                        modifier = Modifier,
                        contentAlignment = Alignment.Center // 内容上下居中
                    ) {
                        OutlinedButton(
                            onClick = {
                                showBranchDialog = !showBranchDialog
                            },
                            modifier = Modifier
                                .width(200.dp)
                                .height(56.dp)
                                .padding(8.dp),
                            shape = CircleShape,
                            border = BorderStroke(1.dp, Color.White)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Face,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.White
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "时辰",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        IconButton(onClick = {
                            datePickerState.setSelection(
                                LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(9)) * 1000
                            )
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
                            color = Color.Yellow
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
        // 左上角按钮
        IconButton(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
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
        CustomTimePickerDialog { index ->
            chooseTime = index * 2
        }
    }
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
