package com.frank.calendar.ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.frank.calendar.CalendarView

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerSample() {

    val pagerState = rememberPagerState(pageCount = {
        Int.MAX_VALUE
    })
    HorizontalPager(state = pagerState) { page ->
        CalendarView()
//        Text(text = "page: ${page - Int.MAX_VALUE / 2}")
    }

    LaunchedEffect(key1 = UInt, block = {
        var initPage = Int.MAX_VALUE / 2
        while (initPage % 5 != 0) {
            initPage++
        }
        pagerState.scrollToPage(initPage)
    })
}
