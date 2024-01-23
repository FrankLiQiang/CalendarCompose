package com.frank.calendar

import android.app.DatePickerDialog
import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frank.calendar.ui.theme.CalendarTheme
import com.frank.calendar.ui.theme.jumpToPage
import com.frank.calendar.ui.theme.monthOffset
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import kotlin.math.pow
import kotlin.math.sqrt

fun getNongLi(dayNum: Int): String {
    return LunarCalendar.getLunarText2(now.year, now.monthValue, dayNum)
}

fun getSixDay(dayNum: Int): String {
    return LunarCalendar.getSixDay(now.year, now.monthValue, dayNum)
}

fun getWeeksOfMonth(): Int {
    val lengthOfMonth = now.toLocalDate().lengthOfMonth()
    firstDay = now.toLocalDate().with(TemporalAdjusters.firstDayOfMonth())
    firstDayOfWeek = firstDay.dayOfWeek.value % 7
    val ret = lengthOfMonth - (7 - firstDayOfWeek)
    for (i in 0 until firstDayOfWeek) {
        dateArray[i] = -1
        nongliArray[i] = ""
        sixDaysArray[i] = ""
    }
    var d = firstDayOfWeek
    for (i in 1..lengthOfMonth) {
        dateArray[d] = i
        nongliArray[d] = getNongLi(i)
        sixDaysArray[d] = getSixDay(i)
        d++
    }
    for (i in d until 42) {
        dateArray[i] = -1
        nongliArray[i] = ""
        sixDaysArray[i] = ""
    }
    return ret / 7 + if (ret % 7 == 0) 1 else 2
}

@Composable
fun CalendarView() {
    if (isRedraw > 100) return
    var textSize1 by remember("") { mutableStateOf(maxTextSize4) }
    Column(Modifier.padding(bottom = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            Modifier
                .weight(0.8f)
                .padding(start = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = if (is_Pad) nowDate() + "  " + nowWeek() + "  $time" else nowDate(),
                maxLines = 1,
                fontSize = textSize1,
                onTextLayout = {
                    if (it.hasVisualOverflow && textSize1 > minTextSize) {
                        textSize1 = (textSize1.value - 1.0F).sp
                    } else {
                        maxTextSize4 = textSize1
                        with(sharedPreferences.edit()) {
                            putFloat("SHARED_PREFS_CALENDAR_TITLE", maxTextSize4.value)
                            commit()
                        }
                    }
                },
                color = Color(0xFF018786),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(6.dp)
                    .clickable {
                        monthOffset = 0
                        now = LocalDateTime.now()
                        isClock = !isClock
                    })
        }
        if (!is_Pad) {
            Text(
                text = time,
                maxLines = 1,
                fontSize = textSize1,
                color = Color(0xFF018786),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(0.8f)
                    .padding(6.dp)
            )
        }
        weeksMonth = getWeeksOfMonth()
        var d = 0
        for (i in 0 until weeksMonth) {
            Row(Modifier.height(if (is_Pad) 10.dp else 30.dp)) {}
            Row(Modifier.weight(1.0f), verticalAlignment = Alignment.CenterVertically) {
                for (j in 0..6) {
                    Date(j, Modifier.weight(1.0f), dateArray[d], nongliArray[d], sixDaysArray[d])
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
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
//            ClockUI({ isRed = true })
            CalendarView()
        }
    }
}

@Composable
fun Date(weekId: Int, modifier: Modifier, dateVal: Int, nongLi0: String, sixDays: String) {
    if (isRedraw > 100) return
    val context = LocalContext.current
    var nongLi = nongLi0
    var textSize1 by remember("") { mutableStateOf(maxTextSizeGongli) }
    var textSize2 by remember("") { mutableStateOf(maxTextSizeSix) }
    var textSize3 by remember("") { mutableStateOf(maxTextSizeSix) }
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
    Box(modifier = modifier
        .padding(0.dp)
        .clickable {
            if (dateVal == -1) {
                val dpd = DatePickerDialog(
                    context, { _, year, month, day ->
                        wantDate = LocalDateTime.of(year, month + 1, 1, 0, 0, 0, 0)
                        val currentDay = LocalDateTime.of(
                            LocalDateTime.now().year,
                            LocalDateTime.now().month,
                            1,
                            0,
                            0,
                            0,
                            0
                        )
                        monthOffset = ChronoUnit.MONTHS
                            .between(currentDay, wantDate)
                            .toInt()
                        jumpToPage(monthOffset + Int.MAX_VALUE / 2 + 2)
                    }, wantDate.year, wantDate.monthValue - 1, wantDate.dayOfMonth
                )
                dpd.show()
            } else if (dayOfMonth == dateVal && monthOffset != 0) {
                monthOffset = 0
                jumpToPage(Int.MAX_VALUE / 2 + 2)
            }
        }) {
        Row(
            Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1.0f), horizontalAlignment = Alignment.CenterHorizontally) {
                if (dateVal != -1) {
                    Box(
                        modifier = Modifier.weight(1.6f, true)
                    ) {
                        if (dayOfMonth == dateVal) {
                            Image(
                                painter = painterResource(id = R.drawable.round),
                                modifier = Modifier.fillMaxSize(),
                                contentDescription = stringResource(id = R.string.app_name),
                            )
                        }
                        Text(
                            text = "$dateVal",
                            maxLines = 1,
                            onTextLayout = {
                                if (it.hasVisualOverflow && textSize1 > minTextSize) {
                                    textSize1 = (textSize1.value - 1.0F).sp
                                } else {
                                    maxTextSizeGongli = textSize1
                                    with(sharedPreferences.edit()) {
                                        putFloat("SHARED_PREFS_GONG_LI", maxTextSizeGongli.value)
                                        commit()
                                    }
                                }
                            },
                            fontSize = textSize1,
                            color = theColor1,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Box(
                        modifier = Modifier.weight(1.0f, true)
                    ) {
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
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Box(
                        modifier = Modifier.weight(1.0f, true)
                    ) {
                        Text(
                            text = sixDays,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            onTextLayout = {
                                if (it.hasVisualOverflow && textSize3 > minTextSize) {
                                    textSize3 = (textSize3.value - 1.0F).sp
                                } else {
                                    maxTextSizeSix = textSize3
                                    with(sharedPreferences.edit()) {
                                        putFloat(
                                            "SHARED_PREFS_SIX",
                                            maxTextSizeSix.value + 3.0f
                                        )
                                        commit()
                                    }
                                }
                            },
                            fontSize = textSize3,
                            color = theColor2,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxSize() //.weight(1f, false)
                        )
                    }
                }
            }
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
