package com.frank.calendar

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
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
var maxYearTextSize = 312.sp
var maxSixdayTextSize = 32.sp
var maxTextSizeTB = 312.sp
var maxTextSizeLeftDate = 112.sp
var maxLeftdaysTextSize = 112.sp
var maxTextSizeGongli = 112.sp
var maxTimeTextSize = 312.sp
var maxFestivalDateTextSize = 212.sp
var maxWeekdayTextSize = 212.sp
var maxJieqiTextSize = 212.sp
var maxCharacterTextSize = 212.sp
var maxDateTextSize = 212.sp
var maxTextSizeTitle_LANDSCAPE = 112.sp
var maxTextSizeCalendarDate_LANDSCAPE = 132.sp
var maxTextSizeCalendarSix_LANDSCAPE = 132.sp
var maxTextSizeTitle_PORTRAIT = 112.sp
var maxTextSizeCalendarDate_PORTRAIT = 132.sp
var maxTextSizeCalendarSix_PORTRAIT = 132.sp
var weeksMonth: Int = 5
var isPort by mutableStateOf(true)
var time by mutableStateOf("09:35:23")
var oldTime by mutableStateOf("子时")
var showBranchDialog by mutableStateOf(false)

//日期相关数据
var date by mutableStateOf("2023年12月08日")     //公立年月日
var weekDay by mutableStateOf("")               //星期几
var termText by mutableStateOf("")              //节气
var festival by mutableStateOf("")              //中日公立节日
var lunarFestival by mutableStateOf("")         //中国农历节日
var AnimalYear by mutableStateOf("")            //生肖
var year_name by mutableStateOf("")             //日本年号
var character by mutableStateOf("")             //八字
var sixDay by mutableStateOf("")                //六曜
var leftDays by mutableStateOf("")              //剩余天数
var iLeftDays: Long by mutableStateOf(0)              //剩余天数


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
var defaultColor = Color(0xFF018786)
val jieqiColor = Color(0xFF88FF33)
val todayColor = Color(0xFFFFA00C)
var x10 = 0f
var y10 = 0f
var isFirstDraw0 = true
var chooseTime by mutableIntStateOf(theTime.hour)

fun readToDate() {
    maxTextSizeTime = sharedPreferences.getFloat("SHARED_PREFS_TIME", 312.0f).sp
    maxTextSizeTB = sharedPreferences.getFloat("SHARED_PREFS_TB", 312.0f).sp
    maxTextSizeLeftDate = sharedPreferences.getFloat("SHARED_PREFS_LEFT", 112.0f).sp
    maxTextSizeGongli = sharedPreferences.getFloat("SHARED_PREFS_WEEK", 112.0f).sp

    maxYearTextSize = sharedPreferences.getFloat("SHARED_PREFS_MAX_YEAR_TEXT_SIZE", 312.0f).sp
    maxSixdayTextSize = sharedPreferences.getFloat("SHARED_PREFS_SIX_DAY", 32.0f).sp
    maxLeftdaysTextSize = sharedPreferences.getFloat("SHARED_PREFS_LEFT_DAYS", 112.0f).sp
    maxTimeTextSize = sharedPreferences.getFloat("SHARED_PREFS_MAX_TEXT_SIZE_TIME", 312.0f).sp
    maxFestivalDateTextSize = sharedPreferences.getFloat("SHARED_PREFS_FESTIVAL", 212.0f).sp
    maxWeekdayTextSize = sharedPreferences.getFloat("SHARED_PREFS_WEEK_DAY", 212.0f).sp
    maxJieqiTextSize = sharedPreferences.getFloat("SHARED_PREFS_JIEQI", 212.0f).sp
    maxCharacterTextSize = sharedPreferences.getFloat("SHARED_PREFS_CHARACTER", 212.0f).sp
    maxDateTextSize = sharedPreferences.getFloat("SHARED_PREFS_DATE", 212.0f).sp

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
