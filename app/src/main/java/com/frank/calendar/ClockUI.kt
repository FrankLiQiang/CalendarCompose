package com.frank.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import com.frank.calendar.LunarCalendar.getSolarTerm
import com.frank.calendar.ui.theme.CalendarTheme
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

fun saveTimePerSet() {
    sharedPreferences.edit(commit = true) {
        val df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        putString("SHARED_PREFS_TIME_PER_WORKSET", df.format(toDate))
    }
}

@Composable
fun ClockUI() {

    var textSize by remember("") { mutableStateOf(maxTextSizeTime) }
    var textSize2 by remember("") { mutableStateOf(maxTextSizeLeftDate) }
    var textSize3 by remember("") { mutableStateOf(maxTextSizeGongli) }
    var textSize4 by remember("") { mutableStateOf(maxTextSizeTB) }
    fun getDateString(): AnnotatedString {
        return buildAnnotatedString {
            withStyle(style = SpanStyle(color = defaultColor)) { // 设置第一部分颜色
                append(date.substring(0,12))
            }
            if (isRed) {
                withStyle(style = SpanStyle(color = Color(0xFFBB86FC))) { // 设置第一部分颜色
                    append(date.substring(13, 16))
                }
            }
            withStyle(style = SpanStyle(color = defaultColor)) { // 设置第一部分颜色
                append(" " + date.substring(date.length - 4))
            }
        }
    }


    Column(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = time,
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
            modifier = Modifier.weight(1.2f, true)
        )
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
        Text(
            text = leftDate,
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
            modifier = Modifier.weight(0.3f, true)
        )
        Text(
            text = getDateString(),
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
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.5f, true)
        )
    }
}

@Composable
fun ClockUIV() {

    var textSizeA by remember("") { mutableStateOf(maxTextSizeTimeA) }
    var textSize1A by remember("") { mutableStateOf(maxTextSizeTime1A) }
    var textSize2A by remember("") { mutableStateOf(maxTextSizeLeftDateA) }
    var textSize3A by remember("") { mutableStateOf(maxTextSizeGongliA) }
    Column(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.2f, true))
        Text(
            text = trunck_branch.substring(0, 4) + "  ($year_name)",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (it.hasVisualOverflow && textSize1A > minTextSize) {
                    textSize1A = (textSize1A.value - 1.0F).sp
                } else {
                    maxTextSizeTime1A = textSize1A
                    with(sharedPreferences.edit()) {
                        putFloat("SHARED_PREFS_TIME1A", maxTextSizeTime1A.value)
                        commit()
                    }
                }
            },
            fontSize = textSize1A,
            color = textColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.18f, true)
        )
        Text(
            text = leftDate.substring(0, 11),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (it.hasVisualOverflow && textSize2A > minTextSize) {
                    textSize2A = (textSize2A.value - 1.0F).sp
                } else {
                    maxTextSizeLeftDateA = textSize2A
                    with(sharedPreferences.edit()) {
                        putFloat("SHARED_PREFS_LEFTA", maxTextSizeLeftDateA.value)
                        commit()
                    }
                }
            },
            fontSize = textSize2A,
            color = defaultColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.2f, true)
        )
        if (leftDate.length > 15) {
            Text(
                text = leftDate.substring(11),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = {
                    if (it.hasVisualOverflow && textSize2A > minTextSize) {
                        textSize2A = (textSize2A.value - 1.0F).sp
                    } else {
                        maxTextSizeLeftDateA = textSize2A
                        with(sharedPreferences.edit()) {
                            putFloat("SHARED_PREFS_LEFTA", maxTextSizeLeftDateA.value)
                            commit()
                        }
                    }
                },
                fontSize = textSize2A,
                color = defaultColor,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(0.2f, true)
            )
        } else {
            Spacer(modifier = Modifier.weight(0.3f, true))
        }
        Text(
            text = time,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (it.hasVisualOverflow && textSizeA > minTextSize) {
                    textSizeA = (textSizeA.value - 1.0F).sp
                } else {
                    maxTextSizeTimeA = textSizeA
                    with(sharedPreferences.edit()) {
                        putFloat("SHARED_PREFS_TIMEA", maxTextSizeTimeA.value)
                        commit()
                    }
                }
            },
            fontSize = textSizeA,
            color = textColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(25.dp)
                .weight(1.1f, true)
        )
        if (isRed) {
            Spacer(modifier = Modifier.weight(0.1f, true))
            Text(
                text = date.substring(13, 16),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = {
                    if (it.hasVisualOverflow && textSize3A > minTextSize) {
                        textSize3A = (textSize3A.value - 1.0F).sp
                    } else {
                        maxTextSizeGongliA = textSize3A
                        with(sharedPreferences.edit()) {
                            putFloat("SHARED_PREFS_WEEKA", maxTextSizeGongliA.value)
                            commit()
                        }
                    }
                },
                fontSize = textSize3A,
                color = Color(0xFFBB86FC),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(0.7f, true)
            )
        }
        Spacer(modifier = Modifier.weight(0.1f, true))
        Text(
            text = date.substring(date.length - 4),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (it.hasVisualOverflow && textSize3A > minTextSize) {
                    textSize3A = (textSize3A.value - 1.0F).sp
                } else {
                    maxTextSizeGongliA = textSize3A
                    with(sharedPreferences.edit()) {
                        putFloat("SHARED_PREFS_WEEKA", maxTextSizeGongliA.value)
                        commit()
                    }
                }
            },
            fontSize = textSize3A,
            color = defaultColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.7f, true)
        )
        Text(
            text = trunck_branch.substring(5),
            maxLines = 1,
            fontSize = textSize2A,
            onTextLayout = {
                if (it.hasVisualOverflow && textSize2A > minTextSize) {
                    textSize2A = (textSize2A.value - 1.0F).sp
                } else {
                    maxTextSizeLeftDateA = textSize2A
                    with(sharedPreferences.edit()) {
                        putFloat("SHARED_PREFS_LEFTA", maxTextSizeLeftDateA.value)
                        commit()
                    }
                }
            },
            color = textColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(0.2f, true)
        )
        Spacer(modifier = Modifier.weight(0.1f, true))
        Text(
            text = date.substring(0, 11),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (it.hasVisualOverflow && textSize3A > minTextSize) {
                    textSize3A = (textSize3A.value - 1.0F).sp
                } else {
                    maxTextSizeGongliA = textSize3A
                    with(sharedPreferences.edit()) {
                        putFloat("SHARED_PREFS_WEEKA", maxTextSizeGongliA.value)
                        commit()
                    }
                }
            },
            fontSize = textSize3A,
            color = defaultColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(3.dp)
                .weight(0.35f, true)
        )
        Spacer(modifier = Modifier.weight(0.1f, true))
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
    val solar = LunarCalendar.gregorianFestival(
        now.year,
        now.monthValue,
        now.dayOfMonth,
        now.dayOfWeek.value,
        termText
    )
    val day: Int = now.dayOfWeek.value
    val weekString = "一二三四五六日"
    "$solar 星期${weekString.substring(day - 1, day)} $termText"
}

@Preview()
@Composable
fun PreviewMessageListScreenDark() {
    CalendarTheme(darkTheme = false) {
        ClockUI()
    }
}

