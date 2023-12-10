package com.frank.calendar

import com.frank.calendar.LunarCalendar.getLunarText2
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.concurrent.TimeUnit

var gongliDate = ""
var nongliDate = ""
var nongli_Date = ""
var newCurrentDate = -1
var currentDateNum = -2
var dateColor = true
var toDate: Calendar = Calendar.getInstance()

fun getNongLiDate(): String {
    return if (currentDateNum == newCurrentDate) {
        nongliDate
    } else {
        nongliDate = "${nongli()}  ${leftDays()}"
        nongliDate
    }
}

fun getNongLi(dayNum: Int): String {
    val calendar: Calendar = Calendar.getInstance()
    nongli_Date = getLunarText2(
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH) + 1,
        dayNum
    )
    return nongli_Date
}

fun getCurrentTime(): String {
    val localDateTime = LocalDateTime.now()
    newCurrentDate = localDateTime.dayOfMonth
//    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return localDateTime.format(formatter)
}

fun getCurrentDate(): String {
    return if (currentDateNum == newCurrentDate) {
        gongliDate
    } else {
        weeksMonth = getWeeksOfMonth()
        isRedraw = 1 - isRedraw
        val wk = nowWeek()
        dateColor = !wk.startsWith("星期")
        gongliDate = "${nowDate()}  $wk"
        currentDateNum = newCurrentDate
        gongliDate
    }
}

val nongli: () -> String = {
    val calendar: Calendar = Calendar.getInstance()
    "农历 ${
        LunarCalendar.getLunarText(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }"
}

val leftDays: () -> String = {
    val cNow = Calendar.getInstance()
    cNow.set(Calendar.HOUR_OF_DAY, 0)
    cNow.set(Calendar.MINUTE, 0)
    cNow.set(Calendar.SECOND, 0)
    cNow.set(Calendar.MILLISECOND, 0)
    val diff = toDate.timeInMillis - cNow.timeInMillis
    val leftDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()
    if (leftDays > 0) "  还剩 $leftDays 天" else ""
}

val nowDate: () -> String = {
    val localDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日")
    localDateTime.format(formatter)
}

val nowWeek: () -> String = {
    val calendar: Calendar = Calendar.getInstance()

    val solar = LunarCalendar.gregorianFestival(
        calendar.get(Calendar.MONTH) + 1,
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    solar.ifEmpty {
        val day: Int = calendar.get(Calendar.DAY_OF_WEEK)
        val weekString = "日一二三四五六"
        "星期${weekString.substring(day - 1, day)}"
    }
}

