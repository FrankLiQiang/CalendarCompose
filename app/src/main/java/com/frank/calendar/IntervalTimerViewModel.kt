package com.frank.calendar

import com.frank.calendar.LunarCalendar.getLunarText2
import com.frank.calendar.ui.theme.monthOffset
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

var gongliDate = ""
var nongliDate = ""
var newCurrentDate = -1
var currentDateNum = -2
var dateColor = true
var toDate: LocalDateTime = now

fun getNongLiDate(): String {
    return if (currentDateNum == newCurrentDate) {
        nongliDate
    } else {
        nongliDate = "${nongli()}  ${leftDays()}"
        nongliDate
    }
}

fun getNongLi(dayNum: Int): String {
    return getLunarText2(now.year, now.monthValue, dayNum)
}

fun getSixDay(dayNum: Int): String {
    return LunarCalendar.getSixDay(now.year, now.monthValue, dayNum)
}

fun getCurrentTime(): String {
    now = LocalDateTime.now()
    newCurrentDate = now.dayOfMonth
//    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return now.format(formatter)
}

fun getCurrentDate(): String {
    return if (currentDateNum == newCurrentDate) {
        gongliDate
    } else {
        weeksMonth = getWeeksOfMonth()
        val wk = nowWeek()
        dateColor = !wk.startsWith("星期")
        gongliDate = "${nowDate()}  $wk"
        currentDateNum = newCurrentDate
        return gongliDate
    }
}

val nongli: () -> String = {
    "农历 ${
        LunarCalendar.getLunarText(now.year, now.monthValue, now.dayOfMonth)
    }  ${
        LunarCalendar.getSixDay(now.year, now.monthValue, now.dayOfMonth)
    }"
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
    val solar = LunarCalendar.gregorianFestival(now.monthValue, now.dayOfMonth)
    solar.ifEmpty {
        val day: Int = now.dayOfWeek.value
        val weekString = "一二三四五六日"
        "星期${weekString.substring(day - 1, day)}"
    }
}

