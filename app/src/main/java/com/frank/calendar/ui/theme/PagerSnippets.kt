package com.frank.calendar.ui.theme

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.frank.calendar.CalendarView
import com.frank.calendar.currentDateNum
import com.frank.calendar.getWeeksOfMonth
import com.frank.calendar.weeksMonth

var monthOffset = 0

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerSample() {

    val pagerState = rememberPagerState(pageCount = {
        Int.MAX_VALUE
    })
    HorizontalPager(state = pagerState) { page ->
        if (page != 0) {
            monthOffset = page - Int.MAX_VALUE / 2 - 2
            currentDateNum = -1
            weeksMonth = getWeeksOfMonth()
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
