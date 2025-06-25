package com.frank.calendar.ui.theme

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.frank.calendar.CalendarView
import com.frank.calendar.ClockUI
import com.frank.calendar.DoubleView
import com.frank.calendar.DoubleView1
import com.frank.calendar.isPort
import com.frank.calendar.maxTextSizeCalendarDate_LANDSCAPE
import com.frank.calendar.maxTextSizeCalendarDate_PORTRAIT
import com.frank.calendar.maxTextSizeCalendarSix_LANDSCAPE
import com.frank.calendar.maxTextSizeCalendarSix_PORTRAIT
import kotlinx.coroutines.launch

var monthOffset = 0
lateinit var jumpToPage: (Int) -> Unit

var firstOffset = 0

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerSample(navController: NavHostController) {

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
        Log.i("aaab", "HorizontalPager")
        if (page != 0) {
            if (firstOffset == 0) {
                firstOffset = page
            }
            when (page % 4) {
                0 -> {
                    if (isPort) {
                        DoubleView1(navController)
                    } else {
                        DoubleView(navController)
                    }
                }
                1 -> {
                    ClockUI({},{})
                }
                2 -> {
                    maxTextSizeCalendarDate_LANDSCAPE = (maxTextSizeCalendarDate_LANDSCAPE.value + 12.0f).sp
                    maxTextSizeCalendarSix_LANDSCAPE = (maxTextSizeCalendarSix_LANDSCAPE.value + 12.0f).sp
                    maxTextSizeCalendarDate_PORTRAIT = (maxTextSizeCalendarDate_PORTRAIT.value + 12.0f).sp
                    maxTextSizeCalendarSix_PORTRAIT = (maxTextSizeCalendarSix_PORTRAIT.value + 12.0f).sp
                    CalendarView(navController)
                }
                3 -> {
                    Text("Settings")
                }
            }
        }
//
//        if (page != 0) {
//            monthOffset = page - firstOffset
////            Text("monthOffset = " + monthOffset + "\n   page = " + page + "\n   firstOffset = " + firstOffset)
////            Log.i("aaab", "1 monthOffset = " + monthOffset + "   page = " + page + "   firstOffset = " + firstOffset)
//
//            maxTextSizeCalendarDate_LANDSCAPE = (maxTextSizeCalendarDate_LANDSCAPE.value + 12.0f).sp
//            maxTextSizeCalendarSix_LANDSCAPE = (maxTextSizeCalendarSix_LANDSCAPE.value + 12.0f).sp
//            maxTextSizeCalendarDate_PORTRAIT = (maxTextSizeCalendarDate_PORTRAIT.value + 12.0f).sp
//            maxTextSizeCalendarSix_PORTRAIT = (maxTextSizeCalendarSix_PORTRAIT.value + 12.0f).sp
//            CalendarView(navController)
//        }
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
