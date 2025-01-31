package com.frank.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.frank.calendar.LunarCalendar.getSolarTerm
import com.frank.calendar.ui.theme.CalendarTheme
import com.frank.calendar.ui.theme.MyTheme
import com.frank.calendar.ui.theme.monthOffset
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

var gongliDate = ""
var nongliDate = ""
var newCurrentDate = -1
var currentDateNum = -2
var dateColor = true
var nongliDateColor = true
var toDate: LocalDateTime = now
var wantDate: LocalDateTime = now

@Composable
fun ClockUI(event: () -> Unit) {

    var textSize by remember("") { mutableStateOf(maxTextSizeTime) }
    var textSize2 by remember("") { mutableStateOf(maxTextSizeLeftDate) }
    var textSize3 by remember("") { mutableStateOf(maxTextSizeGongli) }
    var textSize4 by remember("") { mutableStateOf(maxTextSizeTB) }
    Column(
        modifier = Modifier.background(MyTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = time,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (it.hasVisualOverflow && textSize > minTextSize) {
                    textSize = (textSize.value - 1.0F).sp
                } else {
                    maxTextSizeTime = textSize
                    with(sharedPreferences.edit()) {
                        putFloat("SHARED_PREFS_TIME", maxTextSizeTime.value)
                        commit()
                    }
                }
            },
            fontSize = textSize,
            color = textColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(1.2f, true)
                .background(MyTheme.colors.background)
                .clickable {
                    textColor = if (textColor == DarkGray) {
                        Color(0xFF018786)
                    } else {
                        DarkGray
                    }
                })
        Text(
            text = "$trunck_branch ($year_name)",
            maxLines = 1,
            fontSize = textSize4,
            onTextLayout = {
                if (it.hasVisualOverflow && textSize4 > minTextSize) {
                    textSize4 = (textSize4.value - 1.0F).sp
                } else {
                    maxTextSizeTB = textSize4
                    with(sharedPreferences.edit()) {
                        putFloat("SHARED_PREFS_TB", maxTextSizeTB.value)
                        commit()
                    }
                }
            },
            color = textColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(0.3f, true)
        )
        Text(text = leftDate,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (it.hasVisualOverflow && textSize2 > minTextSize) {
                    textSize2 = (textSize2.value - 1.0F).sp
                } else {
                    maxTextSizeLeftDate = textSize2
                    with(sharedPreferences.edit()) {
                        putFloat("SHARED_PREFS_LEFT", maxTextSizeLeftDate.value)
                        commit()
                    }
                }
            },
            fontSize = textSize2,
            color = if (nongliDateColor) Color.Red else textColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clickable { event() }
                .weight(0.3f, true))
        Text(text = date,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (it.hasVisualOverflow && textSize3 > minTextSize) {
                    textSize3 = (textSize3.value - 1.0F).sp
                } else {
                    maxTextSizeGongli = textSize3
                    with(sharedPreferences.edit()) {
                        putFloat("SHARED_PREFS_WEEK", maxTextSizeGongli.value)
                        commit()
                    }
                }
            },
            fontSize = textSize3,
            color = if (isRed) Color(0xFFBB86FC) else textColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(0.5f, true)
                .clickable {
                    monthOffset = 0
                    isClock = !isClock
                }
        )
    }
}

fun getCurrentTime(): String {
    now = LocalDateTime.now()
    newCurrentDate = now.dayOfMonth
//    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return now.format(formatter)
}

fun getNongLiDate(): String {
    return if (currentDateNum == newCurrentDate) {
        nongliDate
    } else {
        nongliDate = "${nongli()}${leftDays()}"
        nongliDate
    }
}

fun getCurrentDate(): String {
    return if (currentDateNum == newCurrentDate) {
        gongliDate
    } else {
        val wk = nowWeek()
        dayOfMonth = now.dayOfMonth
        dateColor = wk.length > 5
        gongliDate = "${nowDate()}  $wk"
        currentDateNum = newCurrentDate
        isRedraw = 1 - isRedraw
        return gongliDate
    }
}

val nongli: () -> String = {
    "农历 ${
        LunarCalendar.getLunarText(
            now.year,
            now.monthValue,
            now.dayOfMonth
        )
    }  ${LunarCalendar.getSixDay(now.year, now.monthValue, now.dayOfMonth)}"
}

val main_branch: () -> String = {
    LunarCalendar.getMainBranch(now.year, now.monthValue, now.dayOfMonth, now.hour)
}

val jp_year_name: () -> String = {
    LunarCalendar.getYearName(now.year, now.monthValue, now.dayOfMonth)
}

val leftDays: () -> String = {
    val cNow = LocalDateTime.of(now.year, now.monthValue, now.dayOfMonth, 0, 0, 0, 0)
    val leftDays = ChronoUnit.DAYS.between(cNow, toDate)
    if (leftDays > 0) "  还剩 $leftDays 天" else ""
}

val nowDate: () -> String = {
    now = LocalDateTime.now().plusMonths(monthOffset.toLong())
    val formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日")
    now.format(formatter)
}

val nowWeek: () -> String = {
    val termText: String = getSolarTerm(now.year, now.monthValue, now.dayOfMonth)       //节气
    val solar = LunarCalendar.gregorianFestival(now.year, now.monthValue, now.dayOfMonth, now.dayOfWeek.value, termText)
    val day: Int = now.dayOfWeek.value
    val weekString = "一二三四五六日"
    "$solar 星期${weekString.substring(day - 1, day)} $termText"
}

@Preview()
@Composable
fun PreviewMessageListScreenDark() {
    CalendarTheme(darkTheme = false) {
        ClockUI({})
    }
}
