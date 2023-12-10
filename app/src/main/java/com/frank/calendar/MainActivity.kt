package com.frank.calendar

import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frank.calendar.ui.theme.CalendarTheme
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.Calendar
import java.util.Timer
import java.util.TimerTask
import kotlin.math.pow
import kotlin.math.sqrt


lateinit var firstDay: LocalDate
var is_Pad = false
var dayOfMonth = 1
var firstDayOfWeek: Int = 0
var minTextSize = 12.sp
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
                        CalendarView()
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
            this,
            { _, year2, monthOfYear, dayOfMonth ->
                toDate.set(year2, monthOfYear, dayOfMonth, 0, 0, 0)
                toDate.set(Calendar.MILLISECOND, 0)
                saveTimePerSet()
                currentDateNum = -1
            },
            year,
            month,
            day
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

@Composable
fun ClockUI(event: () -> Unit, modifier: Modifier = Modifier) {

    var textSize by remember("") { mutableStateOf(maxTextSize1) }
    var textSize2 by remember("") { mutableStateOf(maxTextSize2) }
    var textSize3 by remember("") { mutableStateOf(maxTextSize3) }
    Column(Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = time,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (it.hasVisualOverflow && textSize > minTextSize) {
                    textSize = (textSize.value - 1.0F).sp
                } else {
                    maxTextSize1 = textSize
                }
            },
            fontSize = textSize,
            color = textColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(1.2f, true)
                .clickable {
                    textColor = if (textColor == DarkGray) {
                        Color(0xFF018786)
                    } else {
                        DarkGray
                    }
                }
        )
        Text(
            text = leftDate,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (it.hasVisualOverflow && textSize2 > minTextSize) {
                    textSize2 = (textSize2.value - 1.0F).sp
                } else {
                    maxTextSize2 = textSize2
                }
            },
            fontSize = textSize2,
            color = textColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clickable { event() }
                .weight(0.3f, true)
        )
        Text(
            text = date,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (it.hasVisualOverflow && textSize3 > minTextSize) {
                    textSize3 = (textSize3.value - 1.0F).sp
                } else {
                    maxTextSize3 = textSize3
                }
            },
            fontSize = textSize3,
            color = if (isRed) Color(0xFFBB86FC) else textColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = modifier
                .weight(0.5f, true)
                .clickable { isClock = !isClock }

        )
    }
}

@Composable
fun Date(weekId: Int, modifier: Modifier, dateVal: Int) {
    if (isRedraw > 100) return
    var nongLi = if (dateVal == -1) "" else nongliArray[dateVal - 1]
    val maxTextSizeDate00 = 132.sp
    var textSize1 by remember("") { mutableStateOf(maxTextSizeDate00) }
    var textSize2 by remember("") { mutableStateOf(maxTextSizeDate00) }
    var theColor1 = Color(0xFF018786)
    var theColor2 = Color(0xFF018786)
    if (weekId == 0 || weekId == 6) theColor1 = Color.Blue
    if (weekId == 0 || weekId == 6) theColor2 = Color.Blue
    if (nongLi.startsWith("*")) {
        nongLi = nongLi.substring(1)
        theColor2 = Color.Red
    }
    if (nongLi.startsWith("%")) {
        nongLi = nongLi.substring(1)
        theColor1 = Color.Red
    }
    if (nongLi.startsWith("@")) {
        nongLi = nongLi.substring(1)
        theColor2 = Color.Yellow
    }
    val padding = if (is_Pad) 0.dp else 5.dp
    Box(modifier = modifier.padding(padding)) {
        if (dayOfMonth == dateVal) {
            Image(
                painter = painterResource(id = R.drawable.round),
                modifier = Modifier.fillMaxSize(),
                contentDescription = stringResource(id = R.string.app_name),
            )
        }
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1.0f), horizontalAlignment = Alignment.CenterHorizontally) {
                if (dateVal != -1) {
                    Text(
                        text = "$dateVal",
                        maxLines = 1,
                        onTextLayout = {
                            if (it.hasVisualOverflow && textSize1 > minTextSize) {
                                textSize1 = (textSize1.value - 1.0F).sp
                            }
                        },
                        fontSize = textSize1,
                        color = theColor1,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(2.3f, true)
                    )
                    Text(
                        text = nongLi,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        onTextLayout = {
                            if (it.hasVisualOverflow && textSize2 > minTextSize) {
                                textSize2 = (textSize2.value - 1.0F).sp
                            }
                        },
                        fontSize = textSize2,
                        color = theColor2,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f, false)
                    )
                }
            }
        }
    }
}


fun getWeeksOfMonth(): Int {
    val now = LocalDate.now()
    val lengthOfMonth = now.lengthOfMonth()
    dayOfMonth = now.dayOfMonth
    firstDay = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())
    firstDayOfWeek = firstDay.dayOfWeek.value % 7
    var ret = lengthOfMonth - (7 - firstDayOfWeek)
    for (i in 0 until firstDayOfWeek) {
        dateArray[i] = -1
    }
    var d = firstDayOfWeek
    for (i in 1..lengthOfMonth) {
        dateArray[d++] = i
        nongliArray[i - 1] = getNongLi(i)
    }
    for (i in d until 42) {
        dateArray[i] = -1
    }
    return ret / 7 + if (ret % 7 == 0) 1 else 2
}

@Composable
fun CalendarView() {
    if (isRedraw > 100) return
    var textSize1 by remember("") { mutableStateOf(maxTextSize4) }
    Column(Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            Modifier
                .weight(1.0f)
                .padding(start = 10.dp, end = 10.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (is_Pad) "$date   $time" else nowDate(),
                maxLines = 1,
                fontSize = textSize1,
                onTextLayout = {
                    if (it.hasVisualOverflow && textSize1 > minTextSize) {
                        textSize1 = (textSize1.value - 1.0F).sp
                    } else {
                        maxTextSize4 = textSize1
                    }
                },
                color = Color(0xFF018786),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(6.dp)
                    .clickable { isClock = !isClock }
            )
        }
        var d = 0
        for (i in 0 until weeksMonth) {
            Row(Modifier.weight(if (is_Pad) 0.2f else 0.5f)) {}
            Row(Modifier.weight(1.0f), verticalAlignment = Alignment.CenterVertically) {
                for (i in 0..6) {
                    Date(i, Modifier.weight(1.0f), dateArray[d])
                    d++
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 2000,
    heightDp = 900,
)
@Composable
fun CalendarPreview() {
    CalendarTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
//            ClockUI({ isRed = true })
            CalendarView()
        }
    }
}

fun isPad(context: Context): Boolean {
    val isPad: Boolean = (context.resources.configuration.screenLayout
            and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val dm = DisplayMetrics()
    display.getMetrics(dm)
    val x = (dm.widthPixels / dm.xdpi).toDouble().pow(2.0)
    val y = (dm.heightPixels / dm.ydpi).toDouble().pow(2.0)
    val screenInches = sqrt(x + y) // 屏幕尺寸
    return isPad || screenInches >= 7.0
}
