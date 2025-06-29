package com.frank.calendar

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

var monthOffset = 0
lateinit var jumpToPage: (Int) -> Unit

var firstOffset = 0

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClockAndCalendar(isToday: Boolean, navController: NavHostController) {

    val pagerState = rememberPagerState(pageCount = {
        Int.MAX_VALUE
    })

    val coroutineScope = rememberCoroutineScope()

    jumpToPage = { page ->
        coroutineScope.launch {
            // Call scroll to on pagerState
            pagerState.scrollToPage(page)
        }
    }


    HorizontalPager(
        state = pagerState,
        modifier = Modifier.background(Color.Black),
    ) { page ->
        if (page == 0) {
            return@HorizontalPager
        }

        if (isToday) {
            when (page % 4) {
                0 -> {
                    if (isPort) {
                        DoubleView1(navController)
                    } else {
                        DoubleView(navController)
                    }
                }

                1 -> {
                    if (isPort) {
                        ClockUIV()
                    } else {
                        ClockUI()
                    }
                }

                2 -> {
                    maxTextSizeCalendarDate_LANDSCAPE =
                        (maxTextSizeCalendarDate_LANDSCAPE.value + 12.0f).sp
                    maxTextSizeCalendarSix_LANDSCAPE =
                        (maxTextSizeCalendarSix_LANDSCAPE.value + 12.0f).sp
                    maxTextSizeCalendarDate_PORTRAIT =
                        (maxTextSizeCalendarDate_PORTRAIT.value + 12.0f).sp
                    maxTextSizeCalendarSix_PORTRAIT =
                        (maxTextSizeCalendarSix_PORTRAIT.value + 12.0f).sp
                    CalendarView(true)
                }

                3 -> {
                    TowTools(navController)
                }
            }
        } else {
            if (firstOffset == 0) {
                firstOffset = page
            }
            monthOffset = page - firstOffset
            maxTextSizeCalendarDate_LANDSCAPE = (maxTextSizeCalendarDate_LANDSCAPE.value + 12.0f).sp
            maxTextSizeCalendarSix_LANDSCAPE = (maxTextSizeCalendarSix_LANDSCAPE.value + 12.0f).sp
            maxTextSizeCalendarDate_PORTRAIT = (maxTextSizeCalendarDate_PORTRAIT.value + 12.0f).sp
            maxTextSizeCalendarSix_PORTRAIT = (maxTextSizeCalendarSix_PORTRAIT.value + 12.0f).sp
            CalendarView(false)
        }
    }

    LaunchedEffect(key1 = UInt, block = {
        Log.i("aaab", "LaunchedEffect")

        if (pagerState.currentPage == 0) {
            var initPage = Int.MAX_VALUE / 2
            while (initPage % 4 != 0) {
                initPage++
            }
            pagerState.scrollToPage(initPage)
        }
    })
}
