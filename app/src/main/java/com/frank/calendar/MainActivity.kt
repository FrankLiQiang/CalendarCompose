package com.frank.calendar

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.unit.sp
import com.frank.calendar.ui.theme.CalendarTheme
import com.frank.calendar.ui.theme.HorizontalPagerSample
import com.frank.calendar.ui.theme.monthOffset
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Timer
import java.util.TimerTask


lateinit var firstDay: LocalDate
var is_Pad = false
var dayOfMonth = 1
var firstDayOfWeek: Int = 0
var minTextSize = 8.sp
var maxTextSize1 = 312.sp
var maxTextSize2 = 112.sp
var maxTextSize3 = 112.sp
var maxTextSize4 = 112.sp
var maxTextSizeGongli = 132.sp
var maxTextSizeSix = 132.sp
var weeksMonth: Int = 5
private var thisTimer: Timer = Timer()
private var thisTask: TimerTask? = null
var textColor by mutableStateOf(DarkGray)
var time by mutableStateOf("09:35:23")
var leftDate by mutableStateOf("农历十月廿六")
var date by mutableStateOf("2023年12月08日")
var isRed by mutableStateOf(false)
var isClock by mutableStateOf(true)
var dateArray = Array(42) { -1 }
var nongliArray = Array(42) { "" }
var sixDaysArray = Array(42) { "" }
var isRedraw by mutableStateOf(1)
var now: LocalDateTime = LocalDateTime.now()
lateinit var sharedPreferences: SharedPreferences

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        WindowCompat.setDecorFitsSystemWindows(window, false)
        is_Pad = isPad(this)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        setContent {
            LunarCalendar.init(this)
            readToDate()

            thisTask = object : TimerTask() {
                override fun run() {
                    try {
                        time = getCurrentTime()
                        leftDate = getNongLiDate()
                        date = getCurrentDate()
                        isRed = dateColor
                    } catch (e: Exception) {
                        e.toString()
                    }
                }
            }
            thisTimer.scheduleAtFixedRate(thisTask, 0, 1000)
            CalendarTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                ) {
                    if (isClock) {
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                        ClockUI(::leftTimeClicked)
                    } else {
                        monthOffset = 0
                        if (!is_Pad) {
                            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                        }
                        HorizontalPagerSample()
                    }
                }
            }
        }
    }

    private fun leftTimeClicked() {
        val dpd = DatePickerDialog(
            this, { _, year, month, day ->
                toDate = LocalDateTime.of(year, month + 1, day, 0, 0, 0, 0)
                saveTimePerSet()
                currentDateNum = -1
            }, toDate.year, toDate.monthValue - 1, toDate.dayOfMonth
        )
        dpd.show()
    }

    private fun saveTimePerSet() {
        with(sharedPreferences.edit()) {
            val df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            putString("SHARED_PREFS_TIME_PER_WORKSET", df.format(toDate))
            commit()
        }
    }

    private fun readToDate() {
        maxTextSize4 = sharedPreferences.getFloat("SHARED_PREFS_CALENDAR_TITLE", 112.0f).sp
        maxTextSizeSix = sharedPreferences.getFloat("SHARED_PREFS_SIX", 132.0f).sp
        maxTextSizeGongli = sharedPreferences.getFloat("SHARED_PREFS_GONG_LI", 132.0f).sp
        maxTextSize1 = sharedPreferences.getFloat("SHARED_PREFS_TIME", 312.0f).sp
        maxTextSize2 = sharedPreferences.getFloat("SHARED_PREFS_LEFT", 112.0f).sp
        maxTextSize3 = sharedPreferences.getFloat("SHARED_PREFS_WEEK", 112.0f).sp

        val df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        var defaultDate = df.format(LocalDateTime.now())
        var highScore = sharedPreferences.getString("SHARED_PREFS_TIME_PER_WORKSET", defaultDate)
        toDate = LocalDateTime.parse(highScore, df)
    }
}
