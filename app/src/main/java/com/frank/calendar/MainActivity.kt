package com.frank.calendar

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.frank.calendar.ui.theme.CalendarTheme
import com.frank.calendar.ui.theme.HorizontalPagerSample
import com.frank.calendar.ui.theme.SetSettingDialog
import com.frank.calendar.ui.theme.monthOffset
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Timer
import java.util.TimerTask


lateinit var firstDay: LocalDate
var dayOfMonth = 1
var firstDayOfWeek: Int = 0
val minTextSize = 5.sp
var maxTextSizeTime = 312.sp
var maxTextSizeTB = 312.sp
var maxTextSizeLeftDate = 112.sp
var maxTextSizeGongli = 112.sp
var maxTextSizeTitle_LANDSCAPE = 112.sp
var maxTextSizeCalendarDate_LANDSCAPE = 132.sp
var maxTextSizeCalendarSix_LANDSCAPE = 132.sp
var maxTextSizeTitle_PORTRAIT = 112.sp
var maxTextSizeCalendarDate_PORTRAIT = 132.sp
var maxTextSizeCalendarSix_PORTRAIT = 132.sp
var weeksMonth: Int = 5
private var thisTimer: Timer = Timer()
private var thisTask: TimerTask? = null
var textColor by mutableStateOf(DarkGray)
var isPort by mutableStateOf(true)
var time by mutableStateOf("09:35:23")
var leftDate by mutableStateOf("")
var trunck_branch by mutableStateOf("")
var year_name by mutableStateOf("")
var date by mutableStateOf("2023年12月08日")
var isRed by mutableStateOf(false)
var isClock by mutableStateOf(true)
var dateArray = Array(42) { -1 }
var nongliArray = Array(42) { "" }
var sixDaysArray = Array(42) { "" }
var tbDaysArray = Array(42) { "" }
var isRedraw by mutableIntStateOf(1)
var hourState by mutableLongStateOf(1)
var minuteState by mutableLongStateOf(1)
var secondState by mutableLongStateOf(1)
var now: LocalDateTime = LocalDateTime.now()
lateinit var sharedPreferences: SharedPreferences

class MainActivity : ComponentActivity() {
    private val currentTime = MutableLiveData(0L)
    private var timerStartedAt = 0L
    private var timerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        WindowCompat.setDecorFitsSystemWindows(window, false)
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isPort = false
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            isPort = true
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        startTimer()
//        setContent {
//            LunarCalendar.init(this)
//            readToDate()
//
//            thisTask = object : TimerTask() {
//                override fun run() {
//                    try {
//                        time = getCurrentTime()
//                        leftDate = getNongLiDate()
//                        trunck_branch = main_branch()
//                        year_name = jp_year_name()
//                        date = getCurrentDate()
//                        isRed = dateColor
//
//                        now = LocalDateTime.now()
//                        if (now.hour == 7 && now.minute == 0) {
//                            textColor = Color(0xFF018786)
//                        }
//                        if (now.hour == 23 && now.minute == 0) {
//                            textColor = DarkGray
//                        }
//                    } catch (e: Exception) {
//                        e.toString()
//                    }
//                }
//            }
//            thisTimer.scheduleAtFixedRate(thisTask, 0, 1000)
//            CalendarTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(Color.White),
//                ) {
//                    if (isClock) {
//                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
//                        ClockUI(::leftTimeClicked)
//                    } else {
//                        monthOffset = 0
//                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
//                        HorizontalPagerSample()
//                    }
//                    SetSettingDialog()
//                }
//            }
//        }
        setContent {
            CalendarTheme {
                ActivityView()
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
        maxTextSizeTime = sharedPreferences.getFloat("SHARED_PREFS_TIME", 312.0f).sp
        maxTextSizeTB = sharedPreferences.getFloat("SHARED_PREFS_TB", 312.0f).sp
        maxTextSizeLeftDate = sharedPreferences.getFloat("SHARED_PREFS_LEFT", 112.0f).sp
        maxTextSizeGongli = sharedPreferences.getFloat("SHARED_PREFS_WEEK", 112.0f).sp

        maxTextSizeTitle_LANDSCAPE =
            sharedPreferences.getFloat("SHARED_PREFS_CALENDAR_TITLE_L", 112.0f).sp
        maxTextSizeCalendarDate_LANDSCAPE =
            sharedPreferences.getFloat("SHARED_PREFS_GONG_LI_L", 132.0f).sp
        maxTextSizeCalendarSix_LANDSCAPE =
            sharedPreferences.getFloat("SHARED_PREFS_SIX_L", 132.0f).sp

        maxTextSizeTitle_PORTRAIT =
            sharedPreferences.getFloat("SHARED_PREFS_CALENDAR_TITLE_P", 112.0f).sp
        maxTextSizeCalendarDate_PORTRAIT =
            sharedPreferences.getFloat("SHARED_PREFS_GONG_LI_P", 132.0f).sp
        maxTextSizeCalendarSix_PORTRAIT =
            sharedPreferences.getFloat("SHARED_PREFS_SIX_P", 132.0f).sp

        val df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val defaultDate = df.format(LocalDateTime.now())
        val highScore = sharedPreferences.getString("SHARED_PREFS_TIME_PER_WORKSET", defaultDate)
        toDate = LocalDateTime.parse(highScore, df)
    }


    private fun startTimer() {
        lifecycleScope.launch {
            timerStartedAt = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
            timerRunning = true
            while (timerRunning) {
                hourState = LocalDateTime.now().hour * 5 * 1000L + LocalDateTime.now().minute * 5000L / 60 + LocalDateTime.now().second * 5000 / 3600   //小时
                minuteState = LocalDateTime.now().minute * 1000L + LocalDateTime.now().second * 1000 / 60    //分钟
                secondState = LocalDateTime.now().second * 1000 + LocalDateTime.now().nano / 1_000_000L      //秒
                delay(16)
            }
        }
    }

    @Composable
    fun ActivityView() {
        Box {
            StopWatch(
                modifier = Modifier,
//            currentTime = currentTime,
            )
        }
    }
}
