package com.frank.calendar

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.runtime.remember
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.frank.calendar.ui.theme.CalendarTheme
import com.frank.calendar.ui.theme.datePickerState
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
var dayOfMonth0 = 1
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
    private var timerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isPort = false
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            isPort = true
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
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


    @OptIn(ExperimentalMaterial3Api::class)
    private fun startTimer() {
        lifecycleScope.launch {
            timerRunning = true
            dayOfMonth0 = LocalDateTime.now().dayOfMonth
            Log.i("aaa","dayOfMonth0 -------------------------- ")
            while (timerRunning) {
                hourState =
                    LocalDateTime.now().hour * 5 * 1000L + LocalDateTime.now().minute * 5000L / 60 + LocalDateTime.now().second * 5000 / 3600   //小时
                minuteState =
                    LocalDateTime.now().minute * 1000L + LocalDateTime.now().second * 1000 / 60    //分钟
                secondState =
                    LocalDateTime.now().second * 1000 + LocalDateTime.now().nano / 1_000_000L      //秒
                if (dayOfMonth0 != LocalDateTime.now().dayOfMonth) {
                    Log.i("aaa","dayOfMonth0 " + dayOfMonth0.toString())
                    Log.i("aaa","LocalDateTime.now().dayOfMonth: " + LocalDateTime.now().dayOfMonth)

                    dayOfMonth0 = LocalDateTime.now().dayOfMonth
                    isRedraw = 1 - isRedraw
                }
                delay(16)
            }
        }
    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ActivityView() {
        startTimer()
        var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
        Log.i("aaa", "999999999999999999999")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        if (isRedraw > -1) {
            datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = selectedDate
            )
//            datePickerState =
//                rememberDatePickerState(
//                    initialSelectedDateMillis = LocalDateTime.now().toLocalDate()
//                        .atStartOfDay(ZoneOffset.ofHours(0)).toInstant().toEpochMilli()
//                )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
        ) {
            Box(
                modifier = Modifier
                    .padding(26.dp)
                    .weight(1f)
            ) {
                StopWatch(
                    modifier = Modifier,
                )
            }

            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
            ) {
                if (isRedraw < 10) {
                    DatePicker(
                        modifier = Modifier,
                        headline = null,
                        title = null,
                        state = datePickerState,
                        showModeToggle = false,
                    )
                }
                // 透明覆盖层
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent) // 透明背景
                        .clickable { /* 不执行任何操作，阻止用户选择 */ }
                ) {
                }
            }
        }
    }
}
