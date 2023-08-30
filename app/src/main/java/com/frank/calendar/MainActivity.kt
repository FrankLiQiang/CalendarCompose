package com.frank.calendar

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frank.calendar.ui.theme.CalendarTheme
import java.util.Calendar
import java.util.Timer
import java.util.TimerTask

private var thisTimer: Timer = Timer()
private var thisTask: TimerTask? = null
var time by mutableStateOf("")
var leftDate by mutableStateOf("")
var date by mutableStateOf("")
var isRed by mutableStateOf(false)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContent {
            LunarCalendar.init(this)
            ReadToDate()

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
                    MainUI(::leftTimeClicked)
                }
            }
        }
    }

    fun leftTimeClicked() {
        val year = toDate.get(Calendar.YEAR)
        val month = toDate.get(Calendar.MONTH)
        val day = toDate.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            { _, year2, monthOfYear, dayOfMonth ->
                toDate.set(year2, monthOfYear, dayOfMonth, 0, 0, 0)
                toDate.set(Calendar.MILLISECOND, 0)
                SaveTimePerSet()
                currentDateNum = -1
            },
            year,
            month,
            day
        )
        dpd.show()
    }

    private fun SaveTimePerSet() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putLong(getString(R.string.prefs_timePerWorkSet), toDate.timeInMillis)
            commit()
        }
    }

    private fun ReadToDate() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val defaultValue = Calendar.getInstance().timeInMillis
        val highScore = sharedPref.getLong(getString(R.string.prefs_timePerWorkSet), defaultValue)
        toDate.timeInMillis = highScore
    }
}

@Composable
fun MainUI(event: () -> Unit, modifier: Modifier = Modifier) {
    Column(Modifier.background(Black), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = time,
            fontSize = 130.sp,   //160.sp,
            //color = DarkGray,
            color = Color(0xFF018786),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f, true)
        )
        Text(
            text = leftDate,
            fontSize = 40.sp,       //50.sp,
            //color = DarkGray,
            color = Color(0xFF018786),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { event() }
        )
        Text(
            text = date,
            fontSize = 50.sp,       //60.sp,
            //color = if (isRed) Red else DarkGray,
            color = if (isRed) Color(0xFFBB86FC) else Color(0xFF018786),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarPreview() {
    CalendarTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MainUI({ isRed = true })
        }
    }
}