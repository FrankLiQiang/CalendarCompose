package com.frank.calendar.ui.theme

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
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
fun HorizontalPagerSample() {

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
        modifier = Modifier.background(MyTheme.colors.background)
    ) { page ->
        if (page != 0) {
            monthOffset = page - Int.MAX_VALUE / 2 - 2
            maxTextSizeCalendarDate_LANDSCAPE = (maxTextSizeCalendarDate_LANDSCAPE.value + 12.0f).sp
            maxTextSizeCalendarSix_LANDSCAPE = (maxTextSizeCalendarSix_LANDSCAPE.value + 12.0f).sp

            maxTextSizeCalendarDate_PORTRAIT = (maxTextSizeCalendarDate_PORTRAIT.value + 12.0f).sp
            maxTextSizeCalendarSix_PORTRAIT = (maxTextSizeCalendarSix_PORTRAIT.value + 12.0f).sp

            Log.i("ABC", "monthOffset = " + monthOffset)
//        Text(
//            text = "$monthOffset",
//            textAlign = TextAlign.Center,
//            modifier = Modifier.fillMaxSize()
//        )
            CalendarView()
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
