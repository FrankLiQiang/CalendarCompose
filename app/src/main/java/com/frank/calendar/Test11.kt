package com.frank.calendar

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.frank.calendar.ui.theme.CalendarTheme
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.ZoneOffset

class Test11 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalendarTheme {
                ActivityView()
            }
        }
    }
    fun getUtcStartOfTodayMillis(): Long {
    return LocalDate.now(ZoneOffset.systemDefault()) // 先用本地时区得到今天
        .atStartOfDay(ZoneOffset.systemDefault())           // 以UTC 0点为一天的起点
        .toInstant()
        .toEpochMilli()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ActivityView() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = getUtcStartOfTodayMillis(),
        )

        LaunchedEffect(Unit) {
            var lastDay = LocalDate.now()
            while (true) {
                val now = LocalDate.now()
                if (lastDay != now) {
                    lastDay = now
                    datePickerState.setSelection(getUtcStartOfTodayMillis())
                }
                delay(100)
            }
        }

        DatePicker(
            modifier = Modifier,
            headline = null,
            title = null,
            state = datePickerState,
            showModeToggle = false,
        )
    }
}
