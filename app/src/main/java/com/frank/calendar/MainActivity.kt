package com.frank.calendar

import android.app.DatePickerDialog
import android.content.Context
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
import java.time.LocalDate
import java.util.Calendar
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
var nongliArray = Array(31) { "" }
var sixDaysArray = Array(31) { "" }
var isRedraw by mutableStateOf(1)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        WindowCompat.setDecorFitsSystemWindows(window, false)
        is_Pad = isPad(this)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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
                        weeksMonth = getWeeksOfMonth()
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
        val year = toDate.get(Calendar.YEAR)
        val month = toDate.get(Calendar.MONTH)
        val day = toDate.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this, { _, year2, monthOfYear, dayOfMonth ->
                toDate.set(year2, monthOfYear, dayOfMonth, 0, 0, 0)
                toDate.set(Calendar.MILLISECOND, 0)
                saveTimePerSet()
                currentDateNum = -1
            }, year, month, day
        )
        dpd.show()
    }

    private fun saveTimePerSet() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putLong(getString(R.string.prefs_timePerWorkSet), toDate.timeInMillis)
            commit()
        }
    }

    private fun readToDate() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val defaultValue = Calendar.getInstance().timeInMillis
        val highScore = sharedPref.getLong(getString(R.string.prefs_timePerWorkSet), defaultValue)
        toDate.timeInMillis = highScore
    }
}
