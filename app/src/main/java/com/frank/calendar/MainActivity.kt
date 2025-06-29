package com.frank.calendar

import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.frank.calendar.ui.theme.CalendarTheme
import com.frank.calendar.ui.theme.ClockAndCalendar
import com.frank.calendar.ui.theme.BirthdaySearch
import com.frank.calendar.ui.theme.monthOffset

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
                            ClockAndCalendar(true, navController)
                        }
                        composable("calendar") {
                            PerpetualCalendar(navController)
                        }

                        composable("birthday") {
                            BirthdaySearch(navController)
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
