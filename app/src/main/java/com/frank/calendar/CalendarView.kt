package com.frank.calendar

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.frank.calendar.ui.theme.CalendarTheme
import com.frank.calendar.ui.theme.firstOffset
import com.frank.calendar.ui.theme.isShowSettingDialog
import com.frank.calendar.ui.theme.jumpToPage
import com.frank.calendar.ui.theme.monthOffset
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

fun getNongLi(dayNum: Int, weekDay: Int): String {
    return LunarCalendar.getLunarText2(now.year, now.monthValue, dayNum, weekDay)
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
        tbDaysArray[i] = ""
    }
    var d = firstDayOfWeek
    for (i in 1..lengthOfMonth) {
        dateArray[d] = i
        nongliArray[d] = getNongLi(i, d % 7)
        sixDaysArray[d] = getSixDay(i)
        tbDaysArray[d] = mainBranchDay(i)
        d++
    }
    for (i in d until 42) {
        dateArray[i] = -1
        nongliArray[i] = ""
        sixDaysArray[i] = ""
        tbDaysArray[i] = ""
    }
    return ret / 7 + if (ret % 7 == 0) 1 else 2
}

@Composable
fun CalendarView(navController: NavHostController) {
    if (isRedraw > 100) return
    var textSize by remember("") { mutableStateOf(if (isPort) maxTextSizeTitle_PORTRAIT else maxTextSizeTitle_LANDSCAPE) }
    Column(
        Modifier.padding(start = 10.dp, bottom = 10.dp, end = if (isPort) 10.dp else 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier
                .weight(if (isPort) 0.7f else 1.2f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = if (isPort) nowDate() else nowDate() + "  " + nowWeek() + if (monthOffset == 0) "  $time" else "",
                maxLines = 1,
                fontSize = textSize,
                onTextLayout = {
                    if (it.hasVisualOverflow && textSize > minTextSize) {
                        textSize = (textSize.value - 1.0F).sp
                    } else {
                        if (isPort) {
                            maxTextSizeTitle_PORTRAIT = textSize
                            with(sharedPreferences.edit()) {
                                putFloat(
                                    "SHARED_PREFS_CALENDAR_TITLE_P",
                                    maxTextSizeTitle_PORTRAIT.value
                                )
                                commit()
                            }
                        } else {
                            maxTextSizeTitle_LANDSCAPE = textSize
                            with(sharedPreferences.edit()) {
                                putFloat(
                                    "SHARED_PREFS_CALENDAR_TITLE_L",
                                    maxTextSizeTitle_LANDSCAPE.value
                                )
                                commit()
                            }
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
                        navController.navigate("search") {
                            // 清除起始画面
                            popUpTo("calendar") { inclusive = true }
                        }
                    })
        }
        if (isPort && monthOffset == 0) {
            Text(
                text = time,
                maxLines = 1,
                fontSize = textSize,
                color = Color(0xFF018786),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(0.7f)
                    .clickable {
                        isShowSettingDialog = true
                    }
            )
        }
        if (!isPort) {
            isShowSettingDialog = false
        }
        weeksMonth = getWeeksOfMonth()
        Row(Modifier.height(if (isPort) 15.dp else 27.dp)) {}
        var d = 0
        for (i in 0 until weeksMonth) {
            Row(Modifier.weight(1.0f), verticalAlignment = Alignment.CenterVertically) {
                for (j in 0..6) {
                    if (!isPort) {
                        Row(Modifier.width(17.dp)) {}
                    }
                    Date(
                        j,
                        Modifier.weight(1.0f),
                        dateArray[d],
                        nongliArray[d],
                        sixDaysArray[d],
                        tbDaysArray[d]
                    )
                    d++
                }
            }
//            Row(Modifier.height(if (isPort) 22.dp else 0.dp)) {}
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
//            CalendarView(navController)
        }
    }
}

@Composable
fun Date(
    weekId: Int,
    modifier: Modifier,
    dateVal: Int,
    nongLi0: String,
    sixDays: String,
    tb_day: String
) {
    if (isRedraw > 100) return
    val context = LocalContext.current
    var nongLi = nongLi0
    var textSize1 by remember("") { mutableStateOf(if (isPort) maxTextSizeCalendarDate_PORTRAIT else maxTextSizeCalendarDate_LANDSCAPE) }
    var textSize2 by remember("") { mutableStateOf(if (isPort) maxTextSizeCalendarSix_PORTRAIT else maxTextSizeCalendarSix_LANDSCAPE) }
    var theColor1 = Color(0xFF018786)
    var theColor2 = Color(0xFF018786)
    if (weekId == 0 || weekId == 6) theColor1 = Color.Green
    if (weekId == 0 || weekId == 6) theColor2 = Color.Green
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
//        theColor2 = Color.Magenta
        theColor2 = Color(0xFF88FF33)
    }
    Box(modifier = modifier
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
                        jumpToPage(monthOffset + firstOffset)
                        isRedraw = 1 - isRedraw
                    }, wantDate.year, wantDate.monthValue - 1, wantDate.dayOfMonth
                )
                dpd.show()
            } else if (dayOfMonth == dateVal && monthOffset != 0) {
                monthOffset = 0
                jumpToPage(firstOffset)
                isRedraw = 1 - isRedraw
            }
        }) {
        Row(
            Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically
        ) {
            @Composable
            fun showSix() {
                Box(
                    modifier = Modifier
                        .weight(if (isPort) 1.5f else 0.6f),
                    contentAlignment = Alignment.Center
                ) {
                    if (dayOfMonth == dateVal && !isPort) {
                        Image(
                            painter = painterResource(id = R.drawable.round),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit,
                            contentDescription = stringResource(id = R.string.app_name),
                        )
                    }
                    if (dateVal != -1) {
                        Text(
                            text = nongLi.subSequence(0, 2)
                                .toString() + "\n" + tb_day + "\n" + sixDays,
                            color = theColor2,
                            fontSize = textSize2,
                            lineHeight = (textSize2.value + 2).sp,
                            textAlign = TextAlign.Center,
                            onTextLayout = {
                                if (it.hasVisualOverflow && textSize2 > minTextSize) {
                                    textSize2 = (textSize2.value - 1.0F).sp
                                } else {
                                    if (isPort) {
                                        maxTextSizeCalendarSix_PORTRAIT = textSize2
                                        with(sharedPreferences.edit()) {
                                            putFloat(
                                                "SHARED_PREFS_SIX_P",
                                                maxTextSizeCalendarSix_PORTRAIT.value + 3.0f
                                            )
                                            commit()
                                        }
                                    } else {
                                        maxTextSizeCalendarSix_LANDSCAPE = textSize2
                                        with(sharedPreferences.edit()) {
                                            putFloat(
                                                "SHARED_PREFS_SIX_L",
                                                maxTextSizeCalendarSix_LANDSCAPE.value + 3.0f
                                            )
                                            commit()
                                        }
                                    }
                                }
                            },
                        )
                    }
                }
            }

            @Composable
            fun showNum() {
                Box(
                    modifier = (if (isPort) Modifier
                        .weight(1.0f, true)
                        .align(alignment = Alignment.Bottom)
                    else Modifier
                        .weight(1.0f, true)),
                    contentAlignment = Alignment.Center
                ) {
                    if (dayOfMonth == dateVal && isPort) {
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
                                if (isPort) {
                                    maxTextSizeCalendarDate_PORTRAIT = textSize1
                                    with(sharedPreferences.edit()) {
                                        putFloat(
                                            "SHARED_PREFS_GONG_LI_P",
                                            maxTextSizeCalendarDate_PORTRAIT.value
                                        )
                                        commit()
                                    }
                                } else {
                                    maxTextSizeCalendarDate_LANDSCAPE = textSize1
                                    with(sharedPreferences.edit()) {
                                        putFloat(
                                            "SHARED_PREFS_GONG_LI_L",
                                            maxTextSizeCalendarDate_LANDSCAPE.value
                                        )
                                        commit()
                                    }
                                }
                            }
                        },
                        fontSize = textSize1,
                        color = theColor1,
                        textAlign = if (isPort) TextAlign.Center else TextAlign.Right,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            if (isPort) {
                Column(Modifier.weight(2.0f), horizontalAlignment = Alignment.CenterHorizontally) {
                    if (dateVal != -1) {
                        showNum()
                        showSix()
                    }
                }
            } else {
                Row(Modifier.weight(2.0f), verticalAlignment = Alignment.CenterVertically) {
                    if (dateVal != -1) {
                        showNum()
                        showSix()
                    }
                }
            }
        }
    }
}

fun mainBranchDay(day: Int): String {
    return LunarCalendar.getMainBranchDay(now.year, now.monthValue, day)
}
