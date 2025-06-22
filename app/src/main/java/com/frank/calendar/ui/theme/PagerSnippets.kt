package com.frank.calendar.ui.theme

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
import com.frank.calendar.CalendarView
import com.frank.calendar.maxTextSizeCalendarDate_LANDSCAPE
import com.frank.calendar.maxTextSizeCalendarDate_PORTRAIT
import com.frank.calendar.maxTextSizeCalendarSix_LANDSCAPE
import com.frank.calendar.maxTextSizeCalendarSix_PORTRAIT
import kotlinx.coroutines.launch

var monthOffset = 0
lateinit var jumpToPage: (Int) -> Unit

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
        modifier = Modifier.background(Color.Black)
    ) { page ->
        if (page != 0) {
            monthOffset = page - Int.MAX_VALUE / 2 - 2
            maxTextSizeCalendarDate_LANDSCAPE = (maxTextSizeCalendarDate_LANDSCAPE.value + 12.0f).sp
            maxTextSizeCalendarSix_LANDSCAPE = (maxTextSizeCalendarSix_LANDSCAPE.value + 12.0f).sp

            maxTextSizeCalendarDate_PORTRAIT = (maxTextSizeCalendarDate_PORTRAIT.value + 12.0f).sp
            maxTextSizeCalendarSix_PORTRAIT = (maxTextSizeCalendarSix_PORTRAIT.value + 12.0f).sp

            CalendarView(navController)
        }
    }

    LaunchedEffect(key1 = UInt, block = {
        var initPage = Int.MAX_VALUE / 2
        while (initPage % 5 != 0) {
            initPage++
        }
        pagerState.scrollToPage(initPage)
    })
}
