package com.frank.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frank.calendar.LunarCalendar.getSolarTerm
import com.frank.calendar.ui.theme.CalendarTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

fun getNongLi(dayNum: Int, weekDay: Int): String {
    return LunarCalendar.getLunarText2(now.year, now.monthValue, dayNum, weekDay)
}

fun getSixDay(dayNum: Int): String {
    return LunarCalendar.getSixDay(now.year, now.monthValue, dayNum)
}

fun getWeeksOfMonth(): Int {
    val lengthOfMonth = now.toLocalDate().lengthOfMonth()
    firstDay = now.toLocalDate().with(TemporalAdjusters.firstDayOfMonth())
    firstDayOfWeek = firstDay.dayOfWeek.value % 7
    val ret = lengthOfMonth - (7 - firstDayOfWeek)
    for (i in 0 until firstDayOfWeek) {
        dateArray[i] = -1
        nongliArray[i] = ""
        sixDaysArray[i] = ""
        tbDaysArray[i] = ""
    }
    var d = firstDayOfWeek
    for (i in 1..lengthOfMonth) {
        dateArray[d] = i
        nongliArray[d] = getNongLi(i, d % 7)
        sixDaysArray[d] = getSixDay(i)
        tbDaysArray[d] = mainBranchDay(i)
        d++
    }
    for (i in d until 42) {
        dateArray[i] = -1
        nongliArray[i] = ""
        sixDaysArray[i] = ""
        tbDaysArray[i] = ""
    }
    return ret / 7 + if (ret % 7 == 0) 1 else 2
}

@Composable
fun CalendarView(isToday: Boolean) {
    if (isRedraw > 100) return
    var textSize by remember("") { mutableStateOf(if (isPort) maxTextSizeTitle_PORTRAIT else maxTextSizeTitle_LANDSCAPE) }
    Column(
        Modifier.padding(start = 10.dp, bottom = 10.dp, end = if (isPort) 10.dp else 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier.weight(if (isPort) 0.7f else 1.2f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isToday) if (isPort) nowDate() else nowDate() + "  " + nowWeek() + if (monthOffset == 0) "  $time" else ""
                else nowDate().substring(0, 8),
                maxLines = 1,
                fontSize = textSize,
                onTextLayout = {
                    if (it.hasVisualOverflow && textSize > minTextSize) {
                        textSize = (textSize.value - 1.0F).sp
                    } else {
                        if (isPort) {
                            maxTextSizeTitle_PORTRAIT = textSize
                            with(sharedPreferences.edit()) {
                                putFloat(
                                    "SHARED_PREFS_CALENDAR_TITLE_P", maxTextSizeTitle_PORTRAIT.value
                                )
                                commit()
                            }
                        } else {
                            maxTextSizeTitle_LANDSCAPE = textSize
                            with(sharedPreferences.edit()) {
                                putFloat(
                                    "SHARED_PREFS_CALENDAR_TITLE_L",
                                    maxTextSizeTitle_LANDSCAPE.value
                                )
                                commit()
                            }
                        }
                    }
                },
                color = defaultColor,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(6.dp)
            )
        }
        if (isPort && monthOffset == 0 && isToday) {
            Text(
                text = time,
                maxLines = 1,
                fontSize = textSize,
                color = defaultColor,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(0.5f)
            )
        }
        if (!isPort) {
            isShowSettingDialog = false
        }
        weeksMonth = getWeeksOfMonth()
//        Row(Modifier.height(if (isPort) 15.dp else 27.dp)) {}
        var d = 0
        for (i in -1 until weeksMonth) {
            if (i == -1) {
                Row(Modifier.weight(0.4f), verticalAlignment = Alignment.Bottom) {
                    for (j in 0..6) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.BottomCenter// .Center
                        ) {
                            Text(text = arr[j], color = defaultColor)
                        }
                    }
                }
            } else {
                Row(
                    Modifier
                        .weight(1.0f)
                        .padding(start = 4.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (j in 0..6) {
                        Date(
                            isToday = isToday,
                            Modifier.weight(1.0f),
                            dateArray[d],
                            nongliArray[d],
                            sixDaysArray[d],
                            tbDaysArray[d]
                        )
                        d++
                    }
                }
            }
        }
//            Row(Modifier.height(if (isPort) 22.dp else 0.dp)) {}
    }
}

@Preview(
    showBackground = true,
    widthDp = 2000,
    heightDp = 900,
)
@Composable
fun CalendarPreview() {
    CalendarTheme {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            CalendarView(true)
        }
    }
}

@Composable
fun Date(
    isToday: Boolean,
    modifier: Modifier,
    dateVal: Int,
    nongLi0: String,
    sixDays: String,
    tb_day: String
) {
    if (isRedraw > 100) return
    var nongLi = nongLi0
    var textSize1 by remember("") { mutableStateOf(if (isPort) maxTextSizeCalendarDate_PORTRAIT else maxTextSizeCalendarDate_LANDSCAPE) }
    var sixTextSizePort by remember("") { mutableStateOf(maxTextSizeCalendarSix_PORTRAIT) }
    var sixTextSize by remember("") { mutableStateOf(maxTextSizeCalendarSix_LANDSCAPE) }
    val todayColor = Color.White
    var theColor1 = defaultColor
    var theColor2 = defaultColor
    if (nongLi.startsWith("*")) {
        nongLi = nongLi.substring(1)
        theColor2 = Color.Red
    }
    if (nongLi.startsWith("%")) {
        nongLi = nongLi.substring(1)
        theColor1 = Color.Red
    }
    if (nongLi.startsWith("@")) {
        nongLi = nongLi.substring(1)
        theColor2 = jieqiColor
    }
    fun getBuildAnnotatedString(): AnnotatedString {
        return if (isPort) buildAnnotatedString {
            withStyle(style = SpanStyle(color = if (theColor1 == Color.Red) theColor1 else theColor2)) { // 设置第一部分颜色
                append(nongLi.subSequence(0, 2).toString() + "\n")
            }
            withStyle(style = SpanStyle(color = defaultColor)) {
                append(tb_day + "\n" + sixDays)
            }
        }
        else {
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = if (theColor1 == Color.Red) theColor1 else theColor2)) {
                    append(nongLi.substring(0, 1))
                }
                withStyle(style = SpanStyle(color = defaultColor)) {
                    append(tb_day.substring(0, 1) + sixDays.substring(0, 1) + "\n")
                }
                withStyle(style = SpanStyle(color = if (theColor1 == Color.Red) theColor1 else theColor2)) {
                    append(nongLi.substring(1, 2))
                }
                withStyle(style = SpanStyle(color = defaultColor)) {
                    append(tb_day.substring(1, 2) + sixDays.substring(1, 2))
                }
            }
        }
    }


    Box(modifier = modifier) {
        Row(
            Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically
        ) {
            @Composable
            fun showSix() {
                Box(
                    modifier = Modifier
                        .weight(if (isPort) 1.5f else 1.0f)
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (dateVal != -1) {
                        Text(
                            getBuildAnnotatedString(),
                            fontSize = if (isPort) sixTextSizePort else sixTextSize,
                            maxLines = if (isPort) 3 else 2,
                            lineHeight = ((if (isPort) sixTextSizePort else sixTextSize).value + 2).sp,
                            textAlign = TextAlign.Center,
                            onTextLayout = {
                                if (isPort) {
                                    if (it.hasVisualOverflow && sixTextSizePort > minTextSize) {
                                        sixTextSizePort = (sixTextSizePort.value - 1.0F).sp
                                    } else {
                                        maxTextSizeCalendarSix_PORTRAIT = sixTextSizePort
                                        with(sharedPreferences.edit()) {
                                            putFloat(
                                                "SHARED_PREFS_SIX_P",
                                                maxTextSizeCalendarSix_PORTRAIT.value + 3.0f
                                            )
                                            commit()
                                        }
                                    }
                                } else {
                                    if (it.hasVisualOverflow && sixTextSize > minTextSize) {
                                        sixTextSize = (sixTextSize.value - 1.0F).sp
                                    } else {
                                        maxTextSizeCalendarSix_LANDSCAPE = sixTextSize
                                        with(sharedPreferences.edit()) {
                                            putFloat(
                                                "SHARED_PREFS_SIX_L",
                                                maxTextSizeCalendarSix_LANDSCAPE.value + 3.0f
                                            )
                                            commit()
                                        }
                                    }
                                }
                            },
                        )
                    }
                }
            }

            @Composable
            fun showNum() {
                Text(
                    text = "$dateVal",
                    maxLines = 1,
                    onTextLayout = {
                        if (it.hasVisualOverflow && textSize1 > minTextSize) {
                            textSize1 = (textSize1.value - 1.0F).sp
                        } else {
                            if (isPort) {
                                maxTextSizeCalendarDate_PORTRAIT = textSize1
                                with(sharedPreferences.edit()) {
                                    putFloat(
                                        "SHARED_PREFS_GONG_LI_P",
                                        maxTextSizeCalendarDate_PORTRAIT.value
                                    )
                                    commit()
                                }
                            } else {
                                maxTextSizeCalendarDate_LANDSCAPE = textSize1
                                with(sharedPreferences.edit()) {
                                    putFloat(
                                        "SHARED_PREFS_GONG_LI_L",
                                        maxTextSizeCalendarDate_LANDSCAPE.value
                                    )
                                    commit()
                                }
                            }
                        }
                    },
                    fontSize = textSize1,
                    color = if (dayOfMonth == dateVal && isToday) todayColor else defaultColor,
                    textAlign = if (isPort) TextAlign.Center else TextAlign.Right,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1.0f, true)
                        .padding(2.dp)//.align(alignment = Alignment.Center)// .Bottom)///Modifier.fillMaxSize()
                )
            }

            if (isPort) {
                Column(
                    Modifier.weight(2.0f),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (dateVal != -1) {
                        showNum()
                        showSix()
                    }
                }
            } else {
                Row(Modifier.weight(2.0f), verticalAlignment = Alignment.CenterVertically) {
                    if (dateVal != -1) {
                        showNum()
                        Spacer(modifier = Modifier.width(5.dp))
                        showSix()
                    }
                }
            }
        }
    }
}

fun mainBranchDay(day: Int): String {
    return LunarCalendar.getMainBranchDay(now.year, now.monthValue, day)
}

val nowDate: () -> String = {
    now = LocalDateTime.now().plusMonths(monthOffset.toLong())
    val formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日")
    now.format(formatter)
}

val nowWeek: () -> String = {
    val termText0: String = getSolarTerm(now.year, now.monthValue, now.dayOfMonth)       //节气
    val solar = LunarCalendar.gregorianFestival(
        now.year,
        now.monthValue,
        now.dayOfMonth,
        now.dayOfWeek.value,
        termText0
    )
    val day: Int = now.dayOfWeek.value
    val weekString = "一二三四五六日"
    "$solar 星期${weekString.substring(day - 1, day)} $termText"
}


