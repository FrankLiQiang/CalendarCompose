package com.frank.calendar.ui.theme

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.sp
import com.frank.calendar.CalendarView
import com.frank.calendar.maxTextSizeGongli
import com.frank.calendar.maxTextSizeNongli
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


    HorizontalPager(state = pagerState) { page ->
        if (page != 0) {
            monthOffset = page - Int.MAX_VALUE / 2 - 2
            maxTextSizeGongli = (maxTextSizeGongli.value + 12.0f).sp
            maxTextSizeNongli = (maxTextSizeNongli.value + 12.0f).sp

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
