package com.frank.calendar

import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.frank.calendar.ui.theme.CalendarTheme
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.Calendar
import java.util.Timer
import java.util.TimerTask


lateinit var firstDay: LocalDate
var firstDayOfWeek: Int = 0
var minTextSize = 12.sp
var maxTextSize = 312.sp
var maxTextSize2 = 112.sp
var maxTextSize3 = 112.sp
var weeksMonth: Int = 5
private var thisTimer: Timer = Timer()
private var thisTask: TimerTask? = null
var textColor by mutableStateOf(DarkGray)
var time by mutableStateOf("09:35:23")
var titleSize by mutableStateOf(20.sp)
var isMeasured by mutableStateOf(false)
var leftDate by mutableStateOf("农历十月廿六")
var date by mutableStateOf("2023年12月08日")
var isRed by mutableStateOf(false)
var isClock by mutableStateOf(true)
var dateArray = Array(42) { -1 }
var isRedraw by mutableStateOf(1)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        WindowCompat.setDecorFitsSystemWindows(window, false)
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
//                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                        ClockUI(::leftTimeClicked)
                    } else {
                        weeksMonth = getWeeksOfMonth()
//                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
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

    var textSize by remember("") { mutableStateOf(maxTextSize) }
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
                    maxTextSize = textSize
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
    val maxTextSizeDate = 23.sp
    var minTextSize = 52.sp
    var nongLi = if (dateVal == -1) "" else getNongLi(dateVal)
    var textSize by remember("") { mutableStateOf(maxTextSizeDate) }
    var weight = 1f
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
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isMeasured) {
            Row(Modifier.weight(weight)) {}
        }
        Column(Modifier.weight(1.0f), horizontalAlignment = Alignment.CenterHorizontally) {
            if (dateVal != -1) {
                Text(
                    text = "$dateVal",
                    maxLines = 1,
                    fontSize = textSize * (if (weeksMonth == 6) 2.2f else if (weeksMonth == 5) 2.5f else 3f),
                    color = theColor1,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(2f, true)
                )
                Text(
                    text = nongLi,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = {
                        if (!isMeasured && nongLi.length == 2) {
                            if (it.hasVisualOverflow && textSize > minTextSize) {
                                textSize = (textSize.value - 1.0F).sp
                            } else {
                                isMeasured = true
                                titleSize = textSize * 2.5f
                            }
                        }
                    },
                    fontSize = textSize,
                    color = theColor2,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1.2f, true)
                )
            }
        }
        if (!isMeasured) {
            Row(Modifier.weight(weight)) {}
        }
    }
}

fun getWeeksOfMonth(): Int {
    val now = LocalDate.now()
    val lengthOfMonth = now.lengthOfMonth()
    firstDay = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())
    firstDayOfWeek = firstDay.dayOfWeek.value % 7
    var ret = lengthOfMonth - (7 - firstDayOfWeek)
    for (i in 0 until firstDayOfWeek) {
        dateArray[i] = -1
    }
    var d = firstDayOfWeek
    for (i in 1..lengthOfMonth) {
        dateArray[d++] = i
    }
    for (i in d until 42) {
        dateArray[i] = -1
    }
    return ret / 7 + if (ret % 7 == 0) 1 else 2
}

@Composable
fun CalendarView() {
    if (isRedraw > 100) return
    Column(Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.weight(1.0f), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$date   $time",
//                text = nowDate(),
                maxLines = 1,
                fontSize = titleSize,
//                fontSize = 22.sp,
                color = Color(0xFF018786),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { isClock = !isClock }
            )
        }
        var d = 0
        for (i in 0 until weeksMonth) {
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

