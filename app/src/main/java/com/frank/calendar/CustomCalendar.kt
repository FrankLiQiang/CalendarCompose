/*The MIT License (MIT)

Copyright (c) 2016 Danylyk Dmytro

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

//https://github.com/amit-bhandari/Stopwatch-Jetpack-Compose?tab=readme-ov-file
//https://github.com/amit-bhandari/Stopwatch-Jetpack-Compose.git
//https://amit-bhandari.github.io/posts/jetpack-compose-custom-view/
*/

package com.frank.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CustomCalendar(
    modifier: Modifier = Modifier,
    highlightDate: LocalDate = LocalDate.now(),
    onDateClick: (LocalDate) -> Unit = {},
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val daysInMonth = remember(currentMonth) { currentMonth.lengthOfMonth() }
    val firstDayOfWeek = remember(currentMonth) {
        currentMonth.atDay(1).dayOfWeek.value % 7 // 周日为0
    }

    Column(
        modifier = modifier
            .background(Color.Black)
            .padding(8.dp)
    ) {
        // 年月标题，居中
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.material3.Text(
                "${currentMonth.year}年${
                    currentMonth.month.getDisplayName(
                        TextStyle.SHORT,
                        Locale.CHINA
                    )
                }",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row(modifier = Modifier.height(30.dp)) {}
        // 月份导航
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 8.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            TextBtn("<") { currentMonth = currentMonth.minusMonths(1) }
//            androidx.compose.material3.Text(
//                "${currentMonth.year}年${
//                    currentMonth.month.getDisplayName(
//                        TextStyle.SHORT,
//                        Locale.CHINA
//                    )
//                }",
//                color = Color.White,
//                fontSize = 20.sp,
//                fontWeight = FontWeight.Bold
//            )
//            TextBtn(">") { currentMonth = currentMonth.plusMonths(1) }
//        }

        // 星期标题
        Row(modifier = Modifier.fillMaxWidth()) {
//            listOf("日", "一", "二", "三", "四", "五", "六").forEach { day ->
            listOf("S", "M", "T", "W", "T", "F", "S").forEach { day ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material3.Text(
                        text = day,
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }
        }

        // 日期网格
        val weeks = ((daysInMonth + firstDayOfWeek + 6) / 7)
        Column {
            for (week in 0 until weeks) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (dayOfWeek in 0..6) {
                        val dayNum = week * 7 + dayOfWeek - firstDayOfWeek + 1
                        if (dayNum in 1..daysInMonth) {
                            val theDate = currentMonth.atDay(dayNum)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(2.dp)
                                    .clickable { onDateClick(theDate) },
                                contentAlignment = Alignment.Center
                            ) {
                                val isHighlight = theDate == highlightDate
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .background(
                                            if (isHighlight) todayColor else Color.Transparent,
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
/*
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        // 显示公历日期
                                        Text(
                                            text = dayNum.toString(),
                                            color = Color.White,
                                            fontSize = 22.sp
                                        )
                                        // 显示农历日期
                                        Text(
                                            text = "初六",
                                            color = if (isHighlight)  Color.White else  Color(0xFFAAAAAA),
                                            fontSize = 16.sp,
//                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                        // ...下方你的日历内容
                                    }
 */
                                    Text(
                                        text = dayNum.toString(),
                                        color = if (isHighlight) Color.White else Color.LightGray,
                                        fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 24.sp
                                    )
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                            ) { }
                        }
                    }
                }
            }
        }
    }
}

// 简单按钮
@Composable
fun TextBtn(text: String, onClick: () -> Unit) {
    androidx.compose.material3.Text(
        text,
        color = Color.White,
        fontSize = 24.sp,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    )
}
