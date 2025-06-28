package com.frank.calendar

import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.frank.calendar.ui.theme.CalendarTheme
import com.frank.calendar.ui.theme.HorizontalPagerSample
import com.frank.calendar.ui.theme.firstOffset
import com.frank.calendar.ui.theme.jumpToPage
import com.frank.calendar.ui.theme.monthOffset
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

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

        @Composable
        fun NavHostTime() {
            val navController = rememberNavController()
            startTimer(lifecycleScope)
            CalendarTheme {
                Surface(
                    modifier = Modifier
                        .background(Color.Black)
                        .fillMaxSize()
                ) {
                    NavHost(navController, startDestination = "double") {
                        composable("double") {
                            monthOffset = 0
                            HorizontalPagerSample(true, navController)
                        }
                        composable("calendar") {
                            HorizontalPagerWithFloatingButton(navController)
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

    override fun onDestroy() {
        super.onDestroy()
        timerRunning = false
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HorizontalPagerWithFloatingButton(navController: NavHostController) {
    val c = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPagerSample(false, navController)
        TextButton(
            onClick = {
                //Toast.makeText(context, "跳转到指定月份", Toast.LENGTH_SHORT).show()
            }, modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = if (isPort) 90.dp else 10.dp, start = 16.dp)
        ) {
            Text(
                text = "Today", modifier = Modifier, fontSize = 25.sp,
                color = Color(0xFF40A9FF), // 明亮蓝色
                fontWeight = FontWeight.Bold
            )
        }
        TextButton(
            onClick = {
                val dpd = DatePickerDialog(
                    c, { _, year, month, day ->
                        wantDate = LocalDateTime.of(year, month + 1, 1, 0, 0, 0, 0)
                        val currentDay = LocalDateTime.of(
                            LocalDateTime.now().year, LocalDateTime.now().month, 1, 0, 0, 0, 0
                        )
                        monthOffset = ChronoUnit.MONTHS.between(currentDay, wantDate).toInt()
                        jumpToPage(monthOffset + firstOffset)
                        isRedraw = 1 - isRedraw
                    }, wantDate.year, wantDate.monthValue - 1, wantDate.dayOfMonth
                )
                dpd.show()
            }, modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = if (isPort) 90.dp else 10.dp, end = 16.dp)
        ) {
            Text(
                text = "Go to", modifier = Modifier, fontSize = 25.sp,
                color = Color(0xFF40A9FF), // 明亮蓝色
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun VerticalText(text: String) {
    Column(
        modifier = Modifier.fillMaxHeight(), // 让文字按列排列
        horizontalAlignment = Alignment.CenterHorizontally // 水平居中对齐
    ) {
        // 将字符串拆分成字符，并逐一显示
        text.forEach { char ->
            Text(
                text = char.toString(), fontSize = 24.sp, // 设置字体大小
                modifier = Modifier.padding(vertical = 2.dp) // 每个字符之间的间距
            )
        }
    }
}

@Composable
fun VerticalTextExample() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center // 整体居中
    ) {
        VerticalText("竖直排列")
    }
}