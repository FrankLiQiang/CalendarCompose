package com.frank.calendar

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

lateinit var sharedPreferences: SharedPreferences
lateinit var firstDay: LocalDate
var dayOfMonth = 1
var dayOfMonth0 = 1
var firstDayOfWeek: Int = 0
val minTextSize = 5.sp
var maxTextSizeTime = 312.sp
var maxTextSizeTimeA = 312.sp
var maxTextSizeTime1A = 32.sp
var maxTextSizeTB = 312.sp
var maxTextSizeTBA = 312.sp
var maxTextSizeLeftDate = 112.sp
var maxTextSizeLeftDateA = 112.sp
var maxTextSizeGongli = 112.sp
var maxTextSizeGongliA = 112.sp
var maxTextSizeTitle_LANDSCAPE = 112.sp
var maxTextSizeCalendarDate_LANDSCAPE = 132.sp
var maxTextSizeCalendarSix_LANDSCAPE = 132.sp
var maxTextSizeTitle_PORTRAIT = 112.sp
var maxTextSizeCalendarDate_PORTRAIT = 132.sp
var maxTextSizeCalendarSix_PORTRAIT = 132.sp
var weeksMonth: Int = 5
var textColor by mutableStateOf(Color(0xFF018786))
var isPort by mutableStateOf(true)
var time by mutableStateOf("09:35:23")
var oldTime by mutableStateOf("子时")
var showBranchDialog by mutableStateOf(false)
var leftDate by mutableStateOf("")
var trunck_branch by mutableStateOf("")
var year_name by mutableStateOf("")
var date by mutableStateOf("2023年12月08日")
var isRed by mutableStateOf(false)
var dateArray = Array(42) { -1 }
var nongliArray = Array(42) { "" }
var sixDaysArray = Array(42) { "" }
var tbDaysArray = Array(42) { "" }
var isRedraw by mutableIntStateOf(1)
var hourState by mutableLongStateOf(1)
var minuteState by mutableLongStateOf(1)
var secondState by mutableLongStateOf(1)
var now: LocalDateTime = LocalDateTime.now()
var timerRunning = false
val arr: Array<String> = arrayOf("S", "M", "T", "W", "T", "F", "S")
val defaultColor = Color(0xFF018786)

fun readToDate() {
    maxTextSizeTime = sharedPreferences.getFloat("SHARED_PREFS_TIME", 312.0f).sp
    maxTextSizeTimeA = sharedPreferences.getFloat("SHARED_PREFS_TIMEA", 312.0f).sp
    maxTextSizeTime1A = sharedPreferences.getFloat("SHARED_PREFS_TIME1A", 32.0f).sp
    maxTextSizeTB = sharedPreferences.getFloat("SHARED_PREFS_TB", 312.0f).sp
    maxTextSizeTBA = sharedPreferences.getFloat("SHARED_PREFS_TBA", 312.0f).sp
    maxTextSizeLeftDate = sharedPreferences.getFloat("SHARED_PREFS_LEFT", 112.0f).sp
    maxTextSizeLeftDateA = sharedPreferences.getFloat("SHARED_PREFS_LEFTA", 112.0f).sp
    maxTextSizeGongli = sharedPreferences.getFloat("SHARED_PREFS_WEEK", 112.0f).sp
    maxTextSizeGongliA = sharedPreferences.getFloat("SHARED_PREFS_WEEKA", 112.0f).sp

    maxTextSizeTitle_LANDSCAPE =
        sharedPreferences.getFloat("SHARED_PREFS_CALENDAR_TITLE_L", 112.0f).sp
    maxTextSizeCalendarDate_LANDSCAPE =
        sharedPreferences.getFloat("SHARED_PREFS_GONG_LI_L", 132.0f).sp
    maxTextSizeCalendarSix_LANDSCAPE = sharedPreferences.getFloat("SHARED_PREFS_SIX_L", 132.0f).sp

    maxTextSizeTitle_PORTRAIT =
        sharedPreferences.getFloat("SHARED_PREFS_CALENDAR_TITLE_P", 112.0f).sp
    maxTextSizeCalendarDate_PORTRAIT =
        sharedPreferences.getFloat("SHARED_PREFS_GONG_LI_P", 132.0f).sp
    maxTextSizeCalendarSix_PORTRAIT = sharedPreferences.getFloat("SHARED_PREFS_SIX_P", 132.0f).sp

    val df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val defaultDate = df.format(LocalDateTime.now())
    val highScore = sharedPreferences.getString("SHARED_PREFS_TIME_PER_WORKSET", defaultDate)
    toDate = LocalDateTime.parse(highScore, df)
}
