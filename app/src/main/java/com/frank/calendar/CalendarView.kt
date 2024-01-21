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


@Composable
fun Date(weekId: Int, modifier: Modifier, dateVal: Int) {
    if (isRedraw > 100) return
    val context = LocalContext.current
    var nongLi = if (dateVal == -1) "" else nongliArray[dateVal - 1]
    val sixDays = if (dateVal == -1) "" else sixDaysArray[dateVal - 1]
    val maxTextSizeDate00 = 132.sp
    var textSize1 by remember("") { mutableStateOf(maxTextSizeDate00) }
    var textSize2 by remember("") { mutableStateOf(maxTextSizeDate00) }
    var textSize3 by remember("") { mutableStateOf(maxTextSizeDate00) }
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
    Box(modifier = modifier.padding(0.dp).clickable {
        if (dateVal == -1) {
            val dpd = DatePickerDialog(
                context, { _, year, month, day ->
                    wantDate = LocalDateTime.of(year, month + 1, 1, 0, 0, 0, 0)
                    val currentDay = LocalDateTime.of(LocalDateTime.now().year, LocalDateTime.now().month, 1, 0, 0, 0, 0)
                    monthOffset = ChronoUnit.MONTHS.between(currentDay, wantDate).toInt()
                    currentDateNum = -1
                    weeksMonth = getWeeksOfMonth()
                    jumpToPage(monthOffset + Int.MAX_VALUE / 2 + 2)
//                    isRedraw = 1 - isRedraw
                }, wantDate.year, wantDate.monthValue - 1, wantDate.dayOfMonth
            )
            dpd.show()
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
//                            onTextLayout = {
//                                if (it.hasVisualOverflow && textSize1 > minTextSize) {
//                                    textSize1 = (textSize1.value - 1.0F).sp
//                                }
//                            },
//                            fontSize = textSize1,
                            fontSize = 15.sp,
                            color = theColor1,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Text(
                        text = nongLi,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
//                        onTextLayout = {
//                            if (it.hasVisualOverflow && textSize2 > minTextSize) {
//                                textSize2 = (textSize2.value - 1.0F).sp
//                            }
//                        },
//                        fontSize = textSize2,
                        fontSize = 15.sp,
                        color = theColor2,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f, false)
                    )
                    Text(
                        text = sixDays,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
//                        onTextLayout = {
//                            if (it.hasVisualOverflow && textSize3 > minTextSize) {
//                                textSize3 = (textSize3.value - 1.0F).sp
//                            }
//                        },
//                        fontSize = textSize3,
                        fontSize = 15.sp,
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
    now = LocalDateTime.now().plusMonths(monthOffset.toLong())
    val lengthOfMonth = now.toLocalDate().lengthOfMonth()
    dayOfMonth = now.dayOfMonth
    firstDay = now.toLocalDate().with(TemporalAdjusters.firstDayOfMonth())
    firstDayOfWeek = firstDay.dayOfWeek.value % 7
    val ret = lengthOfMonth - (7 - firstDayOfWeek)
    for (i in 0 until firstDayOfWeek) {
        dateArray[i] = -1
    }
    var d = firstDayOfWeek
    for (i in 1..lengthOfMonth) {
        dateArray[d++] = i
        nongliArray[i - 1] = getNongLi(i)
        sixDaysArray[i - 1] = getSixDay(i)
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
    Column(Modifier.padding(bottom = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            Modifier
                .weight(1.0f)
                .padding(start = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = if (is_Pad) "$date   $time" else nowDate(),
                maxLines = 1,
//                fontSize = textSize1,
                fontSize = 23.sp,
//                onTextLayout = {
//                    if (it.hasVisualOverflow && textSize1 > minTextSize) {
//                        textSize1 = (textSize1.value - 1.0F).sp
//                    } else {
//                        maxTextSize4 = textSize1
//                    }
//                },
                color = Color(0xFF018786),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(6.dp)
                    .clickable {
                        monthOffset = 0
                        currentDateNum = -1
                        now = LocalDateTime.now()
                        isClock = !isClock
                    })
        }
        var d = 0
        for (i in 0 until weeksMonth) {
            Row(Modifier.weight(if (is_Pad) 0.2f else 0.5f)) {}
            Row(Modifier.weight(1.0f), verticalAlignment = Alignment.CenterVertically) {
                for (j in 0..6) {
                    Date(j, Modifier.weight(1.0f), dateArray[d])
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
