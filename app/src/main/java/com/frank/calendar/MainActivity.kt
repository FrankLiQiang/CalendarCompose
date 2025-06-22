package com.frank.calendar

import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.frank.calendar.ui.theme.CalendarTheme
import com.frank.calendar.ui.theme.HorizontalPagerSample
import com.frank.calendar.ui.theme.ShowSettingDialog
import com.frank.calendar.ui.theme.monthOffset
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
var textColor by mutableStateOf(Color(0xFF018786))
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
var timerRunning = false

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isPort = false
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            isPort = true
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        sharedPreferences = getPreferences(MODE_PRIVATE)
        readToDate()
        LunarCalendar.init(this)

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun TextClock(navController: NavHostController) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            startTextTimer()
            ClockUI({
                val dpd = DatePickerDialog(
                    this, { _, year, month, day ->
                        toDate = LocalDateTime.of(year, month + 1, day, 0, 0, 0, 0)
                        saveTimePerSet()
                        currentDateNum = -1
                    }, toDate.year, toDate.monthValue - 1, toDate.dayOfMonth
                )
                dpd.show()},
                {timerRunning = false
                navController.navigate("calendar") {
                    popUpTo("text") { inclusive = true }
                }
            })
        }

        @Composable
        fun NavHostTime() {
            val navController = rememberNavController()
            CalendarTheme {
                Surface(modifier = Modifier.background(Color.Black).fillMaxSize()) {
                    NavHost(navController, startDestination = "double") {
                        composable("double") {
                            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                            startTimer(lifecycleScope)
                            DoubleView(navController)
                        }
                        composable("text") {

                            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                            startTextTimer()
                            TextClock(navController)
                        }
                        composable("calendar") {
                            startTextTimer()
                            monthOffset = 0
                            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
                            HorizontalPagerSample(navController)
                        }
                        composable("search") {
                            monthOffset = 0
                            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                            ShowSettingDialog {
                                navController.navigate("text") {
                                    popUpTo("search") { inclusive = true }
                                }
                            }
                        }
                    }
                }
            }
        }
        setContent {
            MyApp {
                NavHostTime()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun startTextTimer() {
        lifecycleScope.launch {
            timerRunning = true
            while (timerRunning) {
                time = getCurrentTime()
                leftDate = getNongLiDate()
                trunck_branch = main_branch()
                year_name = jp_year_name()
                date = getCurrentDate()
                isRed = dateColor

                now = LocalDateTime.now()
                if (now.hour == 7 && now.minute == 0) {
                    textColor = Color(0xFF018786)
                }
                if (now.hour == 23 && now.minute == 0) {
                    textColor = DarkGray
                }
                delay(1000)
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    MaterialTheme {
        Surface {
            content()
        }
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
