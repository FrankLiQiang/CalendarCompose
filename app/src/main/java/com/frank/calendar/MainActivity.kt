package com.frank.calendar

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.frank.calendar.ui.theme.CalendarTheme

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
            val context = LocalContext.current
            val activity = context as? Activity
            val navController = rememberNavController()
            BackHandler(enabled = navController.previousBackStackEntry == null) {
                timerRunning = false
                activity?.finish()
            }
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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        isRedraw = 1 - isRedraw
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
