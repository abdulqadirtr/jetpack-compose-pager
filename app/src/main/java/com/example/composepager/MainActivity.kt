package com.example.composepager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.example.composepager.ui.theme.ComposePagerTheme
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposePagerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BasicPager(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun BasicPager(modifier: Modifier = Modifier) {

    val pagerState = rememberPagerState(pageCount = { 10 })


    // To swipe multiple pages with long swipe
    val fling = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(3)
    )

    Box(modifier = modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 5,
            flingBehavior = fling,
            contentPadding = PaddingValues(vertical = 64.dp),
            pageSize = PageSize.Fill
        ) { page ->
            val offset = pagerState.getOffsetDistanceInPages(page).absoluteValue.coerceIn(0f, 1f)
            PagerItem(page = page, offset = offset)
        }

        DotsIndicator(
            totalDots = pagerState.pageCount,
            selectedIndex = pagerState.currentPage,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp)
        )
    }
}

@Composable
fun PagerItem(page: Int, offset: Float) {
    val scale = androidx.compose.ui.util.lerp(1f, 0.7f, offset)
    val alpha = androidx.compose.ui.util.lerp(1f, 0.3f, offset)

    Box(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            }
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
            .padding(32.dp)
            .background(
                color = Color(
                    red = (100 + page * 10) % 256,
                    green = (150 + page * 20) % 256,
                    blue = (200 + page * 30) % 256
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Page $page",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(totalDots) { index ->
            val color = if (index == selectedIndex) Color.DarkGray else Color.LightGray
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(12.dp)
            )
        }
    }
}
