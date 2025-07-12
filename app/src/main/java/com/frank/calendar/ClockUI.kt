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

    var timeTextSize by remember("") { mutableStateOf(maxTextSizeTime) }
    var tbTextSize by remember("") { mutableStateOf(maxTextSizeTB) }
    var leftTextSize by remember("") { mutableStateOf(maxTextSizeLeftDate) }
    var dateTextSize by remember("") { mutableStateOf(maxTextSizeGongli) }

    fun getDateString(): AnnotatedString {
        return buildAnnotatedString {
            withStyle(style = SpanStyle(color = defaultColor)) { // 设置第一部分颜色
                append(date)
            }
            if (isRed) {
                withStyle(style = SpanStyle(color = Color(0xFFBB86FC))) { // 设置第一部分颜色
                    append(festival)
                }
            }
            withStyle(style = SpanStyle(color = defaultColor)) { // 设置第一部分颜色
                append(" $weekDay")
            }
            withStyle(style = SpanStyle(color = jieqiColor)) { // 设置第一部分颜色
                append(" $termText")
            }
        }
    }

    fun getNongliDate(): AnnotatedString {
        return buildAnnotatedString {
            withStyle(style = SpanStyle(color = defaultColor)) { // 设置第一部分颜色
                append("农历 ")
            }
            withStyle(style = SpanStyle(color = if (nongliDateColor) Color.Red else defaultColor)) { // 设置第一部分颜色
                append(" $lunarFestival")
            }
            withStyle(style = SpanStyle(color = defaultColor)) { // 设置第一部分颜色
                append("  $sixDay  $leftDays")
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
                if (it.hasVisualOverflow && timeTextSize > minTextSize) {
                    timeTextSize = (timeTextSize.value - 1.0F).sp
                } else {
                    maxTextSizeTime = timeTextSize
                    with(sharedPreferences.edit()) {
                        putFloat("SHARED_PREFS_TIME", maxTextSizeTime.value)
                        commit()
                    }
                }
            },
            fontSize = timeTextSize,
            color = defaultColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1.2f, true)
        )
        Text(
            text = "$character  ($year_name)",
            maxLines = 1,
            fontSize = tbTextSize,
            onTextLayout = {
                if (it.hasVisualOverflow && tbTextSize > minTextSize) {
                    tbTextSize = (tbTextSize.value - 1.0F).sp
                } else {
                    maxTextSizeTB = tbTextSize
                    with(sharedPreferences.edit()) {
                        putFloat("SHARED_PREFS_TB", maxTextSizeTB.value)
                        commit()
                    }
                }
            },
            color = defaultColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(0.3f, true)
        )
        Text(
            text = getNongliDate(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (it.hasVisualOverflow && leftTextSize > minTextSize) {
                    leftTextSize = (leftTextSize.value - 1.0F).sp
                } else {
                    maxTextSizeLeftDate = leftTextSize
                    with(sharedPreferences.edit()) {
                        putFloat("SHARED_PREFS_LEFT", maxTextSizeLeftDate.value)
                        commit()
                    }
                }
            },
            fontSize = leftTextSize,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.3f, true)
        )
        Text(
            text = getDateString(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (it.hasVisualOverflow && dateTextSize > minTextSize) {
                    dateTextSize = (dateTextSize.value - 1.0F).sp
                } else {
                    maxTextSizeGongli = dateTextSize
                    with(sharedPreferences.edit()) {
                        putFloat("SHARED_PREFS_WEEK", maxTextSizeGongli.value)
                        commit()
                    }
                }
            },
            fontSize = dateTextSize,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.5f, true)
        )
    }
}

@Composable
fun ClockUIV() {

    var yearTextSize by remember("") { mutableStateOf(maxYearTextSize) }
    var sixdayTextSize by remember("") { mutableStateOf(maxSixdayTextSize) }
    var leftdaysTextSize by remember("") { mutableStateOf(maxLeftdaysTextSize) }
    var timeTextSize by remember("") { mutableStateOf(maxTimeTextSize) }
    var festivalTextSize by remember("") { mutableStateOf(maxFestivalDateTextSize) }
    var weekdayTextSize by remember("") { mutableStateOf(maxWeekdayTextSize) }
    var jieqiTextSize by remember("") { mutableStateOf(maxJieqiTextSize) }
    var characterTextSize by remember("") { mutableStateOf(maxCharacterTextSize) }
    var dateTextSize by remember("") { mutableStateOf(maxDateTextSize) }
    Column(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.2f, true))
        Text(
            text = "$AnimalYear  ($year_name)",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (it.hasVisualOverflow && yearTextSize > minTextSize) {
                    yearTextSize = (yearTextSize.value - 1.0F).sp
                } else {
                    maxYearTextSize = yearTextSize
                    with(sharedPreferences.edit()) {
                        putFloat("SHARED_PREFS_MAX_YEAR_TEXT_SIZE", maxYearTextSize.value)
                        commit()
                    }
                }
            },
            fontSize = yearTextSize,
            color = defaultColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.21f, true)
        )
        Text(
            text = "农历 $lunarFestival   $sixDay",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (it.hasVisualOverflow && sixdayTextSize > minTextSize) {
                    sixdayTextSize = (sixdayTextSize.value - 1.0F).sp
                } else {
                    maxSixdayTextSize = sixdayTextSize
                    with(sharedPreferences.edit()) {
                        putFloat("SHARED_PREFS_SIX_DAY", maxSixdayTextSize.value)
                        commit()
                    }
                }
            },
            fontSize = sixdayTextSize,
            color = if (nongliDateColor) Color.Red else defaultColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.22f, true)
        )
        Text(
            text = leftDays,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (it.hasVisualOverflow && leftdaysTextSize > minTextSize) {
                    leftdaysTextSize = (leftdaysTextSize.value - 1.0F).sp
                } else {
                    maxLeftdaysTextSize = leftdaysTextSize
                    with(sharedPreferences.edit()) {
                        putFloat("SHARED_PREFS_LEFT_DAYS", maxLeftdaysTextSize.value)
                        commit()
                    }
                }
            },
            fontSize = leftdaysTextSize,
            color = defaultColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.3f, true)
        )
        Text(
            text = time,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (it.hasVisualOverflow && timeTextSize > minTextSize) {
                    timeTextSize = (timeTextSize.value - 1.0F).sp
                } else {
                    maxTimeTextSize = timeTextSize
                    with(sharedPreferences.edit()) {
                        putFloat("SHARED_PREFS_MAX_TEXT_SIZE_TIME", maxTimeTextSize.value)
                        commit()
                    }
                }
            },
            fontSize = timeTextSize,
            color = defaultColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(25.dp)
                .weight(1.1f, true)
        )
        Text(
            text = festival,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (it.hasVisualOverflow && festivalTextSize > minTextSize) {
                    festivalTextSize = (festivalTextSize.value - 1.0F).sp
                } else {
                    maxFestivalDateTextSize = festivalTextSize
                    with(sharedPreferences.edit()) {
                        putFloat("SHARED_PREFS_FESTIVAL", maxFestivalDateTextSize.value)
                        commit()
                    }
                }
            },
            fontSize = festivalTextSize,
            color = Color(0xFFBB86FC),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.37f, true)
        )
        Spacer(modifier = Modifier.weight(0.1f, true))
        Text(
            text = weekDay,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (it.hasVisualOverflow && weekdayTextSize > minTextSize) {
                    weekdayTextSize = (weekdayTextSize.value - 1.0F).sp
                } else {
                    maxWeekdayTextSize = weekdayTextSize
                    with(sharedPreferences.edit()) {
                        putFloat("SHARED_PREFS_WEEK_DAY", maxWeekdayTextSize.value)
                        commit()
                    }
                }
            },
            fontSize = weekdayTextSize,
            color = defaultColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.37f, true)
        )
        Spacer(modifier = Modifier.weight(0.05f, true))
        Text(
            text = termText,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (it.hasVisualOverflow && jieqiTextSize > minTextSize) {
                    jieqiTextSize = (jieqiTextSize.value - 1.0F).sp
                } else {
                    maxJieqiTextSize = jieqiTextSize
                    with(sharedPreferences.edit()) {
                        putFloat("SHARED_PREFS_JIEQI", maxJieqiTextSize.value)
                        commit()
                    }
                }
            },
            fontSize = jieqiTextSize,
            color = jieqiColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.37f, true)
        )
        Spacer(modifier = Modifier.weight(0.05f, true))
        Text(
            text = character,
            maxLines = 1,
            fontSize = characterTextSize,
            onTextLayout = {
                if (it.hasVisualOverflow && characterTextSize > minTextSize) {
                    characterTextSize = (characterTextSize.value - 1.0F).sp
                } else {
                    maxCharacterTextSize = characterTextSize
                    with(sharedPreferences.edit()) {
                        putFloat("SHARED_PREFS_CHARACTER", maxCharacterTextSize.value)
                        commit()
                    }
                }
            },
            color = defaultColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(0.2f, true)
        )
        Spacer(modifier = Modifier.weight(0.1f, true))
        Text(
            text = date,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (it.hasVisualOverflow && dateTextSize > minTextSize) {
                    dateTextSize = (dateTextSize.value - 1.0F).sp
                } else {
                    maxDateTextSize = dateTextSize
                    with(sharedPreferences.edit()) {
                        putFloat("SHARED_PREFS_DATE", maxDateTextSize.value)
                        commit()
                    }
                }
            },
            fontSize = dateTextSize,
            color = defaultColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(3.dp)
                .weight(0.45f, true)
        )
        Spacer(modifier = Modifier.weight(0.1f, true))
    }
}

fun getCurrentTime(): String {
    now = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return now.format(formatter)
}

fun getCurrentDate() {

    //公立年月日
    now = LocalDateTime.now()       //.plusMonths(monthOffset.toLong())
    val formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日")
    date = now.format(formatter)
    dayOfMonth = now.dayOfMonth
    val day: Int = now.dayOfWeek.value
    val weekString = "一二三四五六日"
    weekDay = "星期${weekString.substring(day - 1, day)}"
    termText = getSolarTerm(now.year, now.monthValue, now.dayOfMonth)       //节气
    festival = LunarCalendar.gregorianFestival(
        now.year,
        now.monthValue,
        now.dayOfMonth,
        now.dayOfWeek.value,
        termText
    )
    year_name = LunarCalendar.getYearName(now.year, now.monthValue, now.dayOfMonth)
    val cNow = LocalDateTime.of(now.year, now.monthValue, now.dayOfMonth, 0, 0, 0, 0)
    iLeftDays = ChronoUnit.DAYS.between(cNow, toDate)
    leftDays = if (iLeftDays > 0) "  还剩 $iLeftDays 天" else ""
    lunarFestival = LunarCalendar.getLunarText(now.year, now.monthValue, now.dayOfMonth)
    sixDay = LunarCalendar.getSixDay(now.year, now.monthValue, now.dayOfMonth)
    character = LunarCalendar.getMainBranch(now.year, now.monthValue, now.dayOfMonth, now.hour)
}

@Preview()
@Composable
fun PreviewMessageListScreenDark() {
    CalendarTheme(darkTheme = false) {
        ClockUI()
    }
}

